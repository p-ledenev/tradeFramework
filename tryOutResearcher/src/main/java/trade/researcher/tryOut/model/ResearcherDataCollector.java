package trade.researcher.tryOut.model;

import trade.researcher.tryOut.writers.*;

import java.util.*;

/**
 * Created by ledenev.p on 16.12.2015.
 */

public class ResearcherDataCollector {

	List<IResearchResult> results;

	public ResearcherDataCollector() {
		results = new ArrayList<>();
	}

	public void add(IResearchResult result) {
		results.add(result);
	}

	public String print(DataWriterStrategy writerStrategy) throws Throwable {
		String response = "";

		for (IResearchResult result : results)
			response += writerStrategy.print(result) + "\n";

		return response;
	}

	public IResearchResult getLast() throws Throwable {
		return results.get(results.size() - 1);
	}
}
