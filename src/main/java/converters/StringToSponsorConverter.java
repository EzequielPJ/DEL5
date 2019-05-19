
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.SponsorRepository;
import domain.Sponsor;

@Component
@Transactional
public class StringToSponsorConverter implements Converter<String, Sponsor> {

	@Autowired
	private SponsorRepository	repositorySponsor;


	@Override
	public Sponsor convert(final String source) {
		Sponsor res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new Sponsor();
				else
					res = this.repositorySponsor.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}

}
