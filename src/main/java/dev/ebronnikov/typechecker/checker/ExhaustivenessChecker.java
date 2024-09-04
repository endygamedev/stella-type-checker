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
        Class<? extends Type> patternType;

        if (pattern instanceof PatternVariantContext) {
            patternType = VariantType.class;
        } else if (pattern instanceof PatternInlContext ||
                pattern instanceof PatternInrContext) {
            patternType = SumType.class;
        } else if (pattern instanceof PatternTupleContext) {
            patternType = TupleType.class;
        } else if (pattern instanceof PatternRecordContext) {
            patternType = RecordType.class;
        } else if (pattern instanceof PatternListContext ||
                pattern instanceof PatternConsContext) {
            patternType = ListType.class;
        } else if (pattern instanceof PatternFalseContext ||
                pattern instanceof PatternTrueContext) {
            patternType = BoolType.class;
        } else if (pattern instanceof PatternUnitContext) {
            patternType = UnitType.class;
        } else if (pattern instanceof PatternIntContext ||
                pattern instanceof PatternSuccContext) {
            patternType = NatType.class;
        } else if (pattern instanceof PatternVarContext) {
            return true;
        } else if (pattern instanceof PatternAscContext) {
            Type ascType = SyntaxTypeProcessor.getType(((PatternAscContext) pattern).type_);
            if (!ascType.equals(expectedType)) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE,
                        pattern,
                        expectedType
                );
                return false;
            }
            return checkForPatternTypeMismatch(((PatternAscContext) pattern).pattern_, ascType, errorManager);
        } else if (pattern instanceof ParenthesisedPatternContext) {
            return checkForPatternTypeMismatch(((ParenthesisedPatternContext) pattern).pattern_, expectedType, errorManager);
        } else {
            throw new IllegalArgumentException("Unsupported pattern: " + pattern.getClass());
        }

        return validatePatternType(expectedType, patternType, pattern, errorManager);
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
