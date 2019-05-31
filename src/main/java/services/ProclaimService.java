
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
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
import domain.StudentCard;

@Service
@Transactional
@EnableCaching
public class ProclaimService extends AbstractService {

	@Autowired
	private ProclaimRepository									repository;

	@Autowired
	private Validator											validator;

	private boolean												inFinal;

	private TickerServiceInter<Proclaim, ProclaimRepository>	interm;


	public Actor findByUserAccount(final int id) {
		return this.repository.findActorByUserAccount(id);
	}

	public Collection<Proclaim> findProclaimAssigned(final int id) {
		return this.repository.findProclaimAssigned(id);
	}

	@Cacheable(value = "proclaims")
	public Collection<Proclaim> findNoAssigned() {
		return this.repository.findAllProclaim();
	}

	@Cacheable(value = "proclaims")
	public Collection<Proclaim> findAllByMember() {
		Member m;
		m = (Member) this.repository.findActorByUserAccount(LoginService.getPrincipal().getId());
		return this.repository.findAllByMember(m.getId());
	}

	@Cacheable(value = "proclaims")
	public Collection<Proclaim> findAllByStudent() {

		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT));
		Student m;
		m = (Student) this.repository.findActorByUserAccount(LoginService.getPrincipal().getId());

		return this.repository.findAllByStudent(m.getId());
	}

	public Proclaim create() {

		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT));

		this.interm = new TickerServiceInter<>();

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

		proclaim.setMembers(new ArrayList<Member>());
		proclaim.setAttachments("");
		proclaim.setStatus("SUBMITTED");

		StudentCard studentCard;
		studentCard = new StudentCard();
		studentCard.setCentre("");
		studentCard.setCode(0000);
		studentCard.setVat("");

		proclaim.setStudentCard(studentCard);

		this.interm.setRepository(this.repository);
		proclaim.setTicker(this.interm.create());

		return proclaim;
	}
	public boolean assign(final int id) {

		boolean res = false;

		Proclaim proclaim;
		proclaim = this.repository.findOne(id);

		Member h;
		h = (Member) this.repository.findActorByUserAccount(LoginService.getPrincipal().getId());

		Collection<Member> members;
		members = proclaim.getMembers();

		if (!members.contains(h)) {
			res = true;
			members.add(h);
			proclaim.setMembers(members);
		}

		return res;
	}
	@Cacheable(value = "proclaims")
	public Proclaim findOne(final int id) {
		Proclaim result;
		result = this.repository.findOne(id);

		this.inFinal = result.isFinalMode();

		if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
			Member m;
			m = (Member) this.repository.findActorByUserAccount(LoginService.getPrincipal().getId());
			Assert.isTrue(result.getMembers().contains(m));
		} else if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT)) {
			Student m;
			m = (Student) this.repository.findActorByUserAccount(LoginService.getPrincipal().getId());
			Assert.isTrue(result.getStudent().getId() == m.getId());
		}

		return result;
	}

	@CachePut(value = "proclaims", key = "#aux.id")
	public Proclaim save(final Proclaim aux) {
		this.interm = new TickerServiceInter<>();
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
				Assert.isTrue(this.inFinal == false);
				Assert.isTrue(aux.getStudent().getId() == m.getId());
			}

		if (aux.isFinalMode()) {
			if (aux.getStatus().equals("SUBMITTED"))
				aux.setStatus("PENDING");
			if (aux.getStatus().equals("ENVIADO"))
				aux.setStatus("PENDIENTE");
		}

		this.interm.setRepository(this.repository);

		result = this.interm.withTicker(aux);

		return result;
	}

	@CachePut(value = "proclaims", key = "#aux.id")
	public void delete(final int id) {
		Proclaim p;
		p = this.repository.findOne(id);

		Assert.isTrue(p.isFinalMode() == false);
		Assert.isTrue(p.getStudent().getId() == ((Student) this.repository.findActorByUserAccount(LoginService.getPrincipal().getId())).getId());

		this.repository.delete(p.getId());

	}

	public void save(final Collection<Proclaim> col) {
		this.repository.save(col);
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
				}

			if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER))
				if (aux.isClosed() == false) {
					result.setStatus(aux.getStatus());

					if (result.getStatus().equals("ACCEPTED") || result.getStatus().equals("ACEPTADO"))
						result.setLaw(aux.getLaw());
					if (result.getStatus().equals("REJECTED") || result.getStatus().equals("RECHAZADO"))
						result.setReason(aux.getReason());

					result.setClosed(false);
				} else if (aux.isClosed() != result.isClosed())
					result.setClosed(aux.isClosed());
		}

		boolean check;

		if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER) && !result.getStatus().equals("PENDING") && !result.getStatus().equals("PENDIENTE")) {
			if (result.getStatus().equals("ACCEPTED") || result.getStatus().equals("ACEPTADO")) {
				check = result.getLaw().isEmpty();
				if (check)
					binding.rejectValue("law", "proclaim.lawEmpty");
			}
			if (result.getStatus().equals("REJECTED") || result.getStatus().equals("RECHAZADO")) {
				check = result.getReason().isEmpty();
				if (check)
					binding.rejectValue("reason", "proclaim.reasonEmpty");
			}
		}

		this.validator.validate(result, binding);

		if (result.getCategory().getId() == 0 || result.getCategory() == null)
			binding.rejectValue("category", "category.wrong");

		StudentCard studentCard;
		studentCard = result.getStudentCard();

		if (studentCard.getCentre().equals("") || studentCard.getCentre().equals(" "))
			binding.rejectValue("studentCard.centre", "centre.wrong");

		if (String.valueOf(studentCard.getCode()).isEmpty() || String.valueOf(studentCard.getCode()).length() != 4)
			binding.rejectValue("studentCard.code", "code.wrong");

		if (studentCard.getCentre().equals("") || studentCard.getCentre().equals(" "))
			binding.rejectValue("studentCard.vat", "vat.wrong");

		if (binding.hasErrors()) {
			if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT))
				result.setFinalMode(false);
			else
				result.setFinalMode(true);

			throw new ValidationException();

		}

		return result;
	}

	public void delete(final Collection<Proclaim> col) {
		this.repository.delete(col);
	}
	public void flush() {
		this.repository.flush();
	}
}
