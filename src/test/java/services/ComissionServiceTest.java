
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Comission;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ComissionServiceTest extends AbstractTest {

	@Autowired
	private ComissionService	service;


	/**
	 * TEST 1
	 * 
	 * Requirement tested:
	 * 14. Members can create organizations. Every organization must have a name, a description and the moment when it was created.
	 * Furthermore, the different interested collaborators can join to any organization depending on its wishes.
	 * 
	 * - Analysis of sentence coverage of ComissionService: 38.9%
	 * 
	 * Total instructions: 203; Covered Instructions: 79
	 * 
	 * - Analysis of data coverage: 70%
	 * 
	 * Attribute: name| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: description| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: finalMode| Bad value: false | Normal value: true | Coverage: 100% |
	 * Attribute: moment| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: member| Bad value: null | Normal value: Yes | Coverage: 100% |
	 */

	@Test
	public void driver() {

		final Object[][] testingData = {
			{	//Positive Case - Creation of comission
				"member1", 0, null
			}, {
				//Negative Case - An administrator can not create comissions
				"admin1", super.getEntityId("comission1"), IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void template(final String auth, final int entity, final Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticate(auth);

			Comission c;

			if (entity == 0)
				c = this.service.createComission();
			else
				c = this.service.findOne(entity);

			c.setDescription("hola a la comission");
			c.setName("DP");

			this.service.save(c);
			this.service.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	@Test
	public void driverDelete() {

		final Object[][] testingData = {
			{
				//Negative Case - An administrator can not create comissions
				"member2", super.getEntityId("comission1"), IllegalArgumentException.class
			}
		};

		this.templateDelete((String) testingData[0][0], (int) testingData[0][1], (Class<?>) testingData[0][2]);
	}
	protected void templateDelete(final String auth, final int entity, final Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticate(auth);

			this.service.deleteComission(entity);
			this.service.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
