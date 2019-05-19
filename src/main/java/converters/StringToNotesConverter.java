
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.NotesRepository;
import domain.Notes;

@Component
@Transactional
public class StringToNotesConverter implements Converter<String, Notes> {

	@Autowired
	private NotesRepository	repositoryNotes;


	@Override
	public Notes convert(final String source) {
		Notes res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new Notes();
				else
					res = this.repositoryNotes.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}

}
