
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Swap;

@Component
@Transactional
public class SwapToStringConverter implements Converter<Swap, String> {

	@Override
	public String convert(final Swap swap) {
		String result;

		if (swap == null)
			result = null;
		else
			result = String.valueOf(swap.getId());

		return result;
	}

}
