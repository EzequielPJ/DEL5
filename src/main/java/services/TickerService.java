
package services;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.TickerRepository;
import domain.Ticker;

@Service
@Transactional(value = TxType.REQUIRES_NEW)
public class TickerService {

	@Autowired
	private TickerRepository	repository;


	public Ticker create() {
		Ticker ticker;
		ticker = new Ticker();
		ticker.setTicker(this.generateTicker());
		return ticker;
	}

	public Ticker saveTicker(final Ticker saveTo) {

		Ticker result = null;

		result = this.repository.saveAndFlush(saveTo);

		return result;
	}

	private String generateTicker() {

		final Character[] ch = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
		};

		String c = "";

		for (int i = 0; i < 6; i++)
			c += ch[new SecureRandom().nextInt(ch.length)];

		return new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-" + c;
	}

	public void deleteTicker(final int ticker) {
		this.repository.delete(ticker);
	}
}
