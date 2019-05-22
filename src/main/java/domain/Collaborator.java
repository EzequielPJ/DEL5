
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Collaborator extends Actor {

	private Comission	comission;
	private Portfolio		portfolio;


	@ManyToOne(optional = true)
	public Comission getComission() {
		return this.comission;
	}

	public void setComission(final Comission comission) {
		this.comission = comission;
	}

	@OneToOne(optional = true)
	public Portfolio getPortfolio() {
		return this.portfolio;
	}

	public void setPortfolio(final Portfolio portfolio) {
		this.portfolio = portfolio;
	}

}
