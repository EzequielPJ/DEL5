
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Notes extends DomainEntity {

	private Double	note;
	private String	description;
	private Event	event;
	private Actor	actor;


	@DecimalMin("0")
	@DecimalMax("10")
	public Double getNote() {
		return this.note;
	}

	public void setNote(final Double note) {
		this.note = note;
	}

	@NotBlank
	@SafeHtml
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@ManyToOne(optional = false)
	public Event getEvent() {
		return this.event;
	}

	public void setEvent(final Event event) {
		this.event = event;
	}

	@ManyToOne(optional = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public Actor getActor() {
		return this.actor;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}

}
