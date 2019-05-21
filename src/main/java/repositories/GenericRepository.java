
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import domain.Ticketable;

@NoRepositoryBean
public interface GenericRepository<K extends Ticketable> extends JpaRepository<K, Integer> {

}
