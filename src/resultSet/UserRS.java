package resultSet;

public class UserRS {
	private String id = new String();
	private String name = new String();
	private int review_count;
	private int useful;
	private int funny;
	private int cool;
	private double avg_stars;
	private String date;
	
	public UserRS(String id, String name, int review_count, int useful, int funny, int cool, double avg_stars, String data) {
		this.id = id;
		this.name = name;
		this.review_count = review_count;
		this.useful = useful;
		this.funny = funny;
		this.cool = cool;
		this.avg_stars = avg_stars;
		this.date = data;
	}
	
	// Getters
    public String getId() { return id; }
    public String getName() { return name; }
	public int getReviewCount() {return review_count;}
	public int getUseful() {return useful;}
	public int getFunny() {return funny;}
	public int getCool() {return cool;}
	public double getAvgStars() {return avg_stars;}
	public String getDate() {return date;}
}