package de.nativehint.service;

import de.nativehint.valueobject.HintEntry;
import de.nativehint.valueobject.HintType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class GenerateClass {


    @Value("${hintsgenerator.generatedClassName}")
    private  String generatedClassName;

    @Value("${hintsgenerator.generatedClassPackageName}")
    private String generatedClassPackageName;

    private static final Comparator<HintEntry> comparator = Comparator.comparing(a -> Objects.toString(a.getComment(), ""));

    public String generateClassSource(List<HintEntry> hints) {
        return generateClassSource() +
            generateBlocks(hints.stream().sorted(comparator).toList()) +
            generateClassSourceCodeEnd();
    }

    private String generateClassSource() {
        return "package " + generatedClassPackageName + ";\n\n" +
            "import org.springframework.aot.hint.MemberCategory;\n" +
            "import org.springframework.aot.hint.RuntimeHints;\n" +
            "import org.springframework.aot.hint.RuntimeHintsRegistrar;\n" +
            "import org.springframework.aot.hint.TypeReference;\n\n" +
            "@SuppressWarnings({\"checkstyle:LineLength\", \"checkstyle:MethodLength\"})\n" +
            "public class " + generatedClassName + " implements RuntimeHintsRegistrar {\n\n" +
            "    @Override\n" +
            "    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {\n";
    }

    private String generateClassSourceCodeEnd() {
        return "" +
            "    }\n" +
            "}\n";
    }

    private String generateBlocks(List<HintEntry> hints) {
        StringBuilder sb = new StringBuilder();

        for (HintEntry entry : hints) {
            if (StringUtils.hasText(entry.getComment())) {
                sb.append("        // ").append(entry.getComment()).append("\n");
            }
            generateBlocks(entry, sb);
        }

        return sb.toString();
    }

    private static void generateBlocks(HintEntry hintEntry, StringBuilder sb) {
        Iterator<String> iterator = hintEntry.getFullClassNames().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (i % 30 == 0) {
                sb
                    .append("        hints.")
                    .append(hintEntry.getHintType().name())
                    .append("()\n");
            }
            String entry = iterator.next();
            sb
                .append("            .registerType(TypeReference.of(\"")
                .append(entry)
                .append("\")");

            if(HintType.reflection.equals(hintEntry.getHintType())) {
                sb.append(", MemberCategory.values()");
            }
            sb.append(")");

            if (i % 30 == 29 || !iterator.hasNext()) {
                sb.append(";\n");
            }
            sb.append("\n");

            i++;
        }
    }

}
