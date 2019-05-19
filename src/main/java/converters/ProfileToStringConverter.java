
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Profile;

@Component
@Transactional
public class ProfileToStringConverter implements Converter<Profile, String> {

	@Override
	public String convert(final Profile profile) {
		String result;

		if (profile == null)
			result = null;
		else
			result = String.valueOf(profile.getId());

		return result;
	}
}
