
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BoxRepository;
import security.LoginService;
import domain.Actor;
import domain.Box;
import domain.MessageEntity;

@Service
@Transactional
public class BoxService extends AbstractService {

	@Autowired
	private Validator		validator;

	@Autowired
	private BoxRepository	boxRepository;


	public Collection<String> getNamesFromBoxes(final int accountId) {
		return this.boxRepository.getNamesFromBoxes(accountId);
	}

	public Collection<Box> save(final Collection<Box> boxes) {
		return this.boxRepository.save(boxes);
	}

	//Queries del repo de box
	public Box getActorEntryBox(final int id) {
		return this.boxRepository.getActorEntryBox(id);
	}

	public Box getActorSpamBox(final int id) {
		return this.boxRepository.getActorSpamBox(id);
	}

	public Box getActorSendedBox(final int id) {
		return this.boxRepository.getActorSendedBox(id);
	}

	public Box getActorTrashBox(final int id) {
		return this.boxRepository.getActorTrashBox(id);
	}

	public Box getActorNotificationBox(final int id) {
		return this.boxRepository.getActorNotificationBox(id);
	}

	public Box getActorOtherBox(final int id, final int other) {
		return this.boxRepository.getActorOtherBox(id, other);
	}

	public Collection<Box> getBoxesFromUserAccount(final int id) {
		return this.boxRepository.getBoxesFromUserAccount(id);
	}

	public Collection<Box> getBoxesFromActorNoSystem(final int id) {
		return this.boxRepository.getBoxesFromActorNoSystem(id);
	}

	public Actor getActorByUserAccount(final int id) {
		return this.boxRepository.getActorByUserAccount(id);
	}

	public Collection<Actor> findAllActorsSystem() {
		return this.boxRepository.findAllActorsSystem();
	}
	//findOne
	public Box findOne(final int id) {
		Box b;
		b = this.boxRepository.findOne(id);
		return b;
	}
	//create
	public Box createBox() {
		Box b;
		b = new Box();
		b.setFromSystem(false);
		b.setName("");
		b.setMessageEntity(new ArrayList<MessageEntity>());
		return b;
	}
	//save
	public Box save(final Box b) {
		Box saved;
		saved = this.boxRepository.save(b);
		final Actor a;
		a = this.boxRepository.getActorByUserAccount(LoginService.getPrincipal().getId());
		Collection<Box> colbox;
		colbox = a.getBoxes();
		if (!a.getBoxes().contains(saved)) {
			colbox.add(saved);
			a.setBoxes(colbox);
		}
		return saved;
	}
	//save subbox
	public Box saveSubBox(final Box b, final Box parent) {
		Box saved;
		saved = this.boxRepository.save(b);
		final Actor a;
		a = this.boxRepository.getActorByUserAccount(LoginService.getPrincipal().getId());
		Collection<Box> colbox;
		colbox = a.getBoxes();
		colbox.add(saved);
		a.setBoxes(colbox);

		Collection<Box> colboxp;
		colboxp = parent.getBoxes();
		colboxp.add(saved);
		parent.setBoxes(colboxp);

		return saved;
	}
	//delete
	public void delete(final Box b) {
		Assert.isTrue(!b.isFromSystem());

		final Actor a;
		a = this.boxRepository.getActorByUserAccount(LoginService.getPrincipal().getId());

		Collection<MessageEntity> colmes;
		colmes = b.getMessageEntity();
		//elimina las boxes que tienen los mensajes ya que es bidireccional
		for (final MessageEntity m : colmes)
			if (m.getBox().contains(b)) {
				final Collection<Box> boxes = m.getBox();
				boxes.remove(b);
				m.setBox(boxes);
			}
		//setteamos los mensajes de la box a vac�o
		colmes.clear();
		b.setMessageEntity(colmes);
		//eliminamos la caja al actor que la tiene
		for (final Box bo : this.boxRepository.getBoxesFromUserAccount(LoginService.getPrincipal().getId()))
			if (bo.getBoxes().contains(b)) {
				Collection<Box> co;
				co = bo.getBoxes();
				co.remove(b);
			}
		Collection<Box> boxesActor;
		boxesActor = a.getBoxes();
		boxesActor.remove(b);
		a.setBoxes(boxesActor);

		this.boxRepository.delete(b);

	}

	public Box reconstruct(final Box box, final BindingResult binding) {
		Box result;
		result = null;

		if (this.boxRepository.getNamesFromBoxes(LoginService.getPrincipal().getId()).contains(box.getName()))
			binding.rejectValue("name", "box.name.error");

		if (box.getId() == 0)
			result = box;
		else {
			result = this.boxRepository.findOne(box.getId());

			result.setName(box.getName());
			result.setMessageEntity(box.getMessageEntity());

		}

		this.validator.validate(result, binding);
		if (binding.hasErrors())
			throw new ValidationException();
		return result;
	}
	public void flush() {
		this.boxRepository.flush();
	}
	public void delete(final Collection<Box> boxes) {
		this.boxRepository.delete(boxes); //Este delete es el delete(Iterable) del repo
	}
}
