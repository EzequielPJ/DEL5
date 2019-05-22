
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Comission;

@Component
@Transactional
public class ComissionToStringConverter implements Converter<Comission, String> {

	@Override
	public String convert(final Comission comission) {
		String result;

		if (comission == null)
			result = null;
		else
			result = String.valueOf(comission.getId());

		return result;
	}

}
