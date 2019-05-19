
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SponsorshipService;

@Controller
@RequestMapping("/sponsorship/provider")
public class SponsorshipController extends BasicController {

	@Autowired
	private SponsorshipService	serviceSponsorship;

	@Autowired
	private ActorService		serviceActor;


	//	@RequestMapping(value = "/listSponsorship", method = RequestMethod.GET)
	//	public ModelAndView listSponsorship() {
	//		UserAccount user;
	//		user = LoginService.getPrincipal();
	//		Provider p;
	//		p = (Provider) this.serviceActor.findByUserAccount(user.getId());
	//		ModelAndView result;
	//		result = super.listModelAndView("sponsorships", "sponsorship/list", this.serviceSponsorship.getSponsorshipByProviderId(p.getId()), "sponsorship/provider/list.do");
	//		return result;
	//	}

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
