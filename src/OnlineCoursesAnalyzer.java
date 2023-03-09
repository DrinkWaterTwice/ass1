import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
                //add last
                midStrings[indexOfStrings] = sb.toString();
                array.add(midStrings);
            }
            array.forEach(t -> {
                t[4] = t[4].replace(", ", ",");
                t[5] = t[5].replace(", ", ",");
                t[5] = t[5].replace("and", "");
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        OnlineCoursesAnalyzer on = new OnlineCoursesAnalyzer(
            "E:\\yysScript\\assigment1\\src\\local.csv");
        Map<String, Integer> map = on.getPtcpCountByInst();
        map.forEach((k, t) -> System.out.println(k + "=" + t));
        map = on.getPtcpCountByInstAndSubject();
        map.forEach((k, t) -> System.out.println(k + "=" + t));

        Map<String, List<List<String>>> map1;
        map1 = on.getCourseListOfInstructor();
        map1.forEach(
            (k, v) -> v.forEach(t -> System.out.println(k + "=" + Arrays.toString(t.toArray()))));
        List<String> list = on.searchCourses("Science",0,100);
        System.out.println(Arrays.toString(list.toArray()));
    }

    public Map<String, Integer> getPtcpCountByInst() {
        Map<String, Integer> map;
        map = array.stream().collect(
            Collectors.groupingBy(t -> t[0], Collectors.summingInt(t -> Integer.parseInt(t[8]))));
        return map;
    }

    public Map<String, Integer> getPtcpCountByInstAndSubject() {
        Map<String, Integer> map;
        map = array.stream().collect(Collectors.groupingBy(t -> (t[0] + "-" + t[5]),
            Collectors.summingInt(t -> Integer.parseInt(t[8]))));
        return map;
    }

    public Map<String, List<List<String>>> getCourseListOfInstructor() {
        HashSet<String> ins = new HashSet<>();
        Map<String, List<List<String>>> ans = new HashMap<>();
        array.forEach(t ->
            ins.addAll(Arrays.asList(t[4].split(",")))
        );

        ins.forEach(t -> ans.put(t, getList()));
        array.forEach(t -> {
            String[] inst = t[4].split(",");
            int length = inst.length;
            if (length == 1) {
                ans.get(inst[0]).get(0).add(t[3]);
            }
            if (length >= 2) {
                Arrays.stream(inst).forEach(i -> ans.get(i).get(1).add(t[3]));
            }
        });
        //sort
        ans.forEach((k, v) ->
            v.forEach(k1 -> k1.sort(String::compareTo)));
        return ans;
    }

    public List<String> searchCourses(String courseSubject, double
        percentAudited, double totalCourseHours) {
        List<String> ans = new ArrayList<>();
        array.stream().filter(t -> {
            String[] s = t[5].split(",");
            boolean sb = false;
            for (String e : s) {
                if (e.equals(courseSubject)) {
                    sb = true;
                    break;
                }
            }
            return sb && Double.parseDouble(t[11]) >= percentAudited
                && Double.parseDouble(t[16]) <= totalCourseHours;
        }).forEach(t -> ans.add(t[3]));
        return ans;
    }

    static List<List<String>> getList() {
        List<List<String>> lists = new ArrayList<>();
        lists.add(new ArrayList<>());
        lists.add(new ArrayList<>());
        return lists;
    }


}
