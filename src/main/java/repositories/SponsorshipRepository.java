
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	//	@Query("select c.flat_rate from CustomisationSystem c")
	//	Double getFlatRate();

	//	@Query("select c.vat from CustomisationSystem c")
	//	Double getVat();

	@Query("select ss from Sponsorship ss join s.sponsor s where s.id = ?1")
	Collection<Sponsorship> getSponsorshipBySponsorId(final int idProvider);
}
