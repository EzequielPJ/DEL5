
package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/about-us")
public class AboutUsController extends AbstractController {

	@RequestMapping(value = "terms", method = RequestMethod.GET)
	public ModelAndView termsAndConditions() {
		return super.custom(new ModelAndView("about-us/terms-conditions"));
	}
}
