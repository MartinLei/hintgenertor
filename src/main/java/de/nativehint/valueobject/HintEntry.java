package de.nativehint.valueobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HintEntry {

    private HintType hintType;
    private String comment;
    private List<String> fullClassNames;
}
