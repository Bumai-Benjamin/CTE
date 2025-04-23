import java.util.*;

public class LexicalAnalyzer {
    private static final Set<String> keywords = new HashSet<>(Arrays.asList(
        "BEGIN", "INTEGER", "LET", "INPUT", "WRITE", "END"
    ));
    private static final Set<Character> operators = new HashSet<>(Arrays.asList(
        '+', '-', '*', '/'
    ));
    private static final Set<Character> symbols = new HashSet<>(Arrays.asList(
        '=', ';'
    ));
    private static final Set<Character> invalidSymbols = new HashSet<>(Arrays.asList(
        '%', '$', '&', '<', '>', ',', '.', ':', '!', '@', '#', '^', '`', '~', '\\', '[', ']', '{', '}', '(', ')'
    ));

    public static class Token {
        public String value;
        public String type;

        public Token(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public String toString() {
            return String.format("[%s: %s]", type, value);
        }
    }

    public static class LexicalResult {
        public List<Token> tokens = new ArrayList<>();
        public List<String> errors = new ArrayList<>();
    }

    public static LexicalResult analyze(String line) {
        LexicalResult result = new LexicalResult();

        String[] parts = line.split("\\s+|(?=[=;+\\-*/])|(?<=[=;+\\-*/])");

        for (String part : parts) {
            if (part.isBlank()) continue;

            if (keywords.contains(part)) {
                result.tokens.add(new Token(part, "KEYWORD"));
            } else if (part.length() == 1 && Character.isLetter(part.charAt(0))) {
                result.tokens.add(new Token(part, "IDENTIFIER"));
            } else if (part.matches("[a-z]+")) {
                result.tokens.add(new Token(part, "IDENTIFIER"));
            } else if (operators.contains(part.charAt(0)) && part.length() == 1) {
                result.tokens.add(new Token(part, "OPERATOR"));
            } else if (symbols.contains(part.charAt(0)) && part.length() == 1) {
                result.tokens.add(new Token(part, "SYMBOL"));
            } else if (invalidSymbols.contains(part.charAt(0)) || part.matches(".*[^a-zA-Z=;+\\-*/\\s].*")) {
                result.errors.add("Lexical error: Invalid character '" + part + "'");
            } else if (Character.isDigit(part.charAt(0))) {
                result.errors.add("Lexical error: Digits not allowed '" + part + "'");
            } else if (keywords.stream().noneMatch(k -> k.equals(part)) && part.equalsIgnoreCase("write")) {
                result.errors.add("Lexical error: Misspelled keyword '" + part + "'");
            } else {
                result.tokens.add(new Token(part, "UNKNOWN"));
            }
        }

        return result;
    }
}