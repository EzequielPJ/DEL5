
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Notes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class NotesServiceTest extends AbstractTest {

	@Autowired
	private NotesService	service;


	/**
	 * TODO: Por hacer. Hasta que no se arregle servicio no puede valorarse la cobertura del test.
	 * 
	 * TEST 1
	 * 
	 * Requirement tested:
	 * 16. Students, collaborators and members who go to any event can create notes.
	 * A note consists on a value from 0 to 10 and a short optional description.
	 * 
	 * - Analysis of sentence coverage of NotesService: %
	 * Total instructions: ; Covered Instructions:
	 * 
	 * - Analysis of data coverage: 75%
	 * 
	 * Attribute: note| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: description| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: event| Bad value: null | Normal value: Yes | Coverage: 100% |
	 * Attribute: collaborator| Bad value: null | Normal value: Yes | Coverage: 100% |
	 */

	@Test
	public void driver() {
		final Object[][] testingData = {
			{
				"", 0, super.getEntityId("event1"), null
			}, {
				"", super.getEntityId("note2"), 0, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void template(final String auth, final int entity, final int event, final Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticate(auth);

			Notes n;

			if (entity == 0)
				n = this.service.createNotes(event);
			else
				n = this.service.findOne(entity);

			n.setDescription("descriptionJUNIT4");
			n.setNote(5.0);

			this.service.save(n);

			this.service.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
