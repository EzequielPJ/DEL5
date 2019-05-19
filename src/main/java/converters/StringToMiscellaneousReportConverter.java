
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.MiscellaneousReportRepository;
import domain.MiscellaneousReport;

@Component
@Transactional
public class StringToMiscellaneousReportConverter implements Converter<String, MiscellaneousReport> {

	@Autowired
	private MiscellaneousReportRepository	repositoryMiscellaneousReport;


	@Override
	public MiscellaneousReport convert(final String source) {
		MiscellaneousReport res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new MiscellaneousReport();
				else
					res = this.repositoryMiscellaneousReport.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}

}
