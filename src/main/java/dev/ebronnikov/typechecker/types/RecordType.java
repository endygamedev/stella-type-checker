package dev.ebronnikov.typechecker.types;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class RecordType extends Type {
    private final ArrayList<String> labels;
    private final ArrayList<Type> types;
    private final String name;

    public RecordType(ArrayList<String> labels, ArrayList<Type> types, boolean isKnownType) {
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

    public RecordType(ArrayList<String> labels, ArrayList<Type> types) {
        this(labels, types, true);
    }

    @Override
    public String getName() {
        return name;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public ArrayList<Type> getTypes() {
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
}
