public class Movie {
    public String _id = "none";
    public String _year = "none";
    public String _title = "none";
    public float _rating = 0;
    public int _numberOfComments = 0;

    Movie() {

    }

    Movie(String id, String year, String title, float rating, int numberOfComments) {
        _id = id;
        _title = title;
        _year = year;
        _rating = rating;
        _numberOfComments = numberOfComments;
    }


    Movie(String id, String year, String title) {
        _id = id;
        _title = title;
        _year = year;
    }
}
