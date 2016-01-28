package writers;

import model.ResearchResult;

/**
 * Created by ledenev.p on 28.01.2016.
 */
public interface DataWriterStrategy {

	String printHeader();

	String print(ResearchResult result) throws Throwable;

	String getFileExtension();
}
