package com.nixmash.web.utils;

/**
 * Created by daveburke on 6/28/17.
 */

import java.io.*;

public class MustacheUtils {

    public static String getContents(File root, String file) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(root, file)), "UTF-8"));
        StringWriter capture = new StringWriter();
        char[] buffer = new char[8192];
        int read;
        while ((read = br.read(buffer)) != -1) {
            capture.write(buffer, 0, read);
        }
        return capture.toString();
    }

    public static File getRoot(String fileName) {
        File file = new File("src/main/resources/templates");
        return new File(file, fileName).exists() ? file : new File("src/main/resources/templates");
    }
}
