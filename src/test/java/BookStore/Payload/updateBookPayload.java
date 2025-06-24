package BookStore.Payload;

public class updateBookPayload {
	
	int id;
	String name;
	String author;
	int published_year;
	String book_summary;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getpublished_year() {
		return published_year;
	}
	public void setpublished_year(int published_year) {
		this.published_year = published_year;
	}
	public String getbook_summary() {
		return book_summary;
	}
	public void setbook_summary(String book_summary) {
		this.book_summary = book_summary;
	}	
}