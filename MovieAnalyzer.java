import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiFunction;

public class MovieAnalyzer {

    static final String COMMENTS_NUMBER = "comments number";
    static final String CURRENT_RATING = "current rating";
    static final String CURRENT_ID = "current movie id";
    static final String MAX_RATING = "max rating";
    static final String MIN_RATING = "min rating";
    static final String MAX_MARKS = "max marks";
    static final Float HIGHT_RATING = 4f;
    static final Float LOW_RATING = 2f;

    public final static String MOVIES_TABLE = "archive\\movie_titles.csv";
    public final static HashMap<String, Object> resultMap = new HashMap<String, Object>();

    public void AnalizeRatingTable(String[] usersFiles, BiFunction<String[], DataStorage, Void> searchingFunc) {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (int i = 0; i < 4; i++) {
            final int index = i;

            Runnable task = () -> {
                BufferedReader ratingReader = null;
                try {
                    ratingReader = new BufferedReader(new FileReader(usersFiles[index]));
                    String temp;
                    DataStorage tempData = new DataStorage();
                    while ((temp = ratingReader.readLine()) != null) {
                        String[] tableElements = temp.split(",");
                        searchingFunc.apply(tableElements, tempData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        ratingReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread thread = new Thread(task);
            thread.start();
            list.add(thread);
        }

        for (Thread thread : list) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Void GetMaxRating(String[] tableElements, DataStorage arr) {
        if (arr.GetSize() < 1) {
            arr.AddValue(COMMENTS_NUMBER, 0); // 0 - comments number
            arr.AddValue(CURRENT_RATING, 0f); // 1 - current rating
            arr.AddValue(CURRENT_ID, ""); // 2 - current movie id
        }

        if (tableElements[0].charAt(tableElements[0].length() - 1) == ':') {
            arr.AddValue(CURRENT_RATING, arr.GetFloat(CURRENT_RATING) / arr.GetInteger(COMMENTS_NUMBER));
            if (!resultMap.containsKey(MAX_RATING)) {
                resultMap.put(MAX_RATING, new Movie("", "", "", -1f, arr.GetInteger(COMMENTS_NUMBER)));
            } else {
                Movie movie = (Movie) resultMap.get(MAX_RATING);
                if (movie._rating < arr.GetFloat(CURRENT_RATING)) {
                    Movie tempMovie = FindMovieByID(arr.GetString(CURRENT_ID), MOVIES_TABLE);
                    tempMovie._rating = arr.GetFloat(CURRENT_RATING);
                    tempMovie._numberOfComments = arr.GetInteger(COMMENTS_NUMBER);
                    resultMap.replace(MAX_RATING, tempMovie);
                }
            }
            arr.AddValue(CURRENT_ID, tableElements[0].substring(0, tableElements[0].length() - 1));
            arr.AddValue(COMMENTS_NUMBER, 0);
            arr.AddValue(CURRENT_RATING, 0f);
        } else {
            arr.AddValue(COMMENTS_NUMBER, arr.GetInteger(COMMENTS_NUMBER) + 1);
            arr.AddValue(CURRENT_RATING, arr.GetFloat(CURRENT_RATING) + Integer.parseInt(tableElements[1]));
        }
        return null;
    }

    public static Void GetMinRating(String[] tableElements, DataStorage arr) {
        if (arr.GetSize() < 1) {
            arr.AddValue(COMMENTS_NUMBER, 0); // 0 - comments number
            arr.AddValue(CURRENT_RATING, 0f); // 1 - current rating
            arr.AddValue(CURRENT_ID, ""); // 2 - current movie id
        }

        if (tableElements[0].charAt(tableElements[0].length() - 1) == ':') {
            arr.AddValue(CURRENT_RATING, arr.GetFloat(CURRENT_RATING) / arr.GetInteger(COMMENTS_NUMBER));
            if (!resultMap.containsKey(MIN_RATING)) {
                resultMap.put(MIN_RATING, new Movie("", "", "", 6f, arr.GetInteger(COMMENTS_NUMBER)));
            } else {
                Movie movie = (Movie) resultMap.get(MIN_RATING);
                if (movie._rating > arr.GetFloat(CURRENT_RATING)) {
                    Movie tempMovie = FindMovieByID(arr.GetString(CURRENT_ID), MOVIES_TABLE);
                    tempMovie._rating = arr.GetFloat(CURRENT_RATING);
                    tempMovie._numberOfComments = arr.GetInteger(COMMENTS_NUMBER);
                    resultMap.replace(MIN_RATING, tempMovie);
                }
            }
            arr.AddValue(CURRENT_ID, tableElements[0].substring(0, tableElements[0].length() - 1));
            arr.AddValue(COMMENTS_NUMBER, 0);
            arr.AddValue(CURRENT_RATING, 0f);
        } else {
            arr.AddValue(COMMENTS_NUMBER, arr.GetInteger(COMMENTS_NUMBER) + 1);
            arr.AddValue(CURRENT_RATING, arr.GetFloat(CURRENT_RATING) + Integer.parseInt(tableElements[1]));
        }
        return null;
    }

    public static Void GetMovieWithMaxMarks(String[] tableElements, DataStorage arr) {
        if (arr.GetSize() < 1) {
            arr.AddValue(COMMENTS_NUMBER, 0); // 0 - current marks number
            arr.AddValue(CURRENT_ID, ""); // 1 - current movie id
        }

        if (tableElements[0].charAt(tableElements[0].length() - 1) == ':') {
            if (!resultMap.containsKey(MAX_MARKS)) {
                resultMap.put(MAX_MARKS, new Movie());
            } else {
                Movie movie = (Movie) resultMap.get(MAX_MARKS);
                if (movie._numberOfComments < arr.GetInteger(COMMENTS_NUMBER)) {
                    Movie tempMovie = FindMovieByID(arr.GetString(CURRENT_ID), MOVIES_TABLE);
                    tempMovie._numberOfComments = arr.GetInteger(COMMENTS_NUMBER);
                    resultMap.replace(MAX_MARKS, tempMovie);
                }
            }

            arr.AddValue(CURRENT_ID, tableElements[0].substring(0, tableElements[0].length() - 1));
            arr.AddValue(COMMENTS_NUMBER, 0);
        } else {
            arr.AddValue(COMMENTS_NUMBER, arr.GetInteger(COMMENTS_NUMBER) + 1);
        }
        return null;
    }

    public static Void GetMostPopularMark(String[] tableElements, DataStorage arr) {
        if (!resultMap.containsKey("marks number")) {
            resultMap.put("marks number", new Integer[] { 0, 0, 0, 0, 0, 0 });
        }
        if (tableElements[0].charAt(tableElements[0].length() - 1) != ':') {
            Integer[] marksArray = (Integer[]) resultMap.get("marks number");
            marksArray[Integer.parseInt(tableElements[1])] += 1;
        }
        return null;
    }

    public static Void GetMostTalkativeUser(String[] tableElements, DataStorage arr) {
        if (!resultMap.containsKey("array number of user comments")) {
            resultMap.put("array number of user comments", new HashMap<String, Integer>());
        }
        if (tableElements[0].charAt(tableElements[0].length() - 1) != ':') {
            HashMap<String, Integer> users = (HashMap<String, Integer>) resultMap.get("array number of user comments");
            if (users.containsKey(tableElements[0])) {
                users.replace(tableElements[0], users.get(tableElements[0]) + 1);
            } else {
                users.put(tableElements[0], 1);
            }
        }
        return null;
    }

    public static Void GetHaters(String[] tableElements, DataStorage arr) {
        if (arr.GetSize() < 1) {
            arr.AddValue(COMMENTS_NUMBER, 0); // 0 - comments number
            arr.AddValue(CURRENT_RATING, 0f); // 1 - current rating
            arr.AddValue("users marks array", new HashMap<String, Integer>()); // 1 - current rating
        }

        if (!resultMap.containsKey("haters")) {
            resultMap.put("haters", new HashMap<String, Integer>());
        }

        if (tableElements[0].charAt(tableElements[0].length() - 1) == ':') {
            Float movie_rating = arr.GetFloat(CURRENT_RATING) / arr.GetInteger(COMMENTS_NUMBER);
            if (movie_rating >= HIGHT_RATING) {
                HashMap<String, Integer> hatersMap = (HashMap<String, Integer>) resultMap.get("haters");
                for (HashMap.Entry<String, Integer> entry : arr.GetMapStringInteger("users marks array").entrySet()) {
                    if (entry.getValue() <= LOW_RATING) {
                        String userId = entry.getKey();
                        if (hatersMap.containsKey(userId)) {
                            hatersMap.put(userId, (Integer) hatersMap.get(userId) + 1);
                        } else {
                            hatersMap.put(userId, 1);
                        }
                    }
                }
            }
            arr.AddValue(COMMENTS_NUMBER, 0);
            arr.AddValue(CURRENT_RATING, 0f);
            arr.AddValue("users marks array", new HashMap<String, Integer>());
        } else {
            arr.GetMapStringInteger("users marks array").put(tableElements[0], Integer.parseInt(tableElements[1]));
            arr.AddValue(COMMENTS_NUMBER, arr.GetInteger(COMMENTS_NUMBER) + 1);
            arr.AddValue(CURRENT_RATING, arr.GetFloat(CURRENT_RATING) + Integer.parseInt(tableElements[1]));
        }
        return null;
    }

    public static Movie FindMovieByID(String id, String moviesTable) {
        BufferedReader moviesReader = null;
        String line = null;
        Movie res = null;
        try {
            moviesReader = new BufferedReader(new FileReader(moviesTable));
            while ((line = moviesReader.readLine()) != null) {
                String[] movieFields = line.split(",");
                if (movieFields[0].equals(id)) {
                    res = new Movie(movieFields[0], movieFields[1], movieFields[2]);
                    break;
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                moviesReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}