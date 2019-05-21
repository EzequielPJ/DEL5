
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.ComissionRepository;
import domain.Comission;

@Component
@Transactional
public class StringToComissionConverter implements Converter<String, Comission> {

	@Autowired
	private ComissionRepository	repositoryComission;


	@Override
	public Comission convert(final String source) {
		Comission res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new Comission();
				else
					res = this.repositoryComission.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}

}
