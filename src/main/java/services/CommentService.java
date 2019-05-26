
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import security.Authority;
import security.LoginService;
import domain.Comment;

@Service
@Transactional
public class CommentService extends AbstractService {

	@Autowired
	private CommentRepository	commRepository;

	@Autowired
	private ProclaimService		procService;


	public Collection<Comment> findAllByActorId(final int actorId) {
		return this.commRepository.findCommentByActor(actorId);
	}

	public Comment create(final int proclaimId) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER) || super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT));
		Comment c;
		c = new Comment();
		c.setActor(this.procService.findByUserAccount(LoginService.getPrincipal().getId()));
		c.setAttachments("");
		c.setDescription("");
		c.setProclaim(this.procService.findOne(proclaimId));

		return c;
	}

	public Comment save(final Comment c) {
		return null;

	}

	public void delete() {
	}

}
