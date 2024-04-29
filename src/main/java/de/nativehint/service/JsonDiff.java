package de.nativehint.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.nativehint.valueobject.ReflectionEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JsonDiff {

    @Value("#{'${hintsgenerator.diff.excludePackageList}'.split(',')}")
    private List<String> excludePackageList;

    private List<ReflectionEntry> generateDiff(File extendedMetaInfoJson, File baseMetaInfoJson) {

        List<ReflectionEntry> extendedList = readFile(extendedMetaInfoJson);
        List<ReflectionEntry> baseList = readFile(baseMetaInfoJson);

        List<ReflectionEntry> diff = extendedList.stream()
            .filter(entry -> !baseList.contains(entry))
            .toList();

        log.info(String.format("Found %s entries", diff.size()));
        return diff;
    }

    private List<ReflectionEntry> readFile(File fileJson) {
        ObjectMapper mapper = new ObjectMapper();
        List<ReflectionEntry> entries;
        try {
            entries = mapper.readValue(fileJson, new TypeReference<>() {
            });
        } catch (IOException ex) {
            throw new RuntimeException("Could not parse json files", ex);
        }
        return entries;
    }

    public List<String> generate_ReflectionConfig(File fileJson) {
        return filter(readFile(fileJson));
    }

    public List<String> generateDiff_ReflectionConfig(File extendedMetaInfoJson, File baseMetaInfoJson) {

        return filter(generateDiff(extendedMetaInfoJson, baseMetaInfoJson));

    }

    private List<String> filter(List<ReflectionEntry> list) {
        return list.stream()
            .map(ReflectionEntry::getName)
            .filter(entry -> !entry.endsWith("_"))
            .filter(entry -> !entry.endsWith("Test"))
            .filter(entry -> !entry.endsWith("package-info"))
            .filter(entry -> excludePackageList.stream()
                .noneMatch(entry::startsWith)
            )
            .filter(entry -> !entry.contains("_"))
            .filter(entry -> !entry.endsWith(".1"))
            .toList();
    }


}
