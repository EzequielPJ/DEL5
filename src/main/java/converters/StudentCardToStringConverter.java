
package converters;

import java.net.URLEncoder;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.StudentCard;

@Component
@Transactional
public class StudentCardToStringConverter implements Converter<StudentCard, String> {

	@Override
	public String convert(final StudentCard source) {
		String result;
		StringBuilder builder;
		if (source == null)
			result = null;
		else
			try {

				builder = new StringBuilder();

				builder.append(URLEncoder.encode(source.getCentre(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(source.getVat(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(String.valueOf(source.getCode()), "UTF-8"));
				builder.append("|");

				result = builder.toString();
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return result;
	}

}
