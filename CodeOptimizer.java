import java.util.*;

public class CodeOptimizer {
    public List<String> optimize(List<String> code) {
        List<String> optimized = new ArrayList<>();
        for (String line : code) {
            optimized.add(line.replace(" + 0", "").replace(" * 1", ""));
        }
        return optimized;
    }
}
