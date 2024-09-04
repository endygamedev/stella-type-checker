package dev.ebronnikov.typechecker.types;

import dev.ebronnikov.antlr.stellaParser.*;
import dev.ebronnikov.typechecker.checker.ExhaustivenessChecker;
import dev.ebronnikov.typechecker.checker.ExtensionManager;
import dev.ebronnikov.typechecker.errors.ErrorManager;
import dev.ebronnikov.typechecker.errors.ErrorType;
import org.antlr.v4.runtime.ParserRuleContext;
import dev.ebronnikov.typechecker.utils.Pair;

import java.util.*;

public final class TypeInferrer {
    private final ErrorManager errorManager;
    private final ExtensionManager extensionManager;
    private final TypeContext context;

    public TypeInferrer(ErrorManager errorManager, ExtensionManager extensionManager, TypeContext parentContext) {
        this.errorManager = errorManager;
        this.extensionManager = extensionManager;
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
            case ParenthesisedExprContext parenthesisedExprContext ->
                    visitExpression(parenthesisedExprContext.expr_, expectedType);
            case RecordContext recordContext -> visitRecord(recordContext, expectedType);
            case LetContext letContext -> visitLet(letContext, expectedType);
            case TypeAscContext typeAscContext -> visitTypeAsc(typeAscContext, expectedType);
            case NatRecContext natRecContext -> visitNatRec(natRecContext, expectedType);
            case DotTupleContext dotTupleContext -> visitDotTuple(dotTupleContext, expectedType);
            case IfContext ifContext -> visitIf(ifContext, expectedType);
            case TupleContext tupleContext -> visitTuple(tupleContext, expectedType);
            case TerminatingSemicolonContext terminatingSemicolonContext ->
                    visitExpression(terminatingSemicolonContext.expr_, expectedType);
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
            case SequenceContext sequenceContext -> visitSequence(sequenceContext, expectedType);
            case RefContext refContext -> visitRef(refContext, expectedType);
            case ConstMemoryContext constMemoryContext -> visitConstMemory(constMemoryContext, expectedType);
            case DerefContext derefContext -> visitDeref(derefContext, expectedType);
            case AssignContext assignContext -> visitAssign(assignContext, expectedType);
            case PanicContext panicContext -> visitPanic(panicContext, expectedType);
            case ThrowContext throwContext -> visitThrow(throwContext, expectedType);
            case TryWithContext tryWithContext -> visitTryWith(tryWithContext, expectedType);
            case TryCatchContext tryCatchContext -> visitTryCatch(tryCatchContext, expectedType);
            case TypeCastContext typeCastContext -> visitTypeCast(typeCastContext, expectedType);
            default -> {
                System.out.printf("Unsupported syntax for %s%n", ctx.getClass().getSimpleName());
                yield null;
            }
        };

        if (type == null) {
            return null;
        }

