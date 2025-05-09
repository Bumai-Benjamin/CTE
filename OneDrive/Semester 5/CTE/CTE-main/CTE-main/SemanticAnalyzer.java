import java.util.*;

// Analyzes the semantic correctness of the program, focusing on variable declarations and usage
public class SemanticAnalyzer implements CompilerStage<List<LexicalAnalyzer.Token>, SemanticAnalyzer.SemanticResult> {
    // Track variables that have been declared
    private Set<String> declaredVariables = new HashSet<>();
    // Track variables that have been used
    private Set<String> usedVariables = new HashSet<>();

    // Contains the results of semantic analysis including errors and variable tracking
    public static class SemanticResult {
        public boolean isValid;
        public List<String> errors;
        public Set<String> declaredVariables;
        public Set<String> usedVariables;

        public SemanticResult() {
            this.isValid = true;
            this.errors = new ArrayList<>();
            this.declaredVariables = new HashSet<>();
            this.usedVariables = new HashSet<>();
        }
    }

    @Override
    public String getStageName() {
        return "Semantic Analysis";
    }

    @Override
    public SemanticResult process(List<LexicalAnalyzer.Token> tokens) {
        return analyze(tokens);
    }

    // Main analysis method to check semantic correctness
    public SemanticResult analyze(List<LexicalAnalyzer.Token> tokens) {
        SemanticResult result = new SemanticResult();
        
        if (tokens.isEmpty()) {
            result.isValid = false;
            result.errors.add("Empty statement");
            return result;
        }

        // Check for invalid symbols in tokens
        for (LexicalAnalyzer.Token token : tokens) {
            if (token.value.contains("%") || token.value.contains("&") || 
                token.value.contains("$") || token.value.contains("<") || 
                token.value.contains(">")) {
                result.isValid = false;
                result.errors.add(String.format("Semantic error at line %d: Invalid symbol '%s'", 
                    token.lineNumber, token.value));
            }
        }

        // Process variable declarations and usage based on statement type
        if (tokens.get(0).value.equals("INTEGER")) {
            handleIntegerDeclaration(tokens, result);
        }
        else if (tokens.get(0).value.equals("LET") || tokens.get(0).value.equals("INPUT")) {
            handleVariableUsage(tokens, result);
        }

        result.declaredVariables = new HashSet<>(declaredVariables);
        result.usedVariables = new HashSet<>(usedVariables);
        return result;
    }

    // Handle variable declarations in INTEGER statements
    private void handleIntegerDeclaration(List<LexicalAnalyzer.Token> tokens, SemanticResult result) {
        for (int i = 1; i < tokens.size(); i++) {
            LexicalAnalyzer.Token token = tokens.get(i);
            if (token.type.equals("IDENTIFIER")) {
                String varName = token.value;
                if (declaredVariables.contains(varName)) {
                    result.isValid = false;
                    result.errors.add(String.format("Semantic error at line %d: Variable '%s' already declared", 
                        token.lineNumber, varName));
                } else {
                    declaredVariables.add(varName);
                }
            }
        }
    }

    // Handle variable usage in LET and INPUT statements
    private void handleVariableUsage(List<LexicalAnalyzer.Token> tokens, SemanticResult result) {
        for (LexicalAnalyzer.Token token : tokens) {
            if (token.type.equals("IDENTIFIER")) {
                String varName = token.value;
                usedVariables.add(varName);
                if (!declaredVariables.contains(varName)) {
                    result.isValid = false;
                    result.errors.add(String.format("Semantic error at line %d: Variable '%s' used before declaration", 
                        token.lineNumber, varName));
                }
            }
        }
    }

    // Reset the analyzer's state
    public void reset() {
        declaredVariables.clear();
        usedVariables.clear();
    }
}