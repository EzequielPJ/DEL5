
package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;

import security.Authority;
import domain.Actor;
import domain.Box;
import domain.MessageEntity;

public class AbstractService {

	public String limpiaCadena(final String s) {
		return s.replaceAll("[^a-zA-Z0-9$]", "");
	}

	public Collection<String> limpiaString(final String s) {
		return Arrays.asList(s.replaceAll("[^a-zA-Z0-9$]", "#").trim().split("##|#"));
	}
	public Collection<Box> initBoxes() {

		List<Box> boxesSystem;

		boxesSystem = new ArrayList<Box>();

		boxesSystem.add(this.createBox(true, "In Box"));
		boxesSystem.add(this.createBox(true, "Out Box"));
		boxesSystem.add(this.createBox(true, "Spam Box"));
		boxesSystem.add(this.createBox(true, "Notification Box"));
		boxesSystem.add(this.createBox(true, "Trash Box"));

		return boxesSystem;

	}
	public Box createBox(final boolean fromSystem, final String name) {

		Box b;

		b = new Box();

		b.setMessageEntity(new ArrayList<MessageEntity>());

		b.setFromSystem(fromSystem);

		b.setName(name);

		return b;

	}
	public boolean spamWord(final Collection<String> contentMessage) {
		boolean res = false;
		String str;
		str = "";
		for (final String s : contentMessage)
			str += s + " ";

		Map<String, Boolean> result;
		result = new HashMap<>();

		for (final String word : System.getProperty("spamwords").split(",")) {
			result.put(word, contentMessage.contains(word.trim().toLowerCase()));
			result.put(word, word.trim().equals(str.trim()));

		}

		for (final Boolean b : result.values())
			if (b) {
				res = true;
				break;
			}
		return res;
	}

	public Boolean findAuthority(final Collection<Authority> comp, final String a) {
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
	private boolean isSpammer(final Collection<MessageEntity> cm) {
		boolean res = false;
		if (cm.size() != 0)
			for (final MessageEntity message : cm) {
				final boolean spam = this.spamWord(this.limpiaString(message.getSubject())) || this.spamWord(this.limpiaString(message.getBody()));
				if (spam) {
					res = true;
					break;
				}
			}
		return res;
	}
	public boolean checkSpammer(final Actor a) {
		boolean res;
		res = false;
		for (final Box b : a.getBoxes())
			if (b.getName().equals("Out Box"))
				res = this.isSpammer(b.getMessageEntity());
		return res;
	}

	public void checkLinkedInOrGithub(final String url) {
		Assert.isTrue(url.substring(0, 22).equals("http://www.github.com/") || url.substring(0, 24).equals("http://www.linkedin.com/") || url.substring(0, 23).equals("https://www.github.com/") || url.substring(0, 25).equals("https://www.linkedin.com/"));
	}

	public Boolean spamTags(final Collection<String> col) {
		boolean res = false;
		String str;
		str = "";
		for (final String s : col)
			str += s + " ";

		Map<String, Boolean> result;
		result = new HashMap<>();

		for (final String word : System.getProperty("spamwords").split(",")) {
			result.put(word, col.contains(word.trim().toLowerCase()));
			result.put(word, word.trim().equals(str.trim()));
		}
		for (final Boolean b : result.values())
			if (b) {
				res = true;
				break;

			}
		return res;
	}
	public Boolean checkScript(final Collection<String> col) {
		Boolean res = false;
		for (final String a : col)
			if (a.length() >= 8)
				if (a.substring(0, 8).equals("<script>"))
					res = true;
		return res;
	}

	public String getLanguageSystem() {
		return LocaleContextHolder.getLocale().getLanguage();

	}
	public Set<String> statusByLang() {
		Map<String, Set<String>> result;
		result = new HashMap<String, Set<String>>();

		result.put("en", new HashSet<>(Arrays.asList("PENDING", "SUBMITTED", "ACCEPTED", "REJECTED")));
		result.put("es", new HashSet<>(Arrays.asList("PENDIENTE", "ENVIADO", "ACEPTADO", "RECHAZADO")));

		return result.get(this.getLanguageSystem());
	}

}
