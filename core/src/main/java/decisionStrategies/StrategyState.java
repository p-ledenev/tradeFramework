package decisionStrategies;

import lombok.Data;
import model.Candle;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ledenev.p on 14.05.2015.
 */
@Data
public class StrategyState extends Object {

    private Candle candle;
    private List<String> params;

    public StrategyState(Candle candle, String... params) {
        this.candle = candle;
        this.params = Arrays.asList(params);
    }

    public String printCSV() {
        String response = candle.printCSV();

        for (String param : params)
            response += ";" + param;

        return response;
    }

    public boolean equals(StrategyState state) {
        if (state == null)
            return false;

        return candle.equals(state.getCandle());
    }
}
