package model;

import dataSources.*;
import decisionStrategies.neuron.*;
import settings.*;
import siftStrategies.*;
import tools.*;

import java.util.*;

/**
 * Created by DiKey on 11.08.2015.
 */
public class Runner {

    //public static String resultPath = "F:/Teddy/Alfa/java/v1.0/tradeFramework/nnTutor/data/results/";
    public static String resultPath = "d:/Projects/Alfa/java/tradeFramework/nnTutor/data/results/";
    //public static String resultPath  = "./";

    public static void main(String[] args) throws Throwable {

        String ticket = "usd";
        String year = "2010";
        int depth = 51;

        IDataSource dataSource = DataSourceFactory.createDataSource();
        List<TryOutCandle> candles = dataSource.readCandlesFrom(InitialSettings.settingPath + "sources/" + year + "/" + ticket + "_1min.txt");

        ISiftCandlesStrategy siftStrategy = SiftCandlesStrategyFactory.createSiftStrategy(0.0145);
        CandlesStorage allDataStorage = new CandlesStorage(siftStrategy, Arrays.asList(candles.toArray(new Candle[candles.size()])));
        CandlesStorage storage = new CandlesStorage(siftStrategy);

        NeuronTrainingDecisionStrategy strategy = new ApproximationNeuronTrainingDecisionStrategy();
        strategy.setAllDataStorage(allDataStorage);
        strategy.setCandlesStorage(storage);

        CandlesIterator iterator = new CandlesIterator(candles);

        Candle lastCandle = null;
        List<TrainingResult> results = new ArrayList<TrainingResult>();

        for (int i = 1; iterator.hasNextCandles(); i++) {
            List<Candle> newCandles = iterator.getNextCandles();

            storage.add(newCandles);
            TrainingResult result = strategy.computeTrainingResult(depth);

            if (result.getNormalizedValueIncrements().size() < depth - 1)
                break;

            if (i % 10 == 0)
                results.add(result);

            if (lastCandle == null || !lastCandle.hasSameDay(newCandles.get(0))) {
                Log.info("processing candle - " + newCandles.get(0).print());
                lastCandle = newCandles.get(0);
            }
        }

        ResultWriter writer = new ResultWriter(resultPath + ticket + "_" + year + ".txt");
        writer.write(results);

        Log.info("Preparation finished");

        //hasSameIncrements(results);
        //Log.info("Comparison finished");
    }

    public static void hasSameIncrements(List<TrainingResult> results) throws Throwable {

        int k = 0;
        for (int i = 0; i < results.size() - 1; i++) {
            for (int j = i + 1; j < results.size(); j++) {
                if (results.get(i).hasSameIncrements(results.get(j))) {
                    Log.info(results.get(i).print());
                    Log.info(results.get(j).print());
                    k++;
                }
            }
            // Log.info("iteration: " + i);
        }
        Log.info("Total equals sets " + k);
    }
}
