package de.nativehint.service;

import de.nativehint.valueobject.ReflectionEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class GenerateClass {


    public void generateAndSaveClass(List<ReflectionEntry> entryList) throws URISyntaxException {
        String className = "GeneratedNativeServiceHint";
        String packageName = "de.test";
        String sourceCode = generateClassSourceCode(packageName, className, entryList);
        saveSourceCodeToFile(sourceCode, packageName, className);
    }

    public String generateClassSourceCode(String packageName, String className, List<ReflectionEntry> entryList) {
        return "package " + packageName + ";\n\n" +
            "import org.springframework.aot.hint.MemberCategory;\n" +
            "import org.springframework.aot.hint.RuntimeHints;\n" +
            "import org.springframework.aot.hint.RuntimeHintsRegistrar;\n\n\n" +
            "public class " + className + " implements RuntimeHintsRegistrar {\n" +
            "    @Override\n" +
            "    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {\n" +
            "        hints.reflection()\n" +
            generateRegisterTypes(entryList) +
            "    }\n" +
            "}\n";
    }

    private String generateRegisterTypes(List<ReflectionEntry> entryList) {
        StringBuilder sb = new StringBuilder();

        Iterator<ReflectionEntry> iterator = entryList.iterator();
        while (iterator.hasNext()) {
            ReflectionEntry entry = iterator.next();

            sb.append("            .registerType(");
            sb.append(entry.getName());
            sb.append(".class, MemberCategory.values())");

            if (!iterator.hasNext()) {
                sb.append(";\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private void saveSourceCodeToFile(String sourceCode, String packageName, String className) throws URISyntaxException {
        String directory = ClassLoader.getSystemResource("generatedClass").toURI().getPath();

        try {
            FileWriter writer = new FileWriter(directory + "/" + className + ".java");
            writer.write(sourceCode);
            writer.close();
            log.info(String.format("Write Java file '%s'.java to '%s'", className, directory));
        } catch (IOException e) {
            log.error("Error saving Java file to disk:", e);
        }
    }
}
