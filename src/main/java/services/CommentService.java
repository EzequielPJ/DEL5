
package services;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import security.Authority;
import security.LoginService;
import domain.Actor;
import domain.Comment;
import domain.Member;
import domain.Student;

@Service
@Transactional
public class CommentService extends AbstractService {

	@Autowired
	private CommentRepository	commRepository;

	@Autowired
	private ProclaimService		procService;

	@Autowired
	private Validator			validator;


	//	public Collection<Comment> findAllByActorId(final int actorId) {
	//		return this.commRepository.findCommentByActor(actorId);
	//	}

	public Collection<Comment> getCommentsByProclaim(final int proclaimId) {
		return this.commRepository.findCommentByProclaim(proclaimId);
	}

	public Collection<Comment> getCommentsByActorAndProclaim(final int idAccount, final int idProclaim) {
		return this.commRepository.getCommentsByActorAndProclaim(idAccount, idProclaim);
	}

	public Collection<Comment> getCommentsByActor(final int accountId) {
		return this.commRepository.findCommentByActor(accountId);
	}

	/*
	 * public Collection<Comment> getCommentsByStudent(final int idStudent) {
	 * return this.commRepository.getCommentsByStudent(idStudent);
	 * }
	 * 
	 * public Collection<Comment> getCommentsByMember(final int idMember) {
	 * return this.commRepository.getCommentsByMember(idMember);
	 * }
	 */
	public Comment findOne(final int commentId) {
		return this.commRepository.findOne(commentId);
	}

	public Comment create(final int proclaimId) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER) || super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT));
		Comment c;
		c = new Comment();
		c.setAttachments("");
		c.setDescription("");
		c.setProclaim(this.procService.findOne(proclaimId));

		return c;
	}

	public Comment save(final Comment c) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER) || super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT));
		Comment saved;
		saved = this.commRepository.save(c);
		return saved;

	}

	public void delete(final int commentId) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER) || super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT));
		this.commRepository.delete(commentId);
	}

	public Comment reconstruct(final Comment c, final BindingResult binding) {
		Comment result;

		if (c.getId() == 0) {
			result = c;
			Actor a;
			a = this.procService.findByUserAccount(LoginService.getPrincipal().getId());
			if (a instanceof Member) {
				Member m;
				m = (Member) a;
				result.setActor(m);
			}
			if (a instanceof Student) {
				Student m;
				m = (Student) a;
				result.setActor(m);
			}

		} else {
			result = this.commRepository.findOne(c.getId());
			result.setDescription(c.getDescription());
			result.setAttachments(c.getAttachments());
		}

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();
		return result;
	}
}
