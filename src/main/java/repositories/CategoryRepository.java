
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Category;
import domain.Proclaim;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("select p from Proclaim p where p.category.id = ?1")
	Collection<Proclaim> findAllProclaimsByCategory(int id);

	@Query("select c.name from Category c")
	public Collection<String> getNamesFromCategory();

	@Query("select c from Category c join c.categories ca where ca.id = ?1")
	public Category getCategoryFather(int id);

	@Query("select c From Proclaim p join p.category c group by c having count(c) > 2")
	Collection<Category> getCategoryInMoreThan2Proclaims();

	@Query("select a from Actor a where a.account.id = ?1")
	Actor findByUserAccount(int id);
}
