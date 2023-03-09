import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class OnlineCoursesAnalyzer {

    final int SIZE = 23;

    ArrayList<String[]> array = new ArrayList<>();

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
                        while (index < row.length()) {
                            now = row.charAt(index++);
                            if (now != '"') {
                                sb.append(now);
                            } else {
                                break;
                            }
                        }
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
                array.add(midStrings);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        OnlineCoursesAnalyzer on = new OnlineCoursesAnalyzer(
            "E:\\yysScript\\assigment1\\src\\local.csv");
        Map<String,Integer> map = on.getPtcpCountByInst();
        map.forEach((k,t) -> System.out.println(k + "=" + t));
        map = on.getPtcpCountByInstAndSubject();
        map.forEach((k,t) -> System.out.println(k + "=" + t));

    }

    public Map<String, Integer> getPtcpCountByInst() {
        Map<String, Integer> map;
        map = array.stream().collect(
            Collectors.groupingBy(t -> t[0], Collectors.summingInt(t -> Integer.parseInt(t[8]))));
        return map;
    }

    public Map<String, Integer> getPtcpCountByInstAndSubject(){
        Map<String,Integer> map;
        map = array.stream().collect(Collectors.groupingBy(t -> (t[0] +"-"+ t[5]),Collectors.summingInt(t -> Integer.parseInt(t[8]))));
        return map;
    }



}
