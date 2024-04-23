package de.nativehint.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GenerateClass {


    @Value("${hintsgenerator.generatedClassName}")
    String generatedClassName;

    @Value("${hintsgenerator.generatedClassPackageName}")
    String generatedClassPackageName;


    public String generateClassSource(List<String> entryList) {
        return generateClassSource() +
            generateRegisterTypes(entryList) +
            generateClassSourceCodeEnd();
    }

    public String generateClassSource(Map<String, List<String>> folderMap) {
        return generateClassSource() +
            generateRegisterTypes(folderMap) +
            generateClassSourceCodeEnd();
    }

    private String generateClassSource() {
        return "package " + generatedClassPackageName + ";\n\n" +
            "import org.springframework.aot.hint.MemberCategory;\n" +
            "import org.springframework.aot.hint.RuntimeHints;\n" +
            "import org.springframework.aot.hint.RuntimeHintsRegistrar;\n\n" +
            "@SuppressWarnings(\"checkstyle:LineLength\")\n" +
            "public class " + generatedClassName + " implements RuntimeHintsRegistrar {\n\n" +
            "    @Override\n" +
            "    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {\n";
    }

    private String generateClassSourceCodeEnd() {
        return "" +
            "    }\n" +
            "}\n";
    }

    private String generateRegisterTypes(List<String> entryList) {
        StringBuilder sb = new StringBuilder();
        sb.append("        hints.reflection()\n");

        Iterator<String> iterator = entryList.iterator();
        while (iterator.hasNext()) {
            String entry = iterator.next();
            sb.append("            .registerType(");
            sb.append(entry);
            sb.append(".class, MemberCategory.values())");

            if (!iterator.hasNext()) {
                sb.append(";\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private String generateRegisterTypes(Map<String, List<String>> folderMap) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, List<String>> entry : folderMap.entrySet()) {
            sb.append("        // " + entry.getKey() + "\n");
            sb.append(generateRegisterTypes(entry.getValue()));
        }
        return sb.toString();
    }
}
