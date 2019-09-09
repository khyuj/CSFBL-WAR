package v.i.incandenza;

public class Innings {
	private double inningsTotal;
	private String inningsTeam;
	
	public String getTeam() {
		return this.inningsTeam;
	}
	
	public double getInnings() {
		return this.inningsTotal;
	}
	
	public Innings(String innings, String team) {
		this.inningsTeam = team;
		this.inningsTotal = Double.parseDouble(innings);
	}
}
