import java.util.*;

// Main class that handles the compiler's seven stages
public class Main {
    // Lines that will be processed through all stages when option 2 is selected
    private static final Set<Integer> fullProcessLines = new HashSet<>(Arrays.asList(5, 7, 8));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Display menu options to user
        System.out.println("Choose an option:");
        System.out.println("1. Process all lines through all 7 stages.");
        System.out.println("2. Check for errors in all lines, but process lines 5, 7, and 8 through all 7 stages.");
        int choice = scanner.nextInt();
        // Validate user input
        while (choice != 1 && choice != 2) {
            System.out.println("Invalid choice. Please enter 1 or 2:");
            choice = scanner.nextInt();
        }
        scanner.nextLine(); // Consume newline

        // Sample program to be processed
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

        // Initialize all compiler stages
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer();
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        IntermediateCodeGenerator icg = new IntermediateCodeGenerator();
        CodeGenerator cg = new CodeGenerator();
        CodeOptimizer optimizer = new CodeOptimizer();
        TargetMachineCode tmc = new TargetMachineCode();

        // Process each line of the program
        for (int i = 0; i < program.length; i++) {
            System.out.println("\nProcessing Line " + (i + 1) + ": " + program[i]);
            
            // Stage 1: Perform lexical analysis
            LexicalAnalyzer.LexicalResult lexResult = lexicalAnalyzer.analyze(program[i], i + 1);
            if (lexResult.hasErrors()) {
                ErrorReporter.report(lexResult.errors);
                continue;
            }

            // Stage 2: Perform syntax analysis
            SyntaxAnalyzer.SyntaxResult syntaxResult = syntaxAnalyzer.analyze(lexResult.tokens);
            if (!syntaxResult.isValid) {
                ErrorReporter.report(syntaxResult.errors);
                continue;
            }

            // Stage 3: Perform semantic analysis
            SemanticAnalyzer.SemanticResult semanticResult = semanticAnalyzer.analyze(lexResult.tokens);
            if (!semanticResult.isValid) {
                ErrorReporter.report(semanticResult.errors);
                continue;
            }

            // Process remaining stages based on user choice
            if (choice == 1 || (choice == 2 && fullProcessLines.contains(i + 1))) {
                processLineThroughRemainingStages(lexResult.tokens, icg, cg, optimizer, tmc);
            } else {
                System.out.println("Line valid, no further processing required.");
            }
        }
    }

    // Process stages 4-7: ICR, Code Generation, Optimization, and Target Machine Code
    private static void processLineThroughRemainingStages(
        List<LexicalAnalyzer.Token> tokens,
        IntermediateCodeGenerator icg,
        CodeGenerator cg,
        CodeOptimizer optimizer,
        TargetMachineCode tmc
    ) {
        try {
            // Stage 4: Generate intermediate code
            List<String> icr = icg.generate(tokens);
            System.out.println("\n====== STAGE 4: INTERMEDIATE CODE REPRESENTATION (ICR) ======");
            icr.forEach(System.out::println);

            // Stage 5: Generate target code
            List<String> cgCode = cg.generate(icr);
            System.out.println("\n====== STAGE 5: CODE GENERATION (CG) ======");
            cgCode.forEach(System.out::println);

            // Stage 6: Optimize the generated code
            List<String> optimized = optimizer.optimize(cgCode);
            System.out.println("\n====== STAGE 6: CODE OPTIMIZATION (CO) ======");
            optimized.forEach(System.out::println);

            // Stage 7: Convert to machine code
            List<String> binary = tmc.convertToBinary(optimized);
            System.out.println("\n====== STAGE 7: TARGET MACHINE CODE (TMC) ======");
            binary.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error during code generation: " + e.getMessage());
        }
    }
}