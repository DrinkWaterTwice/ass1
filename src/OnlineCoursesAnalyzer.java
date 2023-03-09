import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class OnlineCoursesAnalyzer {

    final int SIZE = 23;

    HashSet<String[]> hashSet = new HashSet<>();

    public OnlineCoursesAnalyzer(String datasetPath) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(datasetPath));
            String row;
            reader.readLine();
            while ((row = reader.readLine()) != null) {
                int index = 0;
                int indexOfStrings = 0;
                StringBuilder sb = new StringBuilder();
                String[] midStrings = new String[SIZE];
                while (index < row.length()) {
                    char now = row.charAt(index++);
                    if (now != ',' && now != '"') {
                        sb.append(now);
                        continue;
                    }
                    if (now == '"') {
                        sb.append(now);
                        while (index < row.length()) {
                            now = row.charAt(index++);
                            if (now != '"') {
                                sb.append(now);
                            } else {
                                break;
                            }
                        }
                        sb.append(now);
                        midStrings[indexOfStrings++] = sb.toString();
                        sb = new StringBuilder();
                        index++;
                    }
                    if (now == ',') {
                        midStrings[indexOfStrings++] = sb.toString();
                        sb = new StringBuilder();
                    }
                }
                midStrings[indexOfStrings] = sb.toString();
                hashSet.add(midStrings);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        OnlineCoursesAnalyzer on = new OnlineCoursesAnalyzer(
            "E:\\yysScript\\assigment1\\src\\local.csv");
        on.hashSet.forEach(strings -> System.out.println(Arrays.toString(strings)));
    }
}
