package v.i.incandenza;

import java.util.ArrayList;

public class Pitcher {
	private String playerName;
	private String playerPosition;
	private ArrayList<Team> playerTeams = new ArrayList<Team>();
	private String playerYear;
	private int gameTotal;
	private int gameStarted;
	private double fIP;
	private double eRA;
	private double dWAR;
	private ArrayList<Innings> inningsTotal = new ArrayList<Innings>();
	private double tWAR;
	private int dollarWAR;
	private int salarY;
	
	public void setTeam(Team name) {
		this.playerTeams.add(name);
	}
	
	public void setName(String name) {
		this.playerName = name;
	}
	
	public void setPos(String pos) {
		this.playerPosition = pos;
	}
	
	public void setStarts(String starts) {
		this.gameStarted = gameStarted + Integer.parseInt(starts);
	}
	
	public void setGames(String games) {
		this.gameTotal = gameTotal + Integer.parseInt(games);
	}
	
	public void setFIP(String fip) {		
		this.fIP = Double.parseDouble(fip);		
	}
	
	public void setERA(String era) {
		this.eRA = Double.parseDouble(era);
	}
	
	public void setDef(double dRaa) {
		this.dWAR = this.dWAR + dRaa;
	}
	
	public void setYear(String year) {
		this.playerYear = year;
	}	
	
	public void setSalary(String salary) {
		this.salarY = Integer.parseInt(salary);
	}
	
	public void setInnings(String innings, String team) {
		Innings newInnings = new Innings(innings, team);
		this.inningsTotal.add(newInnings);
	}
	
	public void setWAR(double war) {
		this.tWAR = this.tWAR + war;
	}
	
	public String getName() {
		return this.playerName;
	}
	
	public String checkPos() {
		if(gameTotal/4 < gameStarted) {
			playerPosition = "SP";
			return this.playerPosition;
		}
		else {
			playerPosition = "Relief Pitchers";
			return this.playerPosition;
		}
		
	}
	
	public String getPos() {
		if(gameTotal/4 < gameStarted) {
			playerPosition = "SP";
			return this.playerPosition;
		}
		else {
			playerPosition = "RP";
			return this.playerPosition;
		}
	}
	
	public String getYear() {
		return this.playerYear;
	}
	
	public ArrayList<Team> getTeam() {
		return this.playerTeams;
	}
	
	public double getFIP() {
		return this.fIP;
	}
	
	public double getERA() {
		return this.eRA;
	}
	
	public double getTWAR() {
		return this.tWAR;
	}
	
	public int getGames() {
		return this.gameTotal;	
	}
	
	public int getStarts() {
		return this.gameStarted;
	}
	
	public int getSalary() {
		return this.salarY;
	}
	
	public int getDollars() {
		return this.dollarWAR;
	}
	
	public Innings getInnings(int index) {
		return this.inningsTotal.get(index);
	}
	
	public double inningsTotal() {
		double innings = 0;
		for(int i = 0; i < inningsTotal.size(); i++) {
			innings = innings + inningsTotal.get(i).getInnings();
		}
		return innings;
	}
	
	public ArrayList<Innings> getInningsArray() {
		return this.inningsTotal;
	}
	
	public Pitcher(String name, String position, Team team, String year, String salary) {
		this.setName(name);
		this.setPos(position);
		this.setTeam(team);
		this.setYear(year);
		this.setSalary(salary);		
	}
}

