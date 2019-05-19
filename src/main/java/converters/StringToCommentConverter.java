
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.CommentRepository;
import domain.Comment;

public class StringToCommentConverter implements Converter<String, Comment> {

	@Autowired
	private CommentRepository	repo;


	@Override
	public Comment convert(final String source) {
		Comment res;
		int id;
		try {
			if (StringUtils.isEmpty(source))
				res = null;
			else {
				id = Integer.valueOf(source);
				if (id == 0)
					res = new Comment();
				else
					res = this.repo.findOne(id);

			}
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;

	}

}
