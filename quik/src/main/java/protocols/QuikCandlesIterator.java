package protocols;

import lombok.AllArgsConstructor;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by dlede on 05.03.2016.
 */

@AllArgsConstructor
public class QuikCandlesIterator implements ICandlesIterator {

    private QuikCandlesGateway gateway;

    @Override
    public List<Candle> getNextCandlesFor(String security, DateTime dateFrom, DateTime dateTo) throws Throwable {
        return null;
    }
}
