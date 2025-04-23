import java.util.*;

public class CodeGenerator {
    public List<String> generate(List<String> icr) {
        List<String> output = new ArrayList<>();
        for (String line : icr) {
            output.add("(CG) ASM_LINE: " + line);
        }
        return output;
    }
}