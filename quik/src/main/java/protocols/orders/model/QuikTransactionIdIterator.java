package protocols.orders.model;

import org.encog.util.file.FileUtil;

import java.io.*;

/**
 * Created by dlede on 08.03.2016.
 */
public class QuikTransactionIdIterator {

    private static QuikTransactionIdIterator iterator;
    private static String fileName = "currentTransactionId.txt";

    private Integer transactionId;

    public static

    private QuikTransactionIdIterator() throws QuikTransactionIdStorageNotFound {
        transactionId = readCurrentTransactionId();
    }

    private synchronized Integer getNext() throws QuikTransactionIdStorageNotFound {
        writeNewTransactionId(transactionId++);

        return transactionId;
    }

    private Integer readCurrentTransactionId() throws QuikTransactionIdStorageNotFound {
        try {
            String stringTransactionId = FileUtil.readFileAsString(new File(fileName));
            return Integer.parseInt(stringTransactionId);

        } catch (IOException e) {
            throw new QuikTransactionIdStorageNotFound();
        }
    }

    private void writeNewTransactionId(Integer transactionId) throws QuikTransactionIdStorageNotFound {
        try {
            FileUtil.writeFileAsString(new File(fileName), transactionId.toString());
        } catch (IOException e) {
            throw new QuikTransactionIdStorageNotFound();
        }
    }
}
