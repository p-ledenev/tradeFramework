package settings;

import model.*;
import org.joda.time.*;
import run.*;

import java.io.*;
import java.util.*;

/**
 * Created by ledenev.p on 19.06.2015.
 */
public class PortfoliosInitializer {

    public static String initFile = "init.dat";

    private List<IPortfolioBuilder> builders;
    private DateTime lastUpdate;

    public PortfoliosInitializer() throws Throwable {
        builders = new ArrayList<>();
        lastUpdate = DateTime.now();
        initialize();
    }

    public IPortfolioBuilder createBuilder() {
        return new ConciseFormatPortfolioBuilder();
    }

    private void initialize() throws Throwable {

        BufferedReader reader = getReader();

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty())
                continue;

            IPortfolioBuilder builder = createBuilder();
            builder.build(line.trim());
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty())
                    break;

                builder.addMachine(line);
            }

            builders.add(builder);
        }
    }

    public void reread() throws Throwable {

        BufferedReader reader = getReader();

        int i = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty())
                continue;

            int k = 0;
            IPortfolioBuilder builder = builders.get(i++);
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty())
                    break;

                Machine machine = builder.getPortfolio().getMachine(k++);
                boolean isBlocked = builder.isMachineBlocked(line);
                machine.setBlocked(isBlocked);
            }
        }
    }

    public List<Portfolio> getPortfolios() {

        List<Portfolio> portfolios = new ArrayList<Portfolio>();
        for (IPortfolioBuilder builder : builders)
            portfolios.add(builder.getPortfolio());

        return portfolios;
    }

    public void write() throws Throwable {

        PrintWriter writer = getWriter();

        for (IPortfolioBuilder builder : builders)
            writer.print(builder.serialize() + "\n");

        writer.close();
        lastUpdate = DateTime.now();
    }

    public BufferedReader getReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(new File(Runner.dataPath + initFile)));
    }

    public PrintWriter getWriter() throws FileNotFoundException {
        return new PrintWriter(Runner.dataPath + initFile);
    }

    public boolean initFileWasModified() {
        File file = new File(Runner.dataPath + initFile);
        DateTime lastInitFileUpdate = new DateTime(file.lastModified());

        return lastUpdate.isBefore(lastInitFileUpdate);
    }
}
