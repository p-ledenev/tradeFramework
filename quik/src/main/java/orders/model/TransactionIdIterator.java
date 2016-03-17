package orders.model;

import org.apache.commons.io.FileUtils;
import org.encog.util.file.FileUtil;

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

    public static Integer getNext() throws TransactionIdStorageNotFound {
        return getIterator().getNextValue();
    }

    private static TransactionIdIterator getIterator() throws TransactionIdStorageNotFound {

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

    private TransactionIdIterator() throws TransactionIdStorageNotFound {
        value = readCurrentTransactionId();
    }

    private synchronized Integer getNextValue() throws TransactionIdStorageNotFound {
        writeNewTransactionId(++value);
        return value;
    }

    private Integer readCurrentTransactionId() throws TransactionIdStorageNotFound {
        try {
            String stringTransactionId = FileUtils.readFileToString(new File(filePath + fileName));
            return Integer.parseInt(stringTransactionId);

        } catch (IOException e) {
            throw new TransactionIdStorageNotFound();
        }
    }

    private void writeNewTransactionId(Integer transactionId) throws TransactionIdStorageNotFound {
        try {
            FileUtil.writeFileAsString(new File(filePath + fileName), transactionId.toString());
        } catch (IOException e) {
            throw new TransactionIdStorageNotFound();
        }
    }
}
