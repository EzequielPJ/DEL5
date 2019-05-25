
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class EventServiceTest extends AbstractTest {

	@Autowired
	private EventService	service;


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

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
