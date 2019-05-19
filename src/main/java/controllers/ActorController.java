
package controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import services.ActorService;
import services.CustomisationSystemService;
import domain.Actor;
import domain.Administrator;
import forms.ActorForm;

@Controller
@RequestMapping(value = "/actor")
public class ActorController extends BasicController {

	@Autowired
	private ActorService				actorService;

	@Autowired
	private CustomisationSystemService	customService;


	@RequestMapping(value = "/listSpammers", method = RequestMethod.GET)
	public ModelAndView listSpammers() {
		ModelAndView result;
		result = super.listModelAndView("actors", "actor/list", this.actorService.getActorSpammer(), "actor/list.do");
		result.addObject("ban", true);
		return result;
	}

	@RequestMapping(value = "/listBan", method = RequestMethod.GET)
	public ModelAndView listBan() {
		ModelAndView result;
		result = super.listModelAndView("actors", "actor/list", this.actorService.getActorEmabled(), "actor/list.do");
		result.addObject("ban", false);
		return result;
	}

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView banActor(@RequestParam final int id) {
		ModelAndView result;
		this.customService.banActor(id);
		result = new ModelAndView("redirect:listSpammers.do");
		result.addObject("ban", true);
		return result;
	}

	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	public ModelAndView unBanActor(@RequestParam final int id) {
		ModelAndView result;
		this.customService.unBanActor(id);
		result = new ModelAndView("redirect:listBan.do");
		result.addObject("ban", false);
		return result;
	}

	@RequestMapping(value = "/createAdmin", method = RequestMethod.GET)
	public ModelAndView createAdmin() {
		ModelAndView model;
		model = super.create(this.actorService.map(this.actorService.createActor(Authority.ADMIN), Authority.ADMIN), "actor/edit", "actor/edit.do", "redirect:../welcome.do");
		model.addObject("authority", Authority.ADMIN);
		model.addObject("view", false);
		model.addObject("makes", super.creditCardMakes());
		model.addObject("prefix", System.getProperty("phonePrefix"));
		return model;

	}

	//	@RequestMapping(value = "/createRookie", method = RequestMethod.GET)
	//	public ModelAndView createRookie() {
	//		ModelAndView model;
	//		final ActorForm a = this.actorService.map(this.actorService.createActor(Authority.ROOKIE), Authority.ROOKIE);
	//		model = super.create(a, "actor/edit", "actor/edit.do", "redirect:../welcome.do");
	//		model.addObject("authority", Authority.ROOKIE);
	//		model.addObject("view", false);
	//		model.addObject("makes", super.creditCardMakes());
	//		model.addObject("prefix", System.getProperty("phonePrefix"));
	//		return model;
	//	}
	//
	//	@RequestMapping(value = "/createCompany", method = RequestMethod.GET)
	//	public ModelAndView createCompany() {
	//		ModelAndView model;
	//		model = super.create(this.actorService.map(this.actorService.createActor(Authority.COMPANY), Authority.COMPANY), "actor/edit", "actor/edit.do", "redirect:../welcome.do");
	//		model.addObject("authority", Authority.COMPANY);
	//		model.addObject("view", false);
	//		model.addObject("makes", super.creditCardMakes());
	//		model.addObject("prefix", System.getProperty("phonePrefix"));
	//		return model;
	//	}
	//	@RequestMapping(value = "/createProvider", method = RequestMethod.GET)
	//	public ModelAndView createProvider() {
	//		ModelAndView model;
	//		final ActorForm a = this.actorService.map(this.actorService.createActor(Authority.PROVIDER), Authority.PROVIDER);
	//		model = super.create(a, "actor/edit", "actor/edit.do", "redirect:../welcome.do");
	//		model.addObject("authority", Authority.PROVIDER);
	//		model.addObject("view", false);
	//		model.addObject("makes", super.creditCardMakes());
	//		model.addObject("prefix", System.getProperty("phonePrefix"));
	//		return model;
	//	}
	//	@RequestMapping(value = "/createAuditor", method = RequestMethod.GET)
	//	public ModelAndView createAuditor() {
	//		ModelAndView model;
	//		final ActorForm a = this.actorService.map(this.actorService.createActor(Authority.AUDITOR), Authority.AUDITOR);
	//		model = super.create(a, "actor/edit", "actor/edit.do", "redirect:../welcome.do");
	//		model.addObject("authority", Authority.AUDITOR);
	//		model.addObject("view", false);
	//		model.addObject("makes", super.creditCardMakes());
	//		model.addObject("prefix", System.getProperty("phonePrefix"));
	//		return model;
	//	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEntity(@ModelAttribute("actor") final ActorForm actor, final BindingResult binding) {
		ModelAndView result;
		result = super.save(actor, binding, "actor.commit.error", "actor/edit", "actor/edit.do", "/actor/list.do", "redirect:../welcome.do");
		result.addObject("makes", super.creditCardMakes());
		result.addObject("authority", actor.getAuthority());
		result.addObject("view", false);
		result.addObject("prefix", System.getProperty("phonePrefix"));
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteEntity(@RequestParam final int id) {
		return super.delete(this.actorService.findByUserAccount(LoginService.getPrincipal().getId()), "actor.commit.error", "actor/edit", "j_spring_security_logout", "redirect:../welcome.do", "redirect:../j_spring_security_logout");

	}

