package de.nativehint.service;

import de.nativehint.helper.FileHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JsonDiffTest {

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private JsonDiff sut;

    private File baseReflectionJson;
    private File extendedReflectionJson;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        String basePath = Paths.get("src", "test", "resources", "exampleJson").toFile().getAbsolutePath();
        baseReflectionJson = fileHelper.getFile(basePath + "/base-reflect-config.json");
        extendedReflectionJson = fileHelper.getFile(basePath + "/extended-reflect-config.json");
    }

    @Test
    void generateDiff_ReflectionConfig() {

        // execute
        List<String> result = sut.generateDiff_ReflectionConfig(extendedReflectionJson, baseReflectionJson);


        // verify
        List<String> expected = List.of("de.test.ShouldBeFound");
        assertThat(result).isEqualTo(expected);
    }


}