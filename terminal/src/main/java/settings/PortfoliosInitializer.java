package settings;

import model.*;
import run.*;

import java.io.*;
import java.util.*;

/**
 * Created by ledenev.p on 19.06.2015.
 */
public class PortfoliosInitializer {

    public static String initFile = "init.dat";

    private List<PortfolioBuilder> builders;

    public PortfoliosInitializer() throws Throwable {
        builders = new ArrayList<PortfolioBuilder>();
        initialize();
    }

    private void initialize() throws Throwable {

        BufferedReader reader = getReader();

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

                builder.addMachine(line);
            }

            builders.add(builder);
        }
    }

    public List<Portfolio> getPortfolios() {

        List<Portfolio> portfolios = new ArrayList<Portfolio>();
        for (PortfolioBuilder builder : builders)
            portfolios.add(builder.getPortfolio());

        return portfolios;
    }

    public void write() throws Throwable {

        PrintWriter writer = getWriter();

        for (PortfolioBuilder builder : builders) {
            writer.print(builder.serialize() + "\n");
        }

        writer.close();
    }

    public BufferedReader getReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(new File(Runner.dataPath + initFile)));
    }

    public PrintWriter getWriter() throws FileNotFoundException {
        return new PrintWriter(Runner.dataPath + initFile);
    }
}
