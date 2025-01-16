import org.json.JSONObject;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShamirsSecretSharing {

    public static void main(String[] args) {
        try {
            // Load and parse JSON input files
            JSONObject testCase1 = new JSONObject(new FileReader("testcase1.json"));
            JSONObject testCase2 = new JSONObject(new FileReader("testcase2.json"));

            // Solve for both test cases
            System.out.println("Secret for Test Case 1: " + solveSecret(testCase1));
            System.out.println("Secret for Test Case 2: " + solveSecret(testCase2));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int solveSecret(JSONObject testCase) {
        try {
            JSONObject keys = testCase.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            // Store the points (x, y) after decoding
            Map<Integer, Integer> points = new HashMap<>();

            // Decode the points from JSON
            Set<String> keysSet = testCase.keySet();
            for (String key : keysSet) {
                if (!key.equals("keys")) {
                    JSONObject point = testCase.getJSONObject(key);
                    int x = Integer.parseInt(key);
                    int base = point.getInt("base");
                    String value = point.getString("value");
                    int y = decodeValue(value, base);
                    points.put(x, y);
                }
            }

            // Use Lagrange interpolation to calculate the constant term (c)
            return findConstantTerm(points, k);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static int decodeValue(String value, int base) {
        return Integer.parseUnsignedInt(value, base);
    }

    private static int findConstantTerm(Map<Integer, Integer> points, int k) {
        double c = 0;

        // Perform Lagrange interpolation
        for (Map.Entry<Integer, Integer> entry : points.entrySet()) {
            int xi = entry.getKey();
            int yi = entry.getValue();
            double li = 1;

            for (Map.Entry<Integer, Integer> otherEntry : points.entrySet()) {
                int xj = otherEntry.getKey();
                if (xi != xj) {
                    li *= (0.0 - xj) / (xi - xj);
                }
            }

            c += li * yi;
        }

        // Return the constant term rounded to the nearest integer
        return (int) Math.round(c);
    }
}

