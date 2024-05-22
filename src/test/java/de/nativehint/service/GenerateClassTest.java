package de.nativehint.service;

import de.nativehint.valueobject.HintEntry;
import de.nativehint.valueobject.HintType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GenerateClassTest {

    @Autowired
    public GenerateClass sut;

    @Test
    void generateReflectionHint() throws IOException {
        // setup
        List<String> entries = List.of(
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.AClass.java",
            "de.dummy.newBlockAClass.java",
            "de.dummy.newBlockAClass.java"
            );
        List<HintEntry> hints = List.of(
            new HintEntry(HintType.reflection, "b comment", List.of("de.dummy.DClass.java")),
            new HintEntry(HintType.serialization, null, entries),
            new HintEntry(HintType.reflection, "a comment", entries)
        );

        // execute
        String result = sut.generateClassSource(hints);

        // verify
        Path resourceDirectory = Paths.get("src", "test", "resources", "generatedClass", "ReflectionHint.java");
        String expected = Files.readString(resourceDirectory);

        assertThat(expected).isEqualTo(result);
    }


}