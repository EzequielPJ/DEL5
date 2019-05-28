
package services;

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
	TickerRepository	repository;


	public Ticker saveTicker(final Ticker saveTo) {

		Ticker result = null;

		result = this.repository.saveAndFlush(saveTo);

		return result;
	}

	public void deleteTicker(final int ticker) {
		this.repository.delete(ticker);
	}
	public void flush() {
		this.repository.flush();
	}
	public void flush() {
		this.repository.flush();
	}
}
