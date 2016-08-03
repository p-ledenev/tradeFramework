package trade.terminals.quik.orders.testing;


import trade.core.model.*;

/**
 * Created by ledenev.p on 16.03.2016.
 */
public class BuyOrderStub extends Order {

	public BuyOrderStub() {
		super(new Machine());
	}

	@Override
	public String getSecurity() {
		return "SiM6";
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
