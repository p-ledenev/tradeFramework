package trade.terminals.core.initialization.testing;

import org.junit.*;
import trade.core.model.Portfolio;
import trade.terminals.core.settings.PortfoliosInitializer;

import java.io.*;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Created by ledenev.p on 24.06.2015.
 */
public class ConciseFormatPortfolioBuilderTest {

	List<Portfolio> portfolios;

	@Before
	public void setUp() throws Throwable {
		PortfoliosInitializer initializer = new PortfoliosInitializer() {

			@Override
			public BufferedReader getReader() throws FileNotFoundException {

				String content = "USD-9.15;AveragingStrategy;0.0145;1;intraday\n" +
						"50;N;0\n" +
						"75;N;0\n" +
						"150;N;0\n" +
						"100;N;0\n" +
						"125;N;0\n" +
						"150;N;0\n" +
						"125;N;0\n" +
						"\n" +
						"USD-9.15;ApproximationStrategy;0.0145;27;continuous\n" +
						"250;N;0\n" +
						"275;N;0\n" +
						"275;N;0";

				return new BufferedReader(new StringReader(content));
			}
		};

		portfolios = initializer.getPortfolios();
	}

	@Test
	public void shouldReadAllPortfolios() throws Throwable {
		assertThat(portfolios.size(), equalTo(2));
	}

	@Test
	public void shouldReadAllMachines() throws Throwable {
		assertThat(portfolios.get(0).getMachines().size(), equalTo(7));
		assertThat(portfolios.get(1).getMachines().size(), equalTo(3));
	}

	@Test
	public void shouldReadTitle() throws Throwable {
		assertThat(portfolios.get(0).getTitle(), equalTo("AveragingStrategy_USD-9.15"));
	}

	@Test
	public void shouldReadStrategyName() throws Throwable {
		assertThat(portfolios.get(0).getDecisonStrategyName(), equalTo("AveragingStrategy"));
	}

	@Test
	public void shouldReadIntradayTrading() throws Throwable {
		assertTrue(portfolios.get(0).isIntradayTrading());
	}

	@Test
	public void shouldReadContinuousTrading() throws Throwable {
		assertFalse(portfolios.get(1).isIntradayTrading());
	}

	@Test
	public void shouldReadSecurity() throws Throwable {
		assertThat(portfolios.get(0).getSecurity(), equalTo("USD-9.15"));
	}

	@Test
	public void shouldReadLot() throws Throwable {
		assertThat(portfolios.get(0).getLot(), equalTo(1));
	}

	@Test
	public void shouldReadSieveParam() throws Throwable {
		assertThat(portfolios.get(0).getCandlesStorage().getSiftStrategy().getSieveParam(), equalTo(0.0145));
	}

	@Test
	public void shouldReadFillingGapsNumber() throws Throwable {
		assertThat(portfolios.get(0).getCandlesStorage().getSiftStrategy().getFillingGapsNumber(), equalTo(1));
	}

	@Test
	public void shouldSetPortfolioToMachine() throws Throwable {
		assertThat(portfolios.get(0), equalTo(portfolios.get(0).getMachines().get(3).getPortfolio()));
	}
}
