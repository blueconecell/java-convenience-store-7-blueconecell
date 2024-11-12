package store.util;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MarkdownReader {

    public static String mdReader(String url) {
        try {
            return new String(Files.readAllBytes(Paths.get(url)));
        } catch (Exception e) {
            return "";
        }
    }
}
