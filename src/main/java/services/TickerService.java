
package services;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.TickerRepository;

import com.mifmif.common.regex.Generex;

import domain.Ticker;

@Service
@Transactional(value = TxType.REQUIRES_NEW)
public class TickerService {

	@Autowired
	private TickerRepository	repository;


	public Ticker findTickerByCode(final String s) {
		return this.repository.findTickerByCode(s);
	}

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
		return new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-" + new Generex("[a-zA-Z0-9]{6}").random().toUpperCase();
	}
	public void deleteTicker(final int ticker) {
		this.repository.delete(ticker);
	}
	public void flush() {
		this.repository.flush();
	}
}
