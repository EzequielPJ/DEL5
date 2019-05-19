
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.PortfolioRepository;
import domain.Portfolio;

@Component
@Transactional
public class StringToPortfolioConverter implements Converter<String, Portfolio> {

	@Autowired
	private PortfolioRepository	repositoryPortfolio;


	@Override
	public Portfolio convert(final String source) {
		Portfolio res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new Portfolio();
				else
					res = this.repositoryPortfolio.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}

}
