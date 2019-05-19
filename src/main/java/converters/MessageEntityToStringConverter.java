
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.MessageEntity;

public class MessageEntityToStringConverter implements Converter<MessageEntity, String> {

	@Override
	public String convert(final MessageEntity mess) {
		String result;

		if (mess == null)
			result = null;
		else
			result = String.valueOf(mess.getId());

		return result;
	}

}
