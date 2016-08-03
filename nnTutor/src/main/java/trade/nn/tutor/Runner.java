package trade.nn.tutor;

import trade.core.decisionStrategies.algorithmic.AveragingDecisionStrategy;
import trade.core.decisionStrategies.neuronTraining.NeuronTrainingDecisionStrategy;
import trade.core.model.*;
import trade.core.siftStrategies.*;
import trade.core.tools.*;
import trade.tryOut.dataSources.*;
import trade.tryOut.model.*;
import trade.tryOut.settings.InitialSettings;

import java.io.PrintWriter;
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
        int year = 2010;
        String timeFrame = "1min";
        int depth = 101;

        IDataSource dataSource = DataSourceFactory.createDataSource();
        List<TryOutCandle> candles = dataSource.readCandlesFrom(InitialSettings.settingPath + "sources/" + year + "/" + ticket + "_" + timeFrame + ".txt");

        ISiftCandlesStrategy siftStrategy = SiftCandlesStrategyFactory.createSiftStrategy(0.022, 7);
        CandlesStorage allDataStorage = new CandlesStorage(new NoSiftStrategy(), Arrays.asList(candles.toArray(new Candle[candles.size()])));
        CandlesStorage storage = new CandlesStorage(siftStrategy);

        NeuronTrainingDecisionStrategy strategy = NeuronTrainingDecisionStrategyFactory.createStrategy();
        strategy.setAllDataStorage(allDataStorage);
        strategy.setCandlesStorage(storage);

        AveragingDecisionStrategy averageDecisionStrategy = new AveragingDecisionStrategy();
        averageDecisionStrategy.setCandlesStorage(storage);

        strategy.setAveragingStrategy(averageDecisionStrategy);

        CandlesIterator iterator = new CandlesIterator(candles);

        Candle lastCandle = null;
        List<TrainingResult> results = new ArrayList<TrainingResult>();

        PrintWriter allDataWriter = new PrintWriter(resultPath + ticket + "_" + year + "_allData.txt");
        for (TryOutCandle candle : candles) {
            allDataWriter.write(Format.asString(candle.getDate()) + ";" + candle.getIndex() + ";" +
                    candle.getValue() + "\n");
        }
        allDataWriter.close();

        PrintWriter tutorWatchWriter = new PrintWriter(resultPath + ticket + "_" + year + "_tutorWatch.txt");

        //int maxRows = 1000;
        int maxRows = iterator.size();
        int j = 0;
        for (int i = 1; iterator.hasNextCandles(); i++) {
            List<Candle> newCandles = iterator.getNextCandles();

            boolean newCandleAdded = storage.add(newCandles);
            if (!newCandleAdded)
                continue;

            TrainingResult result = strategy.computeTrainingResult(depth);

            if (!newCandles.get(0).hasYearAs(year))
                continue;

            if (j >= maxRows) {
                Log.info(newCandles.get(0).print());
                break;
            }

            TryOutCandle candle = (TryOutCandle) newCandles.get(0);
            tutorWatchWriter.write(Format.asString(candle.getDate()) + ";" + candle.getIndex() +
                    ";" + result.getDirectionSign() + "\n");


            //if (result.isActive()) {
            results.add(result);
            j++;
            //          }
//            else if (i % 10 == 0) {
//                results.add(result);
//                j++;
//            }

            if (lastCandle == null || !lastCandle.hasSameDay(newCandles.get(0))) {
                Log.info("processing candle - " + newCandles.get(0).print());
                lastCandle = newCandles.get(0);
            }
        }

        tutorWatchWriter.close();

        ResultWriter writer = new ResultWriter(resultPath + ticket + "_" + year + ".txt");
        writer.write(filter(results));

        Log.info("Preparation finished");

//        hasSameIncrements(results);
//        Log.info("Comparison finished");
    }

    public static List<TrainingResult> filter(List<TrainingResult> results) throws Throwable {

        List<TrainingResult> response = new ArrayList<>();

        List<TrainingResult> inARow = new ArrayList<>();
        for (TrainingResult result : results) {

            if (result.isActive()) {
                inARow.add(result);

            } else {

                if (inARow.size() > 50)
                    response.addAll(inARow);

                inARow = new ArrayList<>();
            }
        }

        return response;

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
