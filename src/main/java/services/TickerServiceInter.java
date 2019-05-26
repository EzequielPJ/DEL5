
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

		Ticker aux = null;

		if (without.getId() != 0)
			auxFromDB = this.repository.findOne(without.getId());

		Ticker aux = null;

		boolean value = false;

		do
			try {

				if (without.getId() != 0) {

					K auxFromDB;
					auxFromDB = this.repository.findOne(without.getId());

					boolean check;
					check = auxFromDB.getTicker().getTicker().equals(without.getTicker().getTicker());

					if (!check)
						without.setTicker(auxFromDB.getTicker());
				}

				if (without.getId() == 0) {
					Ticker findByCode;
					findByCode = this.serviceTicker.findTickerByCode(without.getTicker().getTicker());

					if (findByCode != null)
						throw new IllegalArgumentException();
				}

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

	public void delete(final int id) {
		this.repository.delete(id);
	}
}
