package v.i.incandenza;

import java.util.ArrayList;
import java.util.Comparator;

public class Pitcher {
	private String playerName;
	private String playerPosition;
	private ArrayList<Team> playerTeams = new ArrayList<Team>();
	private String playerYear;
	private int gameTotal;
	private int gameStarted;
	private double fIP;
	private double eRA;
	private int tWAR;
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
		this.gameStarted = Integer.parseInt(starts);
	}
	
	public void setGames(String games) {
		this.gameTotal = Integer.parseInt(games);
	}
	
	public void setFIP(String fip) {		
		this.fIP = Double.parseDouble(fip);		
	}
	
	public void setERA(String era) {
		this.eRA = Double.parseDouble(era);
	}
	
	public void setYear(String year) {
		this.playerYear = year;
	}	
	
	public void setSalary(String salary) {
		this.salarY = Integer.parseInt(salary);
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
	
	public Pitcher(String name, String position, Team team, String year) {
		if(name != null) {
			this.setName(name);
		}
		if(position != null) {
			this.setPos(position);
		}
		if(team != null) {
			this.setTeam(team);
		}		
		if(year != null) {
			this.setYear(year);
		}			
	}
}

