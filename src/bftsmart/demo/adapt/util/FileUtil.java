package bftsmart.demo.adapt.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {
    public static List<String> readFileLines(String filePath) {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            return stream.collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
