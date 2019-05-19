
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Box extends DomainEntity {

	private String						name;
	private boolean						fromSystem;
	private Collection<MessageEntity>	messageEntity;
	private Collection<Box>				boxes;


	@NotBlank
	@SafeHtml
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isFromSystem() {
		return this.fromSystem;
	}

	public void setFromSystem(final boolean fromSystem) {
		this.fromSystem = fromSystem;
	}

	@ManyToMany(mappedBy = "box")
	public Collection<MessageEntity> getMessageEntity() {
		return this.messageEntity;
	}

	public void setMessageEntity(final Collection<MessageEntity> mesageEntity) {
		this.messageEntity = mesageEntity;
	}

	@OneToMany(cascade = {
		CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH
	})
	public Collection<Box> getBoxes() {
		return this.boxes;
	}

	public void setBoxes(final Collection<Box> boxes) {
		this.boxes = boxes;
	}

}
