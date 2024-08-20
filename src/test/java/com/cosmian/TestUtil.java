package com.cosmian;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author chenrenfu
 * @date 2024/8/14 17:03
 * @packageName:com.gfip.trading
 * @className: TestUtil
 */
public class TestUtil {

    public static String readInputStreamAsString(InputStream inputStream) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
