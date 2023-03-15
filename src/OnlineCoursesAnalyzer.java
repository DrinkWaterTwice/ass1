import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is just a demo for you, please run it on JDK17. This is just a demo, and you can extend and
 * implement functions based on this demo, or implement it in a different way.
 */
public class OnlineCoursesAnalyzer {

    List<Course> courses = new ArrayList<>();

    public OnlineCoursesAnalyzer(String datasetPath) {
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(datasetPath, StandardCharsets.UTF_8));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
                Course course = new Course(info[0], info[1], new Date(info[2]), info[3], info[4],
                    info[5],
                    Integer.parseInt(info[6]), Integer.parseInt(info[7]), Integer.parseInt(info[8]),
                    Integer.parseInt(info[9]), Integer.parseInt(info[10]),
                    Double.parseDouble(info[11]),
                    Double.parseDouble(info[12]), Double.parseDouble(info[13]),
                    Double.parseDouble(info[14]),
                    Double.parseDouble(info[15]), Double.parseDouble(info[16]),
                    Double.parseDouble(info[17]),
                    Double.parseDouble(info[18]), Double.parseDouble(info[19]),
                    Double.parseDouble(info[20]),
                    Double.parseDouble(info[20]), Double.parseDouble(info[21]));
                courses.add(course);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //1
    public Map<String, Integer> getPtcpCountByInst() {
        Map<String, Integer> map;
        map = courses.stream().collect(
            Collectors.groupingBy(t -> t.institution, Collectors.summingInt(t -> t.participants)));
        return map;

    }

    //2
    public Map<String, Integer> getPtcpCountByInstAndSubject() {
        Map<String, Integer> map = courses.stream().collect(
            Collectors.groupingBy(t -> t.institution + "-" + t.subject,
                Collectors.summingInt(t -> t.participants)));
        LinkedHashMap<String, Integer> st = new LinkedHashMap<>(16, 0.75f, true);
        map.entrySet().stream().sorted((o2, o1) -> {
            if (o1.getValue() > o2.getValue()) {
                return 1;
            }
            if (o1.getValue() < o2.getValue()) {
                return -1;
            } else {
                return o1.getKey().compareTo(o2.getKey());
            }
        }).forEach(t -> st.put(t.getKey(), t.getValue()));
        return st;
    }

    //3
    public Map<String, List<List<String>>> getCourseListOfInstructor() {
        Map<String, List<List<String>>> maps = new HashMap<>();
        courses.stream().map(t -> t.instructors.replace(", ", ",").split(","))
            .forEach(t -> Arrays.stream(t).forEach(s -> {
                if (maps.get(s) == null) {
                    List<String> a = new ArrayList<>();
                    List<String> b = new ArrayList<>();
                    List<List<String>> big = new ArrayList<>();
                    big.add(a);
                    big.add(b);
                    maps.put(s, big);
                }
            }));
        courses.stream().map(t -> new String[]{t.title, t.instructors}).forEach(t -> {
            String[] strings = t[1].replace(", ", ",").split(",");
            if (strings.length != 1) {
                Arrays.stream(strings).forEach(s -> {
                    if (!maps.get(s).get(1).contains(t[0])) {
                        maps.get(s).get(1).add(t[0]);
                    }
                });
            }
            if (strings.length == 1) {
                if (!maps.get(strings[0]).get(0).contains(t[0])) {
                    maps.get(strings[0]).get(0).add(t[0]);
                }
            }
        });
        maps.forEach((key, value) -> {
            value.get(0).sort(String::compareTo);
            value.get(1).sort(String::compareTo);
        });
        maps.forEach((key, value) -> {
            System.out.print(key + " == ");
            System.out.println(value);
        });

        return maps;
    }

    //4
    public List<String> getCourses(int topK, String by) {
        return null;
    }

    //5
    public List<String> searchCourses(String courseSubject, double percentAudited,
        double totalCourseHours) {
        return null;
    }

    //6
    public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher) {
        return null;
    }

}

class Course {

    String institution;
    String number;
    Date launchDate;
    String title;
    String instructors;
    String subject;
    int year;
    int honorCode;
    int participants;
    int audited;
    int certified;
    double percentAudited;
    double percentCertified;
    double percentCertified50;
    double percentVideo;
    double percentForum;
    double gradeHigherZero;
    double totalHours;
    double medianHoursCertification;
    double medianAge;
    double percentMale;
    double percentFemale;
    double percentDegree;

    public Course(String institution, String number, Date launchDate,
        String title, String instructors, String subject,
        int year, int honorCode, int participants,
        int audited, int certified, double percentAudited,
        double percentCertified, double percentCertified50,
        double percentVideo, double percentForum, double gradeHigherZero,
        double totalHours, double medianHoursCertification,
        double medianAge, double percentMale, double percentFemale,
        double percentDegree) {
        this.institution = institution;
        this.number = number;
        this.launchDate = launchDate;
        if (title.startsWith("\"")) {
            title = title.substring(1);
        }
        if (title.endsWith("\"")) {
            title = title.substring(0, title.length() - 1);
        }
        this.title = title;
        if (instructors.startsWith("\"")) {
            instructors = instructors.substring(1);
        }
        if (instructors.endsWith("\"")) {
            instructors = instructors.substring(0, instructors.length() - 1);
        }
        this.instructors = instructors;
        if (subject.startsWith("\"")) {
            subject = subject.substring(1);
        }
        if (subject.endsWith("\"")) {
            subject = subject.substring(0, subject.length() - 1);
        }
        this.subject = subject;
        this.year = year;
        this.honorCode = honorCode;
        this.participants = participants;
        this.audited = audited;
        this.certified = certified;
        this.percentAudited = percentAudited;
        this.percentCertified = percentCertified;
        this.percentCertified50 = percentCertified50;
        this.percentVideo = percentVideo;
        this.percentForum = percentForum;
        this.gradeHigherZero = gradeHigherZero;
        this.totalHours = totalHours;
        this.medianHoursCertification = medianHoursCertification;
        this.medianAge = medianAge;
        this.percentMale = percentMale;
        this.percentFemale = percentFemale;
        this.percentDegree = percentDegree;
    }
}