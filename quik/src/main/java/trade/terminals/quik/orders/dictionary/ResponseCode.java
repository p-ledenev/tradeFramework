package trade.terminals.quik.orders.dictionary;

import lombok.Getter;

/**
 * Created by pledenev on 05.03.2016.
 */

@Getter
public enum ResponseCode {

	Success(0L),
	Failed(1L),
	QuikTerminalNotFound(2L),
	DllVersionNotSupported(3L),
	AlreadyConnectedToQuik(4L),
	WrongSyntax(5L),
	QuikNotConnected(6L),
	DllNotConnected(7L),
	QuikConnected(8L),
	QuikDisconnected(9L),
	DllConnected(10L),
	DllDisconnected(11L),
	MemoryAllocationError(12L),
	WrongConnectionHandle(13L),
	WrongInputParams(14L),
	Unknown(-1L);

	public static boolean isDllDisconnected(Long value) {
		return DllNotConnected.hasValue(value) || DllDisconnected.hasValue(value);
	}

	public static boolean isSucceed(Long value) {
		return Success.hasValue(value);
	}

	public static boolean isDlLConnected(Long value) {
		return DllConnected.hasValue(value) || AlreadyConnectedToQuik.hasValue(value);
	}

	public static boolean isQuikConnected(Long value) {
		return QuikConnected.hasValue(value);
	}

	private Long value;

	private ResponseCode(Long value) {
		this.value = value;
	}

	public boolean hasValue(Long value) {
		return this.value.equals(value);
	}

	public static ResponseCode getBy(Long value) {
		for (ResponseCode code : values())
			if (code.hasValue(value))
				return code;

		return ResponseCode.Unknown;
	}
}
