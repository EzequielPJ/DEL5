
package controllers;

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
@RequestMapping("/event")
public class EventController extends BasicController {

	@Autowired
	private EventService	eventService;

	@Autowired
	private NotesService	notesService;


	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		Assert.isTrue(this.eventService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR), "You don't have access to do this");
		ModelAndView result = null;
		UserAccount user;
		user = LoginService.getPrincipal();
		Collaborator c;
		c = (Collaborator) this.eventService.getActorByUserId(user.getId());
		result = super.listModelAndView("events", "comission/list", this.eventService.getEventsByCollaboratorId(c.getId()), "event/collaborator/list.do");
		result.addObject("statusCol", "PENDING");
		return result;
	}

	@RequestMapping(value = "/listEvents", method = RequestMethod.GET)
	public ModelAndView listEvents() {
		ModelAndView result;
		result = super.listModelAndView("events", "event/list", this.eventService.findAllFinalMode(), "event/listEvents.do");
		result.addObject("general", true);
		return result;
	}

	@RequestMapping(value = "/showEvent", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int idEvent) {
		ModelAndView result;
		result = super.show(this.eventService.findOne(idEvent), "event/edit", "event/edit.do", "event/listEvents.do");
		Double s;
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
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Event event, final BindingResult binding) {
		ModelAndView result;
		result = super.save(event, binding, "event.commit.error", "event/edit", "event/edit.do", "redirect:list.do", "redirect:list.do");
		return result;
	}

	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		// TODO Auto-generated method stub
		return null;
	}

}
