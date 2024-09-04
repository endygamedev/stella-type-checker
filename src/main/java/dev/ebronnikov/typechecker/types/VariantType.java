package dev.ebronnikov.typechecker.types;

import dev.ebronnikov.typechecker.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Override
    public boolean isSubtypeOf(Type other, boolean subtypingEnabled) {
        if (this.equals(other)) return true;
        if (!subtypingEnabled) return false;
        if (other instanceof VariantType otherVariant) {
            Set<Pair<String, Type>> thisLabels = zip(this.labels, this.types);
            Set<Pair<String, Type>> otherLabels = zip(otherVariant.labels, otherVariant.types);
            return otherLabels.containsAll(thisLabels);
        }
        return false;
    }

    private Set<Pair<String, Type>> zip(List<String> labels, List<Type> types) {
        return IntStream.range(0, labels.size())
                .mapToObj(i -> new Pair<>(labels.get(i), types.get(i)))
                .collect(Collectors.toSet());
    }
}
