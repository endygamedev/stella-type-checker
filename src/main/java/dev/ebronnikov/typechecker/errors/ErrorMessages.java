package dev.ebronnikov.typechecker.errors;

import java.util.AbstractMap;
import java.util.Map;

public class ErrorMessages {
    public static final Map<ErrorType, String> messages = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_MISSING_MAIN, "main function is missing"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNDEFINED_VARIABLE, "variable %s is undefined"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "expected type %s but got %s for expression %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_NOT_A_FUNCTION, "expected a function type but got %s for expression %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_NOT_A_TUPLE, "expected an expression of tuple type but got expression of type %s in %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_NOT_A_RECORD, "expected record but got expression of type %s in %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_NOT_A_LIST, "expected list but got expression of type %s in %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_LAMBDA, "expected an expression of a non-function type %s but got function type %s for expression %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_PARAMETER, "expected type %s but got type %s for parameter %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_TUPLE, "expected an expression of type %s but got tuple %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_RECORD, "expected an expression of type %s but got record %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_LIST, "expected an expression of type %s but got list %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_INJECTION, "expected sum-type but got %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_MISSING_RECORD_FIELDS, "missing field %s in record %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_RECORD_FIELDS, "unexpected record field %s in record %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_FIELD_ACCESS, "unexpected field access %s in record %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_TUPLE_INDEX_OUT_OF_BOUNDS, "tuple index %s is out of bounds %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_TUPLE_LENGTH, "expected %s components for a tuple but got %s in tuple %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_AMBIGUOUS_SUM_TYPE, "can't infer injection type for %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_AMBIGUOUS_LIST, "can't infer the list %s type"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_ILLEGAL_EMPTY_MATCHING, "empty alternatives list for %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "non-exhaustive patterns for type %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "unexpected pattern %s for type %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_VARIANT_LABEL, "unexpected variant label %s in %s of type %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_VARIANT, "expected type %s but got variant variant type"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_AMBIGUOUS_VARIANT_TYPE, "can't infer injection type of variant %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_REFERENCE, "got reference %s, but expected type %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_NOT_A_REFERENCE, "expected reference, but got %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_AMBIGUOUS_PANIC_TYPE, "ambiguous panic type for expression %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_AMBIGUOUS_THROW_TYPE, "cannot infer type for throw %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_AMBIGUOUS_REFERENCE_TYPE, "cannot infer type for reference %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_MEMORY_ADDRESS, "unexpected memory address while %s is expected"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_EXCEPTION_TYPE_NOT_DECLARED, "exception type is not declared"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNEXPECTED_SUBTYPE, "expected subtype of %s but got type %s in %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_INCORRECT_ARITY_OF_MAIN, "the main function must have one parameter but got %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_OCCURS_CHECK_INFINITE_TYPE, "infinite type found while checking expression %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_AMBIGUOUS_PATTERN_TYPE, "ambiguous type %s of pattern %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_NOT_A_GENERIC_FUNCTION, "expected a generic function, but got non-generic function %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_INCORRECT_NUMBER_OF_TYPE_ARGUMENTS, "got unexpected arguments cont, expected %s but got %s"),
            new AbstractMap.SimpleEntry<>(ErrorType.ERROR_UNDEFINED_TYPE_VARIABLE, "type variable %s is undefined")
    );
}
