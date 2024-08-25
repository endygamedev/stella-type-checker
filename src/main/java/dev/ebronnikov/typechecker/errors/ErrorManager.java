package dev.ebronnikov.typechecker.errors;

import java.util.ArrayList;

final public class ErrorManager {
    private final ArrayList<Error> errors = new ArrayList<>();

    public void registerError(ErrorType errorType, Object... args) {
        Error error = new Error(errorType, args);
        errors.add(error);
    }

    ArrayList<Error> getErrors() {
        return errors;
    }
}
