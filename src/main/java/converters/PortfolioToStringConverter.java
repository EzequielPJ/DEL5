
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Portfolio;

@Component
@Transactional
public class PortfolioToStringConverter implements Converter<Portfolio, String> {

	@Override
	public String convert(final Portfolio portfolio) {
		String result;

		if (portfolio == null)
			result = null;
		else
			result = String.valueOf(portfolio.getId());

		return result;
	}

}
