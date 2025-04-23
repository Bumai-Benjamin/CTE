import java.util.*;

public class SyntaxAnalyzer {
    public boolean isValidSyntax(List<LexicalAnalyzer.Token> tokens) {
        String prevType = "";
        for (int i = 0; i < tokens.size(); i++) {
            LexicalAnalyzer.Token token = tokens.get(i);
            String type = token.type;

            if (type.equals("OPERATOR") && prevType.equals("OPERATOR")) {
                return false;
            }
            prevType = type;
        }

        if (!tokens.isEmpty()) {
            LexicalAnalyzer.Token last = tokens.get(tokens.size() - 1);
            if (last.value.equals(";")) {
                return false;
            }
        }
        return true;
    }
}