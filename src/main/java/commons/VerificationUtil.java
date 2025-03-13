package commons;

import java.util.Random;

public class VerificationUtil {

	public static String generateVerificationCode() {
		Random random = new Random();
		int code = random.nextInt(900000) + 100000;
		return String.valueOf(code);
	}

}
