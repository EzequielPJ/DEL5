
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.OrganizationRepository;
import domain.Organization;

@Component
@Transactional
public class StringToOrganizationConverter implements Converter<String, Organization> {

	@Autowired
	private OrganizationRepository	repositoryOrganization;


	@Override
	public Organization convert(final String source) {
		Organization res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new Organization();
				else
					res = this.repositoryOrganization.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}

}
