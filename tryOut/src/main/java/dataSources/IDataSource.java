package dataSources;

import model.Candle;
import settings.InitialSettings;

import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public interface IDataSource {

    String sourcePath = InitialSettings.settingPath + "/sources";

    List<Candle> readCandlesFrom(String fileName) throws Throwable;
}
