import java.util.*;

public class ErrorReporter {
    public static void report(List<String> errors) {
        for (String error : errors) {
            System.out.println("ERROR: " + error);
        }
    }
}