package files;

import io.restassured.path.json.JsonPath;

public class ReusableMethods {
	
	public static JsonPath rawToJson(String string) {
		
		JsonPath json_obj = new JsonPath(string);
		return json_obj;
	}
}
