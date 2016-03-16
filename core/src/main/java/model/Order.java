package model;

import exceptions.*;
import lombok.*;
import tools.*;

/**
 * Created by DiKey on 04.04.2015.
 */

@Data
public class Order {

	private Position newPosition;
	private Machine machine;
	private OrderStatus status;

	public Order(Machine machine) {
		this.machine = machine;
	}

	public Order(Position newPosition, Machine machine) {
		this(machine);
		this.newPosition = newPosition;
	}

	public void executed() {
		status = OrderStatus.executed;
	}

	public Candle getLastCandle() {
		return machine.getLastCandle();
	}

	public String getPortfolioTitle() {
		return machine.getPortfolio().getTitle();
	}

	public boolean hasSameSecurity(Order order) {
		return getSecurity().equals(order.getSecurity());
	}

	public String getSecurity() {
		return machine.getSecurity();
	}

	public void setValue(double value) {
		newPosition.setValue(value);
	}

	public double getValue() {
		return newPosition.getValue();
	}

	public void print() {
		Log.info(toString());
	}

	public int getVolume() {
		return machine.getPositionVolume() + newPosition.getVolume();
	}

	public Direction getDirection() {

		if (machine.getPosition().isNeutral())
			return newPosition.getDirection();

		return machine.getPositionDirection().opposite();
	}

	public boolean isBuy() {
		return Direction.Buy.equals(getDirection());
	}

	public boolean isSell() {
		return Direction.Sell.equals(getDirection());
	}

	public boolean isNewest() {
		return OrderStatus.newest.equals(status);
	}

	public boolean isEmpty() {
		return Direction.Neutral.equals(getDirection()) || getVolume() == 0;
	}

	public boolean hasOppositeDirectionTo(Order order) {
		return getDirection().isOppositeTo(order.getDirection());
	}

	public void applyToMachine() throws PositionAlreadySetFailure {
		if (isExecuted())
			machine.apply(newPosition);
	}

	@Override
	public String toString() {
		return Format.asString(newPosition.getDate()) + " " + machine.printDecisionStrategy() + " " + machine.getDepth() + " " +
				newPosition.getDirection() + " " + getValue() + " " + getVolume() + " " + (isExecuted() ? "executed" : "not executed");
	}

	public void block() {
		machine.block();
		status = OrderStatus.blocked;
	}

	public void unblock() {
		machine.unblock();
	}

	public boolean hasSameVolume(Order order) {
		return getVolume() == order.getVolume();
	}

	public boolean isExecuted() {
		return OrderStatus.executed.equals(status);
	}

	public boolean isProcessed() {
		return !OrderStatus.newest.equals(status);
	}

	public boolean isOppositeTo(Order order) {
		return hasSameSecurity(order)
				&& hasOppositeDirectionTo(order)
				&& hasSameVolume(order)
				&& isNewest() && order.isNewest();
	}
}
