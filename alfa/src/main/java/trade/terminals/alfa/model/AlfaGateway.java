package trade.terminals.alfa.model;

import com.jacob.activeX.*;
import com.jacob.com.*;
import trade.terminals.alfa.exceptions.*;
import org.joda.time.*;
import org.joda.time.format.*;
import trade.core.tools.*;
import trade.core.exceptions.UnsupportedDirection;
import trade.core.model.Candle;

import java.io.*;
import java.util.*;

/**
 * Created by ledenev.p on 27.03.2015.
 */
public class AlfaGateway {

    public LocalTime tradeFrom = new LocalTime(10, 0);
    public LocalTime tradeTo = new LocalTime(23, 45);

    private ActiveXComponent connector;

    private String login;
    private String password;

    private String market;
    private String account;
    private AlfaTimeframe timeframe;

    public AlfaGateway(String login, String password, String market, String account, AlfaTimeframe timeframe) throws Throwable {
        initJacobLibrary();

        this.login = login;
        this.password = password;

        this.market = market;
        this.account = account;
        this.timeframe = timeframe;

        connector = new ActiveXComponent("clsid:{A0AD8986-E9EF-4340-B0AB-062D7A2966F5}");
    }

    protected void initJacobLibrary() throws Throwable {

        InputStream inputStream = AlfaGateway.class.getResourceAsStream("/jacob-1.18-M2-x86.dll");

        File temporaryDll = File.createTempFile("jacob", ".dll");
        FileOutputStream outputStream = new FileOutputStream(temporaryDll);

        byte[] array = new byte[8192];
        for (int i = inputStream.read(array); i != -1; i = inputStream.read(array)) {
            outputStream.write(array, 0, i);
        }
        outputStream.close();

        System.setProperty(LibraryLoader.JACOB_DLL_PATH, temporaryDll.getAbsolutePath());
        LibraryLoader.loadJacobLibrary();
    }

    public boolean isConnected() {
        return connector.invoke("Connected").getBoolean();
    }

    public void connect() {
        if (isConnected())
            return;

        connector.setProperty("UserName", variant(login));
        connector.setProperty("Password", variant(password));
        connector.setProperty("Connected", variant(true));

        try {
            Log.info("Connection is lost. Trying reconnect within 10sec");
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            Log.error("interrupted", e);
        }
    }

    public void dropOrder(int orderId) throws AlfaGatewayFailure {
        connect();

        Variant nil = variant(null);

        connector.invoke("DropOrder", variant(orderId), nil, nil, nil, nil, nil, variant(3));

        String resultMessage = getLastOperationMessage();
        if (!isLastOperationSucceed() || resultMessage == null || !(resultMessage.contains("удалена") || resultMessage.contains("удалению")))
            throw new AlfaGatewayFailure(logMessage("DropOrder"));
    }

    public double loadLastValueFor(String security) throws LoadLastValueFailure, AlfaGatewayFailure {
        connect();

        String conditions = "p_code in (\"" + security + "\")";
        Variant response = connector.invoke("GetLocalDBData", variant("fin_info"), variant("last_price, status"), variant(conditions));

        if (!isLastOperationSucceed())
            throw new AlfaGatewayFailure(logMessage("LoadLastValue"));

        if (response == null)
            throw new LoadLastValueFailure("LoadLastValue: no data received");

        String[] data = response.getString().split("\\|");

        if (!data[1].equals("6"))
            throw new LoadLastValueFailure("LoadLastValue: no trades started");

        return Double.parseDouble(data[0]);
    }

    public String loadStatus(int orderId) throws AlfaGatewayFailure, LoadOrderStatusFailure {
        connect();

        Variant response = connector.invoke("GetLocalDBData", variant("trades"), variant("price, qty"), variant("ord_no = " + orderId));

        if (!isLastOperationSucceed())
            throw new AlfaGatewayFailure(logMessage("LoadOrderStatus"));

        if (response == null)
            throw new LoadOrderStatusFailure("LoadOrderStatus: no data received");

        return response.getString();
    }

