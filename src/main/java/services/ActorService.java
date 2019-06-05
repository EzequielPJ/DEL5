
package services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import repositories.CollaboratorRepository;
import repositories.MemberRepository;
import repositories.SponsorRepository;
import repositories.StudentRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Actor;
import domain.Administrator;
import domain.Box;
import domain.Collaborator;
import domain.Comission;
import domain.CreditCard;
import domain.Event;
import domain.Finder;
import domain.Member;
import domain.MessageEntity;
import domain.Portfolio;
import domain.Proclaim;
import domain.Sponsor;
import domain.Sponsorship;
import domain.Student;
import domain.StudentCard;
import domain.Swap;
import domain.Ticker;
import forms.ActorForm;

@Service
@Transactional
public class ActorService extends AbstractService {

	@Autowired
	private AdministratorRepository		adminRepository;
	@Autowired
	private FinderService				finderService;
	@Autowired
	private UserAccountRepository		userRepository;
	@Autowired
	private Validator					validator;
	@Autowired
	private BoxService					boxService;
	@Autowired
	private MemberRepository			memberRepository;
	@Autowired
	private StudentRepository			studentRepository;
	@Autowired
	private CollaboratorRepository		collaboratorRepository;
	@Autowired
	private SponsorRepository			sponsorRepository;
	@Autowired
	private SponsorshipService			sponsorshipService;
	@Autowired
	private StudyReportService			studyReportService;
	@Autowired
	private WorkReportService			workReportService;
	@Autowired
	private MiscellaneousReportService	miscReportService;

	@Autowired
	private ProfileService				profileService;
	@Autowired
	private MessageService				messService;
	@Autowired
	private PortfolioService			portService;
	@Autowired
	private ProclaimService				proclaimService;
	@Autowired
	private SwapService					swapService;
	@Autowired
	private ComissionService			commService;
	@Autowired
	private EventService				eventService;
	@Autowired
	UserAccountRepository				uaRepository;


	public Collection<Actor> getActorSpammer() {
		return this.adminRepository.getActorsSpammer();
	}

	public Collection<Actor> getActorEmabled() {
		return this.adminRepository.getActorsEnabled();
	}

	public Administrator findFirstAdmin() {
		return this.adminRepository.findFirstAdmin().get(0);
	}

	public Collection<Integer> getActorsIdSpammer() {
		return this.adminRepository.getActorsIdSpammer();
	}

	public Collection<Integer> getActorsIdEnabled() {
		return this.adminRepository.getActorsIdEnabled();
	}

