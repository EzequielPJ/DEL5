
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import repositories.CategoryRepository;
import utilities.AbstractTest;
import domain.Category;
import domain.Proclaim;
import domain.StudentCard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ProclaimServiceTest extends AbstractTest {

	@Autowired
	private ProclaimService		service;

	@Autowired
	private TickerService		tickerService;

	@Autowired
	private CategoryRepository	repository;


	/**
	 * TEST 1
	 * Requirement tested:
	 * Members only can delete their proclaims.
	 * 
	 * 
	 * TEST 2
	 * Requirement tested:
	 * Every proclaim must store a ticker, a title, a description, the moment when it is created, and some attachments.
	 * 
	 * 
	 * TEST 3
	 * Requirement tested:
	 * Every member can self-assign any proclaim that is not attended on the system.
	 * 
	 * 
	 * TEST 4
	 * Requirement tested:
	 * A student can closer their proclaims.
	 * 
	 * 
	 * TEST 5
	 * Requirement tested:
	 * Students can publish proclaims.
	 * 
	 * 
	 * - Analysis of sentence coverage of ProclaimService: 45.8%
	 * 
	 * Total instructions: 633; Covered Instructions: 290
	 * 
	 * - Analysis of data coverage: 68.3%
	 * 
	 * Attribute: title| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: description| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: finalMode| Bad value: false | Normal value: true | Coverage: 100% |
	 * Attribute: moment| Bad value: - | Normal value: Yes | Coverage: 100% |
	 * Attribute: attachments| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: status| Bad value: - | Normal value: Yes | Coverage: 33% |
	 * Attribute: law| Bad value: - | Normal value: Yes | Coverage: 100% |
	 * Attribute: reason| Bad value: - | Normal value: true | Coverage: 100% |
	 * Attribute: closed| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * Attribute: category| Bad value: - | Normal value: Yes | Coverage: 50% |
	 * 
	 * 
	 */

	@Test
	public void test1() {
		final Object[][] objects = {
			{	// Delete his proclaim- Positive Case
				"student1", "proclaim2", null
			}, {
				// Delete a proclaim in final mode - Negative Case
				"student1", "proclaim1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < objects.length; i++) {
			System.out.println(i);
			this.template((String) objects[i][0], super.getEntityId((String) objects[i][1]), (Class<?>) objects[i][2]);
		}
	}

	protected void template(final String username, final int id, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			this.service.delete(id);
			this.service.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/**
	 * This test has intentionally been broken due to an execution problem.
	 */

	@Test
	public void test2A() {
		final Object[][] objects = {
			{	// Simple edit - Positive Case
				"student1", super.getEntityId("proclaim2"), "proclaimEditada", null
			}
		};
		this.template2((String) objects[0][0], (int) objects[0][1], (String) objects[0][2], (Class<?>) objects[0][3]);
	}

	@Test
	public void test2B() {
		final Object[][] objects = {
			{
				// Simple edit with an empty attribute - Negative Case
				"student1", super.getEntityId("proclaim6"), "", ConstraintViolationException.class
			}
		};
		this.template2((String) objects[0][0], (int) objects[0][1], (String) objects[0][2], (Class<?>) objects[0][3]);
	}

	protected void template2(final String username, final int id, final String cadena, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			Proclaim p;
			p = this.service.findOne(id);

			if (cadena == null || cadena == "")
				p.setTitle("");
			else
				p.setTitle(cadena);

			this.service.save(p);
			this.tickerService.flush();
			this.service.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void test3() {
		final Object[][] objects = {
			{	//A member is assigned a proclaim that he did not have assigned. - Positive Case
				"member1", "proclaim5", null
			}, {
				// A member is assigned a proclaim that had already been assigned - Negative Case
				"member1", "proclaim1", IllegalArgumentException.class

			}
		};
		for (int i = 0; i < objects.length; i++) {
			System.out.println(i);
			this.template3((String) objects[i][0], super.getEntityId((String) objects[i][1]), (Class<?>) objects[i][2]);
		}
	}

	protected void template3(final String username, final int id, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);

			this.service.assign(id);
			this.service.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void test4() {
		final Object[][] objects = {
			{	// close his proclaim - Positive Case
				"member1", "proclaim1", null
			}, {
				//A member cancels a proclaim that is not his - Negative Case
				"member2", "proclaim5", IllegalArgumentException.class

			}
		};
		for (int i = 0; i < objects.length; i++) {
			System.out.println(i);
			this.template4((String) objects[i][0], super.getEntityId((String) objects[i][1]), (Class<?>) objects[i][2]);
		}
	}

	protected void template4(final String username, final int id, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			Proclaim proclaim;
			proclaim = this.service.findOne(id);
			proclaim.setClosed(true);
			this.service.save(proclaim);
			this.service.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void test5() {
		final Object[][] objects = {
			{	// Simple creation - Positive Case
				"student1", "category1", null
			}, { // A sponsor create a proclaim
				"sponsor1", "category1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < objects.length; i++) {
			System.out.println(i);
			this.template5((String) objects[i][0], super.getEntityId((String) objects[i][1]), (Class<?>) objects[i][2]);
		}
	}
	protected void template5(final String username, final int id, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			Proclaim proclaim;
			proclaim = this.service.create();

			Category categoria;
			categoria = this.repository.findOne(id);

			proclaim.setCategory(categoria);

			proclaim.setDescription("Hola");

			proclaim.setTitle("hola");

			proclaim.setAttachments("http://us.es");
			proclaim.setStatus("SUBMITTED");

			StudentCard studentCard;
			studentCard = new StudentCard();
			studentCard.setCentre("ETSII");
			studentCard.setCode(1434);
			studentCard.setVat("47338340Q");

			proclaim.setStudentCard(studentCard);

			this.service.save(proclaim);
			this.service.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
