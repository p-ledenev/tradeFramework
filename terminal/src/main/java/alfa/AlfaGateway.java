package alfa;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.LibraryLoader;
import com.jacob.com.Variant;
import model.Candle;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ledenev.p on 27.03.2015.
 */
public class AlfaGateway {

    public LocalTime tradeFrom = new LocalTime(10, 0);
    public LocalTime tradeTo = new LocalTime(23, 45);

    protected ActiveXComponent connector;
    protected String login;
    protected String password;

    public AlfaGateway(String login, String password) throws Throwable {
        initJacobLibrary();

        this.login = login;
        this.password = password;

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
    }

    public void dropOrder(int orderId) throws AlfaGatewayFailure {
        connect();

        Variant nil = variant(null);

        connector.invoke("DropOrder", variant(orderId), nil, nil, nil, nil, nil, variant(3));

        String resultMessage = getLastOperationMessage();
        if (!isLastOperationSucceed() || resultMessage == null || !resultMessage.contains("удалена"))
            throw new AlfaGatewayFailure(logMessage("DropOrder"));
    }

    public double loadLastValue(String ticket) throws AlfaGatewayFailure {
        connect();

        String conditions = "p_code in (\"" + ticket + "\")";
        Variant response = connector.invoke("GetLocalDBData", variant("fin_info"), variant("last_price, status"), variant(conditions));

        if (!isLastOperationSucceed())
            throw new AlfaGatewayFailure(logMessage("LoadLastValue"));

        if (response == null)
            throw new AlfaGatewayFailure("LoadLastValue: no data received");

        String[] data = response.getString().split("\\|");

        if (data[1].equals("6"))
            throw new AlfaGatewayFailure("LoadLastValue: no trades started");

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

    public int submit(String account, String security, String market, AlfaOrderDirection direction, int volume, double value)
            throws AlfaGatewayFailure, OrderSubmitionFailure, UnsupportedDirection {
        connect();

        Variant nil = variant(null);
        Date date = DateTime.now().plusDays(1).toDate();

        Variant response = connector.invoke("CreateLimitOrder", variant(account), variant(market), variant(security),
                variant(date), variant(""), variant("RUR"), variant(direction.name()), variant(volume), variant(value),
                nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, variant(11));

        if (!isLastOperationSucceed())
            throw new AlfaGatewayFailure(logMessage("CreateLimitOrder"));

        if (response == null || response.getInt() <= 0)
            throw new OrderSubmitionFailure("CreateLimitOrder: no data received");

        return response.getInt();
    }

    public int loadSecurityVolume(String account, String security) throws AlfaGatewayFailure {
        connect();

        Variant response = connector.invoke("GetLocalDBData", variant("balance"), variant("real_rest"),
                variant("acc_code in (\"" + account + "\") and p_code in (\"" + security + "\")"));

        if (!isLastOperationSucceed() || response == null)
            throw new AlfaGatewayFailure(logMessage("LoadSecurityVolume"));

        String[] volume = response.getString().split("\\|");

        return Integer.parseInt(volume[0]);
    }

    public List<Candle> loadMarketData(String security, String market, AlfaTimeframe timeframe, DateTime dateFrom, DateTime dateTo) throws AlfaGatewayFailure {
        connect();

        Variant response = connector.invoke("GetArchiveFinInfo", variant(market), variant(security), variant(timeframe.getCode()),
                variant(dateFrom), variant(dateTo), variant(3), variant(20));

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

        DateTimeFormatter format = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
        List<Candle> candles = new ArrayList<Candle>();

        String[] result = data.split("\n\r");
        for (String line : result) {
            String[] strCandle = line.split("|");

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
}
