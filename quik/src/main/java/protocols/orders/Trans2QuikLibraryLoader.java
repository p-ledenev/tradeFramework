package protocols.orders;

import com.sun.jna.Function;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arsentii Nerushev on 14.05.2015.
 *
 * @version 1.0.0
 */
public final class Trans2QuikLibraryLoader {

    private static final Map<Object, Object> names = new HashMap<Object, Object>();

    public static final Trans2QuikLibrary library;

    static {
        names.put(Library.OPTION_FUNCTION_MAPPER, StdCallLibrary.FUNCTION_MAPPER);
        names.put(Library.OPTION_CALLING_CONVENTION, Function.C_CONVENTION);

        System.setProperty("java.library.path", "lib/win32-x86");

        library = (Trans2QuikLibrary) Native.loadLibrary("TRANS2QUIK", Trans2QuikLibrary.class, names);
    }

    private Trans2QuikLibraryLoader() {
        throw new AssertionError("This should never be invoked.");
    }
}
