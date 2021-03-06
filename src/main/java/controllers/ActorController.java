
package controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import services.ActorService;
import services.ComissionService;
import services.CommentService;
import services.CustomisationSystemService;
import services.EventService;
import services.NotesService;
import services.PortfolioService;
import services.ProclaimService;
import services.ProfileService;
import services.SponsorshipService;
import services.SwapService;
import domain.Actor;
import domain.Administrator;
import domain.Collaborator;
import domain.Member;
import domain.Profile;
import domain.Sponsor;
import domain.Student;
import forms.ActorForm;

@Controller
@RequestMapping(value = "/actor")
public class ActorController extends BasicController {

	@Autowired
	private ActorService				actorService;

	@Autowired
	private CustomisationSystemService	customService;

	@Autowired
	private ProfileService				profileService;

	@Autowired
	private ComissionService			comissionService;

	@Autowired
	private ProclaimService				proclaimService;

	@Autowired
	private CommentService				commentService;

	@Autowired
	private NotesService				notesService;

	@Autowired
	private EventService				eventService;

	@Autowired
	private PortfolioService			portfolioService;

	@Autowired
	private SwapService					swapService;

	@Autowired
	private SponsorshipService			sponsorshipService;


	@RequestMapping(value = "/listSpammers", method = RequestMethod.GET)
	public ModelAndView listSpammers() {
		ModelAndView result;
		result = super.listModelAndView("actors", "actor/list", this.actorService.getActorSpammer(), "actor/list.do");
		result.addObject("ban", true);
		result.addObject("comis", true);
		return result;
	}

	@RequestMapping(value = "/listBan", method = RequestMethod.GET)
	public ModelAndView listBan() {
		ModelAndView result;
		result = super.listModelAndView("actors", "actor/list", this.actorService.getActorEmabled(), "actor/list.do");
		result.addObject("ban", false);
		result.addObject("comis", true);
		return result;
	}

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView banActor(@RequestParam final int id) {
		ModelAndView result;
		Assert.isTrue(this.actorService.getActorsIdSpammer().contains(id), "You only can ban spammers actor");
		this.customService.banActor(id);
		result = new ModelAndView("redirect:listSpammers.do");
		result.addObject("ban", true);
		return result;
	}

	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	public ModelAndView unBanActor(@RequestParam final int id) {
		ModelAndView result;
		Assert.isTrue(this.actorService.getActorsIdEnabled().contains(id), "You only can unban ban actor");
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
		model.addObject("prefix", System.getProperty("phonePrefix"));
		return model;

	}

