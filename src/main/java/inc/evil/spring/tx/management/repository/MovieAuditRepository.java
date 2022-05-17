package inc.evil.spring.tx.management.repository;

import inc.evil.spring.tx.management.domain.MovieAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieAuditRepository extends JpaRepository<MovieAudit, String> {
}
