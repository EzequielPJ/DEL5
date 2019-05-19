
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.EventRepository;
import domain.Event;

@Component
@Transactional
public class StringToEventConverter implements Converter<String, Event> {

	@Autowired
	private EventRepository	repositoryEvent;


	@Override
	public Event convert(final String source) {
		Event res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new Event();
				else
					res = this.repositoryEvent.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}

}
