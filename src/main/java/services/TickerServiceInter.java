
package services;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import repositories.GenericRepository;

import com.mifmif.common.regex.Generex;

import domain.Ticker;
import domain.Ticketable;

@Service
public class TickerServiceInter<K extends Ticketable, S extends GenericRepository<K>> {

	S	repository;


	public void setRepository(final S repository) {
		this.repository = repository;
	}

	public Ticker create() {
		Ticker ticker;
		ticker = new Ticker();
		ticker.setTicker(new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-" + new Generex("[a-zA-Z0-9]{6}").random().toUpperCase());
		return ticker;
	}

	public K withTicker(final K without) {

		K result = null;

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
					findByCode = this.repository.findTickerByCode(without.getTicker().getTicker());

					if (findByCode != null)
						throw new IllegalArgumentException();
				}

				result = this.repository.save(without);
				value = true;

			} catch (final Throwable oops) {
				value = false;
				aux = this.create();
				without.setTicker(aux);
			}
		while (value == false);

		return result;
	}
}
