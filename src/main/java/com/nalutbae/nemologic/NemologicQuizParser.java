package com.nalutbae.nemologic;

import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URLDecoder;

/**
 * An application that parses the HTML of the quiz page
 * at <a href="http://nemonemologic.com">nemonemologic.com</a>
 * and solves the Nemonemologic puzzle on that page.
 *  
 * @author nalutbae
 */
public class NemologicQuizParser {

	private static final String QUIZ_URL = "http://nemonemologic.com/play_logic.php?quid=43";

	public static void main(String[] args) throws Exception {
		String link = (args != null && args.length > 0) ? args[0] : QUIZ_URL;
		Document doc = Jsoup.connect(link).get();
		Element input = doc.getElementById("data-holder");
		String encodedValue = input.attr("value");
		String decodedValue = URLDecoder.decode(encodedValue, "UTF-8");

		JSONObject valueObj = parseJson(decodedValue);
		int[][] hhintsArray = transformArray((JSONArray) valueObj.get("hhints"));
		int[][] vhintsArray = transformArray((JSONArray) valueObj.get("vhints"));

		NemoLogicSolver solver = new NemoLogicSolver(hhintsArray, vhintsArray);
		solver.process();
	}

	private static JSONObject parseJson(String json) throws ParseException {
		return (JSONObject) new JSONParser().parse(json);
	}

	private static int[][] transformArray(JSONArray jsonArray) {
		int[][] transposed = new int[jsonArray.size()][];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONArray row = (JSONArray) jsonArray.get(i);
			transposed[i] = new int[row.size()];
			for (int j = 0; j < row.size(); j++) {
				transposed[i][j] = ((Long) row.get(j)).intValue();
			}
		}
		return transposed;
	}
}
