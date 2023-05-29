import java.util.*;

public class GameStrategy {
	public static String toMenu(String initialPrompt, String[] options) {
		return initialPrompt + '\n' + toMenu(options);
	}
	public static String toMenu(String[] options) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < options.length; i++) {
			sb.append((i) + ": " + options[i] + "\n");
		}
		return sb.toString();
	}
}
