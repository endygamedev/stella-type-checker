package dev.ebronnikov.typechecker;

import dev.ebronnikov.antlr.stellaLexer;
import dev.ebronnikov.antlr.stellaParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;


public class Main {
    public static void main(String[] args) {
        final String example = """
            language core;
        
            // addition of natural numbers
            fn Nat::add(n : Nat) -> fn(Nat) -> Nat {
              return fn(m : Nat) {
                return Nat::rec(n, m, fn(i : Nat) {
                  return fn(r : Nat) {
                    return succ( r ); // r := r + 1
                  };
                });
              };
            }

            // square, computed as a sum of odd numbers
            fn square(n : Nat) -> Nat {
              return Nat::rec(n, 0, fn(i : Nat) {
                  return fn(r : Nat) {
                    // r := r + (2*i + 1)
                    return Nat::add(i)( Nat::add(i)( succ( r )));
                  };
              });
            }

            fn main(n : Nat) -> Nat {
              return square(n);
            }
        """.stripIndent();

        stellaLexer lexer = new stellaLexer(CharStreams.fromString(example));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        stellaParser parser = new stellaParser(tokens);
        stellaParser.ExprContext parseTree = parser.expr();

        System.out.println(parseTree.toStringTree());
    }
}
