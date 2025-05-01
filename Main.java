import java.util.*;

public class Main {
    private static final Set<Integer> fullProcessLines = new HashSet<>(Arrays.asList(5, 7, 8));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an option:");
        System.out.println("1. Process all lines through all 7 stages.");
        System.out.println("2. Check for errors in all lines, but process lines 5, 7, and 8 through all 7 stages.");
        int choice = scanner.nextInt();
        while (choice != 1 && choice != 2) {
            System.out.println("Invalid choice. Please enter 1 or 2:");
            choice = scanner.nextInt();
        }
        scanner.nextLine(); // Consume newline

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
            System.out.println("\nInput Line " + (i + 1) + ": " + program[i]);
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

            if (choice == 1 || (choice == 2 && fullProcessLines.contains(i + 1))) {
                processLineThroughAllStages(lexResult.tokens, icg, cg, optimizer, tmc);
            } else {
                System.out.println("Line valid, no further processing required.");
            }
        }
    }

    private static void processLineThroughAllStages(
        List<LexicalAnalyzer.Token> tokens,
        IntermediateCodeGenerator icg,
        CodeGenerator cg,
        CodeOptimizer optimizer,
        TargetMachineCode tmc
    ) {
        // Stage 4: Intermediate Code Representation (ICR)
        List<String> icr = icg.generate(tokens);
        System.out.println("====== STAGE4: INTERMEDIATE CODE REPRESENTATION (ICR)");
        icr.forEach(System.out::println);

        // Stage 5: Code Generation (CG)
        List<String> cgCode = cg.generate(icr);
        System.out.println("====== STAGE5: CODE GENERATION (CG)");
        cgCode.forEach(System.out::println);

        // Stage 6: Code Optimization (CO)
        List<String> optimized = optimizer.optimize(cgCode);
        System.out.println("====== STAGE6: CODE OPTIMIZATION (CO)");
        optimized.forEach(System.out::println);

        // Stage 7: Target Machine Code (TMC)
        List<String> binary = tmc.convertToBinary(optimized);
        System.out.println("====== STAGE7: TARGET MACHINE CODE (TMC)");
        binary.forEach(System.out::println);
    }
}