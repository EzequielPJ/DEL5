
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	//	@Query("select c.flat_rate from CustomisationSystem c")
	//	Double getFlatRate();

	//	@Query("select c.vat from CustomisationSystem c")
	//	Double getVat();

	//	@Query("select s from Sponsorship s join s.provider p where p.id = ?1")
	//	Collection<Sponsorship> getSponsorshipByProviderId(final int idProvider);
}
