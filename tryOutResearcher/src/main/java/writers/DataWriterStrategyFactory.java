package writers;

/**
 * Created by ledenev.p on 28.01.2016.
 */
public class DataWriterStrategyFactory {

	public static DataWriterStrategy create(String title, int xPoints, int yPoints) {
		return new TecplotDataWriterStrategy(title, xPoints, yPoints);
	}
}
