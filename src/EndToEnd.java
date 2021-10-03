import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;

import files.Payload;
import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class EndToEnd {

	// Create place -> Update place with new address -> Get place to validate if new address is present in response

	public static void main(String[] args) {

		// Create place, save response in string var and extract place_id in var
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(Payload.AddPlace())
		.when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)")
		.extract().response().asString();
		
		System.out.println(response);
		JsonPath extracted_json = new JsonPath(response);
		String placeId = extracted_json.getString("place_id");
		System.out.println(placeId);
		
		// Update place
		String newAddress = "70 winter walk, USA";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\r\n"
				+ "\"place_id\":\"" + placeId + "\",\r\n"
				+ "\"address\":\"" + newAddress + "\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n")
		.when().put("/maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		// Get place
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("/maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js1.getString("address");
		System.out.println(actualAddress);
		
		// Assert that actual address matches updated address
		Assert.assertEquals(actualAddress, newAddress);
	}

}
