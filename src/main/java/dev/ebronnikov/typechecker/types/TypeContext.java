package dev.ebronnikov.typechecker.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TypeContext {
    private final TypeContext parent;
    private final Map<String, Type> variantTypes = new HashMap<>();
    private final Map<String, FunctionalType> functionTypes = new HashMap<>();
    private Type exceptionType;

    public TypeContext() {
        this.parent = null;
        this.exceptionType = null;
    }

    public TypeContext(TypeContext parent) {
        this.parent = parent;
        this.exceptionType = null;
    }

    public void setExceptionType(Type exceptionType) {
        this.exceptionType = exceptionType;
    }

    public Type getExceptionType() {
        if (exceptionType != null) {
            return exceptionType;
        }
        return parent != null ? parent.getExceptionType() : null;
    }

    public void saveVariableType(String name, Type type) {
        if (variantTypes.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Already known variable %s with type %s", name, type.getName()));
        }
        variantTypes.put(name, type);
    }

    public Optional<Type> resolveVariableType(String name) {
        Type type = variantTypes.get(name);
        if (type == null && parent != null) {
            return parent.resolveVariableType(name);
        }
        return Optional.ofNullable(type);
    }

    public void saveFunctionType(String functionName, FunctionalType type) {
        if (functionTypes.containsKey(functionName)) {
            throw new IllegalArgumentException(String.format("Already known function %s with type %s", functionName, type.getName()));
        }
        functionTypes.put(functionName, type);
    }

    public Optional<FunctionalType> resolveFunctionType(String name) {
        FunctionalType type = functionTypes.get(name);
        if (type == null && parent != null) {
            return parent.resolveFunctionType(name);
        }
        return Optional.ofNullable(type);
    }
}
