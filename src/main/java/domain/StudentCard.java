
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Embeddable
@Access(AccessType.PROPERTY)
public class StudentCard {

	private String	centre;
	private int		code;
	private String	vat;


	@SafeHtml
	@NotBlank
	public String getVat() {
		return this.vat;
	}

	public void setVat(final String vat) {
		this.vat = vat;
	}

	@SafeHtml
	@NotBlank
	public String getCentre() {
		return this.centre;
	}

	public void setCentre(final String centre) {
		this.centre = centre;
	}
	@Min(value = 4)
	@Max(value = 4)
	public int getCode() {
		return this.code;
	}

	public void setCode(final int code) {
		this.code = code;
	}

}
