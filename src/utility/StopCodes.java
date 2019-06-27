package utility;

public interface StopCodes {
    String ACTOR_STOP = "New_Entity: \"actor_id\",\"actor_name\"";
    String MOVIE_STOP = "New_Entity: \"movie_id\",\"movie_title\",\"movie_plot\",\"genre_name\",\"movie_released\",\"movie_imdbVotes\",\"movie_imdbRating\"";
    String DIRECTOR_STOP = "New_Entity: \"director_id\",\"director_name\"";
    String ACTOR_MOVIE_STOP = "New_Entity: \"actor_id\",\"movie_id\"";
    String DIRECTOR_MOVIE_STOP = "New_Entity: \"director_id\",\"movie_id\"";
}
