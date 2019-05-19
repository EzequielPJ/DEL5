
package controllers;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import forms.ActorForm;

public abstract class BasicController extends AbstractController {

	/*
	 * Estos metodos permiten introducir las operaciones que trabajaran dentro del bloque try.
	 * 
	 * Save
	 * Delete
	 */

	public abstract <T> ModelAndView saveAction(T e, BindingResult binding, String nameResolver);
	public abstract <T> ModelAndView deleteAction(T e, String nameResolver);

	/*
	 * Lista de Elementos
	 * La diferenciacion entre roles debera hacerse en el controlador hijo.
	 */

	//List
	public <T> ModelAndView listModelAndView(final String nameCollection, final String nameView, final Collection<T> objects, final String requestURI) {
		ModelAndView result;
		result = this.custom(new ModelAndView(nameView));
		result.addObject(nameCollection, objects);
		result.addObject("requestURI", requestURI);
		return result;
	}
	//Create
	public <T> ModelAndView create(final T e, final String nameView, final String requestURI, final String requestCancel) {
		return this.createAndEditModelAndView(e, nameView, requestURI, requestCancel);
	}

	//Show
	public <T> ModelAndView show(final T e, final String nameView, final String requestURI, final String requestCancel) {
		ModelAndView result;
		result = this.createAndEditModelAndView(e, nameView, requestURI, requestCancel);
		result.addObject("view", true);
		return result;
	}

	//Show
	public <T> ModelAndView edit(final T e, final String nameView, final String requestURI, final String requestCancel) {
		ModelAndView result;
		result = this.createAndEditModelAndView(e, nameView, requestURI, requestCancel);
		result.addObject("view", false);
		return result;
	}

	// Save - With Form Objects
	public <T> ModelAndView save(final T e, final BindingResult binding, final String error, final String nameView, final String requestURI, final String requestCancel, final String nameResolver) {

		ModelAndView result = null;

		try {
			//Llamo al metodo abstracto descrito arriba. En mi clase hija solo tendre que reevaluar el contenido del mismo.
			result = this.custom(this.saveAction(e, binding, nameResolver));
		} catch (final ValidationException valid) {
			result = this.createAndEditModelAndView(e, nameView, requestURI, requestCancel);
			result.addObject("view", false);
			if (e instanceof ActorForm)
				result.addObject("makes", super.creditCardMakes());
		} catch (final Throwable oops) {
			result = this.createAndEditModelAndView(e, error, nameView, requestURI, requestCancel);
			result.addObject("view", false);
			//			if (e instanceof Position)
			//				((Position) e).setTicker(null);
			if (e instanceof ActorForm)
				result.addObject("makes", super.creditCardMakes());
			result.addObject("duplicate", true);
		}

		return result;
	}
	// Delete
	public <T> ModelAndView delete(final T e, final String error, final String nameView, final String requestURI, final String requestCancel, final String nameResolver) {
		ModelAndView result;
		try {
			result = this.custom(this.deleteAction(e, nameResolver));
		} catch (final Throwable oops) {
			result = this.createAndEditModelAndView(e, error, nameView, requestURI, requestCancel);
		}
		return result;
	}

	// Protected Methods for Objects Form
	protected <T> ModelAndView createAndEditModelAndView(final T e, final String nameView, final String requestURI, final String requestCancel) {
		ModelAndView result;

		result = this.createAndEditModelAndView(e, null, nameView, requestURI, requestCancel);

		return result;
	}
	protected <T> ModelAndView createAndEditModelAndView(final T e, final String message, final String nameView, final String requestURI, final String requestCancel) {
		ModelAndView result;

		result = new ModelAndView(nameView);
		result.addObject(this.nameEntity(e), e);
		result.addObject("message", message);

		this.editFormsUrlId(requestURI, requestCancel, result);

		return this.custom(result);
	}
	public <T> String nameEntity(final T e) {
		String s = e.getClass().getSimpleName();
		s = s.replace(s.charAt(0), Character.toLowerCase(s.charAt(0)));

		if (e instanceof ActorForm)
			s = "actor";

		return s;
	}
	// Estos dos metodos permiten construir urls a partir del Map dado
	public void editFormsUrlId(final String requestURI, final String requestCancel, final ModelAndView parameter) {
		parameter.addObject("requestURI", requestURI);
		parameter.addObject("requestCancel", requestCancel);
	}
}
