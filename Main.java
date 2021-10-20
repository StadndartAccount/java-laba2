import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String[] ratings = { "archive\\combined_data_1.txt", "archive\\combined_data_2.txt",
                "archive\\combined_data_3.txt", "archive\\combined_data_4.txt" };
        MovieAnalyzer ma = new MovieAnalyzer();
        Movie movie;
        ma.AnalizeRatingTable(ratings, MovieAnalyzer::GetMaxRating);
        movie = (Movie) MovieAnalyzer.resultMap.get("max rating");
        System.out.println("max rating: " + movie._title + " >> " + movie._rating);

        ma.AnalizeRatingTable(ratings, MovieAnalyzer::GetMinRating);
        movie = (Movie) MovieAnalyzer.resultMap.get("min rating");
        System.out.println("min rating: " + movie._title + " >> " + movie._rating);

        ma.AnalizeRatingTable(ratings, MovieAnalyzer::GetMovieWithMaxMarks);
        movie = (Movie) MovieAnalyzer.resultMap.get("max marks");
        System.out.println("max marks: " + movie._title + " >> " +
        movie._numberOfComments);

        ma.AnalizeRatingTable(ratings, MovieAnalyzer::GetMostPopularMark);
        Integer[] array = (Integer[]) MovieAnalyzer.resultMap.get("marks number");
        System.out.println("mark 1: " + array[1]);
        System.out.println("mark 2: " + array[2]);
        System.out.println("mark 3: " + array[3]);
        System.out.println("mark 4: " + array[4]);
        System.out.println("mark 5: " + array[5]);

        ma.AnalizeRatingTable(ratings, MovieAnalyzer::GetMostTalkativeUser);
        HashMap<String, Integer> users = (HashMap<String, Integer>)
        MovieAnalyzer.resultMap
        .get("array number of user comments");

        HashMap.Entry<String, Integer> maxEntry = null;

        for (HashMap.Entry<String, Integer> entry : users.entrySet()) {
        if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
        {
        maxEntry = entry;
        }
        }
        System.out.println("user with max number of marks: " + maxEntry.getKey() + " >> " + maxEntry.getValue());

        ma.AnalizeRatingTable(ratings, MovieAnalyzer::GetHaters);
        HashMap<String, Integer> hatersMap = (HashMap<String, Integer>) MovieAnalyzer.resultMap.get("haters");
        hatersMap.values().remove(1);
        System.out.println("Haters:");
        hatersMap.entrySet().stream().sorted(HashMap.Entry.comparingByValue(Comparator.reverseOrder())).limit(10).forEach(System.out::println);

    }
}