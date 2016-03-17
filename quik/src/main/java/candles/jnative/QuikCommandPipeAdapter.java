package candles.jnative;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;
import tools.Log;

import java.io.*;
import java.nio.ByteBuffer;

/*
	Based on Pavel M Bosco, 2014 implementation
*/

public class QuikCommandPipeAdapter implements Closeable {

	private HANDLE pipeHandle = WinNT.INVALID_HANDLE_VALUE;

	static WindowsPipesApi kernel32 = init();

	static WindowsPipesApi init() {
		// проблем с загрузкой ядра вообще-то быть недолжно.
		System.setProperty("jna.encoding", "Cp1251");
		return (WindowsPipesApi) Native.loadLibrary("kernel32", WindowsPipesApi.class, W32APIOptions.UNICODE_OPTIONS);
	}

	private void forceDisconnect() {
		if (pipeHandle != WinNT.INVALID_HANDLE_VALUE) {
			kernel32.CloseHandle(pipeHandle);
			pipeHandle = WinNT.INVALID_HANDLE_VALUE;
			Log.debug("Закрыли трубу!");
		}
	}

	private boolean ensureConnected() {
		if (pipeHandle != WinNT.INVALID_HANDLE_VALUE)
			return true;

		int retryCount = 5;
		while (pipeHandle == WinNT.INVALID_HANDLE_VALUE) {
			pipeHandle = kernel32.CreateFile(
					"\\\\.\\pipe\\pmb.quik.pipe",
					Kernel32.GENERIC_READ | Kernel32.GENERIC_WRITE,
					0,
					null,
					Kernel32.OPEN_EXISTING,
					0,
					null);
			int errorCode = kernel32.GetLastError();
			if (errorCode == 2 || errorCode == 231) {// трубы нет или занята
				forceDisconnect();
				if (--retryCount < 0) {
					Log.debug("errorCode " + errorCode);
					return false;
				}
			}
		}
		Log.debug("Готово!");
//		Log.info(kernel32.GetLastError());
		if (kernel32.GetLastError() != Kernel32.ERROR_SUCCESS) {
			forceDisconnect();
			return false;
		}
		return true;
	}

	public String executeRequest(String command, boolean doCloseConnection) {
		try {
			Log.debug("Соединяемся");
			if (ensureConnected()) {
				//String command = "stm";//"isc";//"staEQBREMU:SBER03";
				WinBase.OVERLAPPED overlapped = new WinBase.OVERLAPPED();
				ByteBuffer commandBytes = ByteBuffer.allocate(command.length() + 1);
				commandBytes.put(command.getBytes()).put((byte) 0);
				kernel32.WriteFile(pipeHandle, commandBytes.array(), commandBytes.capacity(), new IntByReference(), overlapped);
				Log.debug("Last error " + kernel32.GetLastError());

				kernel32.FlushFileBuffers(pipeHandle);
				Log.debug("Last error " + kernel32.GetLastError());

				while (overlapped.Internal.intValue() == WinNT.ERROR_IO_PENDING) {
				}

				Log.debug("Записали");
				ByteBuffer buffer = ByteBuffer.allocate(4 * 1024);
				IntByReference bytesRead = new IntByReference(buffer.capacity());
				int lastError = 0;
				Log.debug("Начали читать..");

				//проверим, чтобы не зависнуть
				if (kernel32.PeekNamedPipe(pipeHandle, buffer, buffer.capacity(), bytesRead, null, null))
					while (!(kernel32.ReadFile(pipeHandle, buffer.array(), buffer.capacity(), bytesRead, overlapped))
							|| (lastError = kernel32.GetLastError()) == Kernel32.ERROR_MORE_DATA) {
						// читаем и читаем
						if (lastError == Kernel32.ERROR_PIPE_NOT_CONNECTED || overlapped.Internal.intValue() != WinNT.ERROR_IO_PENDING)
							break;
					}

				Log.debug("Считали: " + bytesRead.getValue() + " байт");
				if (doCloseConnection) {
					forceDisconnect();
				}
				String result = new String(buffer.array(), 0, bytesRead.getValue());
				Log.debug("Quik pipe -> : " + result);

				if ("not connected".equals(result))
					return null;
				return result;
			} else {
				Log.debug("Quik Pipe cоединение не может быть установлено. Вероятно сервер выключен");
				return null;
			}
		} finally {
			if (doCloseConnection) {
				forceDisconnect();
			}
		}
	}

	public boolean isConnectedToServer(boolean doCloseConnection) {
		return "1".equals(executeRequest("isc", doCloseConnection));
	}

	public String getLastCandlesOf(String classCode, String securityCode, Interval interval, int numberOfCandles, boolean doCloseConnection) {
		return executeRequest("sve" + classCode + ":" + securityCode + ":" + interval.code + ":" + numberOfCandles, doCloseConnection);
	}

	public String getServerCurrentHour(boolean doCloseConnection) {
		String s = executeRequest("stm", doCloseConnection);
		if (s != null && s.length() > 2)
			return s.substring(0, 2);
		return "";
	}

	public String getServerCurrentTime(boolean doCloseConnection) {
		String s = executeRequest("stm", doCloseConnection);
		if (s != null)
			return s;
		return "";
	}

	public String getTradeDate(boolean doCloseConnection) {
		return executeRequest("trd", doCloseConnection);
	}

	public double getContractPrice(String classCode, String securityCode, boolean doCloseConnection) {
		String contract = executeRequest("go " + classCode + ":" + securityCode, doCloseConnection);
		if (contract == null || contract.length() < 1) {
			return 0;
		}
		return Double.valueOf(contract.replaceAll("[^0-9,\\.]", "").replaceAll(",", "."));
	}

	@Override
	public void close() throws IOException {
		forceDisconnect();
	}

	public enum Interval {
		Minute(1), Hour(60), Day(1440),
		Week(10080), Month(23200);

		private int code;

		Interval(int code) {
			this.code = code;
		}
	}
}
