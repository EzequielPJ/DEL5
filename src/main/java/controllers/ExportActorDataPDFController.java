
package controllers;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import security.Authority;
import security.LoginService;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

import domain.Actor;
import domain.Administrator;
import domain.Box;
import domain.Collaborator;
import domain.Comission;
import domain.Comment;
import domain.Event;
import domain.Member;
import domain.MessageEntity;
import domain.MiscellaneousReport;
import domain.Notes;
import domain.Portfolio;
import domain.Proclaim;
import domain.Profile;
import domain.Sponsor;
import domain.Sponsorship;
import domain.Student;
import domain.StudyReport;
import domain.Swap;
import domain.WorkReport;

public class ExportActorDataPDFController extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(final Map<String, Object> model, final Document document, final PdfWriter writer, final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		response.setHeader("Content-Disposition", "attachment; filename=\"Personal-Information.pdf\"");

		Actor a;
		a = (Actor) model.get("actor");

		Table table;
		table = new Table(1);
		table.addCell(new Paragraph("DEL5\n"));

		if (this.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.ADMIN)) {
			Administrator admin;
			admin = (Administrator) a;
			this.commonThings(table, admin);

			String value;
			value = "";
			Collection<Profile> pr;
			pr = (Collection<Profile>) model.get("profiless");
			for (final Profile profile : pr)
				value = " Link: " + profile.getLink() + "\n Social Network Name: " + profile.getSocialNetworkName() + "\n Nick: " + profile.getNick() + "\n";
			if (!pr.isEmpty())
				table.addCell("Profile: " + value + "\n");

		} else if (this.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.MEMBER)) {
			Member c;
			c = (Member) a;
			this.commonThings(table, c);

			String value;
			value = "";
			Collection<Profile> pr;
			pr = (Collection<Profile>) model.get("profiless");
			for (final Profile profile : pr)
				value = " Link: " + profile.getLink() + "\n Social Network Name: " + profile.getSocialNetworkName() + "\n Nick: " + profile.getNick() + "\n";
			if (!pr.isEmpty())
				table.addCell("Profile: " + value + "\n");

			final Collection<Comission> pos = (Collection<Comission>) model.get("comissions");
			String m;
			m = "";
			for (final Comission p : pos)
				m += "\n" + " Title: " + p.getName() + ",\n Description: " + p.getDescription() + ",\n Profile Required: " + p.getMoment() + "\n";
			if (!pos.isEmpty())
				table.addCell("Comissions: " + m + "\n");

			final Collection<Proclaim> pro = (Collection<Proclaim>) model.get("proclaims");
			String w;
			w = "";
			for (final Proclaim p : pro)
				if (p.getCategory() == null)
					w += "\n" + " Title: " + p.getTitle() + ",\n Description: " + p.getDescription() + ",\n Moment: " + p.getMoment() + ",\n Status: " + p.getStatus() + ",\n Attachments: " + p.getAttachments() + ",\n Student: " + p.getStudent().getName()
						+ ",\n Law: " + p.getLaw() + ",\n Reason: " + p.getReason() + "\n";
				else
					w += "\n" + " Title: " + p.getTitle() + ",\n Description: " + p.getDescription() + ",\n Moment: " + p.getMoment() + ",\n Status: " + p.getStatus() + ",\n Attachments: " + p.getAttachments() + ",\n Category: "
						+ p.getCategory().getName() + ",\n Student: " + p.getStudent().getName() + ",\n Law: " + p.getLaw() + ",\n Reason: " + p.getReason() + "\n";
			if (!pro.isEmpty())
				table.addCell("Proclaims: " + w + "\n");

			final Collection<Comment> com = (Collection<Comment>) model.get("commentsMember");
			String t;
			t = "";
			for (final Comment p : com)
				t += "\n" + " Description: " + p.getDescription() + ",\n Proclaim: " + p.getProclaim().getTitle() + ",\n Attachments: " + p.getAttachments() + "\n";
			if (!com.isEmpty())
				table.addCell("Comments: " + t + "\n");

			final Collection<Notes> not = (Collection<Notes>) model.get("notesMember");
			String n;
			n = "";
			for (final Notes p : not)
				n += "\n" + " Description: " + p.getDescription() + ",\n Proclaim: " + p.getNote() + ",\n Attachments: " + p.getEvent().getTitle() + "\n";
			if (!not.isEmpty())
				table.addCell("Notes: " + n + "\n");

		} else if (this.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.COLLABORATOR)) {
			Collaborator h;
			h = (Collaborator) a;
			this.commonThings(table, h);

			String value;
			value = "";
			Collection<Profile> pr;
			pr = (Collection<Profile>) model.get("profiless");
			for (final Profile profile : pr)
				value = " Link: " + profile.getLink() + "\n Social Network Name: " + profile.getSocialNetworkName() + "\n Nick: " + profile.getNick() + "\n";
			if (!pr.isEmpty())
				table.addCell("Profile: " + value + "\n");

			final Collection<Event> ev = (Collection<Event>) model.get("events");
			String e;
			e = "";
			for (final Event p : ev)
				e += "\n" + " Title: " + p.getTitle() + ",\n Description: " + p.getDescription() + ",\n Status: " + p.getStatus() + ",\n Moments: " + p.getMoment() + "\n";
			if (!ev.isEmpty())
				table.addCell("Events: " + e + "\n");

			final Collection<Portfolio> po = (Collection<Portfolio>) model.get("portfolio");

			String p;
			p = "";
			for (final Portfolio am : po) {
				p += "\n" + " Title: " + am.getTitle() + ",\n Full Name: " + am.getFullName() + ",\n Address: " + am.getAddress() + ",\n Phone: " + am.getPhone() + ",\n Moment: " + am.getMoment() + "\n";

				for (final MiscellaneousReport md : am.getMiscellaneousReport())
					p += "\n Miscellaneous Report:" + "\n Title: " + md.getTitle() + ",\n Text: " + md.getText() + ",\n Attachments: " + md.getAttachments() + ",\n Moment: " + md.getMoment() + "\n";
				for (final StudyReport pd : am.getStudyReport())
					p += "\n Study Report:" + "\n Title: " + pd.getTitle() + ",\n Course: " + pd.getCourse() + ",\n Average: " + pd.getAverage() + ",\n Percentaje Credits: " + pd.getPercentajeCredits() + ",\n Moment: " + pd.getMoment() + ",\n StartDate: "
						+ pd.getStartDate() + ",\n EndDate: " + pd.getEndDate() + "\n";
				for (final WorkReport ed : am.getWorkReport())
					p += "\n Work Report:" + "\n Title: " + ed.getTitle() + ",\n Text: " + ed.getText() + ",\n Business Name: " + ed.getBusinessName() + ",\n Moment: " + ed.getMoment() + ",\n StartDate: " + ed.getStartDate() + ",\n EndDate: "
						+ ed.getEndDate() + "\n";
			}
			if (!po.isEmpty())
				table.addCell("Portfolio: " + p + "\n");

			final Collection<Swap> sw = (Collection<Swap>) model.get("swapR");
			String l;
			l = "";
			for (final Swap s : sw)
				l += "\n" + " Title: " + s.getReceiver().getName() + " Title: " + s.getDescription() + ",\n Description: " + s.getPhone() + ",\n Status: " + s.getStatus() + ",\n Moments: " + s.getComission().getName() + "\n";
			if (!sw.isEmpty())
				table.addCell("Swaps Request: " + l + "\n");

			final Collection<Swap> sp = (Collection<Swap>) model.get("swapP");
			String c;
			c = "";
			for (final Swap s : sp)
				c += "\n" + " Title: " + s.getSender().getName() + " Title: " + s.getDescription() + ",\n Description: " + s.getPhone() + ",\n Status: " + s.getStatus() + ",\n Moments: " + s.getComission().getName() + "\n";
			if (!sw.isEmpty())
				table.addCell("Swaps Request: " + c + "\n");

			final Collection<Notes> not = (Collection<Notes>) model.get("notesCollaborator");
			String n;
			n = "";
			for (final Notes pa : not)
				n += "\n" + " Description: " + pa.getDescription() + ",\n Proclaim: " + pa.getNote() + ",\n Attachments: " + pa.getEvent().getTitle() + "\n";
			if (!not.isEmpty())
				table.addCell("Notes: " + n + "\n");

		} else if (this.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.STUDENT)) {
			Student aud;
			aud = (Student) a;
			this.commonThings(table, aud);

			String value;
			value = "";
			Collection<Profile> pr;
			pr = (Collection<Profile>) model.get("profiless");
			for (final Profile profile : pr)
				value = " Link: " + profile.getLink() + "\n Social Network Name: " + profile.getSocialNetworkName() + "\n Nick: " + profile.getNick() + "\n";
			if (!pr.isEmpty())
				table.addCell("Profile: " + value + "\n");

			final Collection<Proclaim> au = (Collection<Proclaim>) model.get("pro");

			String m;
			m = "";
			for (final Proclaim p : au)
				m += "\n" + " Title: " + p.getTitle() + ",\n Description: " + p.getDescription() + ",\n Moment: " + p.getMoment() + ",\n Status: " + p.getStatus() + ",\n Attachments: " + p.getAttachments() + ",\n Category: " + p.getCategory().getName()
					+ ",\n Student: " + p.getStudent().getName() + ",\n Law: " + p.getLaw() + ",\n Reason: " + p.getReason() + "\n";

			table.addCell("Proclaims: " + m + "\n\n");

			final Collection<Comment> com = (Collection<Comment>) model.get("commentsStudent");
			String t;
			t = "";
			for (final Comment p : com)
				t += "\n" + " Description: " + p.getDescription() + ",\n Proclaim: " + p.getProclaim().getTitle() + ",\n Attachments: " + p.getAttachments() + "\n";
			if (!com.isEmpty())
				table.addCell("Comments: " + t + "\n");

			final Collection<Notes> not = (Collection<Notes>) model.get("notesStudent");
			String n;
			n = "";
			for (final Notes p : not)
				n += "\n" + " Description: " + p.getDescription() + ",\n Proclaim: " + p.getNote() + ",\n Attachments: " + p.getEvent().getTitle() + "\n";
			if (!not.isEmpty())
				table.addCell("Notes: " + n + "\n");

		} else if (this.findAuthority(LoginService.getPrincipal().getAuthorities(), Authority.SPONSOR)) {
			Sponsor pro;
			pro = (Sponsor) a;
			this.commonThings(table, pro);

			String value;
			value = "";
			Collection<Profile> pr;
			pr = (Collection<Profile>) model.get("profiless");
			for (final Profile profile : pr)
				value = " Link: " + profile.getLink() + "\n Social Network Name: " + profile.getSocialNetworkName() + "\n Nick: " + profile.getNick() + "\n";
			if (!pr.isEmpty())
				table.addCell("Profile: " + value + "\n");

			final Collection<Sponsorship> spo = (Collection<Sponsorship>) model.get("sponsorshipsAct");
			String j;
			j = "";
			for (final Sponsorship spon : spo)
				j += "\n" + "\n Banner URL: " + spon.getBanner() + ",\n Target URL: " + spon.getTarget() + ",\n Holder Credit Card Associated: " + spon.getCreditCard().getHolder() + ",\n Number Credit Card Associated: " + spon.getCreditCard().getNumber()
					+ "\n";

			table.addCell("Activate Sponsorships: " + j + "\n\n");

			final Collection<Sponsorship> spod = (Collection<Sponsorship>) model.get("sponsorshipsDes");
			String p;
			p = "";
			for (final Sponsorship spons : spod)
				p += "\n" + "\n Banner URL: " + spons.getBanner() + ",\n Target URL: " + spons.getTarget() + ",\n Holder Credit Card Associated: " + spons.getCreditCard().getHolder() + ",\n Number Credit Card Associated: "
					+ spons.getCreditCard().getNumber() + "\n";

			table.addCell("Desactivate Sponsorships: " + p + "\n\n");
		}

		document.add(table);
	}
	public void commonThings(final Table table, final Actor a) {

		try {
			table.addCell("Personal Data \n" + this.takeField(a) + "\n");
			table.addCell("Boxes \n" + this.takeFields(a.getBoxes()) + "\n");

			String m;
			m = "\n";
			for (final Box b : a.getBoxes()) {
				m += "\n" + b.getName() + "\n";
				Collection<MessageEntity> mess;
				mess = b.getMessageEntity();
				for (final MessageEntity message : mess)
					m += "\n" + " Sender: " + message.getSender().getName() + "\n Body: " + message.getBody() + "\n Priority: " + message.getPriority() + "\n Subject: " + message.getSubject() + "\n Tags: " + message.getTags() + "\n Moment Sent: "
						+ message.getMomentsent() + "\n";
			}
			table.addCell("Messages \n" + m + "\n");
			table.addCell("Account Values \n" + this.takeFields(a.getAccount()) + "\n");

		} catch (final BadElementException e) {
			System.out.println("Error al procesar");
		}

	}
	private String takeFields(final Object o) {
		String res = "";

		if (o instanceof Collection)
			for (final Object e : (Collection<Object>) o)
				res += this.takeField(e);
		else
			res += this.takeField(o);

		return res;
	}

	private String takeField(final Object o) {
		String res = "";
		Field[] iterate;

		if (o instanceof Actor)
			iterate = o.getClass().getSuperclass().getDeclaredFields();
		else
			iterate = o.getClass().getDeclaredFields();

		for (final Field field : iterate) {
			field.setAccessible(true);
			String name;
			name = field.getName();

			if (name == "id" || name == "account" || name == "profiles" || name == "serialVersionUID" || name == "enabled" || name == "spammer" || name == "fromSystem" || name == "messageEntity")
				continue;
			else {

				Object value = null;

				try {
					value = field.get(o);
				} catch (final IllegalArgumentException e) {
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				}
				if (name.equals("creditCard") || name.equals("boxes"))
					res = res;
				else
					res = res + name + " = " + value + "\n";

			}
		}
		return res;
	}

	private Boolean findAuthority(final Collection<Authority> comp, final String a) {
		Boolean res = false;
		if (comp.size() > 1) {
			Authority aut;
			aut = new Authority();
			aut.setAuthority(a);
			res = comp.contains(aut);
		} else
			for (final Authority authority : comp)
				if (authority.toString().equals(a))
					res = true;

		return res;
	}

}
