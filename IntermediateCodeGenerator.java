import java.util.*;

public class IntermediateCodeGenerator {
    public List<String> generate(List<LexicalAnalyzer.Token> tokens) {
        List<String> code = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (LexicalAnalyzer.Token token : tokens) {
            sb.append(token.value).append(" ");
        }
        code.add("(ICR) " + sb.toString().trim());
        return code;
    }
}
