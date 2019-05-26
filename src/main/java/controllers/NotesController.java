
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
import services.NotesService;
import domain.Event;
import domain.Notes;

@Controller
@RequestMapping("/notes")
public class NotesController extends BasicController {

	@Autowired
	private NotesService	notesService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(defaultValue = "0") final int id) {
		ModelAndView result;
		result = super.listModelAndView("notesCollection", "notes/list", this.notesService.getNotesByEvent(id), "notes/list.do");
		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int id) {
		ModelAndView result;
		Event event;
		event = this.notesService.getEventByNote(id);
		result = super.show(this.notesService.findOne(id), "notes/edit", "notes/edit.do", "notes/list.do?id=" + event.getId());
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(defaultValue = "0") final int id) {
		Assert.isTrue(
			this.notesService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR) || this.notesService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)
				|| this.notesService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT), "You must be an collaborator, member or student");
		return super.create(this.notesService.createNotes(id), "notes/edit", "notes/edit.do", "/event/listEvents.do");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEntity(final Notes notes, final BindingResult binding) {
		ModelAndView result;
		Assert.isTrue(
			this.notesService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR) || this.notesService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)
				|| this.notesService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT), "You must be an collaborator, member or student");
		result = super.save(notes, binding, "notes.commit.error", "notes/edit", "notes/edit.do", "/event/listEvents.do", "redirect:../event/listEvents.do");
		return result;
	}

	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		ModelAndView result;
		Notes notes;
		notes = (Notes) e;
		notes = this.notesService.reconstruct(notes, binding);
		this.notesService.save(notes);
		result = new ModelAndView(nameResolver);
		return result;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		// TODO Auto-generated method stub
		return null;
	}

}
