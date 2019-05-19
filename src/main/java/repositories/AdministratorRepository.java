
package repositories;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

	@Query("select a from Administrator a where a.account.id = ?1")
	Administrator findAdminByUserAccountId(int id);

	@Query("select a from Administrator a")
	ArrayList<Administrator> findFirstAdmin();

	@Query("select a from Actor a where a.suspicious = true")
	Collection<Actor> getActorsSpammer();

	@Query("select a from Actor a where a.account.enabled = false")
	Collection<Actor> getActorsEnabled();

	//	@Query("select a.creditCard from Actor a where a.id = ?1")
	//	CreditCard getCreditcardByActor(int id);
}
