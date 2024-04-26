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

    private List<ReflectionEntry> generateDiff(File agentJson, File springJson) {

        ObjectMapper mapper = new ObjectMapper();

        List<ReflectionEntry> agentList;
        List<ReflectionEntry> springList;
        try {
            agentList = mapper.readValue(agentJson, new TypeReference<>() {
            });
            springList = mapper.readValue(springJson, new TypeReference<>() {
            });
        } catch (IOException ex) {
            throw new RuntimeException("Could not parse json files", ex);
        }

        List<ReflectionEntry> diff = agentList.stream()
            .filter(entry -> !springList.contains(entry))
            .toList();

        log.info(String.format("Found %s entries", diff.size()));
        return diff;
    }

    public List<String> generateDiff_ReflectionConfig(File agentJson, File springJson) {

        return generateDiff(agentJson, springJson).stream()
            .map(ReflectionEntry::getName)
            .filter(entry -> !entry.endsWith("_"))
            .filter(entry -> !entry.endsWith("Test"))
            .filter(entry -> !entry.endsWith("package-info"))
            .filter(entry -> excludePackageList.stream()
                .noneMatch(filterEntry -> entry.startsWith(filterEntry))
            )
            .filter(entry -> !entry.contains("_"))
            .filter(entry -> !entry.endsWith(".1"))
            .map(path -> path.replace("$", "."))

            .filter(entry -> entry.contains("intramo"))
            .toList();

    }
}
