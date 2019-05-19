
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.TickerRepository;
import domain.Ticker;

@Component
@Transactional
public class StringToTickerConverter implements Converter<String, Ticker> {

	@Autowired
	TickerRepository	tickerRepository;


	@Override
	public Ticker convert(final String text) {
		Ticker result;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				result = this.tickerRepository.findTickerByCode(text);
				if (result == null) {
					result = new Ticker();
					result.setTicker(text);
				}
			}
		} catch (final Throwable opps) {
			throw new IllegalArgumentException(opps);
		}
		return result;
	}
}
