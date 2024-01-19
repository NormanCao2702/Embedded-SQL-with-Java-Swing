package resultSet;


public class BusinessRS {
	private String id = new String();
	private String name = new String();
	private String address= new String();
	private String city = new String();
	private double stars;
	
	public BusinessRS(String id, String name, String address, String city, double stars) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.stars = stars;
	}
	
	// Getters
    public String getId() { return id; }
    public String getName() { return name; }
	public String getAddress() {return address;}
	public String getCity() {return city;}
	public double getStars() {return stars;}
}
