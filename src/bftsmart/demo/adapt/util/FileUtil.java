package bftsmart.demo.adapt.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtil {
    public static List<String> readFileLines(String filePath) {
        List<String> result = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(result::add);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
