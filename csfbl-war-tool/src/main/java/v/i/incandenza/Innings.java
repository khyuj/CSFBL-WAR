package v.i.incandenza;

public class Innings {
	private double inningsTotal;
	private String inningsTeam;
	private double FIP;
	
	public String getTeam() {
		return this.inningsTeam;
	}
	
	public double getInnings() {
		return this.inningsTotal;
	}
	
	public double getFIP() {
		return this.FIP;
	}
	
	public Innings(String innings, String team) {
		this.inningsTeam = team;
		this.inningsTotal = Double.parseDouble(innings);
	}
}
