package dev.ebronnikov.typechecker.types;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class VariantType extends Type {
    private final List<String> labels;
    private final List<Type> types;
    private final String name;

    public VariantType(List<String> labels, List<Type> types) {
        super(true);
        this.labels = labels;
        this.types = types;
        this.name = labels.stream()
                .map(label -> String.format("%s : %s", label, types.get(label.indexOf(label)).getName()))
                .collect(Collectors.joining(", ", "<|", "|>"));
    }

    public List<Type> getTypes() {
        return types;
    }

    public List<String> getLabels() {
        return labels;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        VariantType variantType = (VariantType) other;
        return labels.equals(variantType.labels) && types.equals(variantType.types);
    }
}
