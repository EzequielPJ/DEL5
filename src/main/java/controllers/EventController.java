
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.EventService;

@Controller
@RequestMapping("/event")
public class EventController extends BasicController {

	@Autowired
	private EventService	eventService;


	@RequestMapping(value = "/listEvents", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		result = super.listModelAndView("events", "event/list", this.eventService.findAllFinalMode(), "event/listEvents.do");
		result.addObject("general", true);
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
