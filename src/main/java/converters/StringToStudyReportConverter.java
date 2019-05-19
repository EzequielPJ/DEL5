
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.StudyReportRepository;
import domain.StudyReport;

@Component
@Transactional
public class StringToStudyReportConverter implements Converter<String, StudyReport> {

	@Autowired
	private StudyReportRepository	repositoryStudyReport;


	@Override
	public StudyReport convert(final String source) {
		StudyReport res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new StudyReport();
				else
					res = this.repositoryStudyReport.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}

}
