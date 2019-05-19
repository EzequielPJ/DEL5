
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.MessageEntityRepository;
import domain.MessageEntity;

@Component
@Transactional
public class StringToMessageEntityConverter implements Converter<String, MessageEntity> {

	@Autowired
	MessageEntityRepository	messageRepository;


	@Override
	public MessageEntity convert(final String text) {
		MessageEntity result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.messageRepository.findOne(id);
			}
		} catch (final Throwable opps) {
			throw new IllegalArgumentException(opps);
		}
		return result;
	}

}
