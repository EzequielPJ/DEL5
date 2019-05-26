
package services;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.NotesRepository;
import security.LoginService;
import domain.Actor;
import domain.Event;
import domain.Notes;

@Service
@Transactional
public class NotesService extends AbstractService {

	@Autowired
	private NotesRepository	notesRepository;

	@Autowired
	private EventService	eventService;

	@Autowired
	private Validator		validator;


	public Collection<Notes> getNotesByEvent(final int id) {
		return this.notesRepository.getNotesByEvent(id);
	}

	public Notes findOne(final int id) {
		return this.notesRepository.findOne(id);
	}

	public Event getEventByNote(final int id) {
		return this.notesRepository.getEventByNote(id);
	}

	public Double getAVGNotesByEvent(final int id) {
		return this.notesRepository.getAVGNotesByEvent(id);
	}

	public Notes createNotes(final int id) {
		Notes result;
		result = new Notes();
		Actor a;
		a = this.notesRepository.findActorByUserAccountId(LoginService.getPrincipal().getId());
		result.setActor(a);
		result.setDescription("");
		result.setNote(0.0);
		result.setEvent(this.eventService.findOne(id));
		return result;
	}

	public Notes save(final Notes n) {
		Notes modify;
		modify = this.notesRepository.save(n);
		return modify;
	}

	public Notes reconstruct(final Notes notes, final BindingResult binding) {
		Notes result;
		result = notes;

		if (result.getNote() == null)
			binding.rejectValue("note", "note.error");

		result.setActor(this.notesRepository.findActorByUserAccountId(LoginService.getPrincipal().getId()));

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();
		return result;
	}

	public void flush() {
		this.notesRepository.flush();
	}

}
