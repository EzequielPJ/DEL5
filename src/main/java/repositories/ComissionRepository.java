
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Collaborator;
import domain.Comission;

@Repository
public interface ComissionRepository extends JpaRepository<Comission, Integer> {

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findActorByUserAccountId(int id);

	@Query("select c from Comission c where c.member.id = ?1")
	Collection<Comission> getComissionsByMemberId(final int idMember);

	@Query("select c from Comission c where c.finalMode = true")
	Collection<Comission> findAllComissionFinalMode();

	@Query("select a from Actor a where a.comission.id = ?1")
	Collection<Collaborator> findCollaboratorsByComissionId(final int idComission);
}
