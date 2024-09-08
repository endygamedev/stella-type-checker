package dev.ebronnikov.typechecker.types;

import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.antlr.stellaParser.*;
import dev.ebronnikov.typechecker.checker.ExhaustivenessChecker;
import dev.ebronnikov.typechecker.checker.ExtensionManager;
import dev.ebronnikov.typechecker.checker.UnifySolver;
import dev.ebronnikov.typechecker.errors.ErrorManager;
import dev.ebronnikov.typechecker.errors.ErrorType;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TypeInferrer {
    private final ErrorManager errorManager;
    private final ExtensionManager extensionManager;
    private final TypeContext context;
    private final UnifySolver unifySolver;

    public TypeInferrer(ErrorManager errorManager, ExtensionManager extensionManager, TypeContext parentContext, UnifySolver unifySolver) {
        this.errorManager = errorManager;
        this.extensionManager = extensionManager;
        this.context = new TypeContext(parentContext);
        this.unifySolver = unifySolver;
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
            case TypeApplicationContext typeApplicationContext ->
                    visitTypeApplication(typeApplicationContext, expectedType);
            case TypeAbstractionContext typeAbstractionContext ->
                    visitTypeAbstraction(typeAbstractionContext, expectedType);
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

    private Type visitVar(stellaParser.VarContext ctx, Type expectedType) {
        String name = ctx.name.getText();
        Type type = context.resolveVariableType(name);

        if (type == null) {
            type = context.resolveFunctionType(name);
        }

        if (type == null) {
            errorManager.registerError(
                    ErrorType.ERROR_UNDEFINED_VARIABLE,
                    name,
                    ctx
            );
            return null;
        }

        return validateTypes(type, expectedType, ctx);
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

    private Type visitAbstraction(AbstractionContext ctx, Type expectedType) {
        if (expectedType != null && !(expectedType instanceof FunctionalType) && !(expectedType instanceof TypeVar)) {
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

        if (!ensureTypeIsKnown(argType)) {
            return null;
        }

        TypeContext innerContext = new TypeContext(context);
        innerContext.saveVariableType(arg.name.getText(), argType);

        TypeInferrer innerInferrer = new TypeInferrer(errorManager, extensionManager, innerContext, unifySolver);
        stellaParser.ExprContext returnExpr = ctx.returnExpr;
        Type returnType;

        if (expectedType instanceof TypeVar) {
            TypeVar retTypeVar = TypeVar.newInstance();

            Type expectedFuncType = new FunctionalType(argType, retTypeVar);
            unifySolver.addConstraint(expectedType, expectedFuncType, ctx);

            returnType = innerInferrer.visitExpression(returnExpr, retTypeVar);
        } else {
            Type expectedReturnType = (expectedType instanceof FunctionalType) ? ((FunctionalType) expectedType).getTo() : null;
            returnType = innerInferrer.visitExpression(returnExpr, expectedReturnType);
        }

        if (returnType == null) {
            return null;
        }

        Type result = new FunctionalType(argType, returnType);
        return validateTypes(result, expectedType, ctx);
    }

    private boolean ensureTypeIsKnown(Type type) {
        if (!(type instanceof GenericType genericType)) {
            return true;
        }

        GenericType typeFromContext = context.resolveGenericType(genericType);

        if (typeFromContext == null) {
            errorManager.registerError(
                    ErrorType.ERROR_UNDEFINED_TYPE_VARIABLE,
                    type
            );
            return false;
        }

        return true;
    }

    private Type visitApplication(ApplicationContext ctx, Type expectedType) {
        var func = ctx.fun;

        Type funType = visitExpression(func, null);
        if (funType == null) {
            return null;
        }

        if (extensionManager.isTypeReconstructionEnabled()) {
            stellaParser.ExprContext arg = ctx.args.get(0);
            Type argType = visitExpression(arg, null);
            if (argType == null) {
                return null;
            }

            Type expectedTypeOrVarType = expectedType != null ? expectedType : TypeVar.newInstance();

            unifySolver.addConstraint(funType, new FunctionalType(argType, expectedTypeOrVarType), ctx);

            return expectedTypeOrVarType;
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
        if (visitExpression(arg, functionalType.getFrom()) == null) {
            return null;
        }

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

        TypeInferrer letTypeInferrer = new TypeInferrer(errorManager, extensionManager, letContext, unifySolver);
        return letTypeInferrer.visitExpression(ctx.body, expectedType);
    }

    private Type visitTypeAsc(TypeAscContext ctx, Type expectedType) {
        var expression = ctx.expr_;
        Type targetType = SyntaxTypeProcessor.getType(ctx.type_);

        if (!ensureTypeIsKnown(targetType)) {
            return null;
        }

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

        Type expectedStepFunctionalType = new FunctionalType(
                NatType.INSTANCE,
                new FunctionalType(initialValueType, initialValueType)
        );

        Type stepFunctionType = visitExpression(ctx.step, expectedStepFunctionalType);
        if (stepFunctionType == null) {
            return null;
        }

        return initialValueType;
    }

    private Type visitDotTuple(DotTupleContext ctx, Type expectedType) {
        stellaParser.ExprContext expr = ctx.expr_;
        Type expressionType = visitExpression(expr, null);
        if (expressionType == null) {
            return null;
        }

        String indexContext = ctx.index.getText();
        int indexValue = Integer.parseInt(indexContext);

        if (extensionManager.isTypeReconstructionEnabled()) {
            Type pairType1 = TypeVar.newInstance();
            Type pairType2 = TypeVar.newInstance();

            TupleType pairType = new TupleType(Arrays.asList(pairType1, pairType2));

            unifySolver.addConstraint(expressionType, pairType, ctx);

            return validateTypes(pairType.getTypes().get(indexValue - 1), expectedType, ctx);
        }

        if (!(expressionType instanceof TupleType)) {
            errorManager.registerError(
                    ErrorType.ERROR_NOT_A_TUPLE,
                    expressionType,
                    ctx
            );
            return null;
        }

        TupleType tupleType = (TupleType) expressionType;
        if (indexValue > tupleType.getArity() || indexValue == 0) {
            errorManager.registerError(
                    ErrorType.ERROR_TUPLE_INDEX_OUT_OF_BOUNDS,
                    indexValue,
                    tupleType.getArity()
            );
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

        if (extensionManager.isTypeReconstructionEnabled()) {
            Type expectedTypeOrVar = Optional.ofNullable(expectedType).orElse(TypeVar.newInstance());
            visitExpression(expression, new FunctionalType(expectedTypeOrVar, expectedTypeOrVar));
            return expectedTypeOrVar;
        }

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

    private Type visitPatternConsContext(stellaParser.PatternConsContext ctx, Type expectedType) {
        if (!(expectedType instanceof ListType)) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE,
                    ctx,
                    expectedType
            );
            return null;
        }

        ListType listType = (ListType) expectedType;

        visitPatternContext(ctx.head, listType.getType());
        visitPatternContext(ctx.tail, listType);

        return listType;
    }

    private Type visitTruePatternContext(stellaParser.PatternTrueContext ctx, Type expectedType) {
        return validatePattern(BoolType.INSTANCE, expectedType, ctx);
    }

    private Type visitFalsePatternContext(stellaParser.PatternFalseContext ctx, Type expectedType) {
        return validatePattern(BoolType.INSTANCE, expectedType, ctx);
    }

    private Type visitUnitPatternContext(stellaParser.PatternUnitContext ctx, Type expectedType) {
        return validatePattern(UnitType.INSTANCE, expectedType, ctx);
    }

    private Type visitVarPatternContext(stellaParser.PatternVarContext ctx, Type expectedType) {
        context.saveVariableType(ctx.name.getText(), expectedType);
        return expectedType;
    }

    private Type visitAscPatternContext(stellaParser.PatternAscContext ctx, Type expectedType) {
        Type type = SyntaxTypeProcessor.getType(ctx.type_);
        if (!ensureTypeIsKnown(type)) {
            return null;
        }

        Type trueType = validatePattern(type, expectedType, ctx);

        if (trueType == null) {
            errorManager.registerError(
                    ErrorType.ERROR_AMBIGUOUS_PATTERN_TYPE,
                    type,
                    ctx
            );
            return null;
        }

        return visitPatternContext(ctx.pattern_, trueType);
    }

    private Type visitInlPatternContext(stellaParser.PatternInlContext ctx, Type expectedType) {
        if (expectedType instanceof TypeVar) {
            Type sumTypeLeft = TypeVar.newInstance();
            Type sumTypeRight = TypeVar.newInstance();
            Type sumType = new SumType(sumTypeLeft, sumTypeRight);

            unifySolver.addConstraint(expectedType, sumType, ctx);
            Type result = visitPatternContext(ctx.pattern_, sumTypeLeft);
            if (result == null) {
                return null;
            }

            return expectedType;
        }

        if (!(expectedType instanceof SumType)) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE,
                    ctx,
                    expectedType
            );

            return null;
        }

        Type result = visitPatternContext(ctx.pattern_, ((SumType) expectedType).getLeft());
        if (result == null) {
            return null;
        }

        return expectedType;
    }

    private Type visitInrPatternContext(stellaParser.PatternInrContext ctx, Type expectedType) {
        if (expectedType instanceof TypeVar) {
            Type sumTypeLeft = TypeVar.newInstance();
            Type sumTypeRight = TypeVar.newInstance();
            Type sumType = new SumType(sumTypeLeft, sumTypeRight);

            unifySolver.addConstraint(expectedType, sumType, ctx);
            Type result = visitPatternContext(ctx.pattern_, sumTypeRight);
            if (result == null) {
                return null;
            }

            return expectedType;
        }

        if (!(expectedType instanceof SumType)) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE,
                    ctx,
                    expectedType
            );

            return null;
        }

        Type result = visitPatternContext(ctx.pattern_, ((SumType) expectedType).getRight());
        if (result == null) {
            return null;
        }

        return expectedType;
    }

    private Type visitVariantPatternContext(stellaParser.PatternVariantContext ctx, Type expectedType) {
        if (!(expectedType instanceof VariantType)) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE,
                    ctx,
                    expectedType
            );

            return null;
        }

        String tagName = ctx.label.getText();
        VariantType variantType = (VariantType) expectedType;
        int varTypeIdx = variantType.getLabels().indexOf(tagName);
        if (varTypeIdx == -1) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE,
                    ctx,
                    expectedType
            );

            return null;
        }

        Type varType = variantType.getTypes().get(varTypeIdx);

        Type result = visitPatternContext(ctx.pattern_, varType);
        if (result == null) {
            return null;
        }

        return expectedType;
    }

    private Type visitPatternContext(stellaParser.PatternContext ctx, Type expectedType) {
        Type resultType;

        if (ctx instanceof stellaParser.PatternConsContext) {
            resultType = visitPatternConsContext((stellaParser.PatternConsContext) ctx, expectedType);
        } else if (ctx instanceof stellaParser.PatternTrueContext) {
            resultType = visitTruePatternContext((stellaParser.PatternTrueContext) ctx, expectedType);
        } else if (ctx instanceof stellaParser.PatternFalseContext) {
            resultType = visitFalsePatternContext((stellaParser.PatternFalseContext) ctx, expectedType);
        } else if (ctx instanceof stellaParser.PatternUnitContext) {
            resultType = visitUnitPatternContext((stellaParser.PatternUnitContext) ctx, expectedType);
        } else if (ctx instanceof stellaParser.PatternVarContext) {
            resultType = visitVarPatternContext((stellaParser.PatternVarContext) ctx, expectedType);
        } else if (ctx instanceof stellaParser.PatternAscContext) {
            resultType = visitAscPatternContext((stellaParser.PatternAscContext) ctx, expectedType);
        } else if (ctx instanceof stellaParser.ParenthesisedPatternContext) {
            resultType = visitPatternContext(((stellaParser.ParenthesisedPatternContext) ctx).pattern_, expectedType);
        } else if (ctx instanceof stellaParser.PatternInlContext) {
            resultType = visitInlPatternContext((stellaParser.PatternInlContext) ctx, expectedType);
        } else if (ctx instanceof stellaParser.PatternInrContext) {
            resultType = visitInrPatternContext((stellaParser.PatternInrContext) ctx, expectedType);
        } else if (ctx instanceof stellaParser.PatternVariantContext) {
            resultType = visitVariantPatternContext((stellaParser.PatternVariantContext) ctx, expectedType);
        } else {
            resultType = null;
        }

        if (resultType == null) {
            return null;
        }

        return validatePattern(resultType, expectedType, ctx);
    }

    private Type visitMatch(MatchContext ctx, Type expectedType) {    // Visit the match expression and get its type
        Type matchExprType = visitExpression(ctx.expr_, null);
        if (matchExprType == null) {
            return null;
        }

        List<stellaParser.MatchCaseContext> cases = ctx.cases;

        if (cases.isEmpty()) {
            errorManager.registerError(ErrorType.ERROR_ILLEGAL_EMPTY_MATCHING, ctx);
            return null;
        }

        List<Type> branchExpressions = cases.stream().map(caseContext -> {
            TypeContext newContext = new TypeContext(context);
            TypeInferrer newChecker = new TypeInferrer(errorManager, extensionManager, newContext, unifySolver);

            if (newChecker.visitPatternContext(caseContext.pattern_, matchExprType) == null) {
                return null;
            }
            return newChecker.visitExpression(caseContext.expr_, expectedType);
        }).collect(Collectors.toList());

        if (branchExpressions.contains(null)) {
            return null;
        }

        List<stellaParser.PatternContext> patterns = cases.stream()
                .map(stellaParser.MatchCaseContext::pattern)
                .collect(Collectors.toList());
        ExhaustivenessChecker exhaustivenessChecker = new ExhaustivenessChecker();
        if (!exhaustivenessChecker.check(patterns, matchExprType)) {
            errorManager.registerError(
                    ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS,
                    matchExprType
            );
            return null;
        }

        Type matchType = branchExpressions.get(0);
        Type firstWrongType = branchExpressions.stream()
                .filter(branchType -> validateTypes(matchType, branchType, ctx) == null)
                .findFirst()
                .orElse(null);

        if (firstWrongType != null) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                    matchType,
                    firstWrongType,
                    ctx
            );
            return null;
        }

        return matchType;
    }

    private Type visitInl(stellaParser.InlContext ctx, Type expectedType) {
        if (expectedType == null && !extensionManager.isTypeReconstructionEnabled()) {
            errorManager.registerError(
                    ErrorType.ERROR_AMBIGUOUS_SUM_TYPE,
                    ctx
            );
            return null;
        }

        if (extensionManager.isTypeReconstructionEnabled()) {
            Type leftType = visitExpression(ctx.expr_, null);
            if (leftType == null) {
                return null;
            }
            return new SumType(leftType, TypeVar.newInstance());
        }

        if (expectedType != null && !(expectedType instanceof SumType) && !(expectedType instanceof BotType)) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_INJECTION,
                    expectedType
            );
            return null;
        }

        if (expectedType instanceof SumType) {
            SumType sumType = (SumType) expectedType;
            Type leftType = visitExpression(ctx.expr_, sumType.getLeft());
            if (leftType == null) {
                return null;
            }
            return expectedType;
        }

        Type leftType = visitExpression(ctx.expr_, null);
        if (leftType == null) {
            return null;
        }
        return new SumType(leftType, BotType.INSTANCE);
    }

    private Type visitInr(stellaParser.InrContext ctx, Type expectedType) {
        if (expectedType == null && !extensionManager.isTypeReconstructionEnabled()) {
            errorManager.registerError(
                    ErrorType.ERROR_AMBIGUOUS_SUM_TYPE,
                    ctx
            );
            return null;
        }

        if (extensionManager.isTypeReconstructionEnabled()) {
            Type rightType = visitExpression(ctx.expr_, null);
            if (rightType == null) {
                return null;
            }
            return new SumType(TypeVar.newInstance(), rightType);
        }

        if (expectedType != null && !(expectedType instanceof SumType) && !(expectedType instanceof BotType)) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_INJECTION,
                    expectedType
            );
            return null;
        }

        if (expectedType instanceof SumType) {
            SumType sumType = (SumType) expectedType;
            Type rightType = visitExpression(ctx.expr_, sumType.getRight());
            if (rightType == null) {
                return null;
            }
            return expectedType;
        }

        Type rightType = visitExpression(ctx.expr_, null);
        if (rightType == null) {
            return null;
        }
        return new SumType(BotType.INSTANCE, rightType);
    }

    private VariantType visitVariant(stellaParser.VariantContext ctx, Type expectedType) {
        if (expectedType != null && !(expectedType instanceof VariantType)) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_VARIANT,
                    expectedType
            );
            return null;
        }

        VariantType variantType = (VariantType) expectedType;

        if (variantType == null) {
            errorManager.registerError(
                    ErrorType.ERROR_AMBIGUOUS_VARIANT_TYPE,
                    ctx
            );
            return null;
        }

        String label = ctx.label.getText();
        int labelIndex = variantType.getLabels().indexOf(label);
        if (labelIndex == -1) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_VARIANT_LABEL,
                    label,
                    ctx,
                    variantType
            );
            return null;
        }

        Type expectedExpressionType = variantType.getTypes().get(labelIndex);

        Type expression = visitExpression(ctx.rhs, expectedExpressionType);

        return variantType;
    }

    private Type visitList(stellaParser.ListContext ctx, Type expectedType) {
        List<stellaParser.ExprContext> expressions = ctx.exprs;
        if (extensionManager.isTypeReconstructionEnabled()) {
            Type elementType = TypeVar.newInstance();
            for (stellaParser.ExprContext expr : expressions) {
                visitExpression(expr, elementType);
            }

            return validateTypes(new ListType(elementType), expectedType, ctx);
        }

        if (expectedType != null && !(expectedType instanceof ListType)) {
            Type listType = visitList(ctx, null);
            if (listType == null) {
                return null;
            }
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_LIST,
                    expectedType,
                    listType
            );
            return null;
        }

        if (expectedType == null && expressions.isEmpty()) {
            errorManager.registerError(
                    ErrorType.ERROR_AMBIGUOUS_LIST,
                    ctx
            );
            return null;
        }

        List<Type> expressionTypes = new ArrayList<>();
        for (stellaParser.ExprContext expr : expressions) {
            Type type = visitExpression(expr, null);
            if (type == null) {
                return null;
            }
            expressionTypes.add(type);
        }

        Type listType = (expectedType instanceof ListType) ? ((ListType) expectedType).getType() :
                (expressionTypes.stream().filter(t -> t != null).findFirst().orElse(BotType.INSTANCE));

        if (extensionManager.isTypeReconstructionEnabled()) {
            for (Type expType : expressionTypes) {
                if (expType != null) {
                    unifySolver.addConstraint(listType, expType, ctx);
                }
            }
            return new ListType(listType);
        }

        int firstWrongTypedExpressionIndex = -1;
        for (int i = 0; i < expressionTypes.size(); i++) {
            if (!expressionTypes.get(i).equals(listType)) {
                firstWrongTypedExpressionIndex = i;
                break;
            }
        }

        if (firstWrongTypedExpressionIndex != -1) {
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                    listType,
                    expressionTypes.get(firstWrongTypedExpressionIndex),
                    expressions.get(firstWrongTypedExpressionIndex)
            );
            return null;
        }

        return new ListType(listType);
    }

    private int findFirstWrongTypedExpressionIndex(List<Type> expressionTypes, ListType listType) {
        for (int i = 0; i < expressionTypes.size(); ++i) {
            if (expressionTypes.get(i) != null && !expressionTypes.get(i).equals(listType.getType())) {
                return i;
            }
        }
        return -1;
    }

    private Type visitConsList(stellaParser.ConsListContext ctx, Type expectedType) {
        if (extensionManager.isTypeReconstructionEnabled()) {
            Type elementType = TypeVar.newInstance();
            Type listType = new ListType(elementType);

            stellaParser.ExprContext head = ctx.head;
            if (visitExpression(head, elementType) == null) {
                return null;
            }

            stellaParser.ExprContext tail = ctx.tail;
            if (visitExpression(tail, listType) == null) {
                return null;
            }

            return validateTypes(listType, expectedType, ctx);
        }

        if (expectedType != null && !(expectedType instanceof ListType) && !(expectedType instanceof TopType)) {
            Type listType = visitConsList(ctx, null);
            if (listType == null) {
                return null;
            }
            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_LIST,
                    expectedType,
                    listType
            );
            return null;
        }

        stellaParser.ExprContext head = ctx.head;
        Type headType = visitExpression(head, null);
        if (headType == null) {
            return null;
        }

        if (expectedType != null && expectedType instanceof ListType) {
            if (validateTypes(headType, ((ListType) expectedType).getType(), ctx) == null) {
                return null;
            }
        }

        Type resultType = new ListType(headType);
        stellaParser.ExprContext tail = ctx.tail;
        if (visitExpression(tail, resultType) == null) {
            return null;
        }

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

    private Type visitRef(stellaParser.RefContext ctx, Type expectedType) {
        if (expectedType != null && !(expectedType instanceof ReferenceType) && !(expectedType instanceof TopType)) {
            Type ref = visitRef(ctx, null);
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

        Type innerExpectedType = (expectedType instanceof ReferenceType)
                ? ((ReferenceType) expectedType).getInnerType()
                : null;

        Type innerType = visitExpression(ctx.expr_, innerExpectedType);
        if (innerType == null) {
            return null;
        }

        return validateTypes(new ReferenceType(innerType), expectedType, ctx);
    }

    private Type visitConstMemory(stellaParser.ConstMemoryContext ctx, Type expectedType) {
        if (expectedType == null) {
            errorManager.registerError(
                    ErrorType.ERROR_AMBIGUOUS_REFERENCE_TYPE,
                    ctx
            );
            return null;
        }

        if (!(expectedType instanceof ReferenceType) && !(expectedType instanceof TopType)) {
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
            errorManager.registerError(
                    ErrorType.ERROR_AMBIGUOUS_PANIC_TYPE,
                    ctx
            );

            return null;
        }

        return expectedType;
    }

    private Type visitThrow(ThrowContext ctx, Type expectedType) {
        if (expectedType == null && !extensionManager.isAmbiguousTypeAsBottom()) {
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

        return (expectedType != null) ? expectedType : BotType.INSTANCE;
    }

    private Type visitTryWith(stellaParser.TryWithContext ctx, Type expectedType) {
        Type exceptionType = context.getExceptionType();
        if (exceptionType == null) {
            errorManager.registerError(
                    ErrorType.ERROR_EXCEPTION_TYPE_NOT_DECLARED
            );
            return null;
        }

        Type mainType = visitExpression(ctx.tryExpr, expectedType);
        if (mainType == null) {
            return null;
        }

        if (visitExpression(ctx.fallbackExpr, mainType) == null) {
            return null;
        }

        return mainType;
    }

    private Type visitTryCatch(stellaParser.TryCatchContext ctx, Type expectedType) {
        Type exceptionType = context.getExceptionType();
        if (exceptionType == null) {
            errorManager.registerError(
                    ErrorType.ERROR_EXCEPTION_TYPE_NOT_DECLARED
            );
            return null;
        }

        Type mainType = visitExpression(ctx.tryExpr, expectedType);
        if (mainType == null) {
            return null;
        }

        stellaParser.PatternContext patternInCatch = ctx.pat;
        if (!(patternInCatch instanceof stellaParser.PatternVarContext)) {
            return expectedType;
        }

        String varName = ((stellaParser.PatternVarContext) patternInCatch).name.getText();

        TypeContext newContext = new TypeContext(context);
        newContext.saveVariableType(varName, exceptionType);
        TypeInferrer newTypeInferrer = new TypeInferrer(errorManager, extensionManager, newContext, unifySolver);

        if (newTypeInferrer.visitExpression(ctx.fallbackExpr, expectedType) == null) {
            return null;
        }

        return mainType;
    }

    private Type visitTypeCast(stellaParser.TypeCastContext ctx, Type expectedType) {
        if (visitExpression(ctx.expr_, null) == null) {
            return null;
        }

        Type actualType = SyntaxTypeProcessor.getType(ctx.type_);

        if (!ensureTypeIsKnown(actualType)) {
            return null;
        }

        return validateTypes(actualType, expectedType, ctx);
    }


    private Type visitTypeApplication(stellaParser.TypeApplicationContext ctx, Type expectedType) {
        var func = ctx.fun;

        Type funTypeWithGenerics = visitExpression(func, null);
        if (funTypeWithGenerics == null) {
            return null;
        }

        if (!(funTypeWithGenerics instanceof UniversalWrapperType) ||
                !(((UniversalWrapperType) funTypeWithGenerics).getInnerType() instanceof FunctionalType)) {
            errorManager.registerError(
                    ErrorType.ERROR_NOT_A_GENERIC_FUNCTION,
                    ctx
            );
            return null;
        }

        var funGenerics = ((UniversalWrapperType) funTypeWithGenerics).getTypeParams();
        var generics = ctx.types.stream()
                .map(SyntaxTypeProcessor::getType)
                .toList();

        if (funGenerics.size() != generics.size()) {
            errorManager.registerError(
                    ErrorType.ERROR_INCORRECT_NUMBER_OF_TYPE_ARGUMENTS,
                    generics.size(),
                    funGenerics.size()
            );
            return null;
        }

        Map<GenericType, Type> genericsSubstitutions = IntStream.range(0, funGenerics.size())
                .boxed()
                .collect(Collectors.toMap(funGenerics::get, generics::get));

        var funTypeAfterSubstitution = ((FunctionalType) ((UniversalWrapperType) funTypeWithGenerics)
                .getInnerType()).withSubstitution(genericsSubstitutions);

        var undefinedType = funTypeAfterSubstitution.getFirstUnresolvedType();
        if (undefinedType != null) {
            errorManager.registerError(
                    ErrorType.ERROR_UNDEFINED_TYPE_VARIABLE,
                    undefinedType
            );
            return null;
        }

        return validateTypes(funTypeAfterSubstitution, expectedType, ctx);
    }

    private Type visitTypeAbstraction(stellaParser.TypeAbstractionContext ctx, Type expectedType) {
        FunctionalType expectedFuncType;

        if (expectedType instanceof UniversalWrapperType universalWrapperType) {
            expectedFuncType = (FunctionalType) universalWrapperType.getInnerType();
        } else if (expectedType instanceof FunctionalType functionalType) {
            expectedFuncType = functionalType;
        } else {
            Type actualType = visitExpression(ctx, null);
            if (actualType == null) {
                return null;
            }

            errorManager.registerError(
                    ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                    expectedType != null ? expectedType : "unknown",
                    actualType,
                    ctx
            );

            return null;
        }

        List<GenericType> typeParams = ctx.generics.stream()
                .map(generic -> new GenericType(generic.getText()))
                .toList();

        TypeContext newContext = new TypeContext(context);
        typeParams.forEach(newContext::saveGenericType);
        TypeInferrer newTypeInferrer = new TypeInferrer(errorManager, extensionManager, newContext, unifySolver);
        FunctionalType actualFuncType = (FunctionalType) newTypeInferrer.visitExpression(ctx.expr_, expectedFuncType);

        if (actualFuncType == null) {
            return null;
        }

        return new UniversalWrapperType(typeParams, actualFuncType);
    }

    private Type validateTypes(Type actualType, Type expectedType, ParserRuleContext expression) {
        if (expectedType == null) {
            return actualType;
        }

        if (extensionManager.isTypeReconstructionEnabled()) {
            unifySolver.addConstraint(actualType, expectedType, expression);
            return expectedType;
        }

        if (actualType == expectedType) {
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

        errorManager.registerError(
                ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                expectedType,
                actualType,
                expression
        );

        return null;
    }

    private Type validatePattern(Type expectedType, Type actualType, stellaParser.PatternContext context) {
        if (expectedType.equals(actualType)) {
            return expectedType;
        }

        if (extensionManager.isTypeReconstructionEnabled()) {
            unifySolver.addConstraint(expectedType, actualType, context);
            return expectedType;
        }

        errorManager.registerError(
                ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE,
                context,
                expectedType
        );

        return null;
    }

    private boolean validateRecords(RecordType expectedRecord, RecordType actualRecord, ParserRuleContext ctx) {
        if (!expectedRecord.equals(actualRecord)) {
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
            Map.Entry<String, Type> missingField = missingFields.iterator().next();
            errorManager.registerError(
                    ErrorType.ERROR_MISSING_RECORD_FIELDS,
                    missingField.getKey(),
                    expectedRecord
            );
            return false;
        }

        if (!extraFields.isEmpty()) {
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

    private Set<Map.Entry<String, Type>> zip(List<String> labels, List<Type> types) {
        Set<Map.Entry<String, Type>> entries = new HashSet<>();
        for (int i = 0; i < labels.size(); ++i) {
            String label = labels.get(i);
            Type type = types.get(i);
            entries.add(new AbstractMap.SimpleEntry<>(label, type));
        }
        return entries;
    }
}
