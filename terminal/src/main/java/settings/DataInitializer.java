package settings;

import model.*;
import run.*;

import java.io.*;
import java.util.*;

/**
 * Created by ledenev.p on 19.06.2015.
 */
public class DataInitializer {

    public static String initFile = "init.dat";

    public static List<Portfolio> initialize() throws Throwable {

        List<Portfolio> portfolios = new ArrayList<Portfolio>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(Runner.dataPath + "/" + initFile)));

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty())
                continue;

            PortfolioBuilder builder = new PortfolioBuilder(line);
            builder.create();
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty())
                    break;

                builder.init(line);
            }

            portfolios.add(builder.getPortfolio());
        }

        return portfolios;
    }

    public static void write(List<Portfolio> portfolios) throws Throwable {

        PrintWriter writer = new PrintWriter(Runner.dataPath + "/" + initFile);

        for (Portfolio portfolio : portfolios) {
            PortfolioBuilder builder = new PortfolioBuilder(portfolio);
            writer.println(builder.serialize() + "\n");
        }

        writer.close();
    }
}
