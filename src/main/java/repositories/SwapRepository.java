
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Collaborator;
import domain.Swap;

@Repository
public interface SwapRepository extends JpaRepository<Swap, Integer> {

	@Query("select c from Collaborator c where c.id <> ?1 and c.comission.id > 0 and c.comission.id <> ?2 order by c.comission")
	List<Collaborator> findAllCollaboratorByComission(final int idCollaborator, final int idComission);

	@Query("select c from Collaborator c where c.id = ?1")
	Collaborator findOneCollaborator(final int id);

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findActorByUserAccountId(int id);

	@Query("select s from Swap s where s.sender.id = ?1")
	List<Swap> getSwapsByCollaboratorId(final int idCollaborator);

	@Query("select s from Swap s where s.receiver.id = ?1 and s.status = 'pending'")
	List<Swap> getSwapsPendingByCollaboratorId(final int idCollaborator);

}