	@RequestMapping(value = "/createMember", method = RequestMethod.GET)
	public ModelAndView createMember() {
		ModelAndView model;
		final ActorForm a = this.actorService.map(this.actorService.createActor(Authority.MEMBER), Authority.MEMBER);
		model = super.create(a, "actor/edit", "actor/edit.do", "redirect:../welcome.do");
		model.addObject("authority", Authority.MEMBER);
		model.addObject("view", false);
		model.addObject("prefix", System.getProperty("phonePrefix"));
		return model;
	}
	//
	@RequestMapping(value = "/createCollaborator", method = RequestMethod.GET)
	public ModelAndView createColaborator() {
		ModelAndView model;
		model = super.create(this.actorService.map(this.actorService.createActor(Authority.COLLABORATOR), Authority.COLLABORATOR), "actor/edit", "actor/edit.do", "redirect:../welcome.do");
		model.addObject("authority", Authority.COLLABORATOR);
		model.addObject("view", false);
		model.addObject("prefix", System.getProperty("phonePrefix"));
		return model;
	}
	//
	@RequestMapping(value = "/createStudent", method = RequestMethod.GET)
	public ModelAndView createStudent() {
		ModelAndView model;
		final ActorForm a = this.actorService.map(this.actorService.createActor(Authority.STUDENT), Authority.STUDENT);
		model = super.create(a, "actor/edit", "actor/edit.do", "redirect:../welcome.do");
		model.addObject("authority", Authority.STUDENT);
		model.addObject("view", false);
		model.addObject("prefix", System.getProperty("phonePrefix"));
		return model;
	}
	//
	@RequestMapping(value = "/createSponsor", method = RequestMethod.GET)
	public ModelAndView createSponsor() {
		ModelAndView model;
		final ActorForm a = this.actorService.map(this.actorService.createActor(Authority.SPONSOR), Authority.SPONSOR);
		model = super.create(a, "actor/edit", "actor/edit.do", "redirect:../welcome.do");
		model.addObject("authority", Authority.SPONSOR);
		model.addObject("view", false);
		model.addObject("prefix", System.getProperty("phonePrefix"));
		return model;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEntity(@ModelAttribute("actor") final ActorForm actor, final BindingResult binding) {
		ModelAndView result;
		result = super.save(actor, binding, "actor.commit.error", "actor/edit", "actor/edit.do", "/actor/list.do", "redirect:../welcome.do");
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
		Member member = null;
		Student student = null;
		Collaborator collaborator = null;
		Sponsor sponsor = null;

		if (actor.isTerms()) {
			if (actor.getAuthority().equals(Authority.ADMIN))
				admin = this.actorService.reconstructAdministrator(actor, binding);
			else if (actor.getAuthority().equals(Authority.MEMBER))
				member = this.actorService.reconstructMember(actor, binding);
			else if (actor.getAuthority().equals(Authority.STUDENT))
				student = this.actorService.reconstructStudent(actor, binding);
			else if (actor.getAuthority().equals(Authority.COLLABORATOR))
				collaborator = this.actorService.reconstructCollaborator(actor, binding);
			else if (actor.getAuthority().equals(Authority.SPONSOR))
				sponsor = this.actorService.reconstructSponsor(actor, binding);

			if (!actor.getAuthority().equals(Authority.ADMIN) && actor.getEmail().matches("^([0-9a-zA-Z]([-.\\\\w]*[0-9a-zA-Z])+@)|([\\w\\s]+<[a-zA-Z0-9_!#$%&*+/=?`{|}~^.-]+@+>)$"))
				result = super.createAndEditModelAndView(actor, "actor.wrong.email", "actor/edit", "actor/edit.do", "redirect://../welcome.do");

			else if (!super.checkPhone(actor.getPhone()))
				result = super.createAndEditModelAndView(actor, "actor.wrong.phone", "actor/edit", "actor/edit.do", "redirect://../welcome.do");
			else if (actor.getAccount().getPassword().equals(actor.getPassword2()) && actor.getAccount().getPassword() != "" && actor.getPassword2() != "") {
				if (actor.getAuthority().equals(Authority.ADMIN))
					this.actorService.save(admin, null, null, null, null);

				else if (actor.getAuthority().equals(Authority.MEMBER))
					this.actorService.save(null, member, null, null, null);
				else if (actor.getAuthority().equals(Authority.STUDENT))
					this.actorService.save(null, null, student, null, null);
				else if (actor.getAuthority().equals(Authority.COLLABORATOR))
					this.actorService.save(null, null, null, collaborator, null);
				else if (actor.getAuthority().equals(Authority.SPONSOR))
					this.actorService.save(null, null, null, null, sponsor);
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
		Actor a;
		a = this.actorService.findByUserAccount(LoginService.getPrincipal().getId());
		this.actorService.delete(a.getAccount().getId());
		return new ModelAndView(nameResolver);
	}
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public ModelAndView export() {
		final ModelAndView result;

		Actor actor;
		actor = this.actorService.findByUserAccount(LoginService.getPrincipal().getId());

		Map<String, Object> map;
		map = new HashMap<String, Object>();

		map.put("actor", actor);

		Collection<Profile> col;
		col = this.profileService.getProfilesByActorId(actor.getId());
		map.put("profiless", this.profileService.getProfilesByActorId(actor.getId()));

		if (this.actorService.findAuthority(actor.getAccount().getAuthorities(), Authority.MEMBER)) {
			map.put("comissions", this.comissionService.getComissionsByMemberId(actor.getId()));
			map.put("proclaims", this.proclaimService.findProclaimAssigned(actor.getId()));
			map.put("commentsMember", this.commentService.getCommentsByActor(LoginService.getPrincipal().getId()));
			map.put("notesMember", this.notesService.getNotesByActor(actor.getId()));
		}
		if (this.actorService.findAuthority(actor.getAccount().getAuthorities(), Authority.COLLABORATOR)) {
			map.put("events", this.eventService.getEventsByCollaboratorId(actor.getId()));
			map.put("portfolio", this.portfolioService.findPortfolioByActor(LoginService.getPrincipal().getId()));
			map.put("swapR", this.swapService.getSwapsByCollaboratorId(actor.getId()));
			map.put("swapP", this.swapService.getSwapsPendingByCollaboratorId(actor.getId()));
			map.put("notesCollaborator", this.notesService.getNotesByActor(actor.getId()));
		}
		if (this.actorService.findAuthority(actor.getAccount().getAuthorities(), Authority.STUDENT)) {
			map.put("pro", this.proclaimService.findAllByStudent());
			map.put("commentsStudent", this.commentService.getCommentsByActor(LoginService.getPrincipal().getId()));
			map.put("notesStudent", this.notesService.getNotesByActor(actor.getId()));
		}
		if (this.actorService.findAuthority(actor.getAccount().getAuthorities(), Authority.SPONSOR)) {
			map.put("sponsorshipsAct", this.sponsorshipService.getSponsorshipActiveBySponsorId(actor.getId()));
			map.put("sponsorshipsDes", this.sponsorshipService.getSponsorshipDesactiveBySponsorId(actor.getId()));
		}

		result = new ModelAndView(new ExportActorDataPDFController(), map);

		return result;
	}

}
