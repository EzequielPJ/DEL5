
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Ticketable extends DomainEntity {

	private Ticker	ticker;


	@OneToOne(optional = false, cascade = CascadeType.ALL)
	public Ticker getTicker() {
		return this.ticker;
	}

	public void setTicker(final Ticker ticker) {
		this.ticker = ticker;
	}

}
