package de.nativehint.valueobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HintEntry implements Comparable<HintEntry> {

    private HintType hintType;
    private String comment;
    private List<String> fullClassNames;

    @Override
    public int compareTo(HintEntry other) {
        return CharSequence.compare(comment, other.getComment());
    }
}
