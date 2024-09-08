package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.typechecker.types.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class UnifySolver {
    private final List<Constraint> storedConstraints = new ArrayList<>();

    public void addConstraint(Type left, Type right, ParserRuleContext ruleContext) {
        storedConstraints.add(new Constraint(left, right, ruleContext));
    }

    public UnificationResult solve() {
        try {
            solveInternal(storedConstraints);
            return UnificationOk.INSTANCE;
        } catch (UnificationFailedException e) {
            return new UnificationFailed(e.getExpectedType(), e.getActualType(), e.getExpression());
        } catch (InfiniteTypeException e) {
            return new UnificationFailedInfiniteType(e.getExpectedType(), e.getActualType(), e.getExpression());
        }
    }

    private void solveInternal(List<Constraint> constraints) throws UnificationFailedException, InfiniteTypeException {
        if (constraints.isEmpty()) {
            return;
        }

        Constraint constraint = constraints.get(0);
        List<Constraint> remainingConstraints = constraints.subList(1, constraints.size());

        Type left = constraint.getLeft();
        Type right = constraint.getRight();
        ParserRuleContext ruleContext = constraint.getRuleContext();

        if (left instanceof TypeVar leftVar && right instanceof TypeVar rightVar && leftVar.equals(rightVar)) {
            solveInternal(remainingConstraints);
        } else if (left instanceof TypeVar leftVar && !leftVar.containsIn(right, ruleContext)) {
            solveInternal(replace(remainingConstraints, leftVar, right));
        } else if (right instanceof TypeVar rightVar && !rightVar.containsIn(left, ruleContext)) {
            solveInternal(replace(remainingConstraints, rightVar, left));
        } else if (left instanceof FunctionalType leftFunc && right instanceof FunctionalType rightFunc) {
            List<Constraint> newConstraints = List.of(
                    new Constraint(leftFunc.getFrom(), rightFunc.getFrom(), ruleContext),
                    new Constraint(leftFunc.getTo(), rightFunc.getTo(), ruleContext)
            );
            solveInternal(merge(remainingConstraints, newConstraints));
        } else if (left instanceof SumType leftSum && right instanceof SumType rightSum) {
            List<Constraint> newConstraints = List.of(
                    new Constraint(leftSum.getLeft(), rightSum.getLeft(), ruleContext),
                    new Constraint(leftSum.getRight(), rightSum.getRight(), ruleContext)
            );
            solveInternal(merge(remainingConstraints, newConstraints));
        } else if (left instanceof ListType leftList && right instanceof ListType rightList) {
            List<Constraint> newConstraints = List.of(
                    new Constraint(leftList.getType(), rightList.getType(), ruleContext)
            );
            solveInternal(merge(remainingConstraints, newConstraints));
        } else if (left instanceof TupleType leftTuple && right instanceof TupleType rightTuple) {
            List<Constraint> newConstraints = new ArrayList<>();
            for (int i = 0; i < leftTuple.getTypes().size(); i++) {
                newConstraints.add(new Constraint(leftTuple.getTypes().get(i), rightTuple.getTypes().get(i), ruleContext));
            }
            solveInternal(merge(remainingConstraints, newConstraints));
        } else if (!(left instanceof TypeVar) && !(right instanceof TypeVar) && left.equals(right)) {
            solveInternal(remainingConstraints);
        } else {
            throw new UnificationFailedException(left, right, ruleContext);
        }
    }

    private List<Constraint> replace(List<Constraint> constraints, TypeVar what, Type to) {
        List<Constraint> result = new ArrayList<>();
        for (Constraint constraint : constraints) {
            result.add(constraint.replace(what, to));
        }
        return result;
    }

    private List<Constraint> merge(List<Constraint> constraints1, List<Constraint> constraints2) {
        List<Constraint> result = new ArrayList<>(constraints1);
        result.addAll(constraints2);
        return result;
    }

    private static class UnificationFailedException extends Exception {
        private final Type expectedType;
        private final Type actualType;
        private final ParserRuleContext expression;

        public UnificationFailedException(Type expectedType, Type actualType, ParserRuleContext expression) {
            this.expectedType = expectedType;
            this.actualType = actualType;
            this.expression = expression;
        }

        public Type getExpectedType() {
            return expectedType;
        }

        public Type getActualType() {
            return actualType;
        }

        public ParserRuleContext getExpression() {
            return expression;
        }
    }

    private static class InfiniteTypeException extends Exception {
        private final Type expectedType;
        private final Type actualType;
        private final ParserRuleContext expression;

        public InfiniteTypeException(Type expectedType, Type actualType, ParserRuleContext expression) {
            this.expectedType = expectedType;
            this.actualType = actualType;
            this.expression = expression;
        }

        public Type getExpectedType() {
            return expectedType;
        }

        public Type getActualType() {
            return actualType;
        }

        public ParserRuleContext getExpression() {
            return expression;
        }
    }
}
