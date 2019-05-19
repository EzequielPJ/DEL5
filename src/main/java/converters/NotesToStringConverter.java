
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Notes;

@Component
@Transactional
public class NotesToStringConverter implements Converter<Notes, String> {

	@Override
	public String convert(final Notes notes) {
		String result;

		if (notes == null)
			result = null;
		else
			result = String.valueOf(notes.getId());

		return result;
	}

}
