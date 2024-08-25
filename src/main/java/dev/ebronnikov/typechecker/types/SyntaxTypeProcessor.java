package dev.ebronnikov.typechecker.types;

import dev.ebronnikov.antlr.stellaParser.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class SyntaxTypeProcessor {
    public static Type getType(StellatypeContext ctx) {
        return switch (ctx) {
            case TypeBoolContext ignored -> BoolType.INSTANCE;
            case TypeUnitContext ignored -> UnitType.INSTANCE;
            case TypeNatContext ignored -> NatType.INSTANCE;
            case TypeTupleContext typeTupleContext -> visitTupleType(typeTupleContext);
            case TypeRecordContext typeRecordContext -> visitRecordType(typeRecordContext);
            case TypeFunContext typeFunContext -> visitFunctionalType(typeFunContext);
            case TypeParensContext typeParensContext -> getType(typeParensContext.type_);
            case TypeSumContext typeSumContext -> visitSumTypeType(typeSumContext);
            case TypeVariantContext typeVariantContext -> visitVariantType(typeVariantContext);
            case TypeListContext typeListContext -> visitListType(typeListContext);
            default -> UnknownType.INSTANCE;
        };
    }

    private static TupleType visitTupleType(TypeTupleContext ctx) {
        ArrayList<Type> types = (ArrayList<Type>) ctx.types.stream()
                .map(SyntaxTypeProcessor::getType)
                .collect(Collectors.toList());
        return new TupleType(types);
    }

    private static RecordType visitRecordType(TypeRecordContext ctx) {
        ArrayList<String> labels = (ArrayList<String>) ctx.fieldTypes.stream()
                .map(field -> field.label.getText())
                .toList();
        ArrayList<Type> types = (ArrayList<Type>) ctx.fieldTypes.stream()
                .map(field -> getType(field.type_))
                .toList();
        return new RecordType(labels, types);
    }

    private static FunctionalType visitFunctionalType(TypeFunContext ctx) {
        Type paramType = getType(ctx.paramTypes.getFirst());
        Type returnType = getType(ctx.returnType);
        return new FunctionalType(paramType, returnType);
    }

    private static SumType visitSumTypeType(TypeSumContext ctx) {
        Type left = getType(ctx.left);
        Type right = getType(ctx.right);
        return new SumType(left, right);
    }

    private static Type visitVariantType(TypeVariantContext ctx) {
        ArrayList<String> labels = (ArrayList<String>) ctx.fieldTypes.stream()
                .map(field -> field.label.getText())
                .toList();
        ArrayList<Type> types = (ArrayList<Type>) ctx.fieldTypes.stream()
                .map(field -> getType(field.type_))
                .toList();
        return new VariantType(labels, types);
    }

    private static Type visitListType(TypeListContext ctx) {
        Type type = getType(ctx.type_);
        return new ListType(type);
    }
}
