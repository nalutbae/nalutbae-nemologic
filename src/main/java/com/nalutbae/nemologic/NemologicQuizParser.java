package com.nalutbae.nemologic;

import java.net.URLDecoder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * An application that parses the HTML of the quiz page
 * at <a href="http://nemonemologic.com">nemonemologic.com</a>
 * and solves the Nemonemologic puzzle on that page.
 *  
 * @author nalutbae
 *
 */
public class NemologicQuizParser {
	
	static final String QUIZ_URL = "http://nemonemologic.com/play_logic.php?quid=43";

	public static void main(String[] args) throws Exception {
		String link = QUIZ_URL;
		if(args != null && args.length > 0) link = args[0];
		Document doc = Jsoup.connect(link).get();
		Element input = doc.getElementById("data-holder");		
		String endcodeValue = input.attr("value");
		String value = URLDecoder.decode(endcodeValue, "UTF-8");
		
		JSONParser jsonParser = new JSONParser();
		JSONObject valueObj = (JSONObject) jsonParser.parse(value);		
		JSONArray hhints = (JSONArray) valueObj.get("hhints");
		JSONArray vhints = (JSONArray) valueObj.get("vhints");
		
		int[][] hhintsArray = transformArray(hhints);		
		int[][] vhintsArray = transformArray(vhints);
		
		NemoLogicSolver solver = new NemoLogicSolver(hhintsArray, vhintsArray);
		solver.process();		
	}

	private static int[][] transformArray(JSONArray jsonArray) {
		int[][] transed = new int[jsonArray.size()][];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONArray row = (JSONArray) jsonArray.get(i);
			transed[i] = new int[row.size()];
			for (int j = 0; j < row.size(); j++) {
				transed[i][j] = ((Long) row.get(j)).intValue();
			}
		}
		return transed;
	}
}
