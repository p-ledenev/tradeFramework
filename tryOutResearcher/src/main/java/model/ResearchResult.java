package model;

import lombok.*;
import tools.*;

/**
 * Created by ledenev.p on 16.12.2015.
 */

@AllArgsConstructor
public class ResearchResult {

    public static String header = "sieveParam;gapsNumber;tradeCoefficient";

    private double sieveParam;
    private int gapsNumber;
    private double tradeCoefficient;

    public String print() {
        return Round.toSignificant(sieveParam) + ";" + gapsNumber + ";" + Round.toMoneyAmount(tradeCoefficient);
    }

}
