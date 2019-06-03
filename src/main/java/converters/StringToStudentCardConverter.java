
package converters;

import java.net.URLDecoder;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.StudentCard;

@Component
@Transactional
public class StringToStudentCardConverter implements Converter<String, StudentCard> {

	@Override
	public StudentCard convert(final String source) {
		StudentCard result;
		String[] parts;
		if (source == null)
			result = null;
		else
			try {
				parts = source.split("\\|");
				result = new StudentCard();

				result.setCentre(URLDecoder.decode(parts[0], "UTF-8"));
				result.setVat(URLDecoder.decode(parts[1], "UTF-8"));
				result.setCode(Integer.valueOf(URLDecoder.decode(parts[2], "UTF-8")));

			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return result;
	}

}
