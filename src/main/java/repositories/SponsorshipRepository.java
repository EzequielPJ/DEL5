
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	@Query("select s from Sponsorship s join s.sponsor p where p.id = ?1 and s.isActive = true")
	Collection<Sponsorship> getSponsorshipActiveBySponsorId(final int idProvider);

	@Query("select s from Sponsorship s join s.sponsor p where p.id = ?1 and s.isActive = false")
	Collection<Sponsorship> getSponsorshipDesactiveBySponsorId(final int idProvider);

	@Query("select s from Sponsorship s where s.sponsor.id = ?1 and s.event.id = ?2")
	Collection<Sponsorship> getSponsorshipByEventId(final int idProvider, final int idPosition);

	@Query("select s from Sponsorship s where s.event.id = ?1 and s.isActive = true")
	Collection<Sponsorship> getSponsorshipByEventId(final int idPosition);
}
