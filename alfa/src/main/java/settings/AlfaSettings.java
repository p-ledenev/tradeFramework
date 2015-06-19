package settings;

import model.*;

import java.io.*;

/**
 * Created by ledenev.p on 16.06.2015.
 */
public class AlfaSettings {

    public static String initFile = "d:/Projects/Alfa/java/v1.0/tradeFramework/terminal/alfa.settings";

    public static AlfaGateway createGateway() throws Throwable {

        BufferedReader reader = new BufferedReader(new FileReader(new File(initFile)));

        String login = reader.readLine();
        String password = reader.readLine();
        String market = reader.readLine();
        String account = reader.readLine();
        AlfaTimeframe timeframe = AlfaTimeframe.valueOf(reader.readLine());

        return new AlfaGateway(login, password, market, account, timeframe);
    }
}
