
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.StudyReport;

@Repository
public interface StudyReportRepository extends JpaRepository<StudyReport, Integer> {

}
