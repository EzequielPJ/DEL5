
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import domain.Ticker;
import domain.Ticketable;

@NoRepositoryBean
public interface GenericRepository<K extends Ticketable> extends JpaRepository<K, Integer> {

	@Query("select t from Ticker t where t.ticker = ?1")
	Ticker findTickerByCode(String code);
}
