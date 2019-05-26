
package controllers;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import services.EventService;
import services.NotesService;
import domain.Collaborator;
import domain.Event;

@Controller
@RequestMapping(value = {
	"/event", "/event/collaborator", "/event/member"
})
public class EventController extends BasicController {

	@Autowired
	private EventService	eventService;

	@Autowired
	private NotesService	notesService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result = null;
		UserAccount user;
		user = LoginService.getPrincipal();
		if (this.eventService.findAuthority(user.getAuthorities(), Authority.COLLABORATOR)) {
			Collaborator c;
			c = (Collaborator) this.eventService.getActorByUserId(user.getId());
			result = super.listModelAndView("events", "event/list", this.eventService.getEventsByCollaboratorId(c.getId()), "event/collaborator/list.do");
		} else if (this.eventService.findAuthority(user.getAuthorities(), Authority.MEMBER))
			result = super.listModelAndView("events", "event/list", this.eventService.findAllPending(), "event/member/list.do");
		result.addObject("pub", true);
		return result;
	}

	@RequestMapping(value = "/listEvents", method = RequestMethod.GET)
	public ModelAndView listEvents() {
		ModelAndView result;
		result = super.listModelAndView("events", "event/list", this.eventService.findAllFinalMode(), "event/listEvents.do");
		result.addObject("general", true);
		result.addObject("pub", false);
		return result;
	}

	@RequestMapping(value = "/showEvent", method = RequestMethod.GET)
	public ModelAndView showEvent(@RequestParam final int idEvent) {
		ModelAndView result;
		result = super.show(this.eventService.findOne(idEvent), "event/edit", "event/edit.do", "event/listEvents.do");
		Double s;
		result.addObject("palShow", true);
		if (!this.notesService.getNotesByEvent(idEvent).isEmpty()) {
			s = this.notesService.getAVGNotesByEvent(idEvent);
			if (s > 5.0)
				result.addObject("score", "1");
			else if (s <= 5.0)
				result.addObject("score", "-1");
		} else
			result.addObject("score", "-1");
		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int idEvent) {
		final ModelAndView result;
		Assert.isTrue(this.eventService.findOne(idEvent).getCollaborator().getId() == this.eventService.getActorByUserId(LoginService.getPrincipal().getId()).getId(), "You don't have access, you can only see your events");
		result = super.show(this.eventService.findOne(idEvent), "event/edit", "event/collaborator/edit.do", "event/collaborator/list.do");
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Event event, final BindingResult binding) {
		ModelAndView result = null;
		if (this.eventService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
			result = super.save(event, binding, "event.commit.error", "event/edit", "event/member/edit.do", "redirect:list.do", "redirect:list.do");
			result.addObject("statusCol", Arrays.asList("accepted", "rejected"));
			result.addObject("mem", true);
		} else if (this.eventService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR)) {
			result = super.save(event, binding, "event.commit.error", "event/edit", "event/collaborator/edit.do", "redirect:list.do", "redirect:list.do");
			result.addObject("statusCol", Arrays.asList("pending", "accepted", "rejected"));
		}
		return result;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		Assert.isTrue(this.eventService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR), "You must be a collaborator");
		ModelAndView result;
		result = super.create(this.eventService.createEvent(), "event/edit", "event/collaborator/edit.do", "event/collaborator/list.do");
		result.addObject("statusCol", "pending");
		return result;
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView update(@RequestParam final int idEvent) {
		UserAccount user;
		user = LoginService.getPrincipal();

		Event aux;
		aux = this.eventService.findOne(idEvent);

		Collection<Event> col;
		String requestURI = "";
		String requestCancel = "";
		Assert.isTrue(aux.isFinalMode() == false, "You can't update a event in final mode");
		if (this.eventService.findAuthority(user.getAuthorities(), Authority.COLLABORATOR)) {
			Collaborator c;
			c = (Collaborator) this.eventService.getActorByUserId(user.getId());
			col = this.eventService.getEventsByCollaboratorId(c.getId());
			Assert.isTrue(col.contains(aux), "You don't have permission to do this");
			requestURI = "event/collaborator/edit.do";
			requestCancel = "/event/collaborator/list.do";
		} else if (this.eventService.findAuthority(user.getAuthorities(), Authority.MEMBER)) {
			requestURI = "event/member/edit.do";
			requestCancel = "/event/member/list.do";
		}
		ModelAndView result;
		result = super.edit(this.eventService.findOne(idEvent), "event/edit", requestURI, requestCancel);
		result.addObject("statusCol", Arrays.asList("accepted", "rejected"));
		if (this.eventService.findAuthority(user.getAuthorities(), Authority.COLLABORATOR))
			result.addObject("mem", false);
		else if (this.eventService.findAuthority(user.getAuthorities(), Authority.MEMBER))
			result.addObject("mem", true);
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int idEvent) {
		ModelAndView result;
		Event aux;
		aux = this.eventService.findOne(idEvent);
		Assert.isTrue(aux.isFinalMode() == false, "You can't delete a event in final mode");
		result = super.delete(this.eventService.findOne(idEvent), "event.commit.error", "event/edit", "event/collaborator/edit.do", "redirect:list.do", "redirect:list.do");
		return result;
	}

	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		ModelAndView result;
		Event ev;
		ev = (Event) e;
		ev = this.eventService.reconstruct(ev, binding);
		Event saved;
		saved = this.eventService.save(ev);
		result = new ModelAndView(nameResolver);
		result.addObject("saved", saved);
		return result;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		final ModelAndView result;
		Event event;
		event = (Event) e;
		this.eventService.deleteEvent(event.getId());
		result = new ModelAndView(nameResolver);
		return result;
	}

}
