
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.BoxRepository;
import domain.Box;

@Component
@Transactional
public class StringToBoxConverter implements Converter<String, Box> {

	@Autowired
	BoxRepository	boxRepository;


	@Override
	public Box convert(final String text) {
		Box result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.boxRepository.findOne(id);
			}
		} catch (final Throwable opps) {
			throw new IllegalArgumentException(opps);
		}
		return result;
	}

}
