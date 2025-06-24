package BookStore.EndPoints;

import static io.restassured.RestAssured.given;

import org.testng.ITestContext;

import BookStore.Payload.CreateBookPayload;
import BookStore.Payload.LoginPayload;
import BookStore.Payload.SignupPayload;
import BookStore.Payload.updateBookPayload;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class BooksAppEndPoints {

	public static Response userSignup(SignupPayload payload) {
		Response response = given().log().all().contentType(ContentType.JSON).body(payload).when().post(Routes.signupURL);
		return response;
	}
	
	public static Response userLogin(LoginPayload payload) {
		Response response = given().log().all().contentType(ContentType.JSON).body(payload).when().post(Routes.loginURL);
		return response;
	}
	public static Response createBook(CreateBookPayload payload,ITestContext context) {
		String token = (String) context.getAttribute("token");
		Response response = given().log().all().contentType(ContentType.JSON).header("Authorization", "Bearer " + token).body(payload).when().post(Routes.postBooksURL);
		return response;
	}
	public static Response getAllBook(ITestContext context) {
		String token = (String) context.getAttribute("token");
		Response response = given().log().all().contentType(ContentType.JSON).header("Authorization", "Bearer " + token).when().get(Routes.getBooksURL);
		return response;
	}
	public static Response getBookById(int id,ITestContext context) {
		String token = (String) context.getAttribute("token");
		Response response = given().log().all().contentType(ContentType.JSON).header("Authorization", "Bearer " + token).when().get(Routes.getbyidBooksURL,id);
		return response;
	}
	public static Response updateBook(updateBookPayload payload, int bookId,ITestContext context) {
		String token = (String) context.getAttribute("token");
		Response response = given().log().all().contentType(ContentType.JSON).header("Authorization", "Bearer " + token).body(payload).when().put(Routes.putBooksURL, bookId);
		return response;
	}
	public static Response deleteBook(int bookId,ITestContext context) {
		String token = (String) context.getAttribute("token");
		Response response = given().log().all().contentType(ContentType.JSON).header("Authorization", "Bearer " + token).when().delete(Routes.deleteBooksURL, bookId);
		return response;
	}
}
