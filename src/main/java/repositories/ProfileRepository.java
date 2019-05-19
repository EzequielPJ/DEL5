
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findActorByUserAccountId(int id);

	@Query("select p from Profile p where p.actor.id = ?1")
	Collection<Profile> getProfilesByActorId(int id);
}
