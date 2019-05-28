
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Portfolio;
import domain.WorkReport;

@Repository
public interface WorkReportRepository extends JpaRepository<WorkReport, Integer> {

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findActorByUserAccountId(int id);

	@Query("select p from Portfolio p join p.workReport w where w.id = ?1")
	Portfolio findPortfolioByWorkReportId(int id);

}
