import java.util.*;

// Generates assembly-like code from intermediate code representation
public class CodeGenerator {
    // Convert intermediate code representation to assembly-like code
    public List<String> generate(List<String> icr) {
        List<String> output = new ArrayList<>();
        for (String line : icr) {
            output.add("(CG) ASM_LINE: " + line);
        }
        return output;
    }
}