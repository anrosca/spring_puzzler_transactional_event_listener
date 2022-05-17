package inc.evil.spring.tx.management.domain;

import inc.evil.spring.tx.management.common.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "movie_audit")
public class MovieAudit extends AbstractEntity {
    private String name;
    private String movieId;

    protected MovieAudit() {
    }

    private MovieAudit(MovieAuditBuilder builder) {
        this.name = builder.name;
        this.movieId = builder.movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    @Override
    public String toString() {
        return "MovieAudit{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", movieId='" + movieId + '\'' +
                '}';
    }

    public static MovieAudit from(Movie movie) {
        return MovieAudit.builder()
                .movieId(movie.getId())
                .name(movie.getName())
                .build();
    }

    public static MovieAuditBuilder builder() {
        return new MovieAuditBuilder();
    }

    public static class MovieAuditBuilder {
        private String name;
        private String movieId;

        public MovieAuditBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MovieAuditBuilder movieId(String movieId) {
            this.movieId = movieId;
            return this;
        }


        public MovieAudit build() {
            return new MovieAudit(this);
        }
    }
}
