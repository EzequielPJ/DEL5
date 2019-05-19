
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

import security.LoginService;
import services.BoxService;
import services.MessageService;
import domain.Box;
import domain.MessageEntity;

@Controller
@RequestMapping("/message")
public class MessageEntityController extends BasicController {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private BoxService		boxService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int boxId) {
		Assert.isTrue(this.boxService.getActorByUserAccount(LoginService.getPrincipal().getId()).getBoxes().contains(this.boxService.findOne(boxId)), "You don´t have access");
		final Box b;
		ModelAndView result;
		b = this.boxService.findOne(boxId);
		result = super.listModelAndView("messages", "message/list", b.getMessageEntity(), "message/list.do");
		result.addObject("boxName", b.getName());
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		result = super.create(this.messageService.createMessage(this.boxService.getActorByUserAccount(LoginService.getPrincipal().getId())), "message/edit", "message/send.do", "/message/list.do");
		result.addObject("priorities", Arrays.asList("HIGH", "NEUTRAL", "LOW"));
		result.addObject("actors", this.boxService.findAllActorsSystem(LoginService.getPrincipal().getId()));
		result.addObject("view", false);
		return result;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "send")
	public ModelAndView saveEntity(final MessageEntity messageEntity, final BindingResult binding) {
		ModelAndView result;
		final Box box = this.boxService.getActorSendedBox(this.boxService.getActorByUserAccount(LoginService.getPrincipal().getId()).getId());
		result = super.save(messageEntity, binding, "message.commit.error", "message/edit", "message/send.do", "redirect:list.do", "redirect:list.do?boxId=" + box.getId());
		result.addObject("priorities", Arrays.asList("HIGH", "NEUTRAL", "LOW"));
		result.addObject("actors", this.boxService.findAllActorsSystem(LoginService.getPrincipal().getId()));
		result.addObject("view", false);
		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int id) {
		ModelAndView result;
		Collection<Box> actorBoxes;
		actorBoxes = this.boxService.getBoxesFromUserAccount(LoginService.getPrincipal().getId());
		final int actorId = this.boxService.getActorByUserAccount(LoginService.getPrincipal().getId()).getId();
		Collection<Box> messageBox;
		messageBox = this.messageService.getBoxesFromActorAndMessage(id, actorId);
		Assert.notEmpty(messageBox, "You don't have access");
		Assert.isTrue(actorBoxes.containsAll(messageBox), "Not your message ");

		result = super.show(this.messageService.findOne(id), "message/edit", "message/send.do", "message/dbox.do");
		result.addObject("rece", this.messageService.getReceiver(id));
		result.addObject("boxesOptional", this.boxService.getBoxesFromActorNoSystem(LoginService.getPrincipal().getId()));
		result.addObject("trash", this.boxService.getActorTrashBox(this.boxService.getActorByUserAccount(LoginService.getPrincipal().getId()).getId()).getId());
		return result;
	}

	@RequestMapping(value = "/inbox", method = RequestMethod.GET)
	public ModelAndView moveToInBox(@RequestParam final int id) {
		ModelAndView result;
		MessageEntity mesage = null;
		mesage = this.messageService.findOne(id);
		final int box = this.messageService.moveTo("In Box", mesage);
		result = this.custom(new ModelAndView("redirect:list.do?boxId=" + box));
		return result;
	}

	@RequestMapping(value = "/dbox", method = RequestMethod.GET)
	public ModelAndView moveToOtherBox(@RequestParam final int boxId, @RequestParam(value = "mess") final int mess) {
		ModelAndView result;
		MessageEntity mesage = null;
		mesage = this.messageService.findOne(mess);
		final int box = this.messageService.moveTo(String.valueOf(boxId), mesage);
		result = this.custom(new ModelAndView("redirect:list.do?boxId=" + box));
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteEntity(@RequestParam final int id) {
		ModelAndView result;
		final Box box;
		box = this.boxService.getActorTrashBox(this.boxService.getActorByUserAccount(LoginService.getPrincipal().getId()).getId());
		result = super.delete(this.messageService.findOne(id), "message.commit.error", "message/edit", "message/send.do", "redirect:list.do", "redirect:list.do?boxId=" + box.getId());
		return result;
	}

	@Override
	public <T> ModelAndView saveAction(final T e, final BindingResult binding, final String nameResolver) {
		ModelAndView result;
		MessageEntity mess;
		mess = (MessageEntity) e;
		mess = this.messageService.reconstruct(mess, binding);
		this.messageService.sendMessage(mess);
		result = new ModelAndView(nameResolver);
		return result;
	}

	@Override
	public <T> ModelAndView deleteAction(final T e, final String nameResolver) {
		ModelAndView result;
		MessageEntity mess;
		mess = (MessageEntity) e;
		this.messageService.deleteMessage(mess);
		if (this.messageService.getReceiver(mess.getId()).isEmpty() && !this.messageService.getMessageOutBox(mess.getSender().getId()).contains(mess))
			this.messageService.delete(mess);
		result = new ModelAndView(nameResolver);
		return result;
	}

}
