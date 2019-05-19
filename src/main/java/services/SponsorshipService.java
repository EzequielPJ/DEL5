
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
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

	//	public Collection<Sponsorship> getSponsorshipByProviderId(final int idProvider) {
	//		return this.sponsorshipRepository.getSponsorshipByProviderId(idProvider);
	//	}

	//	public Sponsorship createSponsorship(final Provider provider, final Position position) {
	//		//		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.PROVIDER));
	//		Sponsorship result;
	//		result = new Sponsorship();
	//
	//		result.setBanner("");
	//		result.setCreditCard(provider.getCreditCard());
	//		result.setTarget("");
	//		result.setFlat_rate(0.0);
	//		result.setPosition(position);
	//		result.setProvider(provider);
	//		result.setIsActive(true);
	//
	//		return result;
	//	}

	//	public Sponsorship save(final Sponsorship sponsorship) {
	//
	//		Provider logged;
	//		logged = (Provider) this.actorService.findByUserAccount((LoginService.getPrincipal().getId()));
	//
	//		Assert.notNull(logged);
	//
	//		if (sponsorship.getId() != 0)
	//			Assert.isTrue(this.getSponsorshipByProviderId(logged.getId()).contains(sponsorship), "You don't have access to do this");
	//		//		else
	//		//			Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.PROVIDER));
	//
	//		Sponsorship saved;
	//
	//		saved = this.sponsorshipRepository.save(sponsorship);
	//		Collection<Sponsorship> actorSponshorships;
	//		actorSponshorships = this.getSponsorshipByProviderId(logged.getId());
	//
	//		if (!(actorSponshorships.contains(saved))) {
	//			actorSponshorships.add(saved);
	//			saved.setProvider(logged);
	//		}
	//
	//		return saved;
	//	}
	//
	//	public void desactive(final int idSponsorship) {
	//		final Sponsorship desactive;
	//		desactive = this.sponsorshipRepository.findOne(idSponsorship);
	//		final Provider p;
	//		p = (Provider) this.actorService.findByUserAccount((LoginService.getPrincipal().getId()));
	//		Assert.isTrue(this.getSponsorshipByProviderId(p.getId()).contains(desactive), "You don't have access to do this");
	//
	//		desactive.setIsActive(false);
	//
	//	}
	//
	//	public void reactivate(final int idSponsorship) {
	//		final Sponsorship reactivate;
	//		final Provider p;
	//		p = (Provider) this.actorService.findByUserAccount((LoginService.getPrincipal().getId()));
	//		reactivate = this.sponsorshipRepository.findOne(idSponsorship);
	//		Assert.isTrue(this.getSponsorshipByProviderId(p.getId()).contains(reactivate), "You don't have access to do this");
	//
	//		reactivate.setIsActive(true);
	//	}
	//
	//	public Sponsorship reconstruct(final Sponsorship s, final BindingResult binding, final int positionId) {
	//		Sponsorship result;
	//		if (s.getId() == 0) {
	//			result = s;
	//			if ((s.getCreditCard().getMake().equals("0")))
	//				throw new ValidationException();
	//			else {
	//				result.setProvider((Provider) this.actorService.findByUserAccount((LoginService.getPrincipal().getId())));
	//				result.setIsActive(s.getIsActive());
	//				//				result.setPosition(this.positionService.findOne(positionId));
	//				this.validator.validate(result, binding);
	//			}
	//
	//		} else {
	//			result = this.sponsorshipRepository.findOne(s.getId());
	//			result.setTarget(s.getTarget());
	//			result.setBanner(s.getBanner());
	//			result.setCreditCard(s.getCreditCard());
	//			this.validator.validate(result, binding);
	//		}
	//		if (binding.hasErrors())
	//			throw new ValidationException();
	//		return result;
	//	}

	public void flush() {
		this.sponsorshipRepository.flush();
	}
}