	public Actor findByUserAccount(final int id) {

		Actor res = null;

		if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN))
			res = this.adminRepository.findAdminByUserAccountId(id);
		else if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER))
			res = this.memberRepository.findMemberByUserAccount(id);
		else if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT))
			res = this.studentRepository.findStudentByUserAccount(id);
		else if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR))
			res = this.collaboratorRepository.findCollaboratorByUserAccount(id);
		else
			res = this.sponsorRepository.findSponsorByUserAccount(id);
		return res;
	}

	public String hashPassword(final String old) {
		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		String passEncoded;
		passEncoded = encoder.encodePassword(old, null);

		return passEncoded;

	}
	public <T extends Actor> void setBasicProperties(final T actor, final String auth) {
		actor.setAddress("");
		actor.setBoxes(new ArrayList<Box>());
		actor.setEmail("");
		actor.setName("");
		actor.setPhone("");
		actor.setPhoto("");
		actor.setSurname("");
		actor.setSuspicious(false);
		actor.setAccount(this.userAccountAdapted("", "", auth));
	}

	public Actor createActor(final String auth) {
		Actor actor = null;

		if (auth.equals(Authority.ADMIN)) {
			Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));
			Administrator administrator;
			administrator = new Administrator();
			this.setBasicProperties(administrator, auth);
			actor = administrator;
		} else if (auth.equals(Authority.MEMBER)) {
			Member member;
			member = new Member();
			this.setBasicProperties(member, auth);
			actor = member;
		} else if (auth.equals(Authority.STUDENT)) {
			Student student;
			student = new Student();
			this.setBasicProperties(student, auth);
			actor = student;
		} else if (auth.equals(Authority.COLLABORATOR)) {
			Collaborator collaborator;
			collaborator = new Collaborator();
			this.setBasicProperties(collaborator, auth);
			actor = collaborator;

		} else {
			Sponsor sponsor;
			sponsor = new Sponsor();
			this.setBasicProperties(sponsor, auth);
			actor = sponsor;
		}
		return actor;

	}
	public UserAccount userAccountAdapted(final String username, final String password, final String auth) {

		UserAccount user;
		user = new UserAccount();
		user.setUsername(username);
		user.setPassword(password);
		user.setEnabled(true);

		Authority authority;
		authority = new Authority();
		authority.setAuthority(auth);

		Collection<Authority> authorities;
		authorities = new ArrayList<Authority>();
		authorities.add(authority);
		user.setAuthorities(authorities);

		return user;
	}

	public void setBoxes(final Actor actor) {
		final Collection<Box> boxes = this.boxService.save(super.initBoxes());
		actor.setBoxes(boxes);
	}

	public Actor save(final Administrator admin, final Member member, final Student student, final Collaborator collaborator, final Sponsor sponsor) {
		Actor saved = null;

		if (admin != null) {
			if (admin.getId() != 0)
				Assert.isTrue(LoginService.getPrincipal().getId() == admin.getAccount().getId());
			Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN));

			if (admin.getId() == 0) {
				UserAccount user;
				user = this.userRepository.save(admin.getAccount());
				admin.setAccount(user);
			}

			saved = this.adminRepository.save(admin);
		} else if (member != null) {
			if (member.getId() != 0)
				Assert.isTrue(LoginService.getPrincipal().getId() == member.getAccount().getId());

			if (member.getId() == 0) {
				UserAccount user;
				user = this.userRepository.save(member.getAccount());
				member.setAccount(user);
			}

			saved = this.memberRepository.save(member);
		} else if (student != null) {
			if (student.getId() != 0)
				Assert.isTrue(LoginService.getPrincipal().getId() == student.getAccount().getId());

			if (student.getId() == 0) {
				UserAccount user;
				user = this.userRepository.save(student.getAccount());
				student.setAccount(user);
			}

			saved = this.studentRepository.save(student);

		} else if (collaborator != null) {
			if (collaborator.getId() != 0)
				Assert.isTrue(LoginService.getPrincipal().getId() == collaborator.getAccount().getId());

			if (collaborator.getId() == 0) {
				UserAccount user;
				user = this.userRepository.save(collaborator.getAccount());
				collaborator.setAccount(user);
			}

			saved = this.collaboratorRepository.save(collaborator);
		} else if (sponsor != null) {
			if (sponsor.getId() != 0)
				Assert.isTrue(LoginService.getPrincipal().getId() == sponsor.getAccount().getId());

			if (sponsor.getId() == 0) {
				UserAccount user;
				user = this.userRepository.save(sponsor.getAccount());
				sponsor.setAccount(user);
			}

			saved = this.sponsorRepository.save(sponsor);
		}

		return saved;
	}
	public <T extends Actor> void setToActor(final T result, final ActorForm actor) {

		result.setName(actor.getName());
		result.setAddress(actor.getAdress());
		result.setPhone(actor.getPhone());
		result.setPhoto(actor.getPhoto());
		result.setSurname(actor.getSurname());
		result.setEmail(actor.getEmail());

		if (result.getAccount().getId() == 0) {
			if (actor.getAccount().getPassword().equals(actor.getPassword2()))
				result.setAccount(this.userAccountAdapted(actor.getAccount().getUsername(), this.hashPassword(actor.getAccount().getPassword()), actor.getAuthority()));
		} else {
			UserAccount ua;
			ua = this.userRepository.findOne(result.getAccount().getId());
			if (actor.getAccount().getPassword().equals(actor.getPassword2())) {
				if (!ua.getUsername().equals(actor.getAccount().getUsername()))
					ua.setUsername(actor.getAccount().getUsername());
				ua.setPassword(this.hashPassword(actor.getAccount().getPassword()));
			}
		}

	}
	public ActorForm map(final Actor a, String auth) {

		ActorForm form;
		form = new ActorForm();

		if (auth == null) {
			auth = LoginService.getPrincipal().getAuthorities().toString();
			auth = auth.substring(1, auth.toString().length() - 1);
		}
		if (a.getId() == 0) {

			form.setAdress("");
			form.setAuthority(auth);
			form.setEmail("");

			form.setAccount(this.userAccountAdapted("", "", auth));

			form.setPassword2("");
			form.setPhone("");
			form.setSurname("");
			form.setPhoto("");
			form.setName("");

		} else {
			form.setId(a.getId());
			form.setAdress(a.getAddress());
			form.setAuthority(auth);
			form.setEmail(a.getEmail());
			form.setSurname(a.getSurname());
			form.setPassword2("");
			form.setPhone(a.getPhone());
			form.setAccount(this.userAccountAdapted(a.getAccount().getUsername(), "", auth));
			form.setPhoto(a.getPhoto());
			form.setName(a.getName());
			form.setTerms(true);
		}

		return form;
	}

	public Administrator reconstructAdministrator(final ActorForm actor, final BindingResult binding) {
		Administrator result = null;

		if (actor.getId() == 0) {
			result = (Administrator) this.createActor(Authority.ADMIN);
			this.setToActor(result, actor);
			this.validator.validate(result, binding);
			if (!binding.hasErrors())
				this.setBoxes(result);
		} else {
			result = this.adminRepository.findOne(actor.getId());
			this.setToActor(result, actor);
			this.validator.validate(result, binding);
		}

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}
	//RECONSTRUCT

	public Member reconstructMember(final ActorForm actor, final BindingResult binding) {
		Member result = null;

		if (actor.getId() == 0) {
			result = (Member) this.createActor(Authority.MEMBER);
			this.setToActor(result, actor);
			this.validator.validate(result, binding);
			if (!binding.hasErrors())
				this.setBoxes(result);
			Finder finder;
			finder = this.finderService.save(this.finderService.create(), new ArrayList<Proclaim>());

			result.setFinder(finder);

		} else {
			result = this.memberRepository.findOne(actor.getId());
			this.setToActor(result, actor);
			this.validator.validate(result, binding);
		}
		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public Student reconstructStudent(final ActorForm actor, final BindingResult binding) {
		Student result = null;

		if (actor.getId() == 0) {
			result = (Student) this.createActor(Authority.STUDENT);
			this.setToActor(result, actor);
			this.validator.validate(result, binding);
			if (!binding.hasErrors())
				this.setBoxes(result);
		} else {
			result = this.studentRepository.findOne(actor.getId());
			this.setToActor(result, actor);
			this.validator.validate(result, binding);
		}

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public Collaborator reconstructCollaborator(final ActorForm actor, final BindingResult binding) {
		Collaborator result = null;

		if (actor.getId() == 0) {
			result = (Collaborator) this.createActor(Authority.COLLABORATOR);
			this.setToActor(result, actor);
			this.validator.validate(result, binding);
			if (!binding.hasErrors())
				this.setBoxes(result);
		} else {
			result = this.collaboratorRepository.findOne(actor.getId());
			this.setToActor(result, actor);
			this.validator.validate(result, binding);
		}

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public Sponsor reconstructSponsor(final ActorForm actor, final BindingResult binding) {
		Sponsor result = null;

		if (actor.getId() == 0) {
			result = (Sponsor) this.createActor(Authority.SPONSOR);
			this.setToActor(result, actor);
			this.validator.validate(result, binding);
			if (!binding.hasErrors())
				this.setBoxes(result);
		} else {
			result = this.sponsorRepository.findOne(actor.getId());
			this.setToActor(result, actor);
			this.validator.validate(result, binding);
		}

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}
	public void delete(final int actorId) {
		Assert.notNull(LoginService.getPrincipal().getUsername());
		Administrator admin;
		Student student;
		Member member;
		Sponsor sponsor;
		Collaborator collaborator;

		if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN)) {
			admin = this.adminRepository.findAdminByUserAccountId(actorId);
			Assert.isTrue(LoginService.getPrincipal().getId() == actorId, "Delete not allowed");
			admin = (Administrator) this.deleteCommon(admin);
			this.adminRepository.save(admin);
		} else if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
			member = this.memberRepository.findMemberByUserAccount(actorId);
			Assert.isTrue(LoginService.getPrincipal().getId() == actorId, "Delete not allowed");
			this.anonimizeProclaimsByMember();
			this.anonimiceComissions(member);
			member = (Member) this.deleteCommon(member);
			//this.finderService.delete(member.getFinder());
			//			member.setFinder(this.finderService.create());

			this.memberRepository.save(member);
		} else if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT)) {
			student = this.studentRepository.findStudentByUserAccount(actorId);
			Assert.isTrue(LoginService.getPrincipal().getId() == actorId, "Delete not allowed");
			this.anonimiceProclaims();
			student = (Student) this.deleteCommon(student);
			this.studentRepository.save(student);
		} else if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.SPONSOR)) {
			sponsor = this.sponsorRepository.findSponsorByUserAccount(actorId);
			Assert.isTrue(LoginService.getPrincipal().getId() == actorId, "Delete not allowed");
			this.anonimiceSponsorships(sponsor);
			sponsor = (Sponsor) this.deleteCommon(sponsor);
			this.sponsorRepository.save(sponsor);
		} else if (super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR)) {
			collaborator = this.collaboratorRepository.findCollaboratorByUserAccount(actorId);
			Assert.isTrue(LoginService.getPrincipal().getId() == actorId, "Delete not allowed");
			this.deleteCommon(collaborator);
			this.anonimiceEvent(collaborator);
			this.anonimiceSwap(collaborator);
			this.deletePortfolio(collaborator);
			this.collaboratorRepository.save(collaborator);
		}
	}

	private void anonimiceComissions(final Member m) {
		Collection<Comission> comms;
		comms = this.commService.getComissionsByMemberId(m.getId());
		for (final Comission c : comms) {
			c.setDescription("loremIpsum");
			c.setFinalMode(false);
			c.setMoment(new Date());
			c.setName("loremIpsum");
		}
	}
	private void anonimiceSwap(final Collaborator c) {
		Collection<Swap> swaps;
		swaps = this.swapService.getSwapsByCollaboratorId(c.getId());
		swaps.addAll(this.swapService.getSwapsPendingByCollaboratorId(c.getId()));
		for (final Swap s : swaps) {
			s.setDescription("loremIpsum");
			s.setPhone("loremIpsum");
			s.setStatus("loremIpsum");
		}
		this.swapService.save(swaps);
	}
	private void anonimiceEvent(final Collaborator c) {
		Collection<Event> events;
		events = this.eventService.getEventsByCollaboratorId(c.getId());
		for (final Event e : events) {
			e.setDescription("loremIpsum");
			e.setFinalMode(false);
			e.setMoment(new Date());
			e.setStatus("loremIpsum");
			e.setTitle("loremIpsum");
		}
		this.eventService.save(events);
	}

	private void anonimiceProclaims() {
		Collection<Proclaim> col;
		col = this.proclaimService.findAllByStudent();
		for (final Proclaim p : col) {
			p.setMembers(new ArrayList<Member>());
			p.setCategory(null);
			p.setTicker(this.fakeTicker());
			p.setAttachments("loremIpsum");
			p.setClosed(true);
			p.setDescription("loremIpsum");
			p.setFinalMode(false);
			p.setLaw("loremIpsum");
			p.setMoment(new Date());
			p.setReason("loremIpsum");
			p.setStatus("loremIpsum");
			p.setStudentCard(this.fakeStudentCard());
			p.setTitle("loremIpsum");
		}
		this.proclaimService.save(col);
	}
	private void anonimizeProclaimsByMember() {
		Collection<Proclaim> col;
		col = this.proclaimService.findAllByMember();
		for (final Proclaim p : col)
			p.setMembers(new ArrayList<Member>());
	}
	private void deleteProfile(final Actor a) {
		this.profileService.delete(this.profileService.getProfilesByActorId(a.getId()));
	}

	private void deleteMessage(final Actor a) {
		Collection<MessageEntity> mess;
		mess = this.messService.getMessagesByActor(a.getId());
		for (final MessageEntity m : mess)
			this.messService.deleteMessage(m);
	}
	private void deleteBox(final Actor a) {
		this.boxService.delete(a.getBoxes());
		a.setBoxes(new ArrayList<Box>());
	}

	private CreditCard fakeCreditCard() {
		CreditCard c;
		c = new CreditCard();
		c.setCvv(999);
		c.setNumber("0000000000000000");
		c.setHolder("loremIpsum");
		c.setMake("loremIpsum");
		c.setExpiration(new Date());
		return c;
	}

	private StudentCard fakeStudentCard() {
		StudentCard sc;
		sc = new StudentCard();
		sc.setCentre("loremIpsum");
		sc.setCode(0000);
		sc.setVat("loremIpsum");
		return sc;
	}

	private void deletePortfolio(final Collaborator c) {
		Collection<Portfolio> p;
		p = this.portService.findPortfolioByActor(c.getAccount().getId());
		for (final Portfolio port : p) {
			this.studyReportService.delete(port.getStudyReport());
			this.workReportService.delete(port.getWorkReport());
			this.miscReportService.delete(port.getMiscellaneousReport());
		}
		this.portService.delete(p);
		c.setPortfolio(null);
	}

	private String randomice() {
		SecureRandom random;
		random = new SecureRandom();
		return String.valueOf(random.nextLong());
	}
	private Actor deleteCommon(final Actor a) {
		Authority aut;
		aut = new Authority();
		aut.setAuthority(this.selectAuthority(a));
		//Collection<Authority> auts;
		//auts = new ArrayList<>();
		//		auts.add(aut);
		a.getAccount().setEnabled(false);
		//a.setAccount(this.userAccountAdapted(this.randomice(), this.randomice(), Authority.ANONYMOUS));
		//		a.getAccount().setUsername(this.randomice());
		//		a.getAccount().setPassword(this.randomice());
		//		a.getAccount().setAuthorities(auts);
		a.setName("loremipsum");
		a.setPhone("loremipsum");
		a.setSurname("loremipsum");
		a.setPhone("000000000");
		a.setEmail("loremipsum@email.loremipsum");
		a.setPhoto("http://loremipsum.loremipsum");

		this.deleteProfile(a);
		this.deleteMessage(a);
		this.deleteBox(a);
		UserAccount ua;
		ua = this.uaRepository.findByUsername("anonymous");
		a.getAccount().removeAuthority(aut);
		this.uaRepository.delete(a.getAccount());
		a.setAccount(ua);
		return a;
	}
	private Ticker fakeTicker() {
		Ticker t;
		t = new Ticker();
		t.setTicker("00000000" + this.randomice().substring(0, 5));
		return t;
	}

	private void anonimiceSponsorships(final Sponsor p) {
		Collection<Sponsorship> sponsorships;
		sponsorships = this.sponsorshipService.getSponsorshipActiveBySponsorId(p.getId());
		sponsorships.addAll(this.sponsorshipService.getSponsorshipDesactiveBySponsorId(p.getId()));
		for (final Sponsorship s : sponsorships) {
			s.setBanner("http://lorem.ipsum");
			s.setIsActive(false);
			s.setTarget("http://lorem.ipsum");
			s.setCreditCard(this.fakeCreditCard());
		}
		this.sponsorshipService.save(sponsorships);
	}

	public void flushAdministrator() {
		this.adminRepository.flush();
	}
	public void flushMember() {
		this.memberRepository.flush();
	}
	public void flushStudent() {
		this.studentRepository.flush();
	}
	public void flushCollaborator() {
		this.collaboratorRepository.flush();
	}
	public void flushSponsor() {
		this.sponsorRepository.flush();
	}

	private String selectAuthority(final Actor a) {
		Collection<Authority> auts;
		auts = a.getAccount().getAuthorities();
		String res;
		res = "";
		for (final Authority auth : auts)
			if (auth.getAuthority().equals(Authority.ADMIN))
				res = Authority.ADMIN;
			else if (auth.getAuthority().equals(Authority.COLLABORATOR))
				res = Authority.COLLABORATOR;
			else if (auth.getAuthority().equals(Authority.MEMBER))
				res = Authority.MEMBER;
			else if (auth.getAuthority().equals(Authority.SPONSOR))
				res = Authority.SPONSOR;
			else if (auth.getAuthority().equals(Authority.STUDENT))
				res = Authority.STUDENT;
		return res;
	}
}
