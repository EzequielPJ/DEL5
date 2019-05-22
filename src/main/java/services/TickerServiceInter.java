
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import domain.Ticker;
import domain.Ticketable;

@Service
public class TickerServiceInter<K extends Ticketable, S extends JpaRepository<K, Integer>> extends AbstractService {

	@Autowired
	private TickerService	serviceTicker;

	S						repository;


	public void setRepository(final S repository) {
		this.repository = repository;
	}

	public Ticker createTicker() {
		return this.serviceTicker.create();
	}

	public K withTicker(final K without) {

		K result = null;

		Ticker aux;

		boolean value = false;

		do
			try {
				aux = this.serviceTicker.saveTicker(without.getTicker());
				without.setTicker(aux);
				result = this.repository.save(without);
				value = true;
			} catch (final Throwable oops) {
				value = false;
				aux = this.serviceTicker.create();
				without.setTicker(aux);
			}
		while (value == false);

		return result;
	}

	public void deleteTicker(final Ticker ticker) {
		this.serviceTicker.deleteTicker(ticker);
	}
}
