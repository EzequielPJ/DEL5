
package sample;

import com.mifmif.common.regex.Generex;

public class SampleTest2 {

	public static void main(final String[] args) {
		Generex generex;
		generex = new Generex("[a-zA-Z0-9]{6}");
		System.out.println(generex.random().toUpperCase());
	}
}
