package BookStore.EndPoints;

public class Routes {
	public static String base_url = "http://127.0.0.1:8000/";
	
	public static String getBooksURL 	= 	base_url + "books";
	public static String postBooksURL 	=	base_url + "books/";
	public static String putBooksURL 	=	base_url + "books/{book_id}";
	public static String deleteBooksURL	= 	base_url + "books/{book_id}";
	public static String getbyidBooksURL = 	base_url + "books/{book_id}";
	public static String loginURL = 	base_url + "login";
	public static String signupURL = 	base_url + "signup";
	
}
