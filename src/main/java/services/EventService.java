
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.EventRepository;
import domain.Event;

@Service
@Transactional
public class EventService extends AbstractService {

	@Autowired
	private EventRepository	eventRepository;


	public Collection<Event> findAllFinalMode() {
		return this.eventRepository.findAllFinalMode();
	}

	public Event findOne(final int id) {
		return this.eventRepository.findOne(id);
	}

}
