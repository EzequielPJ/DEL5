
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Member;
import domain.Proclaim;

@Repository
public interface ProclaimRepository extends JpaRepository<Proclaim, Integer> {

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findActorByUserAccount(int id);

	@Query("select p from Proclaim p where p.finalMode = true and p.closed = false and p.members is empty")
	Collection<Proclaim> findAllNoAssignedProclaim();

	@Query("select p from Proclaim p where ?1 in p.members")
	Collection<Proclaim> findAllByMember(Member m);

	@Query("select p from Proclaim p where p.student.id = ?1")
	Collection<Proclaim> findAllByStudent(int id);
}
