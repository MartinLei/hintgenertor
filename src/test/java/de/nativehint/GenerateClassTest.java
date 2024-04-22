package de.nativehint;

import de.nativehint.service.GenerateClass;
import de.nativehint.valueobject.ReflectionEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GenerateClassTest {

    @Autowired
    public GenerateClass sut;

    @Test
    void generateReflectionHint() throws IOException {
        // setup
        List<String> entryList = List.of(
            "de.dummy.AClass.java",
            "de.dummy.BClass.java");

        // execute
        String result = sut.generateClassSource(entryList);

        // verify
        Path resourceDirectory = Paths.get("src", "test", "resources", "generatedClass", "ReflectionHint.java");
        String expected = Files.readString(resourceDirectory);
        assertEquals(expected, result);
    }


}