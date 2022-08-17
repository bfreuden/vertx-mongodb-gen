package org.bfreuden;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Copyright {

    public static String COPYRIGHT;
    static {
        try (InputStream is = Copyright.class.getResourceAsStream("/copyright.txt")) {
            InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            StringBuilder copyright = new StringBuilder();
            char[] cbuf = new char[1024];
            int nb;
            while ((nb = inputStreamReader.read(cbuf)) != -1) {
                copyright.append(cbuf, 0, nb);
            }
            COPYRIGHT = copyright.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
