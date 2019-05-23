
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Finder;
import domain.Proclaim;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findActorByUserAccountId(int id);

	@Query("select p from Proclaim p where p.finalMode = true and p.closed = false and p.members is empty and (p.title LIKE %?1% or p.description LIKE %?1%)")
	Collection<Proclaim> findBySingleKey(String singleKey);

	@Query("select p from Proclaim p where p.finalMode = true and p.closed = false and p.members is empty and p.category.id = ?1")
	Collection<Proclaim> findByCategory(int id);

	@Query("select p from Proclaim p where p.finalMode = true and p.closed = false and p.members is empty and p.moment >= ?1")
	Collection<Proclaim> findByRegisteredAfterDate(Date registeredAfter);

	@Query("select p from Proclaim p where p.finalMode = true and p.closed = false and p.members is empty and p.moment <= ?1")
	Collection<Proclaim> findByRegisteredBeforeDate(Date registeredBefore);
}
