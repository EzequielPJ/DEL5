
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import repositories.CategoryRepository;
import utilities.AbstractTest;
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

	/**
	 * CategoryRepository must be used for this test.
	 * If service is used, an assert will interrupt the execution.
	 * FindOne from Service is intended to be used only by administrators.
	 */

	@Autowired
	private CategoryRepository	repositoryCategory;


	@Test
	public void driverCreation() {
		final Object[][] objects = {
			{	// Simple creation - Positive Case
				"student1", "", super.getEntityId("category1"), null
			}, {
				// Simple creation with duplicate ticker on database - Negative Case
				"student1", "190419-000000", super.getEntityId("category1"), null
			}
		};
		for (int i = 0; i < objects.length; i++) {
			System.out.println(i);
			this.template((String) objects[i][0], (String) objects[i][1], (int) objects[i][2], (Class<?>) objects[i][3]);
		}
	}

	protected void template(final String auth, final String ticker, final int category, final Class<?> expected) {
		Class<?> caught = null;
		try {

			super.authenticate(auth);

			Proclaim p;
			p = this.service.create();

			p.setTitle("prueba");
			p.setCategory(this.repositoryCategory.findOne(category));

			p.setDescription("desc");

			p.setFinalMode(false);
			p.setAttachments("attachment1");

			StudentCard d;
			d = new StudentCard();

			d.setCentre("centre");
			d.setCode(1234);
			d.setVat("12345678Z");

			p.setStudentCard(d);

			if (!ticker.isEmpty())
				p.getTicker().setTicker(ticker);

			this.service.save(p);

			this.service.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
