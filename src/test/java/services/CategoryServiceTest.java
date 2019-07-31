
package services;

/*
 * SampleTest.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Category;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CategoryServiceTest extends AbstractTest {

	@Autowired
	ActorService	actorService;

	@Autowired
	CategoryService	service;


	/*
	 * TEST 1
	 * Requirement tested: There are a lot of kind of proclaims understood under Category´s name. Teachers proclaims, Building Infrastructure Proclaim,
	 * Teaching Schedule or even bullying. They are organised into a hierarchy controlled by the administrators
	 * - Analysis of sentence coverage of CategoryService: 10.3%
	 * Total instructions: 232; Covered Instructions: 24
	 * 
	 * - Analysis of data coverage: 50%
	 * Attribute: name| Bad value: - | Normal value: Yes | Coverage: 50% |
	 */

	@Test
	public void test1() {
		final Object testingData[][] = {
			{
				//Positive test: El administrador es quien crea la categoria
				"admin1", "hola", null
			}, {
				//Negative test: Un miembro no puede crear una categoria.
				"member1", "hola", IllegalArgumentException.class
			}, {
				//Negative test: Un admin1 intenta crear una categoria vacia
				"admin1", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template1((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void template1(final String username, final String word, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(username);

			Category c;
			c = this.service.createCategory();

			c.setName(word);

			this.service.save(c);

			this.service.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
