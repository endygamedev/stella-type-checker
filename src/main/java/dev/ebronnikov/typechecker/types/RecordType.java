package dev.ebronnikov.typechecker.types;

import dev.ebronnikov.typechecker.utils.Pair;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class RecordType extends Type {
    private final List<String> labels;
    private final List<Type> types;
    private final String name;

    public RecordType(List<String> labels, List<Type> types, boolean isKnownType) {
        super(isKnownType);

        if (labels.size() != types.size()) {
            throw new IllegalArgumentException("Labels and types must have same size");
        }

        this.labels = labels;
        this.types = types;
        this.name = isKnownType ? labels.stream()
                .map(label -> String.format("%s : %s", label, types.get(labels.indexOf(label)).getName()))
                .collect(Collectors.joining(", ", "{", "}"))
                : "UnknownRecord";
    }

    public RecordType(List<String> labels, List<Type> types) {
        this(labels, types, true);
    }

    @Override
    public String getName() {
        return name;
    }

    public List<String> getLabels() {
        return labels;
    }

    public List<Type> getTypes() {
        return types;
    }

    @Override
    public boolean equals(Object other) {
        if (!isKnownType() || (other instanceof Type type && !type.isKnownType())) {
            return true;
        }
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        RecordType recordType = (RecordType) other;
        return labels.equals(recordType.labels) && types.equals(recordType.types);
    }

    @Override
    public boolean isSubtypeOf(Type other, boolean subtypingEnabled) {
        if (this.equals(other)) return true;
        if (!subtypingEnabled) return false;
        if (other instanceof RecordType otherRec) {
            Set<Pair<String, Type>> thisLabels = zip(this.labels, this.types);
            Set<Pair<String, Type>> otherLabels = zip(otherRec.labels, otherRec.types);
            return thisLabels.containsAll(otherLabels);
        }
        return false;
    }

    private Set<Pair<String, Type>> zip(List<String> labels, List<Type> types) {
        return IntStream.range(0, labels.size())
                .mapToObj(i -> new Pair<>(labels.get(i), types.get(i)))
                .collect(Collectors.toSet());
    }
}
