
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.CollaboratorRepository;
import domain.Collaborator;

@Component
@Transactional
public class StringToCollaboratorConverter implements Converter<String, Collaborator> {

	@Autowired
	private CollaboratorRepository	repositoryCollaborator;


	@Override
	public Collaborator convert(final String source) {
		Collaborator res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new Collaborator();
				else
					res = this.repositoryCollaborator.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}

}
