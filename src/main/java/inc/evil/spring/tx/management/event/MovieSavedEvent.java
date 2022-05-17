package inc.evil.spring.tx.management.event;

import inc.evil.spring.tx.management.domain.Movie;

public class MovieSavedEvent {
    private final Movie movie;

    public MovieSavedEvent(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "MovieSavedEvent{" +
                "movie=" + movie +
                '}';
    }
}
