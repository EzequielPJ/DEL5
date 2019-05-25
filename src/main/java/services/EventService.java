
package services;

import java.util.Collection;
import java.util.Date;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EventRepository;
import security.Authority;
import security.LoginService;
import domain.Actor;
import domain.Collaborator;
import domain.Event;

@Service
@Transactional
public class EventService extends AbstractService {

	@Autowired
	private EventRepository	eventRepository;

	@Autowired
	private Validator		validator;


	public Collection<Event> findAllFinalMode() {
		return this.eventRepository.findAllFinalMode();
	}

	public Event findOne(final int id) {
		return this.eventRepository.findOne(id);
	}

	public Actor getActorByUserId(final int idActor) {
		return this.eventRepository.findActorByUserAccountId(idActor);
	}

	public Collection<Event> getEventsByCollaboratorId(final int idCollaborator) {
		return this.eventRepository.getEventsByCollaboratorId(idCollaborator);
	}

	public Event createEvent() {
		Assert.isTrue(super.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR));
		Collaborator c;
		c = (Collaborator) this.getActorByUserId(LoginService.getPrincipal().getId());
		Event e;
		e = new Event();
		e.setTitle("");
		e.setDescription("");
		e.setFinalMode(false);
		e.setMoment(new Date());
		e.setStatus("PENDING");
		e.setCollaborator(c);
		return e;
	}

	public Event save(final Event e) {
		Actor a;
		a = this.getActorByUserId(LoginService.getPrincipal().getId());

		Event modify;
		modify = null;

		if (e.getId() == 0)
			modify = this.eventRepository.save(e);
		else if (super.findAuthority(a.getAccount().getAuthorities(), "COLLABORATOR") && e.isFinalMode() == false) {
			Assert.isTrue(e.getCollaborator().getId() == a.getId(), "You don't have access, you can only update your events");
			modify = this.eventRepository.save(e);
		} else if (super.findAuthority(a.getAccount().getAuthorities(), "MEMBER")) {
			e.setFinalMode(true);
			modify = this.eventRepository.save(e);
		}
		return modify;
	}

	public void deleteEvent(final int idEvent) {
		Actor a;
		a = this.getActorByUserId(LoginService.getPrincipal().getId());

		Event e;
		e = this.eventRepository.findOne(idEvent);
		if (super.findAuthority(a.getAccount().getAuthorities(), "COLLABORATOR") && e.isFinalMode() == false) {
			Assert.isTrue(e.getCollaborator().getId() == a.getId(), "You don't have access, you can only delete your events");
			this.eventRepository.delete(idEvent);
		} else if (super.findAuthority(a.getAccount().getAuthorities(), "MEMBER"))
			this.eventRepository.delete(idEvent);
	}

	public Event reconstruct(final Event ev, final BindingResult binding) {
		Event result;
		if (ev.getId() == 0) {
			result = ev;
			result.setCollaborator((Collaborator) this.getActorByUserId(LoginService.getPrincipal().getId()));
			result.setMoment(new Date());
		} else {
			result = this.eventRepository.findOne(ev.getId());
			result.setTitle(ev.getTitle());
			result.setDescription(ev.getDescription());
			result.setFinalMode(ev.isFinalMode());
		}
		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public void flush() {
		this.eventRepository.flush();
	}
}
