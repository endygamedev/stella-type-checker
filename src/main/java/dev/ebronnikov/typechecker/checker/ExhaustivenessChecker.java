package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.typechecker.types.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExhaustivenessChecker {
    public boolean check(List<stellaParser.PatternContext> patterns, Type expectedType) {
        List<stellaParser.PatternContext> preparedPatterns = patterns.stream()
                .map(pattern -> {
                    if (pattern instanceof stellaParser.ParenthesisedPatternContext) {
                        return ((stellaParser.ParenthesisedPatternContext) pattern).pattern_;
                    } else if (pattern instanceof stellaParser.PatternAscContext) {
                        return ((stellaParser.PatternAscContext) pattern).pattern_;
                    } else {
                        return pattern;
                    }
                })
                .collect(Collectors.toList());

        if (isAnyVar(preparedPatterns)) {
            return true;
        }

        if (expectedType instanceof BoolType) {
            return processBoolPattern(patterns);
        } else if (expectedType instanceof NatType) {
            return processNatPattern(patterns);
        } else if (expectedType instanceof UnitType) {
            return processUnitPattern(patterns);
        } else if (expectedType instanceof SumType) {
            return processSumPattern(patterns);
        } else if (expectedType instanceof TypeVar) {
            return processVariantsWithTypeVar(patterns);
        } else if (expectedType instanceof VariantType) {
            return processVariantPattern(patterns, (VariantType) expectedType);
        } else {
            return false;
        }
    }

    private boolean isAnyVar(List<stellaParser.PatternContext> patterns) {
        return patterns.stream().anyMatch(pattern -> pattern instanceof stellaParser.PatternVarContext);
    }

    private boolean processBoolPattern(List<stellaParser.PatternContext> patterns) {
        return patterns.stream().anyMatch(pattern -> pattern instanceof stellaParser.PatternTrueContext)
                && patterns.stream().anyMatch(pattern -> pattern instanceof stellaParser.PatternFalseContext);
    }

    private boolean processNatPattern(List<stellaParser.PatternContext> patterns) {
        return patterns.stream().anyMatch(pattern -> pattern instanceof stellaParser.PatternIntContext)
                && patterns.stream().anyMatch(pattern -> pattern instanceof stellaParser.PatternSuccContext
                && ((stellaParser.PatternSuccContext) pattern).pattern_ instanceof stellaParser.PatternVarContext);
    }

    private boolean processUnitPattern(List<stellaParser.PatternContext> patterns) {
        return patterns.stream().anyMatch(pattern -> pattern instanceof stellaParser.PatternUnitContext);
    }

    private boolean processSumPattern(List<stellaParser.PatternContext> patterns) {
        return patterns.stream().anyMatch(pattern -> pattern instanceof stellaParser.PatternInlContext)
                && patterns.stream().anyMatch(pattern -> pattern instanceof stellaParser.PatternInrContext);
    }

    private boolean processVariantPattern(List<stellaParser.PatternContext> patterns, VariantType type) {
        Set<String> labelsInPattern = patterns.stream()
                .filter(pattern -> pattern instanceof stellaParser.PatternVariantContext)
                .map(pattern -> ((stellaParser.PatternVariantContext) pattern).label.getText())
                .collect(Collectors.toSet());

        Set<String> labelsInType = type.getLabels().stream().collect(Collectors.toSet());

        return labelsInPattern.containsAll(labelsInType);
    }

    private boolean processVariantsWithTypeVar(List<stellaParser.PatternContext> patterns) {
        boolean result = true;

        if (patterns.stream().anyMatch(pattern -> pattern instanceof stellaParser.PatternInlContext
                || pattern instanceof stellaParser.PatternInrContext)) {
            result = processSumPattern(patterns);
        }

        if (patterns.stream().anyMatch(pattern -> pattern instanceof stellaParser.PatternTrueContext
                || pattern instanceof stellaParser.PatternFalseContext)) {
            result = processBoolPattern(patterns);
        }

        return result;
    }
}
