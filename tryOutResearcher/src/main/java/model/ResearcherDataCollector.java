package model;

import writers.*;

import java.util.*;

/**
 * Created by ledenev.p on 16.12.2015.
 */

public class ResearcherDataCollector {

	List<ResearchResult> results;

	public ResearcherDataCollector() {
		results = new ArrayList<>();
	}

	public void add(ResearchResult result) {
		results.add(result);
	}

	public String print(DataWriterStrategy writerStrategy) throws Throwable {
		String response = "";

		for (ResearchResult result : results)
			response += writerStrategy.print(result) + "\n";

		return response;
	}

	public ResearchResult getLast() throws Throwable {
		return results.get(results.size() - 1);
	}
}
