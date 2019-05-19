
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.StudyReport;

@Component
@Transactional
public class StudyReportToStringConverter implements Converter<StudyReport, String> {

	@Override
	public String convert(final StudyReport studyReport) {
		String result;

		if (studyReport == null)
			result = null;
		else
			result = String.valueOf(studyReport.getId());

		return result;
	}

}
