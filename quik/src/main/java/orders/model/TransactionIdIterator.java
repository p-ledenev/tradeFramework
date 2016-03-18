package orders.model;

import org.apache.commons.io.FileUtils;
import org.encog.util.file.FileUtil;
import tools.Log;

import javax.annotation.concurrent.ThreadSafe;
import java.io.*;

/**
 * Created by pledenev on 08.03.2016.
 */

@ThreadSafe // more or less
public class TransactionIdIterator {

    private static TransactionIdIterator iterator;

    private static String filePath = "f:\\tradeFramework\\quik\\data\\";
    private static String fileName = "currentTransactionId.txt";

    private Integer value;

    public static Integer getNext() {
        return getIterator().getNextValue();
    }

    private static TransactionIdIterator getIterator() {

        TransactionIdIterator localInstance = iterator;
        if (localInstance == null) {
            synchronized (TransactionIdIterator.class) {
                localInstance = iterator;
                if (localInstance == null) {
                    localInstance = new TransactionIdIterator();
                    iterator = localInstance;
                }
            }
        }

        return localInstance;
    }

    private TransactionIdIterator() {
        value = readCurrentTransactionId();
    }

    private synchronized Integer getNextValue() {
        writeNewTransactionId(++value);
        return value;
    }

    private Integer readCurrentTransactionId() {
        try {
            String stringTransactionId = FileUtils.readFileToString(new File(filePath + fileName));
            return Integer.parseInt(stringTransactionId);

        } catch (IOException e) {
            Log.debug(e.getMessage());

            return 1;
        }
    }

    private void writeNewTransactionId(Integer transactionId) {
        try {
            FileUtil.writeFileAsString(new File(filePath + fileName), transactionId.toString());
        } catch (IOException e) {
            Log.debug(e.getMessage());
        }
    }
}
