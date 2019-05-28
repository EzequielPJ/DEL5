
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.CustomisationSystemRepository;
import domain.CustomisationSystem;

public class StringToCustomisationConverter implements Converter<String, CustomisationSystem> {

	@Autowired
	private CustomisationSystemRepository	repo;


	@Override
	public CustomisationSystem convert(final String source) {
		CustomisationSystem res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new CustomisationSystem();
				else
					res = this.repo.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;

	}

}
