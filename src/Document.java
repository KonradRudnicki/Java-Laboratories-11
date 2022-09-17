import java.util.Scanner;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document implements IWithName {
	public String name;
	// TODO? You can change implementation of Link collection
	public SortedMap<String, Link> link;

	public Document(String name) {
		this.name = name.toLowerCase();
		link = new TreeMap<String, Link>();
	}

	public Document(String name, Scanner scan) {
		this.name = name.toLowerCase();
		link = new TreeMap<String, Link>();
		load(scan);
	}

	public void load(Scanner scan) {
		while (scan.hasNext()) {
			String line = scan.nextLine();
			if (line.equals("eod")) return;

			String[] oneLine = line.split(" ");
			String regex = "link=(.+)";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher;

			for (String string : oneLine) {
				matcher = pattern.matcher(string);

				if (matcher.matches())
					if (isCorrectLink(matcher.group(1))) {
						Link linkToAdd = new Link(matcher.group(1).toLowerCase());
						link.put(linkToAdd.ref, linkToAdd);

					} else if (checkWithWeight(matcher.group(1))) {
						Link linkToAdd = createLink(matcher.group(1).toLowerCase());
						assert linkToAdd != null;

						link.put(linkToAdd.ref, linkToAdd);
					}
			}
		}
	}

	public static boolean isCorrectId(String id) {
		String regex = "[a-z]*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(id);

		return matcher.matches();
	}

	// accepted only small letters, capitalic letter, digits nad '_' (but not on the begin)
	static Link createLink(String link) {
		String regex = "([a-zA-Z][a-zA-Z_0-9]*)\\((.*)\\)";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(link);

		if (matcher.matches()) {
			return new Link(matcher.group(1), Integer.parseInt(matcher.group(2)));
		}

		regex = "([a-zA-Z][a-zA-Z_0-9]*)";
		pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(link);

		if (matcher.matches()) {
			return new Link(matcher.group(1));
		}

		return null;
	}

	public static boolean isCorrectLink(String id) {
		String regex = "[a-zA-Z][a-zA-Z_0-9]*";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(id);

		return matcher.matches();
	}

	public static boolean checkWithWeight(String id) {
		String regex = "[a-zA-Z][a-zA-Z_0-9]*(.*)";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(id);

		if (matcher.matches()) {
			String weightReg = "(\\({1}([1-9]{1}[0-9]*)\\){1})";
			Pattern weightPat = Pattern.compile(weightReg);
			Matcher weightMatch = weightPat.matcher(matcher.group(1));

			return weightMatch.matches();
		}

		return false;
	}

	@Override
	public String toString() {
		String retStr = "Document: " + name + "\n";
		//TODO?
		retStr += link;
		return retStr;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		Document document = (Document) o;

		return Objects.equals(name, document.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String getName() {
		return name;
	}
}