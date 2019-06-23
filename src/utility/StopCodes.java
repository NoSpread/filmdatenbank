package utility;

public interface StopCodes {
    String ACTOR_STOP = "\"actor_id\",\"actor_name\"";
    String MOVIE_STOP = "\"movie_id\",\"movie_title\",\"movie_plot\",\"genre_name\",\"movie_released\",\"movie_imdbVotes\",\"movie_imdbRating\"";
    String DIRECTOR_STOP = "\"director_id\",\"director_name\"";
    String ACTOR_MOVIE_STOP = "\"actor_id\",\"movie_id\"";
    String DIRECTOR_MOVIE_STOP = "\"director_id\",\"movie_id\"";
}
