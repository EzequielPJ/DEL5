
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.MiscellaneousReport;

@Component
@Transactional
public class MiscellaneousReportToStringConverter implements Converter<MiscellaneousReport, String> {

	@Override
	public String convert(final MiscellaneousReport miscellaneousReport) {
		String result;

		if (miscellaneousReport == null)
			result = null;
		else
			result = String.valueOf(miscellaneousReport.getId());

		return result;
	}

}
