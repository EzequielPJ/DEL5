
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.CustomisationSystem;

@Repository
public interface CustomisationSystemRepository extends JpaRepository<CustomisationSystem, Integer> {

	@Query("select a from Actor a where a.id = ?1")
	Actor findActor(int id);

	@Query("select a from Actor a where a.account.enabled = 1")
	Collection<Actor> findAllSuspiciousActors();

	@Query("select a from Actor a where a.account.enabled = 0")
	Collection<Actor> findAllNoEnabledActors();

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findActorByUserAccountId(int id);

	@Query("select avg(1.0*(select count(p) from Proclaim p where p.student.id = s.id and p.finalMode = true)), max(1.0*(select count(p) from Proclaim p where p.student.id = s.id and p.finalMode = true)),min(1.0*(select count(p) from Proclaim p where p.student.id = s.id and p.finalMode = true)), stddev(1.0*(select count(p) from Proclaim p where p.student.id = s.id and p.finalMode = true)) from Student s")
	Double[] minMaxAvgStddevOfProclaimsByStudents();
	@Query("select avg(1.0*(select count(p) from Proclaim p where m member of p.members and p.finalMode = true)), max(1.0*(select count(p) from Proclaim p where m member of p.members and p.finalMode = true)),min(1.0*(select count(p) from Proclaim p where m member of p.members and p.finalMode = true)), stddev(1.0*(select count(p) from Proclaim p where m member of p.members and p.finalMode = true)) from Member m")
	Double[] minMaxAvgStddevOfTakenProclaimsByMember();
	@Query("select (select distinct c.name from Category c where c.id= p.category.id), count(p) from Proclaim p group by p.category.id")
	Collection<Object[]> histogramNumberProclaimPerCategory();
	@Query("select avg(f.proclaims.size)*1.0, max(f.proclaims.size)*1.0,  min(f.proclaims.size)*1.0,  stddev(f.proclaims.size)*1.0 from Finder f")
	Double[] minMaxAvgStddevOfFinderResults();
	@Query("select avg(1.0*(select count(c) from Collaborator c where c.comission.id = co.id and co.finalMode = true)), max(1.0*(select count(c) from Collaborator c where c.comission.id = co.id and co.finalMode = true)),min(1.0*(select count(c) from Collaborator c where c.comission.id = co.id and co.finalMode = true)),stddev(1.0*(select count(c) from Collaborator c where c.comission.id = co.id and co.finalMode = true)) from Comission co")
	Double[] minMaxAvgStddevOfCollaboratorsPerComission();
	@Query("select avg(1.0*(select count(n) from Notes n where n.event.id = e.id and e.finalMode = true)),max(1.0*(select count(n) from Notes n where n.event.id = e.id and e.finalMode = true)),min(1.0*(select count(n) from Notes n where n.event.id = e.id and e.finalMode = true)),stddev(1.0*(select count(n) from Notes n where n.event.id = e.id and e.finalMode = true)) from Event e")
	Double[] minMaxAvgStddevOfNotesPerEvent();
	@Query("select (((select count(e)*1.0 from Comission e where e.member is not null and e.finalMode = true)/count(c)*1.0)*1.0) from Comission c")
	Double ratioMemberWithComission();
	@Query("select distinct e.title from Event e where e.finalMode = true order by e.moment desc")
	List<String> eventsNearestToday();
}
