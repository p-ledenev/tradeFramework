package trade.tryOut.dataSources;


import trade.tryOut.model.TryOutCandle;
import trade.tryOut.settings.InitialSettings;

import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public interface IDataSource {

    String sourceFolder = "sources";
    String sourcePath = InitialSettings.settingPath + sourceFolder;

    List<TryOutCandle> readCandlesFrom(String fileName) throws Throwable;
}
