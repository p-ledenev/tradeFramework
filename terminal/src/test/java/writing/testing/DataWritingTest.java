package writing.testing;

import org.junit.*;
import settings.*;

import java.io.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ledenev.p on 24.06.2015.
 */
public class DataWritingTest {

    String content = "USD-9.15\tFORTS_USD_DEV_1\tAveragingStrategy\t1\t0.0145\t0.5\n" +
            "50\t11.2\t0\t11.06.2006 10:10:56\tN\t3.0\t6\n" +
            "75\t0.0\t1\t01.05.2011 12:33:35\tN\t34.5\t45\n" +
            "100\t343.2\t0\t01.01.2000 00:00:00\tN\t0.0\t0\n" +
            "125\t105.3\t1\t02.03.2005 12:34:47\tB\t82.5\t1000\n" +
            "150\t10.0\t1\t18.01.2008 10:45:12\tN\t44.6\t100\n" +
            "175\t760.9\t0\t22.01.2009 22:22:12\tN\t76.6\t4\n" +
            "\n" +
            "USD-9.15\tFORTS_USD_APP_1\tApproximationStrategy\t1\t0.0145\t0.5\n" +
            "50\t50.5\t1\t14.01.2004 06:05:05\tN\t45.6\t23\n" +
            "100\t78.8\t0\t02.04.2012 12:45:10\tN\t44.0\t11\n" +
            "125\t91.3\t0\t01.05.2015 01:03:00\tN\t12.2\t45\n" +
            "\n";

    PortfoliosInitializer initializer;
    StringWriter resultWriter;

    @Before
    public void setUp() throws Throwable {

        resultWriter = new StringWriter();

        initializer = new PortfoliosInitializer() {

            @Override
            public BufferedReader getReader() throws FileNotFoundException {
                return new BufferedReader(new StringReader(content));
            }

            @Override
            public PrintWriter getWriter() throws FileNotFoundException {
                return new PrintWriter(resultWriter);
            }
        };
    }

    @Test
    public void writeResults() throws Throwable {

        initializer.write();

        assertThat(resultWriter.getBuffer().toString(), is(equalTo(content)));
    }
}
