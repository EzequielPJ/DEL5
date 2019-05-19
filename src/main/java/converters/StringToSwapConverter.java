
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.SwapRepository;
import domain.Swap;

@Component
@Transactional
public class StringToSwapConverter implements Converter<String, Swap> {

	@Autowired
	private SwapRepository	repositorySwap;


	@Override
	public Swap convert(final String source) {
		Swap res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new Swap();
				else
					res = this.repositorySwap.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}

}
