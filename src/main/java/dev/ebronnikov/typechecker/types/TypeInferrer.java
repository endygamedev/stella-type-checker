package dev.ebronnikov.typechecker.types;

import dev.ebronnikov.antlr.stellaParser.*;
import dev.ebronnikov.typechecker.errors.ErrorManager;
import dev.ebronnikov.typechecker.errors.ErrorType;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class TypeInferrer {
    private final ErrorManager errorManager;
    private final TypeContext context;

    public TypeInferrer(ErrorManager errorManager, TypeContext parentContext) {
        this.errorManager = errorManager;
        this.context = new TypeContext(parentContext);
    }

    public Type visitExpression(ExprContext ctx, Type expectedType) {
        Type type = switch (ctx) {
            case ConstTrueContext ignored -> BoolType.INSTANCE;
            case ConstFalseContext ignored -> BoolType.INSTANCE;
            case ConstIntContext ignored -> NatType.INSTANCE;
            case ConstUnitContext ignored -> UnitType.INSTANCE;
            case IsZeroContext isZeroContext -> visitIsZero(isZeroContext);
            case SuccContext succContext -> visitSucc(succContext);
            case PredContext predContext -> visitPred(predContext);
            case VarContext varContext -> visitVar(varContext, expectedType);
            case DotRecordContext dotRecordContext -> visitDotRecord(dotRecordContext, expectedType);
            case AbstractionContext abstractionContext -> visitAbstraction(abstractionContext, expectedType);
            case ApplicationContext applicationContext -> visitApplication(applicationContext, expectedType);
            case ParenthesisedExprContext parenthesisedExprContext -> visitExpression(parenthesisedExprContext.expr_, expectedType);
            case RecordContext recordContext -> visitRecord(recordContext, expectedType);
            case LetContext letContext -> visitLet(letContext, expectedType);
            case TypeAscContext typeAscContext -> visitTypeAsc(typeAscContext, expectedType);
            case NatRecContext natRecContext -> visitNatRec(natRecContext, expectedType);
            case DotTupleContext dotTupleContext -> visitDotTuple(dotTupleContext, expectedType);
            case IfContext ifContext -> visitIf(ifContext, expectedType);
            case TupleContext tupleContext -> visitTuple(tupleContext, expectedType);
            case TerminatingSemicolonContext terminatingSemicolonContext -> visitExpression(terminatingSemicolonContext.expr_, expectedType);
            case FixContext fixContext -> visitFix(fixContext, expectedType);
            case MatchContext matchContext -> visitMatch(matchContext, expectedType);
            case InlContext inlContext -> visitInl(inlContext, expectedType);
            case InrContext inrContext -> visitInr(inrContext, expectedType);
            case VariantContext variantContext -> visitVariant(variantContext, expectedType);
            case ListContext listContext -> visitList(listContext, expectedType);
            case ConsListContext consListContext -> visitConsList(consListContext, expectedType);
            case HeadContext headContext -> visitHead(headContext, expectedType);
            case TailContext tailContext -> visitTail(tailContext, expectedType);
            case IsEmptyContext isEmptyContext -> visitIsEmpty(isEmptyContext, expectedType);
            default -> {
                System.out.printf("Unsupported syntax for %s%n", ctx.getClass().getSimpleName());
                yield null;
            }
        };
        return validateTypes(type, expectedType, ctx);
    }

    private BoolType visitIsZero(IsZeroContext ctx) {
        if (visitExpression(ctx.n, NatType.INSTANCE) != null) {
            return BoolType.INSTANCE;
        }
        return null;
    }

    private NatType visitSucc(SuccContext ctx) {
        if (visitExpression(ctx.n, NatType.INSTANCE) != null) {
            return NatType.INSTANCE;
        }
        return null;
    }

    private NatType visitPred(PredContext ctx) {
        if (visitExpression(ctx.n, NatType.INSTANCE) != null) {
            return NatType.INSTANCE;
        }
        return null;
    }

    private Type visitVar(VarContext ctx, Type expectedType) {
        String name = ctx.name.getText();

        // NOTE: MAYBE SHIT HERE !!!!
        Type type = context.resolveVariableType(name).orElseGet(() -> context.resolveFunctionType(name).orElse(null));

        if (type == null) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNDEFINED_VARIABLE,
                        name,
                        ctx
                );
            }
            return null;
        }

        if (expectedType != null && !type.equals(expectedType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        expectedType,
                        type,
                        ctx
                );
            }
        }

        return type;
    }

    private Type visitDotRecord(DotRecordContext ctx, Type expectedType) {
        var expression = ctx.expr_;
        String label = ctx.label.getText();

        Type expressionType = visitExpression(expression, null);
        if (expressionType == null) {
            return null;
        }

        if (!(expressionType instanceof RecordType recordType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_NOT_A_RECORD,
                        expressionType,
                        ctx
                );
            }
            return null;
        }

        List<String> declaredLabels = recordType.getLabels();
        if (!declaredLabels.contains(label)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_FIELD_ACCESS,
                        label,
                        ctx
                );
            }
            return null;
        }

        int labelIndex = declaredLabels.indexOf(label);
        List<Type> types = recordType.getTypes();
        Type type = types.get(labelIndex);

        return validateTypes(type, expectedType, ctx);
    }

    private FunctionalType visitAbstraction(AbstractionContext ctx, Type expectedType) {
        if (expectedType != null && !(expectedType instanceof FunctionalType)) {
            Type actualType = visitExpression(ctx, null);
            if (actualType == null) {
                return null;
            }

            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_LAMBDA,
                        expectedType,
                        actualType,
                        ctx
                );
            }

            return null;
        }

        var arg = ctx.paramDecl;
        Type argType = SyntaxTypeProcessor.getType(arg.paramType);

        TypeContext innerContext = new TypeContext(context);
        innerContext.saveVariableType(arg.name.getText(), argType);

        TypeInferrer innerInferrer = new TypeInferrer(errorManager, innerContext);

        var returnExpr = ctx.returnExpr;
        Type returnType = innerInferrer.visitExpression(returnExpr, null);
        if (returnType == null) {
            return null;
        }

        FunctionalType result = new FunctionalType(argType, returnType);
        return (FunctionalType) validateTypes(result, expectedType, ctx);
    }

    private Type visitApplication(ApplicationContext ctx, Type expectedType) {
        var func = ctx.fun;

        Type funType = visitExpression(func, null);
        if (funType == null) {
            return null;
        }

        if (!(funType instanceof FunctionalType functionalType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_NOT_A_FUNCTION,
                        funType,
                        func
                );
            }
            return null;
        }

        var arg = ctx.args.getFirst();
        if (visitExpression(arg, functionalType.getFrom()) == null) {
            return null;
        }

        Type resultType = functionalType.getTo();
        return validateTypes(resultType, expectedType, ctx);
    }

    @SuppressWarnings("DuplicatedCode")
    private RecordType visitRecord(RecordContext ctx, Type expectedType) {
        if (expectedType != null && !(expectedType instanceof RecordType)) {
            Type actualType = visitRecord(ctx, null);
            if (actualType == null) {
                return null;
            }

            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_RECORD,
                        expectedType,
                        actualType
                );
            }
            return null;
        }

        RecordType expectedRecordType = (RecordType) expectedType;
        var bindingsContext = ctx.bindings;
        List<String> labels = bindingsContext.stream()
                .map(bind -> bind.name.getText())
                .toList();

        List<Type> types = bindingsContext.stream()
                .map(bind -> visitExpression(bind.rhs, null))
                .filter(Objects::nonNull)
                .toList();

        if (labels.size() != types.size()) {
            return null;
        }

        if (expectedRecordType != null) {
            if (!canRecordDefMatchExpectedType(expectedRecordType, labels, types, ctx)) {
                return null;
            }
            return expectedRecordType;
        }

        return new RecordType(labels, types);
    }

    private boolean canRecordDefMatchExpectedType(
            RecordType expectedRecord,
            List<String> actualLabels,
            List<Type> actualTypes,
            RecordContext ctx
    ) {
        HashSet<String> expectedLabelsSet = new HashSet<>(expectedRecord.getLabels());
        HashSet<String> actualLabelsSet = new HashSet<>(actualLabels);

        HashSet<String> missingFields = new HashSet<>(expectedLabelsSet);
        missingFields.removeAll(actualLabelsSet);

        HashSet<String> extraFields = new HashSet<>(actualLabelsSet);
        extraFields.removeAll(expectedLabelsSet);

        if (!extraFields.isEmpty()) {
            if(errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_RECORD_FIELDS,
                        extraFields.iterator().next(),
                        expectedRecord
                );
            }
            return false;
        }

        if (!missingFields.isEmpty()) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_MISSING_RECORD_FIELDS,
                        missingFields.iterator().next(),
                        expectedRecord
                );
            }
            return false;
        }

        for (int i = 0; i < actualLabels.size(); ++i) {
            String label = actualLabels.get(i);
            Type type = actualTypes.get(i);

            int expectedTypeIdx = expectedRecord.getLabels().indexOf(label);
            Type expectedTypeForLabel = expectedRecord.getTypes().get(expectedTypeIdx);

            if (type instanceof RecordType actualRecordType) {
                if (!(expectedTypeForLabel instanceof RecordType expectedRecordType)) {
                    if (errorManager != null) {
                        errorManager.registerError(
                                ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                                expectedTypeForLabel,
                                type,
                                ctx
                        );
                    }
                    return false;
                }

                if (!canRecordDefMatchExpectedType(expectedRecordType, actualRecordType.getLabels(), actualRecordType.getTypes(), ctx)) {
                    return false;
                }
            } else {
               if (!type.equals(expectedTypeForLabel)) {
                   if (!(expectedTypeForLabel instanceof RecordType)) {
                       if (errorManager != null) {
                           errorManager.registerError(
                                   ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                                   expectedTypeForLabel,
                                   type,
                                   ctx
                           );
                       }
                       return false;
                   }
               }
            }
        }

        return true;
    }

    private Type visitLet(LetContext ctx, Type expectedType) {
        var patternBinding = ctx.patternBinding(0);
        String name = patternBinding.pat.getText();
        var expression = patternBinding.rhs;

        Type expressionType = visitExpression(expression, null);
        if (expressionType == null) {
            return null;
        }

        TypeContext letContext = new TypeContext(context);
        letContext.saveVariableType(name, expressionType);

        TypeInferrer letTypeInferrer = new TypeInferrer(errorManager, letContext);
        return letTypeInferrer.visitExpression(ctx.body, expectedType);
    }

    private Type visitTypeAsc(TypeAscContext ctx, Type expectedType) {
        var expression = ctx.expr_;
        Type targetType = SyntaxTypeProcessor.getType(ctx.type_);

        Type expressionType = visitExpression(expression, expectedType != null ? expectedType : targetType);
        if (expressionType == null) {
            return null;
        }

        if (!expressionType.equals(targetType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        targetType,
                        expressionType,
                        expression
                );
            }
            return null;
        }

        return validateTypes(expressionType, expectedType, ctx);
    }

    private Type visitNatRec(NatRecContext ctx, Type expectedType) {
        visitExpression(ctx.n, NatType.INSTANCE);

        Type initialValueType = visitExpression(ctx.initial, expectedType);
        if (initialValueType == null) {
            return null;
        }

        Type stepFunctionType = visitExpression(ctx.step, null);
        if (stepFunctionType == null) {
            return null;
        }

        if (!(stepFunctionType instanceof FunctionalType stepFunction)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        FunctionalType.UNKNOWN_FUNCTIONAL_TYPE_NAME,
                        stepFunctionType,
                        ctx.step
                );
            }
            return null;
        }

        // NOTE: Maybe shit
        if (!stepFunction.getFrom().equals(NatType.INSTANCE)) {
            Object errorNode = (ctx.step instanceof AbstractionContext)
                    ? ((AbstractionContext) ctx.step).paramDecl
                    : ctx.step;

            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_PARAMETER,
                        NatType.INSTANCE,
                        stepFunction.getFrom(),
                        errorNode
                );
            }
            return null;
        }

        Type stepFunctionToType = stepFunction.getTo();
        if (!(stepFunctionToType instanceof FunctionalType stepFunctionTo)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        FunctionalType.UNKNOWN_FUNCTIONAL_TYPE_NAME,
                        stepFunctionToType,
                        ctx.step
                );
            }
            return null;
        }

        if (!stepFunctionTo.getFrom().equals(stepFunctionTo.getTo())) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        stepFunctionTo.getTo(),
                        stepFunctionTo.getFrom(),
                        ctx.step
                );
            }
            return null;
        }

        if (!stepFunctionTo.getFrom().equals(initialValueType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        initialValueType,
                        stepFunctionTo.getFrom(),
                        ctx.step
                );
            }
            return null;
        }

        return initialValueType;
    }

    private Type visitDotTuple(DotTupleContext ctx, Type expectedType) {
        var expr = ctx.expr_;

        Type expressionType = visitExpression(expr, null);
        if (expressionType == null) {
            return null;
        }

        if (!(expressionType instanceof TupleType tupleType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_NOT_A_TUPLE,
                        expressionType,
                        ctx
                );
            }
            return null;
        }

        String indexText = ctx.index.getText();
        int indexValue;
        try {
            indexValue = Integer.parseInt(indexText);
        } catch (NumberFormatException e) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_TUPLE_INDEX_OUT_OF_BOUNDS,
                        indexText,
                        tupleType.getArity()
                );
            }
            return null;
        }

        if (indexValue > tupleType.getArity() || indexValue == 0) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_TUPLE_INDEX_OUT_OF_BOUNDS,
                        indexValue,
                        tupleType.getArity()
                );
            }
            return null;
        }

        Type type = tupleType.getTypes().get(indexValue - 1);
        return validateTypes(type, expectedType, ctx);
    }

    private Type visitIf(IfContext ctx, Type expectedType) {
        Type conditionType = visitExpression(ctx.condition, BoolType.INSTANCE);
        if (conditionType == null) {
            return null;
        }

        Type thenType = visitExpression(ctx.thenExpr, expectedType);
        if (thenType == null) {
            return null;
        }

        Type elseType = visitExpression(ctx.elseExpr, expectedType);
        if (elseType == null) {
            return null;
        }

        if (!thenType.equals(elseType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        thenType,
                        elseType,
                        ctx.elseExpr
                );
            }
            return  null;
        }

        return thenType;
    }

    private TupleType visitTuple(TupleContext ctx, Type expectedType) {
        if (!(expectedType instanceof TupleType) && expectedType != null) {
            TupleType tupleType = visitTuple(ctx, null);
            if (tupleType == null) {
                return null;
            }

            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TUPLE,
                        expectedType,
                        tupleType
                );
            }

            return  null;
        }

        List<ExprContext> content = ctx.exprs;
        List<Type> contentTypes = content.stream()
                .map(expr -> visitExpression(expr, null))
                .toList();

        if (contentTypes.contains(null)) {
            return null;
        }

        return new TupleType(contentTypes.stream()
                .filter(Objects::nonNull)
                .toList()
        );
    }

    private Type visitFix(FixContext ctx, Type expectedType) {
        ExprContext expression = ctx.expr_;

        Type inferredExpectedType = (expectedType != null)
                ? new FunctionalType(expectedType, expectedType)
                : null;

        Type expressionType = visitExpression(expression, inferredExpectedType);
        if (expressionType == null) {
            return null;
        }

        if (!(expressionType instanceof FunctionalType funcType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_NOT_A_FUNCTION,
                        expressionType,
                        expression
                );
            }
            return null;
        }

        if (!funcType.getFrom().equals(funcType.getTo())) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        new FunctionalType(funcType.getFrom(), funcType.getFrom()),
                        funcType,
                        expression
                );
            }
            return null;
        }

        return funcType.getTo();
    }

    private Type visitMatch(MatchContext ctx, Type expectedType) {
        ExprContext expression = ctx.expr_;
        Type expressionType = visitExpression(expression, null);
        if (expressionType == null) {
            return null;
        }

        ArrayList<MatchCaseContext> cases = (ArrayList<MatchCaseContext>) ctx.cases;
        if (cases.isEmpty()) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_ILLEGAL_EMPTY_MATCHING,
                        ctx
                );
            }
            return null;
        }

        List<PatternContext> patterns = cases.stream()
                .map(MatchCaseContext::pattern)
                .toList();

        PatternContext wrongPattern = findWrongPattern(patterns, expressionType);
        if (wrongPattern != null) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE,
                        wrongPattern,
                        ctx
                );
            }
            return null;
        }

        boolean arePatternsExhaustive = arePatternsExhaustive(patterns, expressionType);
        if (!arePatternsExhaustive) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS,
                        expressionType
                );
            }
            return null;
        }

        Type resultType = null;
        for (MatchCaseContext caseCtx : cases) {
            TypeContext caseContext = new TypeContext(context);
            TypeInferrer caseInferrer = new TypeInferrer(errorManager, caseContext);

            PatternContext pattern = caseCtx.pattern_;
            ExprContext bodyExpr = caseCtx.expr_;

            Type caseType;
            switch (pattern) {
                case PatternInlContext patternInlContext -> {
                    String variableName = Optional.ofNullable(patternInlContext.pattern_)
                            .filter(p -> p instanceof PatternVarContext)
                            .map(p -> ((PatternVarContext) p).name.getText())
                            .orElse(null);
                    if (variableName == null) return null;
                    Type type = ((SumType) expressionType).getLeft();
                    caseContext.saveVariableType(variableName, type);
                    caseType = caseInferrer.visitExpression(bodyExpr, expectedType);
                }
                case PatternInrContext patternInrContext -> {
                    String variableName = Optional.ofNullable(patternInrContext.pattern_)
                            .filter(p -> p instanceof PatternVarContext)
                            .map(p -> ((PatternVarContext) p).name.getText())
                            .orElse(null);
                    if (variableName == null) return null;
                    Type type = ((SumType) expressionType).getRight();
                    caseContext.saveVariableType(variableName, type);
                    caseType = caseInferrer.visitExpression(bodyExpr, expectedType);
                }
                case PatternVariantContext patternVariantContext -> {
                    String labelName = patternVariantContext.label.getText();
                    int labelIndex = ((VariantType) expressionType).getLabels().indexOf(labelName);
                    if (labelIndex == -1) {
                        if (errorManager != null) {
                            errorManager.registerError(
                                    // NOTE: Strange error
                                    ErrorType.ERROR_UNEXPECTED_VARIANT_LABEL,
                                    labelName,
                                    pattern,
                                    expectedType
                            );
                        }
                        return null;
                    }
                    Type labelType = ((VariantType) expressionType).getTypes().get(labelIndex);
                    String variableName = Optional.ofNullable(patternVariantContext.pattern_)
                            .filter(p -> p instanceof PatternVarContext)
                            .map(p -> ((PatternVarContext) p).name.getText())
                            .orElse(null);
                    caseContext.saveVariableType(variableName, labelType);
                    caseType = caseInferrer.visitExpression(bodyExpr, expectedType);
                }
                case PatternVarContext patternVarContext -> {
                    String variableName = patternVarContext.name.getText();
                    caseContext.saveVariableType(variableName, expressionType);
                    caseType = caseInferrer.visitExpression(bodyExpr, expectedType);
                }
                case null, default -> caseType = caseInferrer.visitExpression(bodyExpr, expectedType);
            }

            if (caseType == null) {
                return null;
            }

            if (resultType != null && !resultType.equals(caseType)) {
                if (errorManager != null) {
                    errorManager.registerError(
                            ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                            resultType,
                            caseType,
                            bodyExpr
                    );
                }
            }

            resultType = caseType;
        }

        return resultType;
    }

    private boolean arePatternsExhaustive(List<PatternContext> patterns, Type type) {
        return hasAny(patterns) || switch (type) {
            case BoolType ignored -> areBoolPatternsExhaustive(patterns);
            case NatType ignored -> areNatPatternsExhaustive(patterns);
            case SumType ignored -> areSumTypePatternsExhaustive(patterns);
            case UnitType ignored -> areUnitPatternsExhaustive(patterns);
            case TupleType ignored -> throw new UnsupportedOperationException("Not yes implemented");
            case RecordType ignored -> throw new UnsupportedOperationException("Not yes implemented");
            case VariantType variantType -> areVariantPatternsExhaustive(patterns, variantType);
            case FunctionalType ignored -> false;
            case ListType ignored -> false;
            case UnknownType ignored -> throw new IllegalStateException(String.format("Wrong type %s", type));
            default -> throw new IllegalStateException(String.format("Unexpected value: %s", type));
        };
    }

    private boolean areBoolPatternsExhaustive(List<PatternContext> patterns) {
        return patterns.stream()
                    .anyMatch(pattern -> pattern instanceof PatternTrueContext) &&
                patterns.stream()
                    .anyMatch(pattern -> pattern instanceof PatternFalseContext);
    }

    private boolean areNatPatternsExhaustive(List<PatternContext> patterns) {
        return patterns.stream()
                    .anyMatch(pattern -> pattern instanceof PatternIntContext) &&
                patterns.stream()
                        .anyMatch(pattern -> pattern instanceof PatternSuccContext &&
                                ((PatternSuccContext) pattern).pattern_ instanceof PatternVarContext);
    }

    private boolean areSumTypePatternsExhaustive(List<PatternContext> patterns) {
        return patterns.stream()
                    .anyMatch(pattern -> pattern instanceof PatternInlContext) &&
                patterns.stream()
                    .anyMatch(pattern -> pattern instanceof PatternInrContext);
    }

    private boolean areUnitPatternsExhaustive(List<PatternContext> patterns) {
        return patterns.stream()
                .anyMatch(pattern -> pattern instanceof PatternUnitContext);
    }

    private boolean areVariantPatternsExhaustive(List<PatternContext> patterns, VariantType type) {
        HashSet<String> labelsInType = new HashSet<>(type.getLabels());
        HashSet<String> labelsInPattern = (HashSet<String>) patterns.stream()
                .filter(pattern -> pattern instanceof PatternVariantContext)
                .map(pattern -> ((PatternVariantContext) pattern).label.getText())
                .collect(Collectors.toSet());
        return labelsInPattern.containsAll(labelsInType);
    }

    private boolean hasAny(List<PatternContext> patterns) {
        return patterns.stream()
                .anyMatch(pattern -> pattern instanceof PatternVarContext);
    }

    private PatternContext findWrongPattern(List<PatternContext> patterns, Type type) {
        return switch (type) {
            case BoolType ignored -> findWrongBoolPattern(patterns);
            case NatType ignored -> findWrongNatPattern(patterns);
            case SumType ignored -> findWrongSumTypePattern(patterns);
            case UnitType ignored -> findWrongUnitPattern(patterns);
            case VariantType variantType -> findWrongVariantPattern(patterns, variantType);
            case TupleType ignored -> {
                throw new UnsupportedOperationException("Handling for TupleType is not yet implemented");
            }
            case RecordType ignored -> {
                throw new UnsupportedOperationException("Handling for RecordType is not yet implemented");
            }
            case FunctionalType ignored -> findNotVarPattern(patterns);
            case ListType ignored -> findNotVarPattern(patterns);
            case UnknownType ignored -> throw  new IllegalStateException(String.format("Unexpected type: %s", type));
            default -> throw new IllegalArgumentException(String.format("Unhandled type: %s", type));
        };
    }

    private PatternContext findWrongBoolPattern(List<PatternContext> patterns) {
        return patterns.stream()
                .filter(pattern -> pattern instanceof PatternTrueContext ||
                        pattern instanceof PatternFalseContext ||
                        pattern instanceof PatternVarContext)
                .findFirst()
                .orElse(null);
    }

    private PatternContext findWrongNatPattern(List<PatternContext> patterns) {
        return patterns.stream()
                .filter(pattern ->
                        !(pattern instanceof PatternIntContext) &&
                        !(pattern instanceof PatternSuccContext succContext &&
                            succContext.pattern_ instanceof PatternVarContext) &&
                        !(pattern instanceof PatternVarContext))
                .findFirst()
                .orElse(null);
    }

    private PatternContext findWrongSumTypePattern(List<PatternContext> patterns) {
        return patterns.stream()
                .filter(pattern ->
                        !(pattern instanceof PatternInlContext) &&
                        !(pattern instanceof PatternInrContext) &&
                        !(pattern instanceof PatternVarContext))
                .findFirst()
                .orElse(null);
    }

    private PatternContext findWrongUnitPattern(List<PatternContext> patterns) {
        return patterns.stream()
                .filter(pattern ->
                        !(pattern instanceof PatternUnitContext) &&
                        !(pattern instanceof PatternVarContext))
                .findFirst()
                .orElse(null);
    }

    private PatternContext findNotVarPattern(List<PatternContext> patterns) {
        return patterns.stream()
                .filter(pattern ->
                        !(pattern instanceof PatternVarContext))
                .findFirst()
                .orElse(null);
    }

    private PatternContext findWrongVariantPattern(List<PatternContext> patterns, VariantType type) {
        HashSet<String> labelsInType = new HashSet<>(type.getLabels());
        return patterns.stream()
                .filter(pattern ->
                        !(pattern instanceof PatternVarContext) &&
                        !(pattern instanceof PatternVariantContext variantContext &&
                                labelsInType.contains(variantContext.label.getText())))
                .findFirst()
                .orElse(null);
    }


    private Type visitInl(InlContext ctx, Type expectedType) {
        if (expectedType == null) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_AMBIGUOUS_SUM_TYPE,
                        ctx
                );
            }
            return null;
        }

        if (!(expectedType instanceof SumType sumType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_INJECTION,
                        expectedType
                );
            }
            return null;
        }

        Type leftType = sumType.getLeft();

        if (visitExpression(ctx.expr_, leftType)== null) {
            return null;
        }

        return expectedType;
    }

    private Type visitInr(InrContext ctx, Type expectedType) {
        if (expectedType == null) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_AMBIGUOUS_SUM_TYPE,
                        ctx
                );
            }
            return null;
        }

        if (!(expectedType instanceof SumType sumType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_INJECTION,
                        expectedType
                );
            }
            return null;
        }

        Type rightType = sumType.getRight();

        if (visitExpression(ctx.expr_, rightType)== null) {
            return null;
        }

        return expectedType;
    }

    private VariantType visitVariant(VariantContext ctx, Type expectedType) {
        if (expectedType == null) {
            throw new IllegalArgumentException(String.format("Cannot infer type of variant %s", ctx.toStringTree()));
        }

        if (!(expectedType instanceof VariantType variantType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_VARIANT,
                        expectedType
                );
            }
            return null;
        }

        String label = ctx.label.getText();
        int labelIndex = variantType.getLabels().indexOf(label);

        if (labelIndex == -1) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_VARIANT_LABEL,
                        label,
                        ctx,
                        expectedType
                );
            }
            return null;
        }

        Type expectedEpxressionType = variantType.getTypes().get(labelIndex);
        ExprContext expression = ctx.rhs;
        visitExpression(expression, expectedEpxressionType);

        return variantType;
    }

    private ListType visitList(ListContext ctx, Type expectedType) {
        if (expectedType != null && !(expectedType instanceof ListType)) {
            ListType listType = visitList(ctx, null);
            if (listType == null) return null;

            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_LIST,
                        expectedType,
                        listType
                );
            }

            return null;
        }

        ArrayList<ExprContext> expressions = (ArrayList<ExprContext>) ctx.exprs;
        if (expectedType == null && expressions.isEmpty()) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_AMBIGUOUS_LIST,
                        ctx
                );
            }
            return null;
        }

        List<Type> expressionTypes = expressions.stream()
                .map(expr -> visitExpression(expr, null))
                .toList();

        if (expressionTypes.contains(null)) return null;

        ListType listType = (expectedType instanceof ListType)
                ? (ListType) expectedType
                : expressionTypes.stream()
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(ListType::new)
                    .orElse(null);

        if (listType == null) return null;

        int firstWrongTypedExpressionIndex = findFirstWrongTypedExpressionIndex(expressionTypes, listType);
        if (firstWrongTypedExpressionIndex != -1) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        listType,
                        expressionTypes.get(firstWrongTypedExpressionIndex),
                        expressions.get(firstWrongTypedExpressionIndex)
                );
            }
            return null;
        }

        return new ListType(listType.getType());
    }

    private int findFirstWrongTypedExpressionIndex(List<Type> expressionTypes, ListType listType) {
        for (int i = 0; i < expressionTypes.size(); ++i) {
            if (expressionTypes.get(i) != null && !expressionTypes.get(i).equals(listType.getType())) {
                return i;
            }
        }
        return -1;
    }

    private ListType visitConsList(ConsListContext ctx, Type expectedType) {
        if (expectedType != null && !(expectedType instanceof ListType)) {
            ListType listType = visitConsList(ctx, null);

            if (listType == null) return null;

            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_LIST,
                        expectedType,
                        listType
                );
            }

            return null;
        }

        ExprContext head = ctx.head;
        Type headType = visitExpression(head, null);
        if (headType == null) return null;

        if (expectedType instanceof ListType expectedListType) {
            if (!expectedListType.getType().equals(headType)) {
                if (errorManager != null) {
                    errorManager.registerError(
                            ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                            expectedListType.getType(),
                            headType,
                            head
                    );
                }
                return null;
            }
        }

        ListType resultType = new ListType(headType);

        ExprContext tail = ctx.tail;
        if (visitExpression(tail, resultType) == null) return null;

        return resultType;
    }

    private Type visitHead(HeadContext ctx, Type expectedType) {
        ListType listType = (expectedType != null)
                ? new ListType(expectedType)
                : null;

        ExprContext list = ctx.list;
        Type expressionType = visitExpression(list, listType);
        if (expressionType == null) return null;

        if (!(expressionType instanceof ListType listTypeExpression)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_NOT_A_LIST,
                        expressionType,
                        list
                );
            }
            return null;
        }

        Type actualType = listTypeExpression.getType();
        return validateTypes(actualType, expectedType, ctx);
    }

    private Type visitTail(TailContext ctx, Type expectedType) {
        if (expectedType != null && !(expectedType instanceof ListType)) {
            Type actualType = visitTail(ctx, null);
            if (actualType == null) return null;

            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        expectedType,
                        actualType,
                        ctx
                );
            }

            return null;
        }

        ExprContext list = ctx.list;
        Type expressionType = visitExpression(list, expectedType);
        if (expressionType == null) return null;

        if (!(expressionType instanceof ListType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_NOT_A_LIST,
                        expressionType,
                        list
                );
            }
            return null;
        }

        return validateTypes(expressionType, expectedType, ctx);
    }

    private BoolType visitIsEmpty(IsEmptyContext ctx, Type expectedType) {
        if (expectedType != null && !(expectedType instanceof BoolType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        expectedType,
                        BoolType.INSTANCE,
                        ctx
                );
            }
            return null;
        }

        ExprContext expression = ctx.expr();
        Type expressionType = visitExpression(expression, null);
        if (expressionType == null) return null;

        if (!(expressionType instanceof ListType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_NOT_A_LIST,
                        expressionType,
                        expression
                );
            }
            return null;
        }

        return BoolType.INSTANCE;
    }


    private Type validateTypes(Type actualType, Type expectedType, ParserRuleContext expression) {
        if (expectedType == null) {
            return actualType;
        }

        if (actualType instanceof FunctionalType && !(expectedType instanceof FunctionalType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_LAMBDA,
                        expectedType,
                        actualType,
                        expression
                );
            }
            return null;
        }

        if (actualType instanceof TupleType && !(expectedType instanceof TupleType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TUPLE,
                        expectedType,
                        actualType
                );
            }
            return null;
        }

        if (actualType instanceof RecordType && !(expectedType instanceof RecordType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_RECORD,
                        expectedType,
                        actualType
                );
            }
            return null;
        }

        if (!(actualType instanceof FunctionalType) && expectedType instanceof FunctionalType) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_NOT_A_FUNCTION,
                        actualType,
                        expression
                );
            }
            return null;
        }

        if (!(actualType instanceof TupleType) && expectedType instanceof TupleType) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_NOT_A_TUPLE,
                        actualType,
                        expression
                );
            }
            return null;
        }

        if (!(actualType instanceof RecordType) && expectedType instanceof RecordType) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_NOT_A_RECORD,
                        actualType,
                        expression
                );
            }
            return null;
        }

        if (actualType instanceof TupleType actualTupleType) {
            TupleType expectedTupleType = (TupleType) expectedType;
            if (actualTupleType.getArity() != expectedTupleType.getArity()) {
                if (errorManager != null) {
                    errorManager.registerError(
                            ErrorType.ERROR_UNEXPECTED_TUPLE_LENGTH,
                            expectedTupleType.getArity(),
                            actualTupleType.getArity(),
                            expression
                    );
                }
            }
            return null;
        }

        if (actualType == null || !actualType.equals(expectedType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        expectedType,
                        actualType,
                        expression
                );
            }
            return null;
        }

        return actualType;
    }
}
