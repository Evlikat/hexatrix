package net.evlikat.hexatrix.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 17, 2014)
 */
public class IOUtils {

    public IOUtils() {
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ex) {
            // That's OK
        }
    }
}
