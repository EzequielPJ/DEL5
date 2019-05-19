
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Ticker;

@Component
@Transactional
public class TickerToStringConverter implements Converter<Ticker, String> {

	@Override
	public String convert(final Ticker ticker) {
		String result;

		if (ticker == null)
			result = null;
		else
			result = ticker.getTicker();

		return result;
	}
}
