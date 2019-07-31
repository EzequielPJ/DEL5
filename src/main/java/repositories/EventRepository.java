
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Collaborator;
import domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

	@Query("select e from Event e where e.finalMode = 1 and e.status = 'accepted'")
	Collection<Event> findAllFinalMode();

	@Query("select e from Event e where e.status = 'pending'")
	Collection<Event> findAllPending();

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findActorByUserAccountId(int id);

	@Query("select e from Event e where e.collaborator.id = ?1")
	Collection<Event> getEventsByCollaboratorId(final int idCollaborator);

	@Query("select e.collaborator from Event e where e.id = ?1")
	Collaborator findCollaboratorByEventId(final int idEvent);

}
