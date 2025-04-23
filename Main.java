import java.util.*;

public class Main {
    private static final Set<Integer> fullProcessLines = new HashSet<>(Arrays.asList(5, 7, 8));

    public static void main(String[] args) {
        String[] program = {
            "BEGIN",
            "INTEGER A, B, C, E, M, N, G, H, I, a, c",
            "INPUT A, B, C",
            "LET B = A */ M",
            "LET G = a + c",
            "temp = <s%**h - j / w +d +*$&;",
            "M = A/B+C",
            "N = G/H-I+a*B/c",
            "WRITE M",
            "WRITEE F;",
            "END"
        };

        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer();
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        IntermediateCodeGenerator icg = new IntermediateCodeGenerator();
        CodeGenerator cg = new CodeGenerator();
        CodeOptimizer optimizer = new CodeOptimizer();
        TargetMachineCode tmc = new TargetMachineCode();

        for (int i = 0; i < program.length; i++) {
            System.out.println("\nLine " + (i + 1) + ": " + program[i]);
            LexicalAnalyzer.LexicalResult lexResult = LexicalAnalyzer.analyze(program[i]);
 
            if (!lexResult.errors.isEmpty()) {
                ErrorReporter.report(lexResult.errors);
                continue;
            }

            if (!syntaxAnalyzer.isValidSyntax(lexResult.tokens)) {
                System.out.println("ERROR: Syntax error");
                continue;
            }

            if (!semanticAnalyzer.isSemanticallyCorrect(lexResult.tokens)) {
                System.out.println("ERROR: Semantic error");
                continue;
            }

            if (fullProcessLines.contains(i + 1)) {
                List<String> icr = icg.generate(lexResult.tokens);
                List<String> cgCode = cg.generate(icr);
                List<String> optimized = optimizer.optimize(cgCode);
                List<String> binary = tmc.convertToBinary(optimized);

                for (String code : binary) {
                    System.out.println(code);
                }
            } else {
                System.out.println("Line valid, no further processing required.");
            }
        }
    }
}
