
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.EventService;
import services.SponsorshipService;
import domain.Sponsor;
import domain.Sponsorship;

@Controller
@RequestMapping("/sponsorship/sponsor")
public class SponsorshipController extends BasicController {

	@Autowired
	private SponsorshipService	serviceSponsorship;

	@Autowired
	private EventService		serviceEvent;

	@Autowired
	private ActorService		serviceActor;


	@RequestMapping(value = "/listSponsorship", method = RequestMethod.GET)
	public ModelAndView listSponsorship() {
		UserAccount user;
		user = LoginService.getPrincipal();
		Sponsor p;
		p = (Sponsor) this.serviceActor.findByUserAccount(user.getId());
		ModelAndView result;
		result = super.listModelAndView("sponsorships", "sponsorship/list", this.serviceSponsorship.getSponsorshipActiveBySponsorId(p.getId()), "sponsorship/sponsor/listSponsorship.do");
		result.addObject("isActive", true);
		result.addObject("isNotActive", false);
		return result;
	}

	@RequestMapping(value = "/listDesactive", method = RequestMethod.GET)
	public ModelAndView listSponsorshipDes() {
		UserAccount user;
		user = LoginService.getPrincipal();
		Sponsor p;
		p = (Sponsor) this.serviceActor.findByUserAccount(user.getId());
		ModelAndView result;
		result = super.listModelAndView("sponsorships", "sponsorship/list", this.serviceSponsorship.getSponsorshipDesactiveBySponsorId(p.getId()), "sponsorship/sponsor/listDesactive.do");
		result.addObject("isActive", false);
		result.addObject("isNotActive", true);
		return result;
	}

	@RequestMapping(value = "/reactive", method = RequestMethod.GET)
	public ModelAndView reactiveSponsorship(@RequestParam final int idSponsorship) {
		ModelAndView result;
		Sponsorship s;
		s = this.serviceSponsorship.findOne(idSponsorship);
		Assert.isTrue(this.serviceActor.findByUserAccount(LoginService.getPrincipal().getId()).equals(s.getSponsor()), "You only can reactive your sponsorships");
		this.serviceSponsorship.reactivate(idSponsorship);
		result = new ModelAndView("redirect:listSponsorship.do");
		return result;
	}

	@RequestMapping(value = "/desactive", method = RequestMethod.GET)
	public ModelAndView desactiveSponsorship(@RequestParam final int idSponsorship) {
		ModelAndView result;
		Sponsorship s;
		s = this.serviceSponsorship.findOne(idSponsorship);
		Assert.isTrue(this.serviceActor.findByUserAccount(LoginService.getPrincipal().getId()).equals(s.getSponsor()), "You only can desactive your sponsorships");
		this.serviceSponsorship.desactive(idSponsorship);
		result = new ModelAndView("redirect:listDesactive.do");
		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int idSponsorship) {
		ModelAndView result;
		Sponsorship s;
		s = this.serviceSponsorship.findOne(idSponsorship);
		Assert.isTrue(this.serviceActor.findByUserAccount(LoginService.getPrincipal().getId()).equals(s.getSponsor()), "You only can view your sponsorships");
		result = super.show(this.serviceSponsorship.findOne(idSponsorship), "sponsorship/edit", "sponsorship/sponsor/edit.do", "/sponsorship/sponsor/listSponsorship.do");
		result.addObject("view2", true);
		result.addObject("reactive", false);
		result.addObject("makes", super.creditCardMakes());
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int idEvent) {
		ModelAndView result;
		UserAccount user;
		user = LoginService.getPrincipal();
		Sponsor p;
		p = (Sponsor) this.serviceActor.findByUserAccount(user.getId());
		Assert.isTrue(this.serviceSponsorship.getSponsorshipByEventId(p.getId(), idEvent).size() < 1, "You don't have permission to do this because you've already created a sponsorship before");
		result = super.create(this.serviceSponsorship.createSponsorship(p, this.serviceEvent.findOne(idEvent)), "sponsorship/edit", "sponsorship/sponsor/edit.do", "sponsorship/sponsor/listSponsorship.do");
		result.addObject("makes", super.creditCardMakes());
		return result;
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView update(@RequestParam final int idSponsorship) {
		Sponsor p;
		p = (Sponsor) this.serviceActor.findByUserAccount(LoginService.getPrincipal().getId());

		Sponsorship s;
		s = this.serviceSponsorship.findOne(idSponsorship);

		Collection<Sponsorship> col;
		col = this.serviceSponsorship.getSponsorshipByEventId(p.getId(), s.getEvent().getId());

		Assert.isTrue(col.contains(s), "You don't have permission to do this");

		ModelAndView result;
		result = super.edit(s, "sponsorship/edit", "sponsorship/sponsor/edit.do", "/sponsorship/sponsor/listSponsorship.do");
		result.addObject("makes", super.creditCardMakes());
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEntity(final Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;
		Collection<String> col;
		col = new ArrayList<>();
		col.add(sponsorship.getTarget());
		col.add(sponsorship.getBanner());

		if (!super.luhnAlgorithm(sponsorship.getCreditCard().getNumber()) || sponsorship.getCreditCard().getMake().equals("0")) {
			result = super.createAndEditModelAndView(sponsorship, "sponsorship.creditCard", "sponsorship/edit", "sponsorship/sponsor/edit.do", "/sponsorship/sponsor/listSponsorship.do");
			result.addObject("makes", super.creditCardMakes());
		} else if (sponsorship.getCreditCard().getExpiration().before(new Date()) || sponsorship.getCreditCard().getMake().equals("0")) {
			result = super.createAndEditModelAndView(sponsorship, "sponsorship.invalid.expiration", "sponsorship/edit", "sponsorship/sponsor/edit.do", "/sponsorship/sponsor/listSponsorship.do");
			result.addObject("makes", super.creditCardMakes());
		} else if (!super.checkURL(col, true)) {
			result = super.createAndEditModelAndView(sponsorship, "bad.url", "sponsorship/edit", "sponsorship/sponsor/edit.do", "/sponsorship/sponsor/listSponsorship.do");
			result.addObject("makes", super.creditCardMakes());
		} else {
			result = super.save(sponsorship, binding, "sponsorship.commit.error", "sponsorship/edit", "sponsorship/sponsor/edit.do", "/sponsorship/sponsor/listSponsorship.do", "redirect:listSponsorship.do");
			result.addObject("makes", super.creditCardMakes());
		}
		return result;
	}
	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		ModelAndView result;
		Sponsorship sponsorship;
		sponsorship = (Sponsorship) e;
		sponsorship = this.serviceSponsorship.reconstruct(sponsorship, binding, sponsorship.getEvent().getId());
		this.serviceSponsorship.save(sponsorship);
		result = new ModelAndView(nameResolver);
		return result;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		// TODO Auto-generated method stub
		return null;
	}

}
