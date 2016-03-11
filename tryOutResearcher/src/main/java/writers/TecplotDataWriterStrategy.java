package writers;

import model.ResearchResult;
import tools.Round;

/**
 * Created by ledenev.p on 28.01.2016.
 */
public class TecplotDataWriterStrategy implements DataWriterStrategy {

	private String title;
	private int xPoints;
	private int yPoints;

	public TecplotDataWriterStrategy(String title, int xPoints, int yPoints) {
		this.title = title;
		this.xPoints = xPoints;
		this.yPoints = yPoints;
	}

	@Override
	public String printHeader() {
		return "TITLE     = \"Converted Excel Data\"\n" +
				"VARIABLES = \"gaps\"\n" +
				"\"sieve\"\n" +
				"\"profit\"\n" +
				"\"losses\"\n" +
				"\"coefficient\"\n" +
				"TEXT\n" +
				"CS=FRAME\n" +
				"X=21.0334788937,Y=91.9213973799\n" +
				"C=BLACK \n" +
				"S=LOCAL\n" +
				"HU=POINT\n" +
				"LS=1 AN=LEFT\n" +
				"BXM=20 LT=0.1 BXO=BLACK BXF=WHITE \n" +
				"F=HELV-BOLD\n" +
				"H=14 A=0\n" +
				"MFC=\"\"\n" +
				"CLIPPING=CLIPTOVIEWPORT\n" +
				"T=\"" + title + "\"\n" +
				"ZONE T=\"Zone 001\"\n" +
				" STRANDID=0, SOLUTIONTIME=0\n" +
				" I=" + xPoints + ", J=" + yPoints + ", K=1, ZONETYPE=Ordered\n" +
				" DATAPACKING=POINT\n" +
				" DT=(DOUBLE DOUBLE DOUBLE DOUBLE DOUBLE )";
	}

	@Override
	public String print(ResearchResult result) throws Throwable {
		return result.getGapsNumber() + " " + Round.toSignificant(result.getSieveParam())
				+ " " + Round.toMoneyAmount(result.getProfit()) + " " + Round.toMoneyAmount(result.getLoss())
				+ " " + Round.toMoneyAmount(result.getTradeCoefficient());
	}

	@Override
	public String getFileExtension() {
		return "dat";
	}
}