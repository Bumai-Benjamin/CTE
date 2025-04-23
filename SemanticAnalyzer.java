import java.util.*;

public class SemanticAnalyzer {
    public boolean isSemanticallyCorrect(List<LexicalAnalyzer.Token> tokens) {
        for (LexicalAnalyzer.Token token : tokens) {
            if (token.value.contains("%") || token.value.contains("&") || token.value.contains("$")) {
                return false;
            }
        }
        return true;
    }
}