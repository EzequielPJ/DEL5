
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
	private Validator		validator;


	public Collection<Sponsorship> findAll() {
		return this.sponsorshipRepository.findAll();
	}

	public Sponsorship findOne(final int id) {
		return this.sponsorshipRepository.findOne(id);
	}

	//	public Double getFlatRate() {
	//		return this.sponsorshipRepository.getFlatRate();
	//	}

	//	public Double getVat() {
	//		return this.sponsorshipRepository.getVat();
	//	}

	public Collection<Sponsorship> getSponsorshipBySponsorId(final int idProvider) {
		return this.sponsorshipRepository.getSponsorshipBySponsorId(idProvider);
	}

	public Sponsorship createSponsorship(final Sponsor sponsor, final Event event) {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.SPONSOR));
		Sponsorship result;
		result = new Sponsorship();

		result.setBanner("");
		result.setCreditCard(null/* provider.getCreditCard() */);
		result.setTarget("");
		//			result.setFlat_rate(0.0);
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
			Assert.isTrue(this.getSponsorshipBySponsorId(logged.getId()).contains(sponsorship), "You don't have access to do this");
		//		else
		//			Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.PROVIDER));

		Sponsorship saved;

		saved = this.sponsorshipRepository.save(sponsorship);
		Collection<Sponsorship> actorSponshorships;
		actorSponshorships = this.getSponsorshipBySponsorId(logged.getId());

		if (!(actorSponshorships.contains(saved))) {
			actorSponshorships.add(saved);
			saved.setSponsor(logged);
		}

		return saved;
	}

	public void desactive(final int idSponsorship) {
		final Sponsorship desactive;
		desactive = this.sponsorshipRepository.findOne(idSponsorship);
		final Sponsor s;
		s = (Sponsor) this.actorService.findByUserAccount((LoginService.getPrincipal().getId()));
		Assert.isTrue(this.getSponsorshipBySponsorId(s.getId()).contains(desactive), "You don't have access to do this");

		desactive.setIsActive(false);

	}

	public void reactivate(final int idSponsorship) {
		final Sponsorship reactivate;
		final Sponsor s;
		s = (Sponsor) this.actorService.findByUserAccount((LoginService.getPrincipal().getId()));
		reactivate = this.sponsorshipRepository.findOne(idSponsorship);
		Assert.isTrue(this.getSponsorshipBySponsorId(s.getId()).contains(reactivate), "You don't have access to do this");
		reactivate.setIsActive(true);
	}

	public Sponsorship reconstruct(final Sponsorship s, final BindingResult binding, final int positionId) {
		Sponsorship result;
		if (s.getId() == 0) {
			result = s;
			if ((s.getCreditCard().getMake().equals("0")))
				throw new ValidationException();
			else {
				result.setSponsor((Sponsor) this.actorService.findByUserAccount((LoginService.getPrincipal().getId())));
				result.setIsActive(s.getIsActive());
				//				result.setPosition(this.positionService.findOne(positionId));
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
