import java.util.*;

// Converts optimized code into binary machine code format
public class TargetMachineCode implements CompilerStage<List<String>, List<String>> {
    // Map of operation codes to their 4-bit binary representation
    private static final Map<String, String> opcodeMap = new HashMap<>();
    
    static {
        // Initialize opcode mappings for each instruction type
        opcodeMap.put("LOAD", "0001");
        opcodeMap.put("STORE", "0010");
        opcodeMap.put("ADD", "0011");
        opcodeMap.put("SUB", "0100");
        opcodeMap.put("MUL", "0101");
        opcodeMap.put("DIV", "0110");
        opcodeMap.put("JUMP", "0111");
        opcodeMap.put("LABEL", "1000");
    }

    @Override
    public String getStageName() {
        return "Target Machine Code Generation";
    }

    @Override
    public List<String> process(List<String> input) {
        return convertToBinary(input);
    }

    // Convert optimized code into 32-bit binary instructions
    public List<String> convertToBinary(List<String> optimizedCode) {
        List<String> binaryCode = new ArrayList<>();
        
        for (String instruction : optimizedCode) {
            String[] parts = instruction.split(" ");
            if (parts.length == 0) continue;

            // Get the binary opcode for the instruction
            String opcode = opcodeMap.getOrDefault(parts[0], "0000");
            StringBuilder binaryInstruction = new StringBuilder(opcode);

            // Convert operands to binary format
            for (int i = 1; i < parts.length; i++) {
                String operand = parts[i];
                if (operand.matches("\\d+")) {
                    // Convert numeric operands to 8-bit binary
                    binaryInstruction.append(String.format("%8s", 
                        Integer.toBinaryString(Integer.parseInt(operand)))
                        .replace(' ', '0'));
                } else if (operand.matches("[A-Za-z]")) {
                    // Convert register/variable names to 8-bit binary
                    binaryInstruction.append(String.format("%8s", 
                        Integer.toBinaryString(operand.charAt(0) - 'A'))
                        .replace(' ', '0'));
                }
            }

            // Ensure instruction is 32 bits by padding with zeros
            while (binaryInstruction.length() < 32) {
                binaryInstruction.append("0");
            }

            // Add spaces for readability (group into 4-bit segments)
            String formattedBinary = binaryInstruction.toString()
                .replaceAll("(.{4})", "$1 ").trim();

            binaryCode.add(formattedBinary);
        }

        return binaryCode;
    }

    // Helper method to convert decimal values to binary with specified bit width
    private String toBinary(int value, int bits) {
        return String.format("%" + bits + "s", Integer.toBinaryString(value))
            .replace(' ', '0');
    }
}