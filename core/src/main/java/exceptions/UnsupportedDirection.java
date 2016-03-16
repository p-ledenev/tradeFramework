package exceptions;

import model.Direction;

/**
 * Created by ledenev.p on 31.03.2015.
 */
public class UnsupportedDirection extends Exception {

	public UnsupportedDirection(String name) {
		super("Direction " + name+ " not declared");
	}

	public UnsupportedDirection(Direction direction) {
		super("Direction " + direction + " not supported by operation");
	}
}
