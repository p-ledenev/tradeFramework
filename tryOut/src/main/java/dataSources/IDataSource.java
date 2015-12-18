package dataSources;

import model.*;
import settings.*;

import java.util.*;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public interface IDataSource {

    String sourceFolder = "sources";
    String sourcePath = InitialSettings.settingPath + sourceFolder;

    List<TryOutCandle> readCandlesFrom(String fileName) throws Throwable;
}
