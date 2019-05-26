
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ProclaimRepository;
import domain.Proclaim;
import domain.Ticker;

@Service
public class ProclaimTickerServiceInter {

	@Autowired
	private TickerService		serviceTicker;

	@Autowired
	private ProclaimRepository	proclaimRepository;


	public Ticker createTicker() {
		return this.serviceTicker.create();
	}

	public Proclaim proclaimWithTicker(final Proclaim without) {

		Proclaim result = null;

		Ticker aux;

		boolean value = false;

		do
			try {
				aux = this.serviceTicker.saveTicker(without.getTicker());
				without.setTicker(aux);
				result = this.proclaimRepository.save(without);
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
		this.serviceTicker.deleteTicker(ticker.getId());
	}
}
