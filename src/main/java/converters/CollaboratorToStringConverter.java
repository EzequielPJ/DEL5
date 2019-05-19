
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Collaborator;

@Component
@Transactional
public class CollaboratorToStringConverter implements Converter<Collaborator, String> {

	@Override
	public String convert(final Collaborator collaborator) {
		String result;

		if (collaborator == null)
			result = null;
		else
			result = String.valueOf(collaborator.getId());

		return result;
	}

}
