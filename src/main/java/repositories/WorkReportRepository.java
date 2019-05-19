
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.WorkReport;

@Repository
public interface WorkReportRepository extends JpaRepository<WorkReport, Integer> {

}
