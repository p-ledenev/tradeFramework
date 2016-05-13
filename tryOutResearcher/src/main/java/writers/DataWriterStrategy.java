package writers;

import model.IResearchResult;

/**
 * Created by ledenev.p on 28.01.2016.
 */
public interface DataWriterStrategy {

	String printHeader();

	String print(IResearchResult result) throws Throwable;

	String getFileExtension();
}
