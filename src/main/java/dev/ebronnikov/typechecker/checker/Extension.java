package dev.ebronnikov.typechecker.checker;

import java.util.HashMap;
import java.util.Map;

public enum Extension {
    UNIT_TYPE("unit-type"),
    PAIRS("pairs"),
    TUPLES("tuples"),
    RECORDS("records"),
    LET_BINDINGS("let-bindings"),
    TYPE_ASCRIPTIONS("type-ascriptions"),
    SUM_TYPES("sum-types"),
    LISTS("lists"),
    VARIANTS("variants"),
    FIXPOINT_COMBINATOR("fixpoint-combinator"),
    PREDECESSOR("predecessor"),
    SEQUENCING("sequencing"),
    REFERENCES("references"),
    EXCEPTIONS("exceptions"),
    EXCEPTION_TYPE_DECLARATION("exception-type-declaration"),
    OPEN_VARIANT_EXCEPTIONS("open-variant-exceptions"),
    TRY_CAST_AS("try-cast-as"),
    TOP_TYPE("top-type"),
    BOTTOM_TYPE("bottom-type"),
    STRUCTURAL_SUBTYPING("structural-subtyping"),
    TYPE_CAST("type-cast"),
    PANIC("panic"),
    AMBIGUOUS_TYPE_AS_BOTTOM("ambiguous-type-as-bottom"),
    NATURAL_LITERALS("natural-literals"),
    NESTED_FUNCTION_DECLARATIONS("nested-function-declarations"),
    NULLARY_FUNCTIONS("nullary-functions"),
    MULTIPARAMETER_FUNCTIONS("multiparameter-functions"),
    STRUCTURAL_PATTERNS("structural-patterns"),
    NULLARY_VARIANT_LABELS("nullary-variant-labels"),
    LETREC_BINDINGS("letrec-bindings"),
    LETREC_MANY_BINDINGS("letrec-many-bindings"),
    LET_PATTERNS("let-patterns"),
    PATTERN_ASCRIPTIONS("pattern-ascriptions"),
    ARITHMETIC_OPERATORS("arithmetic-operators"),
    TYPE_CASE_PATTERNS("type-cast-patterns"),
    TYPE_RECONSTRUCTION("type-reconstruction"),
    UNIVERSAL_TYPES("universal-types");

    private final String extensionName;
    private static final Map<String, Extension> EXTENSION_MAP = new HashMap<>();

    static {
        for (Extension extension : Extension.values()) {
            EXTENSION_MAP.put(extension.extensionName, extension);
        }
    }

    Extension(String extensionName) {
        this.extensionName = extensionName;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public static Extension fromString(String str) {
        Extension extension = EXTENSION_MAP.get(str);
        if (extension != null) {
            return extension;
        }
        throw new IllegalArgumentException("Can't find extension " + str);
    }
}
