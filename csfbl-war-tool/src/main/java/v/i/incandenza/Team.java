package v.i.incandenza;

public class Team {
	private String name;
	private int ID;
		
	public String getName() {
		return this.name;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public Team(String n, int i) {
		this.name = n;
		this.ID = i;
	}
}
