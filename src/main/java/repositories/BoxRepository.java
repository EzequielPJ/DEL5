
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Box;

@Repository
public interface BoxRepository extends JpaRepository<Box, Integer> {

	// Get Actor de las distintas cajas
	@Query("select b from Actor a join a.boxes b where a.id = ?1 and b.name= 'In Box'")
	Box getActorEntryBox(int actorId);
	@Query("select b from Actor a join a.boxes b where a.id = ?1 and b.name= 'Spam Box'")
	Box getActorSpamBox(int actorId);
	@Query("select b from Actor a join a.boxes b where a.id = ?1 and b.name= 'Out Box'")
	Box getActorSendedBox(int actorId);
	@Query("select b from Actor a join a.boxes b where a.id = ?1 and b.name= 'Trash Box'")
	Box getActorTrashBox(int actorId);
	@Query("select b from Actor a join a.boxes b where a.id = ?1 and b.name= 'Notification Box'")
	Box getActorNotificationBox(int actorId);

	@Query("select b from Actor a join a.boxes b where a.id = ?1 and b.id= ?2")
	Box getActorOtherBox(int actorId, int other);

	// Get cajas del sistema del user registrado
	@Query("select b from Actor a join a.boxes b where a.account.id = ?1")
	Collection<Box> getBoxesFromUserAccount(int actorId);

	// Get cajas que no son del sistema con el user registrado
	@Query("select b from Actor a join a.boxes b where a.account.id = ?1 and b.fromSystem = 0")
	Collection<Box> getBoxesFromActorNoSystem(int actorId);

	//Necessary for StringToActorConverter
	@Query("select a from Actor a where a.id = ?1")
	Actor getActorById(int id);

	// Get actor del user registrado
	@Query("select a from Actor a where a.account.id = ?1")
	Actor getActorByUserAccount(int id);

	// Get todos los actores (uderAccount) del sistema menos el registrado y los baneados
	@Query("select a from Actor a where a.account.enabled = 1")
	Collection<Actor> findAllActorsSystem();

	//Query para que 2 cajas del mismo acrtor no tengan el mismo nombre
	@Query("select b.name from Actor a join a.boxes b where a.account.id=?1")
	public Collection<String> getNamesFromBoxes(int accountId);
}
