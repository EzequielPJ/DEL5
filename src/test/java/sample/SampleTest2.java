
package sample;

import com.mifmif.common.regex.Generex;

public class SampleTest2 {

	public static void main(final String[] args) {
		System.out.println(new Generex("[a-zA-Z0-9]{6}").random().toUpperCase());
	}
}
