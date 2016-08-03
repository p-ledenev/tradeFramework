package trade.researcher.fourier;

import trade.researcher.tryOut.model.IResearchResult;
import trade.researcher.tryOut.writers.DataWriterStrategy;

/**
 * Created by ledenev.p on 28.01.2016.
 */
public class FourierTecplotDataWriterStrategy implements DataWriterStrategy {

	private String title;
	private int xPoints;
	private int yPoints;

	public FourierTecplotDataWriterStrategy(String title, int xPoints, int yPoints) {
		this.title = title;
		this.xPoints = xPoints;
		this.yPoints = yPoints;
	}

	@Override
	public String printHeader() {
		return "TITLE     = \"Converted Excel Data\"\n" +
				"VARIABLES = \"depths\"\n" +
				"\"frequencies\"\n" +
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
	public String print(IResearchResult result) throws Throwable {
		return result.print(" ");
	}

	@Override
	public String getFileExtension() {
		return "dat";
	}
}
