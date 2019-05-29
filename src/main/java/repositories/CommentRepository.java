
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("select c from Comment c where c.actor.account.id = ?1")
	Collection<Comment> findCommentByActor(int actorId);

	@Query("select c from Comment c where c.proclaim.id = ?1")
	Collection<Comment> findCommentByProclaim(int idProclaim);

	@Query("select c from Comment c where c.actor.account.id=?1 and c.proclaim.id=?2")
	Collection<Comment> getCommentsByActorAndProclaim(int idActor, int idProclaim);

	//	@Query("select c from Comment c where c.actor.id= ?1")
	//	Collection<Comment> getCommentsByMember(int idMember);

}
