package dev.ebronnikov.typechecker.types;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class VariantType extends Type {
    private final ArrayList<String> labels;
    private final ArrayList<Type> types;
    private final String name;

    public VariantType(ArrayList<String> labels, ArrayList<Type> types) {
        super(true);
        this.labels = labels;
        this.types = types;
        this.name = labels.stream()
                .map(label -> String.format("%s : %s", label, types.get(label.indexOf(label)).getName()))
                .collect(Collectors.joining(", ", "<|", "|>"));
    }

    public ArrayList<Type> getTypes() {
        return types;
    }

    public ArrayList<String> getLabels() {
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
