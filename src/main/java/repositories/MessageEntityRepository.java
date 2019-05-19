
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Box;
import domain.MessageEntity;

@Repository
public interface MessageEntityRepository extends JpaRepository<MessageEntity, Integer> {

	// Get messages de la caja
	@Query("select b.messageEntity from Box b where b.id = ?1")
	Collection<MessageEntity> getMessagesByBox(int id);

	// Get boxes de un actor
	@Query("select b from Actor a join a.boxes b join b.messageEntity m where m.id = ?1 and a.id = ?2")
	Collection<Box> getBoxesFromActorAndMessage(int message, int actor);

	@Query("select b from Actor a join a.boxes b where b.name = ?1 and a IN ?2 and a.account.id <> ?3")
	Collection<Box> getBoxesFromActors(String name, Collection<Actor> actors, int userlogged);

	@Query("select m.receiver from MessageEntity m where m.id = ?1 ")
	Collection<Actor> getReceiver(int id);

	@Query("select m from Actor a join a.boxes b join b.messageEntity m where b.name = 'Out Box' AND a.id = ?1")
	Collection<MessageEntity> getMessagesOutBox(int id);

	@Query("select a from Actor a where a.account.id <> ?1")
	Collection<Actor> getAllActorsMenosLoged(int id);

	//	@Query("select h from Rookie h where h.finder.singleKey LIKE ?1 OR h.finder.singleKey LIKE ?2 OR h.finder.singleKey LIKE ?3 OR h.finder.singleKey LIKE ?4 OR h.finder.singleKey LIKE ?5")
	//	Collection<Rookie> findRookiesNotificationString(String title, String description, String skills, String technologies, String profile);
	//
	//	@Query("select h from Rookie h where h.finder.minSalary <= ?1 OR h.finder.maxSalary >= ?1")
	//	Collection<Rookie> findRookiesNotificationString(Double salary);

	@Query("select m from MessageEntity m where m.subject = 'Notification of rebranding'")
	MessageEntity findSystemConfigMessage();

	@Query("select m from Actor a join a.boxes b join b.messageEntity m where a.id=?1")
	Collection<MessageEntity> getMessagesByActor(int id);

}
