
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProclaimRepository;
import security.Authority;
import security.LoginService;
import domain.Actor;
import domain.Member;
import domain.Proclaim;
import domain.Student;

@Service
@Transactional
public class ProclaimService extends TickerServiceInter<Proclaim, ProclaimRepository> {

	@Autowired
	private ProclaimRepository	repository;

	@Autowired
	private Validator			validator;

	private boolean				inFinal;


	public Actor findByUserAccount(final int id) {
		return this.repository.findActorByUserAccount(id);
	}

	public Collection<Proclaim> findNoAssigned() {
		return this.repository.findAllNoAssignedProclaim();
	}

	public Collection<Proclaim> findAllByMember() {
		Member m;
		m = (Member) this.repository.findActorByUserAccount(LoginService.getPrincipal().getId());
		return this.repository.findAllByMember(m.getId());
	}

	public Collection<Proclaim> findAllByStudent() {

		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT));
		Student m;
		m = (Student) this.repository.findActorByUserAccount(LoginService.getPrincipal().getId());

		return this.repository.findAllByStudent(m.getId());
	}

	public Proclaim create() {

		Proclaim proclaim;
		proclaim = new Proclaim();

		proclaim.setCategory(null);
		proclaim.setClosed(false);
		proclaim.setDescription("");
		proclaim.setFinalMode(false);
		proclaim.setLaw("");
		proclaim.setMoment(new Date());
		proclaim.setReason("");
		proclaim.setTitle("");
		proclaim.setTicker(super.createTicker());
		proclaim.setMembers(new ArrayList<Member>());
		proclaim.setAttachments("");
		proclaim.setStatus("SUBMITTED");

		return proclaim;
	}

	public void assign(final int id) {

		Proclaim proclaim;
		proclaim = this.repository.findOne(id);

		Member h;
		h = (Member) this.repository.findActorByUserAccount(LoginService.getPrincipal().getId());

		Collection<Member> members;
		members = proclaim.getMembers();

		if (!members.contains(h)) {
			members.add(h);
			proclaim.setMembers(members);
		}

	}

	public Proclaim findOne(final int id) {
		Proclaim result;
		result = this.repository.findOne(id);
		this.inFinal = result.isFinalMode();
		return result;
	}

	public Proclaim save(final Proclaim aux) {

		Proclaim result;

		if (aux.getId() == 0) {
			Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT));
			Student m;
			m = (Student) this.repository.findActorByUserAccount(LoginService.getPrincipal().getId());
			aux.setStudent(m);
		} else if (aux.getId() != 0)
			if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
				Member m;
				m = (Member) this.repository.findActorByUserAccount(LoginService.getPrincipal().getId());
				Assert.isTrue(aux.getMembers().contains(m));
			} else if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT)) {
				Student m;
				m = (Student) this.repository.findActorByUserAccount(LoginService.getPrincipal().getId());
				Assert.isTrue(aux.getStudent().getId() == m.getId());
			}

		Assert.isTrue(this.inFinal == false);

		if (aux.isFinalMode()) {
			if (aux.getStatus().equals("SUBMITTED"))
				aux.setStatus("PENDING");
			if (aux.getStatus().equals("ENVIADO"))
				aux.setStatus("PENDIENTE");
		}

		super.setRepository(this.repository);
		result = super.withTicker(aux);

		return result;
	}

	@Override
	public void delete(final int id) {
		Proclaim p;
		p = this.repository.findOne(id);

		Assert.isTrue(p.isFinalMode() == false);
		Assert.isTrue(p.getStudent().getId() == ((Student) this.repository.findActorByUserAccount(LoginService.getPrincipal().getId())).getId());

		super.setRepository(this.repository);

		super.delete(p.getId());

	}

	public Proclaim reconstruct(final Proclaim aux, final BindingResult binding) {
		Proclaim result;

		if (aux.getId() == 0)
			result = aux;
		else {
			result = this.repository.findOne(aux.getId());
			if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT))
				if (this.inFinal == false) {
					result.setAttachments(aux.getAttachments());
					result.setCategory(aux.getCategory());
					result.setFinalMode(aux.isFinalMode());
					result.setDescription(aux.getDescription());
					result.setTitle(aux.getTitle());
					result.setMoment(new Date());
					result.setStudentCard(aux.getStudentCard());
					this.inFinal = false;
				}

			if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER))
				if (this.inFinal == true) {
					result.setLaw(aux.getLaw());
					result.setReason(aux.getReason());
					result.setStatus(aux.getStatus());
					result.setClosed(aux.isClosed());
					this.inFinal = true;
				}
		}

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

}
