package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.antlr.stellaParser.*;
import dev.ebronnikov.typechecker.types.*;
import dev.ebronnikov.typechecker.errors.ErrorManager;
import dev.ebronnikov.typechecker.errors.ErrorType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExhaustivenessChecker {
    public boolean checkForPatternsTypeMismatch(
            List<PatternContext> patterns,
            Type expectedType,
            ErrorManager errorManager) {
        return patterns.stream().allMatch(pattern ->
                checkForPatternTypeMismatch(pattern, expectedType, errorManager));
    }

    private boolean checkForPatternTypeMismatch(
            PatternContext pattern,
            Type expectedType,
            ErrorManager errorManager) {

        var patternType = switch (pattern) {
            case PatternVariantContext ignored -> VariantType.class;
            case PatternInlContext ignored -> SumType.class;
            case PatternInrContext ignored -> SumType.class;
            case PatternTupleContext ignored -> TupleType.class;
            case PatternRecordContext ignored -> RecordType.class;
            case PatternListContext ignored -> ListType.class;
            case PatternConsContext ignored -> ListType.class;
            case PatternFalseContext ignored -> BoolType.class;
            case PatternTrueContext ignored -> BoolType.class;
            case PatternUnitContext ignored -> UnitType.class;
            case PatternIntContext ignored -> NatType.class;
            case PatternSuccContext ignored -> NatType.class;
            case PatternVarContext ignored -> {
                yield true;
            }
            case PatternAscContext ascContext -> {
                Type ascType = SyntaxTypeProcessor.getType(ascContext.type_);
                if (!ascType.equals(expectedType)) {
                    errorManager.registerError(
                            ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE,
                            pattern,
                            expectedType);
                    yield false;
                }
                yield checkForPatternTypeMismatch(ascContext.pattern_, ascType, errorManager);
            }
            case ParenthesisedPatternContext parenthesisedPatternContext ->
                    checkForPatternTypeMismatch(parenthesisedPatternContext.pattern_, expectedType, errorManager);
            default -> throw new IllegalStateException("Unsupported pattern: " + pattern.getClass());
        };

        return validatePatternType(expectedType, (Class<? extends Type>) patternType, pattern, errorManager);
    }

    private boolean validatePatternType(
            Type expectedType,
            Class<? extends Type> actualPatternType,
            PatternContext context,
            ErrorManager errorManager) {
        if (actualPatternType.equals(expectedType.getClass())) {
            return true;
        }

        errorManager.registerError(
                ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE,
                expectedType,
                context);

        return false;
    }

    public boolean arePatternsExhaustive(List<PatternContext> patterns, Type type) {
        return hasAny(patterns) || switch (type) {
            case BoolType ignored -> areBoolPatternsExhaustive(patterns);
            case NatType ignored -> areNatPatternsExhaustive(patterns);
            case SumType ignored -> areSumPatternsExhaustive(patterns);
            case UnitType ignored -> areUnitPatternsExhaustive(patterns);
            case TupleType ignored -> throw new UnsupportedOperationException("TODO");
            case RecordType ignored -> throw new UnsupportedOperationException("TODO");
            case VariantType variantType -> areVariantPatternsExhaustive(patterns, variantType);
            case FunctionalType ignored -> false;
            case ListType ignored -> false;
            case ReferenceType ignored -> true;
            case TopType ignored -> throw new UnsupportedOperationException("TODO");
            case BotType ignored -> throw new UnsupportedOperationException("TODO");
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    private boolean areBoolPatternsExhaustive(List<PatternContext> patterns) {
        return patterns.stream().anyMatch(p -> p instanceof PatternTrueContext) &&
                patterns.stream().anyMatch(p -> p instanceof PatternFalseContext);
    }

    private boolean areNatPatternsExhaustive(List<PatternContext> patterns) {
        return patterns.stream().anyMatch(p -> p instanceof PatternIntContext) &&
                patterns.stream().anyMatch(p -> p instanceof PatternSuccContext &&
                        ((PatternSuccContext) p).pattern_ instanceof PatternVarContext);
    }

    private boolean areSumPatternsExhaustive(List<PatternContext> patterns) {
        return patterns.stream().anyMatch(p -> p instanceof PatternInlContext) &&
                patterns.stream().anyMatch(p -> p instanceof PatternInrContext);
    }

    private boolean areUnitPatternsExhaustive(List<PatternContext> patterns) {
        return patterns.stream().anyMatch(p -> p instanceof PatternUnitContext);
    }

    private boolean areVariantPatternsExhaustive(List<PatternContext> patterns, VariantType type) {
        Set<String> labelsInPattern = patterns.stream()
                .filter(p -> p instanceof PatternVariantContext)
                .map(p -> ((PatternVariantContext) p).label.getText())
                .collect(Collectors.toSet());
        Set<String> labelsInType = type.getLabels().stream().collect(Collectors.toSet());

        return labelsInPattern.containsAll(labelsInType);
    }

    private boolean hasAny(List<PatternContext> patterns) {
        return patterns.stream().anyMatch(p -> p instanceof PatternVarContext);
    }

    public PatternContext findWrongPattern(List<PatternContext> patterns, Type type) {
        return switch (type) {
            case BoolType ignored -> findWrongBoolPattern(patterns);
            case NatType ignored -> findWrongNatPattern(patterns);
            case SumType ignored -> findWrongSumPattern(patterns);
            case UnitType ignored -> findWrongUnitPattern(patterns);
            case VariantType variantType -> findWrongVariantPattern(patterns, variantType);
            case TupleType ignored -> throw new UnsupportedOperationException("TODO");
            case RecordType ignored -> throw new UnsupportedOperationException("TODO");
            case FunctionalType ignored -> findNotVarPattern(patterns);
            case ListType ignored -> findNotVarPattern(patterns);
            case ReferenceType ignored -> null;
            case BotType ignored -> null;
            case TopType ignored -> null;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    private PatternContext findWrongBoolPattern(List<PatternContext> patterns) {
        return patterns.stream()
                .filter(p -> !(p instanceof PatternTrueContext) &&
                        !(p instanceof PatternFalseContext) &&
                        !(p instanceof PatternVarContext))
                .findFirst().orElse(null);
    }

    private PatternContext findWrongNatPattern(List<PatternContext> patterns) {
        return patterns.stream()
                .filter(p -> !(p instanceof PatternIntContext) &&
                        !((p instanceof PatternSuccContext) &&
                                ((PatternSuccContext) p).pattern_ instanceof PatternVarContext) &&
                        !(p instanceof PatternVarContext))
                .findFirst().orElse(null);
    }

    private PatternContext findWrongSumPattern(List<PatternContext> patterns) {
        return patterns.stream()
                .filter(p -> !(p instanceof PatternInlContext) &&
                        !(p instanceof PatternInrContext) &&
                        !(p instanceof PatternVarContext))
                .findFirst().orElse(null);
    }

    private PatternContext findWrongUnitPattern(List<PatternContext> patterns) {
        return patterns.stream()
                .filter(p -> !(p instanceof PatternUnitContext) &&
                        !(p instanceof PatternVarContext))
                .findFirst().orElse(null);
    }

    private PatternContext findNotVarPattern(List<PatternContext> patterns) {
        return patterns.stream()
                .filter(p -> !(p instanceof PatternVarContext))
                .findFirst().orElse(null);
    }

    private PatternContext findWrongVariantPattern(List<PatternContext> patterns, VariantType type) {
        Set<String> labelsInType = type.getLabels().stream().collect(Collectors.toSet());
        return patterns.stream()
                .filter(p -> !(p instanceof PatternVarContext) &&
                        !(p instanceof PatternVariantContext && labelsInType.contains(((PatternVariantContext) p).label.getText())))
                .findFirst().orElse(null);
    }
}
