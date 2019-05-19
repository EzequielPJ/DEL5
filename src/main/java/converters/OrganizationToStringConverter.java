
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Organization;

@Component
@Transactional
public class OrganizationToStringConverter implements Converter<Organization, String> {

	@Override
	public String convert(final Organization organization) {
		String result;

		if (organization == null)
			result = null;
		else
			result = String.valueOf(organization.getId());

		return result;
	}

}
