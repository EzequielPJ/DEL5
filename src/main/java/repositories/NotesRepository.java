
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Event;
import domain.Notes;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer> {

	@Query("select n from Notes n where n.event.id = ?1")
	Collection<Notes> getNotesByEvent(int id);

	@Query("select e from Notes n join n.event e where n.id = ?1")
	Event getEventByNote(int id);

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findActorByUserAccountId(int id);

	@Query("select avg(n.note) from Notes n where n.event.id = ?1")
	Double getAVGNotesByEvent(int id);

}
