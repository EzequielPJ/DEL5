
package converters;

import java.net.URLEncoder;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.CreditCard;

@Component
@Transactional
public class CreditCardToStringConverter implements Converter<CreditCard, String> {

	@Override
	public String convert(final CreditCard source) {
		String result;
		StringBuilder builder;
		if (source == null)
			result = null;
		else
			try {
				builder = new StringBuilder();
				builder.append(URLEncoder.encode(source.getHolder(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(source.getMake(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(source.getNumber(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(String.valueOf(source.getExpiration().getYear()), "UTF-8"));
				builder.append("/");
				builder.append(URLEncoder.encode(String.valueOf(source.getExpiration().getMonth()), "UTF-8"));
				builder.append("/");
				builder.append(URLEncoder.encode(String.valueOf(source.getExpiration().getDay()), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(String.valueOf(source.getCvv()), "UTF-8"));
				builder.append("|");

				result = builder.toString();
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return result;
	}

}
