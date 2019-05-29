
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.MiscellaneousReport;
import domain.Portfolio;

@Repository
public interface MiscellaneousReportRepository extends JpaRepository<MiscellaneousReport, Integer> {

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findActorByUserAccountId(int id);

	@Query("select p from Portfolio p join p.miscellaneousReport m where m.id = ?1")
	Portfolio findPortfolioByMiscellaneousReportId(int id);
}
