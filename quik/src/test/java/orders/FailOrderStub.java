package orders;

import model.Machine;
import model.Order;

/**
 * Created by ledenev.p on 16.03.2016.
 */
public class FailOrderStub extends Order {

	public FailOrderStub() {
		super(new Machine());
	}

	@Override
	public String getSecurity() {
		return "Установление соединения";
	}

	@Override
	public boolean isSell() {
		return false;
	}

	@Override
	public boolean isBuy() {
		return true;
	}

	@Override
	public int getVolume() {
		return 1;
	}
}
