package de.nativehint.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class FileNameCollector {

    private static final Pattern PACKAGE_REGEX = Pattern.compile("package ((\\w+.)+);");
    private static final Pattern INNER_CLASS_REGEX = Pattern
        .compile("\\W+public static class (\\w+)\\W(\\w+ \\w+ )?\\{");
    private static final Pattern INNER_ENUM_REGEX = Pattern
        .compile("\\W+public enum (\\w+) \\{");

    @Value("${hintsgenerator.folder.sourcePath}")
    private String sourcePath;

    @Value("#{'${hintsgenerator.folder.includeList}'.split(',')}")
    private List<String> includeList;

    @Value("#{'${hintsgenerator.folder.excludeList}'.split(',')}")
    private List<String> excludeList;

    public List<Path> getFolders() throws IOException {

        Path sourcePathObj = Path.of(sourcePath);
        try (Stream<Path> pathStream = Files.find(
            sourcePathObj,
            Integer.MAX_VALUE,
            (p, basicFileAttributes) -> Files.isDirectory(p)
                && includeList.stream().anyMatch(item -> p.toString().contains(item))
                && excludeList.stream().noneMatch(item -> p.toString().contains(item)))) {

            return pathStream.toList();
        }
    }

    public Map<String, List<String>> getFileList(List<Path> folders) {

        return new TreeMap<>(folders.stream()
            .map(folder -> Map.entry(folder.toString().split(sourcePath)[1],
                getFiles(folder).stream()
                    .flatMap(file -> getAllClassNames(file).stream())
                    .sorted()
                    .toList())
            ).filter(entry -> !entry.getValue().isEmpty())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    private List<Path> getFiles(Path folder) {

        try (Stream<Path> fileStream = Files.find(
            folder,
            1,
            (p, basicFileAttributes) -> !Files.isDirectory(p))) {
            return fileStream.toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getAllClassNames(Path filePath) {
        if(!"java".equals(StringUtils.getFilenameExtension(filePath.toString()))){
            return new ArrayList<>();
        }

        String className = getClassName(filePath);
        return getAllClassNames(filePath, className);
    }

    private List<String> getAllClassNames(Path filePath, String className) {
        List<String> classNames = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
            String line = reader.readLine();

            String packageName = getPackageName(line);
            classNames.add(packageName + "." + className);

            while (line != null) {
                line = reader.readLine();
                if (!StringUtils.hasText(line)) {
                    continue;
                }

                Matcher matcher = INNER_CLASS_REGEX.matcher(line);
                if (matcher.matches()) {
                    String innerClassName = matcher.group(1);
                    classNames.add(packageName + "." + className + "$" + innerClassName);
                    log.info("Found inner class " + innerClassName);
                    continue;
                }

                matcher = INNER_ENUM_REGEX.matcher(line);
                if (matcher.matches()) {
                    String innerEnumName = matcher.group(1);
                    classNames.add(packageName + "." + className + "$" + innerEnumName);
                    log.info("Found inner enum " + innerEnumName);
                }

            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return classNames;
    }

    private String getPackageName(String firstLine) {
        Matcher matcher = PACKAGE_REGEX.matcher(firstLine);
        if (!matcher.matches()) {
            throw new RuntimeException("No package name found for " + firstLine);
        }
        return matcher.group(1);
    }

    private String getClassName(Path filePath) {
        String[] splitFileNamePath = filePath.toString().split("/");
        String fileName = splitFileNamePath[splitFileNamePath.length - 1];
        return fileName.split("\\.")[0];
    }
}
