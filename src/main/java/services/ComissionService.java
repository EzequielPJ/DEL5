
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ComissionRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Collaborator;
import domain.Comission;
import domain.Member;

@Service
@Transactional
public class ComissionService extends AbstractService {

	@Autowired
	private ComissionRepository	comissionRepository;

	@Autowired
	private Validator			validator;


	public Comission findOne(final int idComission) {
		return this.comissionRepository.findOne(idComission);
	}

	public Collection<Comission> findAllComissionFinalMode() {
		return this.comissionRepository.findAllComissionFinalMode();
	}

	public Collection<Comission> getComissionsByMemberId(final int idMember) {
		return this.comissionRepository.getComissionsByMemberId(idMember);
	}

	public Actor getActorByUserId(final int idActor) {
		return this.comissionRepository.findActorByUserAccountId(idActor);
	}

	public Comission createComission() {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER));
		Member m;
		m = (Member) this.getActorByUserId(LoginService.getPrincipal().getId());
		Comission com;
		com = new Comission();
		com.setName("");
		com.setDescription("");
		com.setMoment(new Date());
		com.setFinalMode(false);
		com.setMember(m);
		return com;
	}

	public Comission save(final Comission com) {
		Actor a;
		a = this.getActorByUserId(LoginService.getPrincipal().getId());

		Comission modify;

		if (com.getId() == 0)
			modify = this.comissionRepository.save(com);
		else {
			Assert.isTrue(com.getMember().getId() == a.getId(), "You don´t have access, you can only update your comissions");
			modify = this.comissionRepository.save(com);
		}
		return modify;
	}

	public void deleteComission(final int idComission) {
		Actor a;
		a = this.getActorByUserId(LoginService.getPrincipal().getId());

		Comission org;
		org = this.comissionRepository.findOne(idComission);

		Assert.isTrue(org.getMember().getId() == a.getId(), "You don´t have access, you can only delete your comissions");

		this.comissionRepository.delete(idComission);
	}

	public Comission reconstruct(final Comission com, final BindingResult binding) {
		Comission result;
		if (com.getId() == 0) {
			result = com;
			result.setMember((Member) this.getActorByUserId(LoginService.getPrincipal().getId()));
			result.setMoment(new Date());
		} else {
			result = this.comissionRepository.findOne(com.getId());
			result.setName(com.getName());
			result.setDescription(com.getDescription());
			result.setFinalMode(com.isFinalMode());
		}
		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}
	public void flush() {
		this.comissionRepository.flush();
	}

	public void joinTo(final int idComission) {
		UserAccount user;
		user = LoginService.getPrincipal();
		Collaborator c;
		c = (Collaborator) this.getActorByUserId(user.getId());
		Comission com;
		com = this.findOne(idComission);
		Assert.isTrue(c.getComission() == null, "You don't have to join another comission");
		c.setComission(com);
	}
}
