
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Event;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class EventServiceTest extends AbstractTest {

	@Autowired
	private EventService	service;


	/**
	 * TEST 1
	 * 
	 * Requirement tested:
	 * 15. Once a collaborator is in an organization, he/she can create events.
	 * For every event, the system must store the title, description,
	 * the celebration moment and a state. When an event is created takes pending status and is registered in draft mode.
	 * It is only gets final mode when a member accepts or rejects it.
	 * 
	 * - Analysis of sentence coverage of EventService: 40.2%
	 * Total instructions: 239; Covered Instructions: 96
	 * 
	 * - Analysis of data coverage: 66.7%
	 * 
	 * Attribute: title| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: description| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: finalMode| Bad value: false | Normal value: true | Coverage: 100% |
	 * Attribute: moment| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: status| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: collaborator| Bad value: null | Normal value: Yes | Coverage: 100% |
	 */

	@Test
	public void test() {
		final Object[][] testingData = {
			{	//Create an event. Positive Case.
				"collaborator1", 0, null
			}, {
				//Modifiying an event whose owner is not the one logged. Negative Case.
				"collaborator2", super.getEntityId("event1"), IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void template(final String auth, final int entity, final Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(auth);

			Event e;
			if (entity == 0)
				e = this.service.createEvent();
			else
				e = this.service.findOne(entity);

			e.setTitle("JUNIT4v1");
			e.setDescription("JUNIT4v2");
			e.setFinalMode(false);

			this.service.save(e);

			this.service.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
