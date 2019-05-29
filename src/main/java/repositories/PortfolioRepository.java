
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Portfolio;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findActorByUserAccountId(int id);

	@Query("select c.portfolio from Collaborator c where c.account.id = ?1")
	List<Portfolio> findPortfolioByActor(int id);

}
