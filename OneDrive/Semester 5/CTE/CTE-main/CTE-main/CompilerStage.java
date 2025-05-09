// Generic interface for all compiler stages with input type T and result type R
public interface CompilerStage<T, R> {
    // Process the input and return the result for this compiler stage
    R process(T input);
    // Get the name of this compiler stage
    String getStageName();
} 