        return validateTypes(type, expectedType, ctx);
    }

    private BoolType visitIsZero(IsZeroContext ctx) {
        if (visitExpression(ctx.n, NatType.INSTANCE) == NatType.INSTANCE) {
            return BoolType.INSTANCE;
        }
        return null;
    }

    private NatType visitSucc(SuccContext ctx) {
        if (visitExpression(ctx.n, NatType.INSTANCE) == NatType.INSTANCE) {
            return NatType.INSTANCE;
        }
        return null;
    }

    private NatType visitPred(PredContext ctx) {
        if (visitExpression(ctx.n, NatType.INSTANCE) == NatType.INSTANCE) {
            return NatType.INSTANCE;
        }
        return null;
    }

    private Type visitVar(VarContext ctx, Type expectedType) {
        String name = ctx.name.getText();
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

        if (expectedType != null && !type.isSubtypeOf(expectedType, extensionManager.isStructuralSubtyping())) {
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

        TypeInferrer innerInferrer = new TypeInferrer(errorManager, extensionManager, innerContext);

        var returnExpr = ctx.returnExpr;
        Type returnType = innerInferrer.visitExpression(returnExpr, (expectedType != null) ? ((FunctionalType) expectedType).getTo() : null);
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

        Type resultType = functionalType.getTo();

        var arg = ctx.args.getFirst();
        visitExpression(arg, functionalType.getFrom());

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
            if (errorManager != null) {
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

        TypeInferrer letTypeInferrer = new TypeInferrer(errorManager, extensionManager, letContext);
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
        visitExpression(ctx.condition, BoolType.INSTANCE);

        Type thenType = visitExpression(ctx.thenExpr, expectedType);
        if (thenType == null) {
            return null;
        }

        Type elseType = visitExpression(ctx.elseExpr, thenType);
        if (elseType == null) {
            return null;
        }

        if (!elseType.equals(thenType)) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        thenType,
                        elseType,
                        ctx.elseExpr
                );
            }
            return null;
        }

        return elseType;
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

            return null;
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
        Type expressionType = visitExpression(ctx.expr_, null);
        if (expressionType == null) {
            return null;
        }

        List<MatchCaseContext> cases = ctx.cases;
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

        ExhaustivenessChecker exhaustivenessChecker = new ExhaustivenessChecker();
        if (!exhaustivenessChecker.checkForPatternsTypeMismatch(patterns, expressionType, errorManager)) {
            return null;
        }

        PatternContext wrongPattern = exhaustivenessChecker.findWrongPattern(patterns, expressionType);
        if (wrongPattern != null) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE,
                    wrongPattern,
                    ctx
            );
            return null;
        }

        boolean arePatternExhaustive = exhaustivenessChecker.arePatternsExhaustive(patterns, expressionType);
        if (!arePatternExhaustive) {
            errorManager.registerError(
                    ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS,
                    expressionType
            );
            return null;
        }

        Type resultType = null;

        for (MatchCaseContext caseContext : cases) {
            TypeContext caseTypeContext = new TypeContext(context);
            TypeInferrer caseInferrer = new TypeInferrer(errorManager, extensionManager, caseTypeContext);

            PatternContext pattern = caseContext.pattern_;
            ExprContext bodyExpr = caseContext.expr_;

            Type caseType = null;

            if (pattern instanceof PatternInlContext) {
                SumType sumType = (SumType) expressionType;
                String variableName = ((PatternVarContext) ((PatternInlContext) pattern).pattern_).name.getText();
                if (variableName == null) return null;
                Type type = sumType.getLeft();
                caseTypeContext.saveVariableType(variableName, type);
                caseType = caseInferrer.visitExpression(bodyExpr, expectedType);
            } else if (pattern instanceof PatternInrContext) {
                SumType sumType = (SumType) expressionType;
                String variableName = ((PatternVarContext) ((PatternInrContext) pattern).pattern_).name.getText();
                if (variableName == null) return null;
                Type type = sumType.getRight();
                caseTypeContext.saveVariableType(variableName, type);
                caseType = caseInferrer.visitExpression(bodyExpr, expectedType);
            } else if (pattern instanceof PatternVariantContext) {
                VariantType variantType = (VariantType) expressionType;
                String labelName = ((PatternVariantContext) pattern).label.getText();
                int labelIndex = variantType.getLabels().indexOf(labelName);
                if (labelIndex == -1) {
                    errorManager.registerError(
                            ErrorType.ERROR_UNEXPECTED_VARIANT_LABEL,
                            labelName,
                            pattern,
                            expectedType != null ? expectedType : null
                    );
                    return null;
                }

                Type labelType = variantType.getTypes().get(labelIndex);
                String variableName = ((PatternVarContext) ((PatternVariantContext) pattern).pattern_).name.getText();
                if (variableName == null) return null;
                caseTypeContext.saveVariableType(variableName, labelType);
                caseType = caseInferrer.visitExpression(bodyExpr, expectedType);
            } else if (pattern instanceof PatternVarContext) {
                String variableName = ((PatternVarContext) pattern).name.getText();
                caseTypeContext.saveVariableType(variableName, expressionType);
                caseType = caseInferrer.visitExpression(bodyExpr, expectedType);
            } else {
                caseType = caseInferrer.visitExpression(bodyExpr, expectedType);
            }

            if (caseType == null) return null;

            if (resultType != null && !resultType.equals(caseType)) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        resultType,
                        caseType,
                        bodyExpr
                );
            }

            resultType = caseType;
        }

        return resultType;
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

        if (visitExpression(ctx.expr_, leftType) == null) {
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

        if (visitExpression(ctx.expr_, rightType) == null) {
            return null;
        }

        return expectedType;
    }

    private VariantType visitVariant(VariantContext ctx, Type expectedType) {
        if (expectedType == null) {
            if (errorManager != null) {
                errorManager.registerError(
                        ErrorType.ERROR_AMBIGUOUS_VARIANT_TYPE,
                        ctx
                );
            }
            return null;
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
        if (expectedType != null && !(expectedType instanceof ListType || expectedType instanceof TopType)) {
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
            if (validateTypes(headType, expectedListType.getType(), ctx) == null) {
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

    private Type visitSequence(SequenceContext ctx, Type expectedType) {
        if (visitExpression(ctx.expr1, UnitType.INSTANCE) == null) {
            return null;
        }
        return visitExpression(ctx.expr2, expectedType);
    }

    private ReferenceType visitRef(RefContext ctx, Type expectedType) {
        if (expectedType != null && !(expectedType instanceof ReferenceType) && !(expectedType instanceof TopType)) {
            ReferenceType ref = visitRef(ctx, null);
            if (ref == null) {
                return null;
            }
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_REFERENCE,
                    ref,
                    expectedType
            );
            return null;
        }

        Type innerExpectedType = (expectedType instanceof ReferenceType referenceType)
                ? referenceType.getInnerType()
                : null;

        Type innerType = visitExpression(ctx.expr_, innerExpectedType);
        if (innerType == null) {
            return null;
        }

        return new ReferenceType(innerType);
    }

    private Type visitConstMemory(ConstMemoryContext ctx, Type expectedType) {
        if (expectedType == null) {
            errorManager.registerError(
                    ErrorType.ERROR_AMBIGUOUS_REFERENCE_TYPE,
                    ctx
            );
            return null;
        }

        if (!(expectedType instanceof ReferenceType)) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_MEMORY_ADDRESS,
                    ctx,
                    expectedType
            );
            return null;
        }

        return expectedType;
    }

    private Type visitDeref(DerefContext ctx, Type expectedType) {
        ReferenceType expectedRefType = (expectedType != null) ? new ReferenceType(expectedType) : null;
        Type refType = visitExpression(ctx.expr_, expectedRefType);

        if (refType == null) {
            return null;
        }

        if (!(refType instanceof ReferenceType referenceType)) {
            errorManager.registerError(
                    ErrorType.ERROR_NOT_A_REFERENCE,
                    refType
            );
            return null;
        }

        return validateTypes(referenceType.getInnerType(), expectedType, ctx);
    }

    private UnitType visitAssign(AssignContext ctx, Type expectedType) {
        if (expectedType != null && !(expectedType instanceof UnitType)) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                    UnitType.INSTANCE,
                    expectedType,
                    ctx
            );
            return null;
        }

        Type lhsType = visitExpression(ctx.lhs, null);
        if (lhsType == null) {
            return null;
        }

        if (!(lhsType instanceof ReferenceType lhsRefType)) {
            errorManager.registerError(
                    ErrorType.ERROR_NOT_A_REFERENCE,
                    lhsType
            );
            return null;
        }

        Type rhsType = visitExpression(ctx.rhs, null);
        if (rhsType == null) {
            return null;
        }

        if (!lhsRefType.getInnerType().equals(rhsType)) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                    lhsRefType.getInnerType(),
                    rhsType,
                    ctx
            );
            return null;
        }

        return UnitType.INSTANCE;
    }

    private Type visitPanic(PanicContext ctx, Type expectedType) {
        if (expectedType == null) {
            if (extensionManager.isAmbiguousTypeAsBottom()) {
                return BotType.INSTANCE;
            }
            errorManager.registerError(
                    ErrorType.ERROR_AMBIGUOUS_PANIC_TYPE,
                    ctx
            );

            return null;
        }

        return expectedType;
    }

    private Type visitThrow(ThrowContext ctx, Type expectedType) {
        if (expectedType == null) {
            errorManager.registerError(
                    ErrorType.ERROR_AMBIGUOUS_THROW_TYPE,
                    ctx
            );
            return null;
        }

        Type exceptionType = context.getExceptionType();
        if (exceptionType == null) {
            errorManager.registerError(
                    ErrorType.ERROR_EXCEPTION_TYPE_NOT_DECLARED
            );
            return null;
        }

        if (visitExpression(ctx.expr_, exceptionType) == null) {
            return null;
        }

        return expectedType;
    }

    private Type visitTryWith(TryWithContext ctx, Type expectedType) {
        Type mainType = visitExpression(ctx.tryExpr, expectedType);
        if (mainType == null) {
            return null;
        }

        if (visitExpression(ctx.fallbackExpr, mainType) == null) {
            return null;
        }

        return mainType;
    }

    private Type visitTryCatch(TryCatchContext ctx, Type expectedType) {
        Type mainType = visitExpression(ctx.tryExpr, expectedType);
        if (mainType == null) {
            return null;
        }

        if (visitExpression(ctx.fallbackExpr, expectedType) == null) {
            return null;
        }

        return mainType;
    }

    private Type visitTypeCast(TypeCastContext ctx, Type expectedType) {
        if (visitExpression(ctx.expr_, null) == null) {
            return null;
        }

        Type actualType = SyntaxTypeProcessor.getType(ctx.type_);

        return validateTypes(actualType, expectedType, ctx);
    }

    private Type validateTypes(Type actualType, Type expectedType, ParserRuleContext expression) {
        if (expectedType == null) {
            return actualType;
        }

        if (actualType.isSubtypeOf(expectedType, extensionManager.isStructuralSubtyping())) {
            return expectedType;
        }

        if (actualType instanceof RecordType actualRecordType && expectedType instanceof RecordType expectedRecordType) {
            boolean result = validateRecords(actualRecordType, expectedRecordType, expression);
            if (result) {
                return expectedType;
            }
            return null;
        }

        if (actualType instanceof TupleType actualTupleType && expectedType instanceof TupleType expectedTupleType) {
            if (actualTupleType.getArity() != expectedTupleType.getArity()) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TUPLE_LENGTH,
                        expectedTupleType.getArity(),
                        actualTupleType.getArity(),
                        expression
                );
                return null;
            }
        }

        if (actualType instanceof VariantType actualVariantType && expectedType instanceof VariantType expectedVariantType) {
            List<String> expectedLabels = expectedVariantType.getLabels();
            List<String> actualLabels = actualVariantType.getLabels();

            if (!expectedLabels.containsAll(actualLabels)) {
                String missingLabel = expectedLabels.stream()
                        .filter(label -> !actualLabels.contains(label))
                        .findFirst()
                        .orElse(null);

                if (missingLabel != null) {
                    errorManager.registerError(
                            ErrorType.ERROR_UNEXPECTED_VARIANT_LABEL,
                            missingLabel,
                            expression,
                            actualType
                    );
                }
                return null;
            }
        }

        if (extensionManager.isStructuralSubtyping()) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_SUBTYPE,
                    expectedType,
                    actualType,
                    expression
            );
        } else {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                    expectedType,
                    actualType,
                    expression
            );
        }

        return null;
    }

    private boolean validateRecords(RecordType expectedRecord, RecordType actualRecord, ParserRuleContext ctx) {
        if (!expectedRecord.equals(actualRecord) && !extensionManager.isStructuralSubtyping()) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                    expectedRecord,
                    actualRecord,
                    ctx
            );
            return false;
        }

        Set<Map.Entry<String, Type>> missingFields = new HashSet<>(expectedRecord.getLabels().size());
        Set<Map.Entry<String, Type>> extraFields = new HashSet<>(actualRecord.getLabels().size());

        Set<Map.Entry<String, Type>> expectedEntries = zip(expectedRecord.getLabels(), expectedRecord.getTypes());
        Set<Map.Entry<String, Type>> actualEntries = zip(actualRecord.getLabels(), actualRecord.getTypes());

        missingFields.addAll(actualEntries);
        missingFields.removeAll(expectedEntries);

        if (!missingFields.isEmpty()) {
            if (extensionManager.isStructuralSubtyping() && missingFields.stream().allMatch(entry -> actualRecord.getLabels().contains(entry.getKey()))) {
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_SUBTYPE,
                        expectedRecord,
                        actualRecord,
                        ctx
                );
                return false;
            }

            Map.Entry<String, Type> missingField = missingFields.iterator().next();
            errorManager.registerError(
                    ErrorType.ERROR_MISSING_RECORD_FIELDS,
                    missingField.getKey(),
                    expectedRecord
            );
            return false;
        }

        if (!extraFields.isEmpty() && !extensionManager.isStructuralSubtyping()) {
            Map.Entry<String, Type> extraField = extraFields.iterator().next();
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_RECORD_FIELDS,
                    extraField.getKey(),
                    expectedRecord
            );
            return false;
        }

        for (Map.Entry<String, Type> entry : zip(actualRecord.getLabels(), actualRecord.getTypes())) {
            String label = entry.getKey();
            Type type = entry.getValue();

            int expectedTypeForLabelIdx = expectedRecord.getLabels().indexOf(label);
            Type expectedTypeForLabel = (expectedTypeForLabelIdx >= 0) ? expectedRecord.getTypes().get(expectedTypeForLabelIdx) : null;

            if (type instanceof RecordType) {
                if (!(expectedTypeForLabel instanceof RecordType)) {
                    errorManager.registerError(
                            ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                            expectedTypeForLabel,
                            type,
                            ctx
                    );
                    return false;
                }

                if (!validateRecords((RecordType) expectedTypeForLabel, (RecordType) type, ctx)) {
                    return false;
                }
            } else {
                if (!type.equals(expectedTypeForLabel)) {
                    if (!(expectedTypeForLabel instanceof RecordType)) {
                        errorManager.registerError(
                                ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                                expectedTypeForLabel,
                                type,
                                ctx
                        );
                        return false;
                    }
                }
            }
        }

        return true;
    }

        private Set<Map.Entry<String, Type>> zip(List < String > labels, List < Type > types){
            Set<Map.Entry<String, Type>> entries = new HashSet<>();
            for (int i = 0; i < labels.size(); ++i) {
                String label = labels.get(i);
                Type type = types.get(i);
                entries.add(new AbstractMap.SimpleEntry<>(label, type));
            }
            return entries;
        }
    }
