package model;

import java.util.*;

/**
 * Created by ledenev.p on 16.12.2015.
 */
public class ResearcherDataCollector {

    List<ResearchResult> results;

    public ResearcherDataCollector() {
        results = new ArrayList<>();
    }

    public void add(double sieveParam, int gapsNumber, double tradeCoefficient) {
        results.add(new ResearchResult(sieveParam, gapsNumber, tradeCoefficient));
    }

    public String printHeader() {
        return ResearchResult.header;
    }

    public String print() {
        String response = "";

        for (ResearchResult result : results)
            response += result.print() + "\n";

        return response;
    }

    public String printLast() {
        return results.get(results.size() - 1).print() + "\n";
    }
}
