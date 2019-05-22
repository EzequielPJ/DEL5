
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import repositories.CategoryRepository;
import security.LoginService;
import utilities.AbstractTest;
import domain.Proclaim;
import domain.Student;
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
	private CategoryRepository	repository;


	@Test
	public void testTicker() {
		Proclaim p;
		p = this.service.create();

		p.setTitle("prueba");
		p.setCategory(this.repository.findOne(super.getEntityId("category1")));

		p.setDescription("desc");

		p.setFinalMode(false);

		p.setLaw("law");
		p.setStatus("SUBMITTED");

		super.authenticate("student1");
		p.setStudent((Student) this.service.findByUserAccount(LoginService.getPrincipal().getId()));

		StudentCard d;
		d = new StudentCard();

		d.setCentre("centre");
		d.setCode(1234);

		p.setStudentCard(d);

		p.getTicker().setTicker("190419-000000");

		Proclaim saved;
		saved = this.service.save(p);

		System.out.println(saved.getTicker().getTicker());

		Assert.isTrue(saved.getId() != 0);
	}
}
