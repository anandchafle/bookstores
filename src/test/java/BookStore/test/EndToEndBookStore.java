package BookStore.test;

import org.json.JSONObject;
import org.openxmlformats.schemas.presentationml.x2006.main.STBookmarkIdSeed;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import BookStore.EndPoints.BooksAppEndPoints;
import BookStore.Payload.CreateBookPayload;
import BookStore.Payload.LoginPayload;
import BookStore.Payload.SignupPayload;
import BookStore.Payload.updateBookPayload;
import io.restassured.response.Response;

import static org.testng.Assert.assertEquals;

import java.util.Random;
import java.util.UUID;

public class EndToEndBookStore {

    int userId;
    String userEmail;
    String userPassword;
    String token;
    int bookid ;
	String bookName;
	int pubYear;
	static String[] authors = {
	        "John Doe", "Jane Smith", "Mark Twain", "Agatha Christie", "J.K. Rowling"
	    };

	    static String[] summaries = {
	        "A thrilling adventure", 
	        "A heartwarming tale", 
	        "A journey of mystery and magic", 
	        "A guide to personal growth", 
	        "A story of love and loss"
	    };

    @BeforeTest
    public void generateRandomUser() {
        userId = new Random().nextInt(10000);
        bookid = new Random().nextInt(10000);
        bookName = UUID.randomUUID().toString().substring(0, 6);
        pubYear = 1990 + new Random().nextInt(35);
        userEmail = "user" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com";
        userPassword = "Test@" + new Random().nextInt(10000);
    }

    @Test(testName = "SignupApi", priority = 1)
    public void userSignUp() {
    	System.out.println("Creating new User");
        SignupPayload signup = new SignupPayload();
        signup.setId(userId);
        signup.setEmail(userEmail);
        signup.setPassword(userPassword);
   
        Response response = BooksAppEndPoints.userSignup(signup);
        JSONObject js = new JSONObject(response.asPrettyString());
        String msg = js.getString("message");
        assertEquals("User created successfully",msg);
        assertEquals(200, response.getStatusCode());
        response.then().log().all();
        System.out.println("new User Created Successfully");
    }
    
    @Test(testName = "LoginApi", priority = 2)
    public void userLoginAPI(ITestContext context) {
    	System.out.println("Login in progress");
        LoginPayload loign = new LoginPayload();
        loign.setId(userId);
        loign.setEmail(userEmail);
        loign.setPassword(userPassword);
        
        String response = BooksAppEndPoints.userLogin(loign).then().log().all().extract().asPrettyString();
        JSONObject js = new JSONObject(response);
        token = js.getString("access_token");
        context.setAttribute("token", token);
        System.out.println("login Successfully");
    }
    
    @Test(testName = "CreateBookApi", priority = 3)
    public void createBookAPI(ITestContext context) {
        System.out.println("Creating new Book");
    	CreateBookPayload createBook = new CreateBookPayload();
        createBook.setId(bookid);
        createBook.setName(bookName);
        createBook.setAuthor("chetan Bhagat");
        createBook.setpublished_year(pubYear);
        createBook.setbook_summary(summaries[new Random().nextInt(summaries.length)]);
       
        Response response = BooksAppEndPoints.createBook(createBook,context);
        Assert.assertEquals(200, response.getStatusCode());
        response.then().log().all();
        System.out.println("New Book Created successfylly");
    }
    @Test(testName = "Get All BookApi", priority = 4)
    public void getBookAPI(ITestContext context) {
    	System.out.println("getting all bookings");
        Response response = BooksAppEndPoints.getAllBook(context);
        Assert.assertEquals(200, response.getStatusCode());
        response.then().log().all();
    }
    
    @Test(testName = "Update BookApi", priority = 5)
    public void updateBookAPI(ITestContext context) {
    	System.out.println("updating book details of book id: "+bookid);
    	updateBookPayload updateBook = new updateBookPayload();
    	updateBook.setId(bookid);
    	updateBook.setName(bookName);
    	updateBook.setAuthor(authors[new Random().nextInt(authors.length)]);
    	updateBook.setpublished_year(pubYear);
    	updateBook.setbook_summary(summaries[new Random().nextInt(summaries.length)]);
       
    	Response response = BooksAppEndPoints.updateBook(updateBook,bookid,context);
    	Assert.assertEquals(200, response.getStatusCode());
        response.then().log().all();
        System.out.println("Booking Id: "+bookid+" is updated");
    }
    