    public int submit(AlfaOrder order) throws AlfaGatewayFailure, OrderSubmissionFailure, UnsupportedDirection {
        return submit(order.getSecurity(), order.getAlfaDirection(), order.getVolume(), order.getValue());
    }

    public int submit(String security, AlfaOrderDirection direction, int volume, double value)
            throws AlfaGatewayFailure, OrderSubmissionFailure {
        connect();

        Variant nil = new Variant();
        DateTime date = DateTime.now().plusDays(1);

        Variant response = connector.invoke("CreateLimitOrder", variant(account), variant(market), variant(security),
                variant(date.toDate()), variant(""), variant("RUR"), variant(direction.name()), variant(volume), variant(value),
                nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, variant(11));

        if (!isLastOperationSucceed()) {
            if (getLastOperationMessage().contains("Время ожидания ответа сервера истекло"))
                throw new OrderSubmissionFailure(logMessage("CreateLimitOrder"));
            else
                throw new AlfaGatewayFailure(logMessage("CreateLimitOrder"));
        }

        if (response == null || response.getInt() <= 0)
            throw new OrderSubmissionFailure("CreateLimitOrder: no data received");

        return response.getInt();
    }

    public int loadSecurityVolume(String security) throws AlfaGatewayFailure {
        connect();

        Variant response = connector.invoke("GetLocalDBData", variant("balance"), variant("real_rest"),
                variant("acc_code in (\"" + account + "\") and p_code in (\"" + security + "\")"));

        if (!isLastOperationSucceed() || response == null)
            throw new AlfaGatewayFailure(logMessage("LoadSecurityVolume"));

        String[] volume = response.getString().split("\\|");

        if (volume[0].isEmpty())
            throw new AlfaGatewayFailure("LoadSecurityVolume: no data received");

        return Integer.parseInt(volume[0]);
    }

    public List<Candle> loadMarketData(String security, DateTime dateFrom, DateTime dateTo) throws AlfaGatewayFailure {
        connect();

        Variant response = connector.invoke("GetArchiveFinInfo", variant(market), variant(security), variant(timeframe.getCode()),
                variant(dateFrom.toDate()), variant(dateTo.toDate()), variant(3), variant(20));

        if (!isLastOperationSucceed() || response == null)
            return new ArrayList<Candle>();

        return parseMarketData(response.toString());
    }

    public String loadVersion() {
        connect();

        Variant response = connector.invoke("Version");

        return response.toString();
    }

    protected List<Candle> parseMarketData(String data) {

        DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        List<Candle> candles = new ArrayList<Candle>();

        String[] result = data.split("\r\n");
        for (String line : result) {

            line = line.trim();
            if (line.isEmpty())
                continue;

            String[] strCandle = line.split("\\|");

            DateTime date = format.parseDateTime(strCandle[0]);
            double value = Double.parseDouble(strCandle[4]);
            Candle candle = new Candle(date, value);

            if (candle.isTimeInRange(tradeFrom, tradeTo))
                candles.add(candle);
        }

        return candles;
    }

    protected boolean isLastOperationSucceed() {

        int lastResultCode = connector.invoke("LastResult").getInt();
        return lastResultCode == 0;
    }

    protected String getLastOperationMessage() {

        String message = connector.invoke("LastResultMsg").getString();
        return message != null ? message : "No message";
    }

    protected String logMessage(String operation) {

        String message = operation + ": " + getLastOperationMessage();
        return message;
    }

    protected Variant variant(Object o) {
        return new Variant(o);
    }

    public void readConsolePassword() {
        Console console = System.console();

        char[] chars = console.readPassword("Enter password: ");
        this.password = new String(chars);
    }

    public void readScannerPassword() {
        Scanner scanner = new Scanner(System.in);

        Log.info("Enter password: ");
        this.password = scanner.nextLine();
    }

    public void readPassword() {
        if (System.console() != null)
            readConsolePassword();
        else
            readScannerPassword();
    }
}
