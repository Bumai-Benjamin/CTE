import java.util.*;

public class SyntaxAnalyzer implements CompilerStage<List<LexicalAnalyzer.Token>, SyntaxAnalyzer.SyntaxResult> {
    private static final Set<String> validStatementTypes = new HashSet<>(Arrays.asList(
        "BEGIN", "INTEGER", "INPUT", "LET", "WRITE", "END"
    ));

    public static class SyntaxResult {
        public boolean isValid;
        public List<String> errors;
        public String statementType;

        public SyntaxResult() {
            this.isValid = true;
            this.errors = new ArrayList<>();
            this.statementType = "";
        }
    }

    @Override
    public String getStageName() {
        return "Syntax Analysis";
    }

    @Override
    public SyntaxResult process(List<LexicalAnalyzer.Token> tokens) {
        return analyze(tokens);
    }

    public SyntaxResult analyze(List<LexicalAnalyzer.Token> tokens) {
        SyntaxResult result = new SyntaxResult();
        
        if (tokens.isEmpty()) {
            result.isValid = false;
            result.errors.add("Empty statement");
            return result;
        }

        // Check for statement type
        LexicalAnalyzer.Token firstToken = tokens.get(0);
        if (firstToken.type.equals("KEYWORD")) {
            result.statementType = firstToken.value;
            if (!validStatementTypes.contains(firstToken.value)) {
                result.isValid = false;
                result.errors.add(String.format("Invalid statement type '%s' at line %d", 
                    firstToken.value, firstToken.lineNumber));
            }
        }

        // Check for combined operators
        for (int i = 0; i < tokens.size() - 1; i++) {
            LexicalAnalyzer.Token current = tokens.get(i);
            LexicalAnalyzer.Token next = tokens.get(i + 1);
            
            if (current.type.equals("OPERATOR") && next.type.equals("OPERATOR")) {
                result.isValid = false;
                result.errors.add(String.format("Syntax error at line %d: Combined operators '%s%s' not allowed", 
                    current.lineNumber, current.value, next.value));
            }
        }

        // Check for semicolon at end
        if (!tokens.isEmpty()) {
            LexicalAnalyzer.Token last = tokens.get(tokens.size() - 1);
            if (last.value.equals(";")) {
                result.isValid = false;
                result.errors.add(String.format("Syntax error at line %d: Semicolon at end of line not allowed", 
                    last.lineNumber));
            }
        }

        // Validate expression structure
        if (result.statementType.equals("LET")) {
            validateLetStatement(tokens, result);
        }

        return result;
    }

    private void validateLetStatement(List<LexicalAnalyzer.Token> tokens, SyntaxResult result) {
        // Check for proper LET statement structure: LET identifier = expression
        if (tokens.size() < 4) {
            result.isValid = false;
            result.errors.add(String.format("Syntax error at line %d: Invalid LET statement structure", 
                tokens.get(0).lineNumber));
            return;
        }

        if (!tokens.get(1).type.equals("IDENTIFIER")) {
            result.isValid = false;
            result.errors.add(String.format("Syntax error at line %d: Expected identifier after LET", 
                tokens.get(1).lineNumber));
        }

        if (!tokens.get(2).value.equals("=")) {
            result.isValid = false;
            result.errors.add(String.format("Syntax error at line %d: Expected '=' after identifier", 
                tokens.get(2).lineNumber));
        }

        // Validate expression structure
        validateExpression(tokens.subList(3, tokens.size()), result);
    }

    private void validateExpression(List<LexicalAnalyzer.Token> tokens, SyntaxResult result) {
        if (tokens.isEmpty()) {
            result.isValid = false;
            result.errors.add("Empty expression");
            return;
        }

        // Check for proper expression structure
        for (int i = 0; i < tokens.size(); i++) {
            LexicalAnalyzer.Token token = tokens.get(i);
            
            if (i % 2 == 0) {
                // Even positions should be identifiers or operators
                if (!token.type.equals("IDENTIFIER") && !token.type.equals("OPERATOR")) {
                    result.isValid = false;
                    result.errors.add(String.format("Syntax error at line %d: Expected identifier or operator, got '%s'", 
                        token.lineNumber, token.value));
                }
            } else {
                // Odd positions should be operators
                if (!token.type.equals("OPERATOR")) {
                    result.isValid = false;
                    result.errors.add(String.format("Syntax error at line %d: Expected operator, got '%s'", 
                        token.lineNumber, token.value));
                }
            }
        }
    }
}