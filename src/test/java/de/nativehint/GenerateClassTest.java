package de.nativehint;

import de.nativehint.service.GenerateClass;
import de.nativehint.valueobject.ReflectionEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

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
        List<ReflectionEntry> entryList = List.of(
            new ReflectionEntry("de.dummy.AClass.java"),
            new ReflectionEntry("de.dummy.BClass.java"));

        // execute
        String result = sut.generateClassSourceCode(entryList);

        // verify
        Path resourceDirectory = Paths.get("src", "test", "resources", "generatedClass", "ReflectionHint.java");
        String expected = Files.readString(resourceDirectory);
        assertEquals(expected, result);
    }


}