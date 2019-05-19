
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.ProclaimRepository;
import domain.Proclaim;

public class StringToProclaimConverter implements Converter<String, Proclaim> {

	@Autowired
	private ProclaimRepository	repo;


	@Override
	public Proclaim convert(final String source) {
		Proclaim res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new Proclaim();
				else
					res = this.repo.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}

}
