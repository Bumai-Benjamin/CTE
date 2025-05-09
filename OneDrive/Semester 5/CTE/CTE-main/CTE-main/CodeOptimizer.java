import java.util.*;

// Optimizes the generated code by removing redundancies and combining operations
public class CodeOptimizer implements CompilerStage<List<String>, List<String>> {
    @Override
    public String getStageName() {
        return "Code Optimization";
    }

    @Override
    public List<String> process(List<String> input) {
        return optimize(input);
    }

    // Main optimization method that applies various optimization techniques
    public List<String> optimize(List<String> code) {
        List<String> optimized = new ArrayList<>();
        
        // Process each instruction for optimization
        for (int i = 0; i < code.size(); i++) {
            String instruction = code.get(i);
            
            // Skip if it's a label or control flow instruction
            if (instruction.startsWith("LABEL") || instruction.startsWith("JUMP")) {
                optimized.add(instruction);
                continue;
            }

            // Remove redundant load-store pairs
            if (instruction.startsWith("LOAD") && i + 1 < code.size()) {
                String nextInstruction = code.get(i + 1);
                if (nextInstruction.startsWith("STORE") && 
                    instruction.split(" ")[1].equals(nextInstruction.split(" ")[1])) {
                    // Skip redundant load-store pair
                    i++;
                    continue;
                }
            }

            // Optimize arithmetic operations by combining them when possible
            if (instruction.startsWith("ADD") || instruction.startsWith("SUB") || 
                instruction.startsWith("MUL") || instruction.startsWith("DIV")) {
                if (i + 1 < code.size()) {
                    String nextInstruction = code.get(i + 1);
                    if (isArithmeticOperation(nextInstruction)) {
                        // Combine operations if possible
                        String combined = combineOperations(instruction, nextInstruction);
                        if (combined != null) {
                            optimized.add(combined);
                            i++;
                            continue;
                        }
                    }
                }
            }

            // Remove dead code (store followed by immediate load)
            if (instruction.startsWith("STORE") && i + 1 < code.size()) {
                String nextInstruction = code.get(i + 1);
                if (nextInstruction.startsWith("LOAD") && 
                    instruction.split(" ")[1].equals(nextInstruction.split(" ")[1])) {
                    // Skip store-load pair if the value isn't used
                    i++;
                    continue;
                }
            }

            optimized.add(instruction);
        }

        return optimized;
    }

    // Check if an instruction is an arithmetic operation
    private boolean isArithmeticOperation(String instruction) {
        return instruction.startsWith("ADD") || instruction.startsWith("SUB") || 
               instruction.startsWith("MUL") || instruction.startsWith("DIV");
    }

    // Attempt to combine two arithmetic operations into a single optimized operation
    private String combineOperations(String op1, String op2) {
        String[] parts1 = op1.split(" ");
        String[] parts2 = op2.split(" ");
        
        if (parts1.length != 3 || parts2.length != 3) {
            return null;
        }

        // Combine operations if they operate on the same registers
        if (parts1[1].equals(parts2[1]) && parts1[2].equals(parts2[2])) {
            if (parts1[0].equals("ADD") && parts2[0].equals("ADD")) {
                return "MUL " + parts1[1] + " " + parts1[2] + " 2"; // 2x = x + x
            }
            if (parts1[0].equals("MUL") && parts2[0].equals("MUL")) {
                return "MUL " + parts1[1] + " " + parts1[2] + " 4"; // 4x = 2x * 2x
            }
        }

        return null;
    }
}
