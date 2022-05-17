package inc.evil.spring.tx.management;

import inc.evil.spring.tx.management.domain.Movie;
import inc.evil.spring.tx.management.domain.MovieAudit;
import inc.evil.spring.tx.management.event.MovieSavedEvent;
import inc.evil.spring.tx.management.repository.MovieAuditRepository;
import inc.evil.spring.tx.management.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootApplication
public class SpringDeclarativeTxManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDeclarativeTxManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(MovieService movieService) {
        return args -> {
            Movie movie = Movie.builder()
                    .name("Joker")
                    .build();
            movieService.save(movie);
        };
    }
}

@Service
@Slf4j
class MovieService {
    private final MovieRepository movieRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MovieService(MovieRepository movieRepository, ApplicationEventPublisher eventPublisher) {
        this.movieRepository = movieRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Movie save(Movie movie) {
        Movie savedMovie = movieRepository.save(movie);
        log.debug("Saved movie: {}", savedMovie);
        eventPublisher.publishEvent(new MovieSavedEvent(savedMovie));
        return savedMovie;
    }
}

@Slf4j
@Component
class MovieAuditEventListener {
    private final MovieAuditRepository movieAuditRepository;

    public MovieAuditEventListener(MovieAuditRepository movieAuditRepository) {
        this.movieAuditRepository = movieAuditRepository;
    }

//    @EventListener(MovieSavedEvent.class)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRED)
    public void on(MovieSavedEvent event) {
        log.debug("Received event: {}", event);
        log.debug("Is transaction active: {}", TransactionSynchronizationManager.isActualTransactionActive());
        MovieAudit movieAudit = MovieAudit.from(event.getMovie());
        movieAuditRepository.save(movieAudit);
        log.debug("Saved movie audit: {}", movieAudit);
    }
}

/**
 * The programmatic approach does not work as well
 *
 */

//@Service
//@Slf4j
//class MovieService {
//    private final MovieRepository movieRepository;
//    private final MovieAuditRepository movieAuditRepository;
//
//    public MovieService(MovieRepository movieRepository, MovieAuditRepository movieAuditRepository) {
//        this.movieRepository = movieRepository;
//        this.movieAuditRepository = movieAuditRepository;
//    }
//
//    @Transactional
//    public Movie save(Movie movie) {
//        Movie savedMovie = movieRepository.save(movie);
//        log.debug("Saved movie: {}", savedMovie);
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//            @Override
//            public void afterCommit() {
//                MovieAudit movieAudit = MovieAudit.from(savedMovie);
//                movieAuditRepository.save(movieAudit);
//                log.debug("Saved movie audit: {}", movieAudit);
//            }
//        });
//        return savedMovie;
//    }
//}


/**
 * Solution, use @Transactional(propagation = Propagation.REQUIRES_NEW) with @TransactionalEventListener
 *
*/

//
//@Slf4j
//@Component
//class MovieAuditEventListener {
//    private final MovieAuditRepository movieAuditRepository;
//
//    public MovieAuditEventListener(MovieAuditRepository movieAuditRepository) {
//        this.movieAuditRepository = movieAuditRepository;
//    }
//
//    //    @EventListener(MovieSavedEvent.class)
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void on(MovieSavedEvent event) {
//        log.debug("Received event: {}", event);
//        MovieAudit movieAudit = MovieAudit.from(event.getMovie());
//        movieAuditRepository.save(movieAudit);
//        log.debug("Saved movie audit: {}", movieAudit);
//    }
//}
