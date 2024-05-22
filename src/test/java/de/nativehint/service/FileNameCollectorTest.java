package de.nativehint.service;

import de.nativehint.valueobject.HintEntry;
import de.nativehint.valueobject.HintType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FileNameCollectorTest {

    @Autowired
    public FileNameCollector sut;

    String sourcePath;

    @BeforeEach
    void setUp() {
        sourcePath = Paths.get("src", "test", "resources", "dummySourcePath").toFile().getAbsolutePath();
        ReflectionTestUtils.setField(sut, "sourcePath", sourcePath);
    }

    @Test
    void getFolders() throws IOException {

        // execute
        List<String> result = sut.getFolders().stream()
            .map(Path::toString)
            .toList();

        // verify
        List<String> expected = List.of(
            sourcePath + "/valueobject",
            sourcePath + "/valueobject/dto",
            sourcePath + "/valueobject/dto/valueobject", // will be filter out later (empty folder)
            sourcePath + "/valueobject/helper"
        );

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getFileList() throws IOException {
        // setup
        List<Path> folder = sut.getFolders();


        // execute
        List<HintEntry> result = sut.getFileList(folder);

        // verify
        List<HintEntry> expected = List.of(
            new HintEntry(HintType.reflection, "/valueobject", List.of(
                "de.valueobject.ShouldBeFound1",
                "de.valueobject.ShouldBeFound1$InnerClassShouldBeFound1",
                "de.valueobject.ShouldBeFound1$InnerClassShouldBeFound2",
                "de.valueobject.ShouldBeFound1$InnerEnum",
                "de.valueobject.ShouldBeFound2")),
            new HintEntry(HintType.reflection, "/valueobject/dto", List.of("de.valueobject.dto.ShouldBeFound3")),
            new HintEntry(HintType.reflection, "/valueobject/helper", List.of("de.valueobject.helper.ShouldBeFound4"))
        );

        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }
}