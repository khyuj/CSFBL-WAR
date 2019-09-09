package v.i.incandenza;

import java.util.ArrayList;
import java.util.Comparator;

public class Player {	
	private String playerName;
	private String playerPosition;
	private ArrayList<Team> playerTeams = new ArrayList<Team>();
	private String playerYear;	
	private double oWAR;
	private double dWAR;
	private double tWAR;
	private double dollarWAR;
	private double salarY;
	
	public void setTeam(Team teamName) {		
		this.playerTeams.add(teamName);		
	}
	
	public void setName(String name) {
		this.playerName = name;
	}
	
	public void setPos(String pos) {
		this.playerPosition = pos;
	}
	
	public void setXRAA(String xRaa) {		
		this.oWAR = this.oWAR + Double.parseDouble(xRaa);		
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
	
	public String getPos() {
		return this.playerPosition;
	}
	
	public ArrayList<Team> getTeam() {
		return this.playerTeams;
	}
	
	public String getYear() {
		return this.playerYear;
	}
	
	public double getOWAR() {
		return this.oWAR;
	}      
	
	public double getDWAR() {
		return this.dWAR;
	}
	
	public double getTWAR() {
		return this.tWAR;
	}
	
	public double getSalary() {
		return this.salarY;
	}
	
	public double getDollars() {
		return this.dollarWAR;
	}	         
	
	public Player(String name, String position, Team team, String xRaa, String year, String salary) {
		this.setName(name);
		this.setPos(position);
		this.setTeam(team);
		this.setXRAA(xRaa);
		this.setYear(year);
		this.setSalary(salary);
	}		
}
