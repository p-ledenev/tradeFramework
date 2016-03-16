package orders;

import model.*;

/**
 * Created by ledenev.p on 16.03.2016.
 */
public class BuyOrderStub extends Order {

	public BuyOrderStub() {
		super(new Machine());
	}

	@Override
	public String getSecurity() {
		return "usd-6.16";
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
