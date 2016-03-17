package candles.jnative;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.ptr.IntByReference;

import java.nio.ByteBuffer;

/**
 * Created by ledenev.p on 17.03.2016.
 */
public interface WindowsPipesApi extends Kernel32 {

	boolean PeekNamedPipe(
			HANDLE hNamedPipe,
			ByteBuffer lpBuffer,
			int nBufferSize,
			IntByReference lpBytesRead,
			DWORDByReference lpTotalBytesAvail,
			DWORDByReference lpBytesLeftThisMessage
	);

	DWORD PIPE_READMODE_MESSAGE = new DWORD(2);
	DWORD PIPE_READMODE_BYTE = new DWORD(0);

	boolean SetNamedPipeHandleState(
			HANDLE hNamedPipe,
			DWORDByReference lpMode,
			DWORDByReference lpMaxCollectionCount,
			DWORDByReference lpCollectDataTimeout
	);

	boolean FlushFileBuffers(HANDLE hNamedPipe);
}
