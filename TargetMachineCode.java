import java.util.*;

public class TargetMachineCode {
    public List<String> convertToBinary(List<String> code) {
        List<String> binary = new ArrayList<>();
        for (String line : code) {
            StringBuilder sb = new StringBuilder();
            for (char ch : line.toCharArray()) {
                sb.append(String.format("%8s", Integer.toBinaryString(ch)).replace(' ', '0')).append(" ");
            }
            binary.add("(TMC) " + sb.toString());
        }
        return binary;
    }
}