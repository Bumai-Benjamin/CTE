import java.util.*;

// Analyzes input text and breaks it down into tokens for the compiler
public class LexicalAnalyzer implements CompilerStage<String, LexicalAnalyzer.LexicalResult> {
    // Define valid keywords in the language
    private static final Set<String> keywords = new HashSet<>(Arrays.asList(
        "BEGIN", "INTEGER", "LET", "INPUT", "WRITE", "END"
    ));
    // Define valid mathematical operators
    private static final Set<Character> operators = new HashSet<>(Arrays.asList(
        '+', '-', '*', '/'
    ));
    // Define valid symbol characters
    private static final Set<Character> symbols = new HashSet<>(Arrays.asList(
        '=', ';'
    ));
    // Define invalid symbols that should trigger errors
    private static final Set<Character> invalidSymbols = new HashSet<>(Arrays.asList(
        '%', '$', '&', '<', '>', ',', '.', ':', '!', '@', '#', '^', '`', '~', '\\', '[', ']', '{', '}', '(', ')'
    ));

    // Represents a token with its value, type, and position information
    public static class Token {
        public String value;
        public String type;
        public int lineNumber;
        public int position;

        public Token(String value, String type, int lineNumber, int position) {
            this.value = value;
            this.type = type;
            this.lineNumber = lineNumber;
            this.position = position;
        }

        public String toString() {
            return String.format("[%s: %s] at line %d, position %d", type, value, lineNumber, position);
        }
    }

    // Holds the result of lexical analysis including tokens and any errors
    public static class LexicalResult {
        public List<Token> tokens = new ArrayList<>();
        public List<String> errors = new ArrayList<>();
        public boolean hasErrors() {
            return !errors.isEmpty();
        }
    }

    @Override
    public String getStageName() {
        return "Lexical Analysis";
    }

    @Override
    public LexicalResult process(String line) {
        return analyze(line, 1); // Default line number
    }

    // Main analysis method that converts input string into tokens
    public LexicalResult analyze(String line, int lineNumber) {
        LexicalResult result = new LexicalResult();
        int position = 0;

        // Split input by whitespace and special characters
        String[] parts = line.split("\\s+|(?=[=;+\\-*/])|(?<=[=;+\\-*/])");

        for (String part : parts) {
            if (part.isBlank()) {
                position += part.length();
                continue;
            }

            try {
                // Classify each part into appropriate token type
                if (keywords.contains(part)) {
                    result.tokens.add(new Token(part, "KEYWORD", lineNumber, position));
                } else if (part.length() == 1 && Character.isLetter(part.charAt(0))) {
                    result.tokens.add(new Token(part, "IDENTIFIER", lineNumber, position));
                } else if (part.matches("[a-z]+")) {
                    result.tokens.add(new Token(part, "IDENTIFIER", lineNumber, position));
                } else if (operators.contains(part.charAt(0)) && part.length() == 1) {
                    result.tokens.add(new Token(part, "OPERATOR", lineNumber, position));
                } else if (symbols.contains(part.charAt(0)) && part.length() == 1) {
                    result.tokens.add(new Token(part, "SYMBOL", lineNumber, position));
                } else if (invalidSymbols.contains(part.charAt(0)) || part.matches(".*[^a-zA-Z=;+\\-*/\\s].*")) {
                    result.errors.add(String.format("Lexical error at line %d, position %d: Invalid character '%s'", 
                        lineNumber, position, part));
                } else if (Character.isDigit(part.charAt(0))) {
                    result.errors.add(String.format("Lexical error at line %d, position %d: Digits not allowed '%s'", 
                        lineNumber, position, part));
                } else if (keywords.stream().noneMatch(k -> k.equals(part)) && part.equalsIgnoreCase("write")) {
                    result.errors.add(String.format("Lexical error at line %d, position %d: Misspelled keyword '%s'", 
                        lineNumber, position, part));
                } else {
                    result.tokens.add(new Token(part, "UNKNOWN", lineNumber, position));
                }
            } catch (Exception e) {
                result.errors.add(String.format("Unexpected error at line %d, position %d: %s", 
                    lineNumber, position, e.getMessage()));
            }
            position += part.length();
        }

        return result;
    }
}