    @Test(testName = "Get Book by id Api", priority = 6)
    public void getBookByIdAPI(ITestContext context) {
    	System.out.println("Geting Book by id: "+bookid);
        Response response = BooksAppEndPoints.getBookById(bookid,context);
        Assert.assertEquals(200, response.getStatusCode());
        response.then().log().all();
    }
    
    @Test(testName = "Delete book Api", priority = 7)
    public void deleteBookAPI(ITestContext context) {
    	System.out.println("Deleting booking ID: "+bookid);
        Response response = BooksAppEndPoints.deleteBook(bookid,context);
        System.out.println(bookid+" deleted successfully");
        Assert.assertEquals(200, response.getStatusCode());
        response.then().log().all();
    }
    
    //------------------------Negative tastcase-------------------------------//
    
    @Test(testName = "SignupApi_Negative_DuplicateEmail", priority = 8)
    public void userSignUp_DuplicateEmail() {
        System.out.println("Trying to create user with existing email");
        SignupPayload signup = new SignupPayload();
        signup.setId(new Random().nextInt(10000)); // different id
        signup.setEmail(userEmail); // same email
        signup.setPassword("AnotherPass@123");

        Response response = BooksAppEndPoints.userSignup(signup);
        Assert.assertEquals(response.getStatusCode(), 400); // or whatever error code is returned
        response.then().log().all();
    }
    
    @Test(testName = "LoginApi_Negative_WrongPassword", priority = 9)
    public void userLogin_WrongPassword(ITestContext context) {
        System.out.println("Trying to login with wrong password");
        LoginPayload login = new LoginPayload();
        login.setId(userId);
        login.setEmail(userEmail);
        login.setPassword("WrongPassword@123"); // wrong password

        Response response = BooksAppEndPoints.userLogin(login);
        Assert.assertEquals(response.getStatusCode(), 400); // Unauthorized
        response.then().log().all();
    }
    
    @Test(testName = "CreateBookApi_Negative_MissingName", priority = 10)
    public void createBook_MissingName(ITestContext context) {
        System.out.println("Creating book with missing name");
        CreateBookPayload createBook = new CreateBookPayload();
        createBook.setId(new Random().nextInt(10000));
        createBook.setAuthor("Anonymous");
        createBook.setpublished_year(2000);
        createBook.setbook_summary("Missing name field");

        Response response = BooksAppEndPoints.createBook(createBook, context);
        Assert.assertEquals(response.getStatusCode(), 500); // Bad Request
        response.then().log().all();
    }
    
    @Test(testName = "GetBookById_Negative_InvalidId", priority = 11)
    public void getBookByInvalidId(ITestContext context) {
        int invalidBookId = 999999; // unlikely to exist
        System.out.println("Trying to get book with invalid id: " + invalidBookId);
        Response response = BooksAppEndPoints.getBookById(invalidBookId, context);
        Assert.assertEquals(response.getStatusCode(), 404); // Not Found
        response.then().log().all();
    }
    
    @Test(testName = "UpdateBookApi_Negative_InvalidToken", priority = 12)
    public void updateBookWithInvalidToken(ITestContext context) {
        System.out.println("Updating book with invalid token");
        
        updateBookPayload updateBook = new updateBookPayload();
        updateBook.setId(bookid);
        updateBook.setName("Invalid Token Test");
        updateBook.setAuthor("No Auth");
        updateBook.setpublished_year(2002);
        updateBook.setbook_summary("Invalid token test");

        context.setAttribute("token", "Bearer invalid.token.value");

        Response response = BooksAppEndPoints.updateBook(updateBook, bookid, context);
        Assert.assertEquals(response.getStatusCode(), 403); // Unauthorized
        response.then().log().all();
    }


}
