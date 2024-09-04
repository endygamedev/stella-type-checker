package dev.ebronnikov.typechecker.errors;

public enum ErrorType {
    ERROR_MISSING_MAIN,
    ERROR_UNDEFINED_VARIABLE,
    ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
    ERROR_NOT_A_FUNCTION,
    ERROR_NOT_A_TUPLE,
    ERROR_NOT_A_RECORD,
    ERROR_NOT_A_LIST,
    ERROR_UNEXPECTED_LAMBDA,
    ERROR_UNEXPECTED_TYPE_FOR_PARAMETER,
    ERROR_UNEXPECTED_TUPLE,
    ERROR_UNEXPECTED_RECORD,
    ERROR_UNEXPECTED_VARIANT,
    ERROR_UNEXPECTED_LIST,
    ERROR_UNEXPECTED_INJECTION,
    ERROR_MISSING_RECORD_FIELDS,
    ERROR_UNEXPECTED_RECORD_FIELDS,
    ERROR_UNEXPECTED_FIELD_ACCESS,
    ERROR_TUPLE_INDEX_OUT_OF_BOUNDS,
    ERROR_UNEXPECTED_TUPLE_LENGTH,
    ERROR_AMBIGUOUS_SUM_TYPE,
    ERROR_AMBIGUOUS_LIST,
    ERROR_AMBIGUOUS_VARIANT_TYPE,
    ERROR_ILLEGAL_EMPTY_MATCHING,
    ERROR_NONEXHAUSTIVE_MATCH_PATTERNS,
    ERROR_UNEXPECTED_PATTERN_FOR_TYPE,
    ERROR_UNEXPECTED_VARIANT_LABEL,
    ERROR_UNEXPECTED_REFERENCE,
    ERROR_NOT_A_REFERENCE,
    ERROR_EXCEPTION_TYPE_NOT_DECLARED,
    ERROR_UNEXPECTED_SUBTYPE,
    ERROR_AMBIGUOUS_SUBTYPE,
    ERROR_AMBIGUOUS_PANIC_TYPE,
    ERROR_AMBIGUOUS_THROW_TYPE,
    ERROR_AMBIGUOUS_REFERENCE_TYPE,
    ERROR_UNEXPECTED_MEMORY_ADDRESS,
    ERROR_INCORRECT_ARITY_OF_MAIN,
}
