
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ValidationException;

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
import services.CategoryService;
import services.FinderService;
import services.ProclaimService;
import domain.Actor;
import domain.Finder;
import domain.Member;
import domain.Proclaim;
import domain.Student;

@Controller
@RequestMapping(value = {
	"/proclaim/member", "/proclaim/student"
})
public class ProclaimController extends BasicController {

	@Autowired
	private ProclaimService	service;

	@Autowired
	private CategoryService	categoryService;

	@Autowired
	private FinderService	serviceFinder;

	private Finder			oldMemberFinder;

	private String			previousStatus;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		Collection<Proclaim> proclaims = null;
		ModelAndView result;

		String requestURI = null;

		if (this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT)) {
			proclaims = this.service.findAllByStudent();
			requestURI = "proclaim/student/list.do";
		}
		if (this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
			proclaims = this.service.findAllByMember();
			requestURI = "proclaim/member/list.do";
		}

		result = super.listModelAndView("proclaims", "proclaim/list", proclaims, requestURI);

		if (this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
			result.addObject("boton", true);
			result.addObject("finderColumns", true);
		}
		return result;
	}
	@RequestMapping(value = "/unassigned", method = RequestMethod.GET)
	public ModelAndView unassignedList() {
		Assert.isTrue(this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER));
		ModelAndView result;
		result = super.listModelAndView("proclaims", "proclaim/list", this.service.findNoAssigned(), "proclaim/member/unassigned.do").addObject("startAssignation", true);
		Collection<Proclaim> col;
		col = this.service.findProclaimAssigned(this.service.findByUserAccount(LoginService.getPrincipal().getId()).getId());
		result.addObject("finderColumns", true);
		result.addObject("ass", col);
		return result;
	}
	@RequestMapping(value = "/closed", method = RequestMethod.GET)
	public ModelAndView closedProclaimsList() {
		Assert.isTrue(this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER));
		ModelAndView result;
		result = super.listModelAndView("proclaims", "proclaim/list", this.service.findAllByMemberClosed(), "proclaim/member/unassigned.do").addObject("startAssignation", true);
		Collection<Proclaim> col;
		col = this.service.findProclaimAssigned(this.service.findByUserAccount(LoginService.getPrincipal().getId()).getId());
		result.addObject("finderColumns", true);
		result.addObject("ass", col);
		return result;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		Assert.isTrue(this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT));
		return super.create(this.service.create(), "proclaim/edit", "proclaim/student/edit.do", "/proclaim/student/list.do").addAllObjects(this.model()).addObject("view", false);
	}
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int id) {

		Proclaim p;
		p = this.service.findOne(id);

		String requestURI = null;
		String requestCancel = null;

		if (this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT)) {
			requestURI = "proclaim/student/edit.do";
			requestCancel = "/proclaim/student/list.do";
		}
		if (this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
			requestURI = "proclaim/member/edit.do";
			requestCancel = "/proclaim/member/list.do";
		}

		this.previousStatus = p.getStatus();

		Assert.isTrue(this.checkAuthorities(p));

		return super.edit(p, "proclaim/edit", requestURI, requestCancel).addAllObjects(this.model()).addObject("previousStatus", this.previousStatus);
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int id) {

		Proclaim p;
		p = this.service.findOne(id);

		String requestURI = null;
		String requestCancel = null;

		if (this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT)) {
			requestURI = "proclaim/student/edit.do";
			requestCancel = "/proclaim/student/list.do";
		}
		if (this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
			requestURI = "proclaim/member/edit.do";
			requestCancel = "/proclaim/member/list.do";
		}

		this.previousStatus = p.getStatus();

		return super.show(p, "proclaim/edit", requestURI, requestCancel).addAllObjects(this.model()).addObject("previousStatus", this.previousStatus);
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Proclaim proclaim, final BindingResult binding) {

		String requestURI = null;
		String requestCancel = null;
		String nameResolver = null;
		ModelAndView result = null;
		if (this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT)) {
			requestURI = "proclaim/student/edit.do";
			requestCancel = "/proclaim/student/list.do";
			nameResolver = "redirect:/proclaim/student/list.do";
			result = super.save(proclaim, binding, "proclaim.commit.error", "proclaim/edit", requestURI, requestCancel, nameResolver).addAllObjects(this.model());
		}
		if (this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
			requestURI = "proclaim/member/edit.do";
			requestCancel = "/proclaim/member/list.do";
			nameResolver = "redirect:/proclaim/member/list.do";

			this.previousStatus = proclaim.getStatus();

			result = super.save(proclaim, binding, "proclaim.commit.error", "proclaim/edit", requestURI, requestCancel, nameResolver).addAllObjects(this.model()).addObject("previousStatus", this.previousStatus);

			if (binding.hasErrors()) {
				boolean checkEnglish;
				checkEnglish = proclaim.getStatus().equals("REJECTED") || proclaim.getStatus().equals("ACCEPTED");

				if (checkEnglish)
					proclaim.setStatus("PENDING");
				else
					proclaim.setStatus("PENDIENTE");
			}
			proclaim.setFinalMode(true);
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Proclaim proclaim) {

		String requestURI = null;
		String requestCancel = null;
		String nameResolver = null;

		Assert.isTrue(this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT));

		requestURI = "proclaim/student/edit.do";
		requestCancel = "/proclaim/student/list.do";
		nameResolver = "redirect:/proclaim/student/list.do";

		return super.delete(proclaim, "proclaim.commit.error", "proclaim/edit", requestURI, requestCancel, nameResolver).addAllObjects(this.model());
	}
	@RequestMapping(value = "/assign", method = RequestMethod.GET)
	public ModelAndView assign(final int id) {
		ModelAndView result;

		boolean res;
		res = this.service.assign(id);

		if (res)
			result = this.custom(new ModelAndView("redirect:/proclaim/member/list.do"));
		else {
			result = this.custom(new ModelAndView("redirect:/proclaim/member/unnasigned.do"));
			result.addObject("asignedYet", "proclaim.asignedYet");
		}

		return result;
	}
	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		Proclaim p;
		p = (Proclaim) e;
		p = this.service.reconstruct(p, binding);
		this.service.save(p);
		return new ModelAndView(nameResolver);
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		Proclaim p;
		p = (Proclaim) e;
		this.service.delete(p.getId());
		return new ModelAndView(nameResolver);
	}

	@RequestMapping(value = "/finder", method = RequestMethod.GET)
	public ModelAndView finder() {
		ModelAndView result;
		Finder finder;
		Member h;

		h = (Member) this.service.findByUserAccount(LoginService.getPrincipal().getId());
		finder = h.getFinder();

		this.oldMemberFinder = finder;

		result = super.create(finder, "proclaim/find", "proclaim/member/search.do", "/").addObject("categories", this.model().get("categories"));

		return result;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, params = "search")
	public ModelAndView search(Finder finder, final BindingResult binding) {

		ModelAndView result = null;
		Member h;
		h = null;

		Collection<Proclaim> res;
		res = new ArrayList<Proclaim>();

		try {

			finder = this.serviceFinder.reconstruct(finder, binding);

			if (finder.getSingleKey().equals("") && finder.getCategory() == null && finder.getRegisteredDate() == null)
				res = this.service.findNoAssigned();
			else {
				h = (Member) this.service.findByUserAccount(LoginService.getPrincipal().getId());
				if (this.checkFinderProperties(this.oldMemberFinder, finder) && this.checkFinderTime(finder))
					res = h.getFinder().getProclaims();
				else {
					res = this.serviceFinder.searchWithRetain(finder.getSingleKey(), finder.getCategory(), finder.getRegisteredDate(), finder.isBeforeOrNot());
					this.serviceFinder.save(finder, res);

				}
			}
			result = super.listModelAndView("proclaims", "proclaim/list", res, "proclaim/member/list.do");
			result.addObject("finderColumns", true);
		} catch (final ValidationException e) {
			result = super.createAndEditModelAndView(finder, "proclaim/find", "proclaim/search.do", "/").addObject("categories", this.model().get("categories"));
		} catch (final Throwable oops) {
			result = super.createAndEditModelAndView(finder, "finder.commit.error", "proclaim/find", "proclaim/search.do", "/").addObject("categories", this.model().get("categories"));
		}

		return result;
	}
	@RequestMapping(value = "/clear", method = RequestMethod.GET)
	public ModelAndView clearFinder() {
		ModelAndView result;
		Member h;
		Finder aux;
		h = (Member) this.service.findByUserAccount(LoginService.getPrincipal().getId());
		aux = h.getFinder();
		this.serviceFinder.clear(aux);
		result = super.edit(this.serviceFinder.findOne(aux), "proclaim/find", "proclaim/member/search.do", "redirect:../welcome.do");
		return result;
	}

	private boolean checkFinderTime(final Finder finder) {
		final Date creationFinder = finder.getCreationDate();
		final Date now = new Date();
		boolean useCache = true;
		final long diff = now.getTime() - creationFinder.getTime();
		final long diffMinutes = diff / (60 * 1000);

		if (diffMinutes >= Integer.valueOf(System.getProperty("hoursFinder")) * 60)
			useCache = false;

		return useCache;
	}

	private boolean checkFinderProperties(final Finder f, final Finder search) {
		boolean res = true;
		if (f.getSingleKey() != null)
			res &= f.getSingleKey().equals(search.getSingleKey());
		if (f.getCategory() != null)
			res &= f.getCategory().equals(search.getCategory());
		if (f.getRegisteredDate() != null)
			res &= f.getRegisteredDate().equals(search.getRegisteredDate());
		if ((f.getCategory() == null && search.getCategory() != null) || (f.getCategory() != null && search.getCategory() == null))
			res = false;
		if ((f.getRegisteredDate() == null && search.getRegisteredDate() != null) || (f.getRegisteredDate() != null && search.getRegisteredDate() == null))
			res = false;
		if ((f.getSingleKey() == null && search.getSingleKey() != null) || (f.getSingleKey() != null && search.getSingleKey() == null))
			res = false;
		return res;
	}
	public Map<String, ?> model() {
		Map<String, Object> map;
		map = new HashMap<String, Object>();

		Collection<String> statusCol;
		statusCol = super.statusByLang();

		map.put("categories", this.categoryService.findAll());

		if (this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
			boolean c = statusCol.remove("SUBMITTED");
			if (!c)
				c = statusCol.remove("ENVIADO");
		}

		map.put("statusCol", statusCol);

		return map;
	}

	private boolean checkAuthorities(final Proclaim p) {

		boolean res = false;

		Actor a;
		a = this.service.findByUserAccount(LoginService.getPrincipal().getId());

		if (this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT)) {
			Student s;
			s = (Student) a;
			res = p.getStudent().getId() == s.getId();
		} else if (this.service.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
			Member s;
			s = (Member) a;
			res = p.getMembers().contains(s);
		}

		return res;
	}
}
