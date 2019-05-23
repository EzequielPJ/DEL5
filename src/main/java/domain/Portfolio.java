
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Portfolio extends DomainEntity {

	private String							title;
	private Date							moment;
	private String							fullName;
	private String							address;
	private String							photo;
	private String							phone;
	private Collection<WorkReport>			workReport;
	private Collection<StudyReport>			studyReport;
	private Collection<MiscellaneousReport>	miscellaneousReport;


	@NotBlank
	@SafeHtml
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotBlank
	@SafeHtml
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(final String fullName) {
		this.fullName = fullName;
	}

	@SafeHtml
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@URL
	@SafeHtml
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@SafeHtml
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	@OneToMany
	public Collection<WorkReport> getWorkReport() {
		return this.workReport;
	}

	public void setWorkReport(final Collection<WorkReport> workReport) {
		this.workReport = workReport;
	}

	@OneToMany
	public Collection<StudyReport> getStudyReport() {
		return this.studyReport;
	}

	public void setStudyReport(final Collection<StudyReport> studyReport) {
		this.studyReport = studyReport;
	}

	@OneToMany
	public Collection<MiscellaneousReport> getMiscellaneousReport() {
		return this.miscellaneousReport;
	}

	public void setMiscellaneousReport(final Collection<MiscellaneousReport> miscellaneousReport) {
		this.miscellaneousReport = miscellaneousReport;
	}

}
