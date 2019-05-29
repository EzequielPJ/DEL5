
package services;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Event;
import domain.Sponsor;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService extends AbstractService {

	@Autowired
	SponsorshipRepository	sponsorshipRepository;

	@Autowired
	ActorService			actorService;

	@Autowired
	EventService			eventService;

	@Autowired
	private Validator		validator;


	public Collection<Sponsorship> findAll() {
		return this.sponsorshipRepository.findAll();
	}

	public Sponsorship findOne(final int id) {
		return this.sponsorshipRepository.findOne(id);
	}

	public Collection<Event> findEventWithSponsorshipId(final int id) {
		return this.sponsorshipRepository.findEventWithSponsorshipId(id);
	}

	public Collection<Sponsorship> getSponsorshipActiveBySponsorId(final int idSponsor) {
		return this.sponsorshipRepository.getSponsorshipActiveBySponsorId(idSponsor);
	}

	public Collection<Sponsorship> getSponsorshipDesactiveBySponsorId(final int idSponsor) {
		return this.sponsorshipRepository.getSponsorshipDesactiveBySponsorId(idSponsor);
	}

	public Collection<Sponsorship> getSponsorshipByEventId(final int idSponsor, final int idEvent) {
		return this.sponsorshipRepository.getSponsorshipByEventId(idSponsor, idEvent);
	}

	public Collection<Sponsorship> getSponsorshipByEventId(final int idEvent) {
		return this.sponsorshipRepository.getSponsorshipByEventId(idEvent);
	}

	public void save(final Collection<Sponsorship> col) {
		this.sponsorshipRepository.save(col);
	}

	public Sponsorship createSponsorship(final Sponsor sponsor, final Event event) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.SPONSOR));
		Sponsorship result;
		result = new Sponsorship();
		Assert.isTrue(this.sponsorshipRepository.getSponsorshipByEventId(sponsor.getId(), event.getId()).size() < 1, "You don't have permission to do this because you've already created a sponsorship before");
		result.setBanner("");
		result.setCreditCard(null);
		result.setTarget("");
		result.setEvent(event);
		result.setSponsor(sponsor);
		result.setIsActive(true);

		return result;
	}

	public Sponsorship save(final Sponsorship sponsorship) {

		Sponsor logged;
		logged = (Sponsor) this.actorService.findByUserAccount((LoginService.getPrincipal().getId()));

		Assert.notNull(logged);

		if (sponsorship.getId() != 0)
			Assert.isTrue(this.getSponsorshipActiveBySponsorId(logged.getId()).contains(sponsorship), "You don't have access to do this");
		else
			Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.SPONSOR));

		Sponsorship saved;

		saved = this.sponsorshipRepository.save(sponsorship);
		Collection<Sponsorship> actorSponshorships;
		actorSponshorships = this.getSponsorshipActiveBySponsorId(logged.getId());

		if (!(actorSponshorships.contains(saved))) {
			actorSponshorships.add(saved);
			saved.setSponsor(logged);
		}

		return saved;
	}

	public void desactive(final int idSponsorship) {
		final Sponsorship desactive;
		desactive = this.sponsorshipRepository.findOne(idSponsorship);
		final Sponsor p;
		p = (Sponsor) this.actorService.findByUserAccount((LoginService.getPrincipal().getId()));
		Assert.isTrue(this.getSponsorshipActiveBySponsorId(p.getId()).contains(desactive), "You don't have access to do this");

		desactive.setIsActive(false);

	}

	public void reactivate(final int idSponsorship) {
		final Sponsorship reactivate;
		final Sponsor p;
		p = (Sponsor) this.actorService.findByUserAccount((LoginService.getPrincipal().getId()));
		reactivate = this.sponsorshipRepository.findOne(idSponsorship);
		Assert.isTrue(this.getSponsorshipDesactiveBySponsorId(p.getId()).contains(reactivate), "You don't have access to do this");

		reactivate.setIsActive(true);
	}

	public Sponsorship reconstruct(final Sponsorship s, final BindingResult binding, final int eventId) {
		Sponsorship result;
		UserAccount user;
		user = LoginService.getPrincipal();
		Sponsor p;
		p = (Sponsor) this.actorService.findByUserAccount(user.getId());
		if (s.getId() == 0) {
			result = s;
			result.setSponsor(p);
			result.setIsActive(true);
			if ((s.getCreditCard().getMake().equals("0")))
				throw new ValidationException();
			else {
				result.setSponsor((Sponsor) this.actorService.findByUserAccount((LoginService.getPrincipal().getId())));
				result.setIsActive(s.getIsActive());
				result.setEvent(this.eventService.findOne(eventId));
				this.validator.validate(result, binding);
			}

		} else {
			result = this.sponsorshipRepository.findOne(s.getId());
			result.setTarget(s.getTarget());
			result.setBanner(s.getBanner());
			result.setCreditCard(s.getCreditCard());
			this.validator.validate(result, binding);
		}
		if (binding.hasErrors())
			throw new ValidationException();
		return result;
	}

	public void flush() {
		this.sponsorshipRepository.flush();
	}
}
