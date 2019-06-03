
package controllers;

import java.util.ArrayList;
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
import services.CommentService;
import services.ProclaimService;
import domain.Comment;

@Controller
@RequestMapping(value = {
	"/comment/student", "/comment/member"
})
public class CommentController extends BasicController {

	@Autowired
	private CommentService	commService;
	@Autowired
	private ProclaimService	procService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(defaultValue = "0") final int id) {
		ModelAndView result;
		Collection<Comment> col;
		col = new ArrayList<Comment>();
		if (this.commService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT)) {
			col = id == 0 ? this.commService.getCommentsByActor(LoginService.getPrincipal().getId()) : this.commService.getCommentsByProclaim(id);
			result = super.listModelAndView("comments", "comment/list", col, "comment/student/list.do");
		} else {
			col = id == 0 ? this.commService.getCommentsByActor(LoginService.getPrincipal().getId()) : this.commService.getCommentsByProclaim(id);
			//if (this.commService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER))
			result = super.listModelAndView("comments", "comment/list", col, "comment/student/list.do");
			//		else
			//			result = new ModelAndView("403");
		}
		result.addObject("idd", id);
		return result;
	}
	/*
	 * @RequestMapping(value = "/show", method = RequestMethod.GET)
	 * public ModelAndView show(@RequestParam final int id) {
	 * ModelAndView result;
	 * try {
	 * Assert.isTrue(this.commService.findOne(id).getActor().getAccount().getId() == LoginService.getPrincipal().getId());
	 * 
	 * if (this.commService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT))
	 * result = super.show(this.commService.findOne(id), "comment/edit", "comment/edit.do", "comment/student/list.do?=" + this.commService.findOne(id).getProclaim().getId());
	 * else
	 * //if (this.commService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER))
	 * result = super.show(this.commService.findOne(id), "comment/edit", "comment/edit.do", "comment/member/list.do?=" + this.commService.findOne(id).getProclaim().getId());
	 * // else
	 * // result = new ModelAndView("403");
	 * } catch (final IllegalArgumentException oops) {
	 * result = new ModelAndView("403");
	 * }
	 * return result;
	 * }
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(defaultValue = "0") final int id) {
		ModelAndView result;
		try {
			Assert.isTrue(this.procService.findOne(id).isClosed() == false);
			//		Assert.isTrue(
			//			this.notesService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR) || this.notesService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)
			//				|| this.notesService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT), "You must be an collaborator, member or student");
			if (this.commService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT)) {
				Assert.isTrue(this.procService.findOne(id).getStudent().getAccount().getId() == LoginService.getPrincipal().getId());
				result = super.create(this.commService.create(id), "comment/edit", "comment/student/edit.do", "comment/student/list.do?=" + id);
			} else {
				// if (this.commService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER))
				Assert.isTrue(this.procService.findOne(id).getMembers().contains(this.procService.findByUserAccount(LoginService.getPrincipal().getId())));
				result = super.create(this.commService.create(id), "comment/edit", "comment/member/edit.do", "comment/member/list.do?=" + id);
				//		else
				//			result = new ModelAndView("403");
			}
		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("403");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEntity(final Comment comment, final BindingResult binding) {
		ModelAndView result;
		try {
			if (comment.getId() != 0) {
				final Comment c = this.commService.findOne(comment.getId());
				Assert.isTrue(c.getActor().getAccount().getId() == LoginService.getPrincipal().getId());
			}
			//		Assert.isTrue(
			//			this.notesService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR) || this.notesService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)
			//				|| this.notesService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT), "You must be an collaborator, member or student");
			if (this.commService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT))
				result = super.save(comment, binding, "comment.commit.error", "comment/edit", "comment/student/edit.do", "comment/student/list.do", "redirect:/comment/student/list.do?=" + comment.getProclaim().getId());
			else
				// if (this.commService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER))
				result = super.save(comment, binding, "comment.commit.error", "comment/edit", "comment/member/edit.do", "comment/member/list.do", "redirect:/comment/member/list.do?=" + comment.getProclaim().getId());
			//		else
			//			result = new ModelAndView("403");

			//result = super.save(comment, binding, "comment.commit.error", "comment/edit", "comment/student/edit.do", "comment/student/list.do", "redirect:../comment/student/list.do");
		} catch (final IllegalArgumentException oops) {
			result = new ModelAndView("403");
		}
		return result;
	}
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int id) {
		ModelAndView result;
		try {
			Assert.isTrue(this.commService.getCommentsByActor(LoginService.getPrincipal().getId()).contains(this.commService.findOne(id)));
			if (this.commService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT))
				result = super.edit(this.commService.findOne(id), "comment/edit", "comment/student/edit.do", "/comment/student/list.do");
			else
				result = super.edit(this.commService.findOne(id), "comment/edit", "comment/member/edit.do", "/comment/member/list.do");
		} catch (final IllegalArgumentException oops) {
			result = this.custom(new ModelAndView("403"));
		}
		return result;
	}
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteEntity(@RequestParam final int id) {
		boolean res = false;
		ModelAndView result;
		if (this.commService.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT)) {
			res = this.commService.getCommentsByActor(LoginService.getPrincipal().getId()).contains(this.commService.findOne(id));
			result = res ? super.delete(this.commService.findOne(id), "comment.commit.error", "comment/edit", "comment/student/edit.do", "/comment/student/list.do?id=" + this.commService.findOne(id).getProclaim().getId(),
				"redirect:/comment/student/list.do") : this.custom(new ModelAndView("403"));
		} else {
			res = this.commService.getCommentsByActor(LoginService.getPrincipal().getId()).contains(this.commService.findOne(id));
			result = res ? super.delete(this.commService.findOne(id), "comment.commit.error", "comment/edit", "comment/member/edit.do", "/comment/member/list.do?id=" + this.commService.findOne(id).getProclaim().getId(), "redirect:/comment/member/list.do")
				: this.custom(new ModelAndView("403"));
		}
		return result;
	}

	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		ModelAndView result;
		Comment comm;
		comm = (Comment) e;
		comm = this.commService.reconstruct(comm, binding);
		this.commService.save(comm);
		result = new ModelAndView(nameResolver);
		return result;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		Comment c;
		c = (Comment) e;
		this.commService.delete(c.getId());
		return new ModelAndView(nameResolver);
	}

}
