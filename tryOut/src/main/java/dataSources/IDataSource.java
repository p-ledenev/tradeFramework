package dataSources;

import model.TryOutCandle;
import settings.InitialSettings;

import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public interface IDataSource {

    String sourcePath = InitialSettings.settingPath + "/sources";

    List<TryOutCandle> readCandlesFrom(String fileName) throws Throwable;
}
