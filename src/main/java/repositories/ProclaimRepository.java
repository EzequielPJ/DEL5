
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Proclaim;

@Repository
public interface ProclaimRepository extends JpaRepository<Proclaim, Integer> {

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findActorByUserAccount(int id);

	@Query("select p from Proclaim p where p.finalMode = true and p.closed = false")
	Collection<Proclaim> findAllProclaim();

	@Query("select p from Proclaim p join p.members m where m.id = ?1")
	Collection<Proclaim> findAllByMember(int id);

	@Query("select p from Proclaim p where p.student.id = ?1")
	Collection<Proclaim> findAllByStudent(int id);
}