	@RequestMapping(value = "/personal", method = RequestMethod.GET)
	public ModelAndView editPersonalData() {
		ModelAndView model;
		final Actor ac = this.actorService.findByUserAccount(LoginService.getPrincipal().getId());
		final ActorForm form = this.actorService.map(ac, null);
		model = super.edit(form, "actor/edit", "actor/edit.do", "redirect:../welcome.do");
		model.addObject("authority", form.getAuthority());
		model.addObject("actorId", ac.getAccount().getId());
		model.addObject("own", LoginService.getPrincipal().getId() == ac.getAccount().getId());
		model.addObject("notCreate", true);
		model.addObject("makes", super.creditCardMakes());
		model.addObject("view", false);
		model.addObject("prefix", System.getProperty("phonePrefix"));
		model.addObject("spammer", this.actorService.checkSpammer(ac));
		return model;
	}
	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		ModelAndView result = null;

		ActorForm actor;
		actor = (ActorForm) e;

		Administrator admin = null;
		//		final Rookie rookie = null;
		//		final Company company = null;
		//		final Provider provider = null;
		//		final Auditor auditor = null;

		if (actor.isTerms()) {
			if (actor.getAuthority().equals(Authority.ADMIN))
				admin = this.actorService.reconstructAdministrator(actor, binding);
			//			else if (actor.getAuthority().equals(Authority.ROOKIE))
			//				rookie = this.actorService.reconstructRookie(actor, binding);
			//			else if (actor.getAuthority().equals(Authority.COMPANY))
			//				company = this.actorService.reconstructCompany(actor, binding);
			//			else if (actor.getAuthority().equals(Authority.PROVIDER))
			//				provider = this.actorService.reconstructProvider(actor, binding);
			//			else if (actor.getAuthority().equals(Authority.AUDITOR))
			//				auditor = this.actorService.reconstructAuditor(actor, binding);

			if (!actor.getAuthority().equals(Authority.ADMIN) && actor.getEmail().matches("^([0-9a-zA-Z]([-.\\\\w]*[0-9a-zA-Z])+@)|([\\w\\s]+<[a-zA-Z0-9_!#$%&*+/=?`{|}~^.-]+@+>)$"))
				result = super.createAndEditModelAndView(actor, "actor.wrong.email", "actor/edit", "actor/edit.do", "redirect://../welcome.do");
			else if (!super.luhnAlgorithm(actor.getCreditCard().getNumber()) || actor.getCreditCard().getMake().equals("0"))
				result = super.createAndEditModelAndView(actor, "actor.wrong.creditCard", "actor/edit", "actor/edit.do", "redirect://../welcome.do");
			else if (actor.getCreditCard().getExpiration().before(new Date()) || actor.getCreditCard().getMake().equals("0"))
				result = super.createAndEditModelAndView(actor, "creditcard.invalid.expiration", "actor/edit", "actor/edit.do", "redirect://../welcome.do");
			else if (!super.checkPhone(actor.getPhone()))
				result = super.createAndEditModelAndView(actor, "actor.wrong.phone", "actor/edit", "actor/edit.do", "redirect://../welcome.do");
			else if (actor.getAccount().getPassword().equals(actor.getPassword2()) && actor.getAccount().getPassword() != "" && actor.getPassword2() != "") {
				if (actor.getAuthority().equals(Authority.ADMIN))
					//					this.actorService.save(admin, null, null, null, null);
					//				else if (actor.getAuthority().equals(Authority.ROOKIE))
					//					this.actorService.save(null, rookie, null, null, null);
					//				else if (actor.getAuthority().equals(Authority.COMPANY))
					//					this.actorService.save(null, null, company, null, null);
					//				else if (actor.getAuthority().equals(Authority.PROVIDER))
					//					this.actorService.save(null, null, null, provider, null);
					//				else if (actor.getAuthority().equals(Authority.AUDITOR))
					//					this.actorService.save(null, null, null, null, auditor);
					result = new ModelAndView("redirect:../j_spring_security_logout");
			} else {
				actor.setTerms(true);
				result = super.createAndEditModelAndView(actor, "actor.password", "actor/edit", "actor/edit.do", "redirect://../welcome.do");

			}

		} else {
			result = super.createAndEditModelAndView(actor, "actor.terms", "actor/edit", "actor/edit.do", "redirect://../welcome.do");
			actor.setTerms(false);
		}

		return result;
	}
	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		//		Actor a;
		//		a = this.actorService.findByUserAccount(LoginService.getPrincipal().getId());
		final Actor a = (Actor) e;
		this.actorService.delete(a.getAccount().getId());
		return new ModelAndView(nameResolver);
	}

}
