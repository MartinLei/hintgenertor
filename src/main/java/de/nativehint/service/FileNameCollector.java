package de.nativehint.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileNameCollector {

    private static final Pattern PACKAGE_REGEX = Pattern.compile("package ((\\w+.)+);");

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
                && excludeList.stream().noneMatch(item -> p.toString().contains(item))
                && includeList.stream().anyMatch(item -> p.getFileName().endsWith(item)))) {

            return pathStream.toList();
        }
    }

    public Map<String, List<String>> getFileList(List<Path> folders) {

        return new TreeMap<>(folders.stream()
            .map(folder -> Map.entry(folder.toString().split(sourcePath)[1],
                getFiles(folder).stream()
                    .map(this::getFullClassPackageName)
                    .sorted()
                    .toList())
            )
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    private List<Path> getFiles(Path folder) {

        try (Stream<Path> fileStream = Files.find(
            folder,
            Integer.MAX_VALUE,
            (p, basicFileAttributes) -> !Files.isDirectory(p))) {
            return fileStream.toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFullClassPackageName(Path filePath) {

        // read first line of file
        String fileLine;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
            fileLine = reader.readLine();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return getFullClassPackageName(filePath, fileLine);
    }

    private String getFullClassPackageName(Path filePath, String firstLine) {

        Matcher matcher = PACKAGE_REGEX.matcher(firstLine);
        if (!matcher.matches()) {
            throw new RuntimeException("No package name found for " + firstLine);
        }
        String packageName = matcher.group(1);


        String[] splitFileNamePath = filePath.toString().split("/");
        String fileName = splitFileNamePath[splitFileNamePath.length - 1];
        String className = fileName.split("\\.")[0];

        return packageName + "." + className;
    }
}
