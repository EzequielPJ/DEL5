
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Portfolio;
import domain.StudyReport;

@Repository
public interface StudyReportRepository extends JpaRepository<StudyReport, Integer> {

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findActorByUserAccountId(int id);

	@Query("select p from Portfolio p join p.studyReport s where s.id = ?1")
	Portfolio findPortfolioByStudyReportId(int id);

}
