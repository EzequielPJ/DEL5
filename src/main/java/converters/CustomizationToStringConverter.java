
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.CustomisationSystem;

public class CustomizationToStringConverter implements Converter<CustomisationSystem, String> {

	@Override
	public String convert(final CustomisationSystem source) {
		String result;

		if (source == null)
			result = null;
		else
			result = String.valueOf(source.getId());

		return result;
	}

}
