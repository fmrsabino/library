package bftsmart.demo.adapt.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    public static List<String> readFileLines(String filePath) {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            return stream.collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static String readAllBytes(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public static Map<Integer, PublicKey> readPublicKeys(String folder, String algorithm) throws IOException {
        Map<Integer, PublicKey> result = new HashMap<>();
        Files.walk(Paths.get(folder)).forEach(file -> {
            if (Files.isRegularFile(file)) {
                String fileName = file.getFileName().toString();
                if (fileName.matches("publickey\\d+$")) {
                    int id = Integer.parseInt(fileName.replaceAll("\\D+", ""));
                    result.put(id, SecurityUtils.getPublicKey(
                            folder + System.getProperty("file.separator") + fileName, algorithm));
                }
            }
        });
        return result;
    }
}
