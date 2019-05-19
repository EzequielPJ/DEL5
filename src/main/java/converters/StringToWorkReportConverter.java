
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.WorkReportRepository;
import domain.WorkReport;

@Component
@Transactional
public class StringToWorkReportConverter implements Converter<String, WorkReport> {

	@Autowired
	private WorkReportRepository	repositoryWorkReport;


	@Override
	public WorkReport convert(final String source) {
		WorkReport res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new WorkReport();
				else
					res = this.repositoryWorkReport.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}

}
