
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.WorkReport;

@Component
@Transactional
public class WorkReportToStringConverter implements Converter<WorkReport, String> {

	@Override
	public String convert(final WorkReport workReport) {
		String result;

		if (workReport == null)
			result = null;
		else
			result = String.valueOf(workReport.getId());

		return result;
	}

}
