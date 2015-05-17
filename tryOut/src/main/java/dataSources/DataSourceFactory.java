package dataSources;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class DataSourceFactory {

    public static IDataSource createDataSource() {
        return new FinamDataSource();
    }
}
