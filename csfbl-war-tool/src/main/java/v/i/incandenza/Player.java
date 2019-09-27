package v.i.incandenza;

import java.util.ArrayList;

public class Player {	
	private String playerName;
	private String playerPosition;
	private ArrayList<Team> playerTeams = new ArrayList<Team>();
	private ArrayList<String> multiPositions = new ArrayList<String>();
	private String playerYear;	
	private double gamesPlayed;
	private double catcherGames;
	private double firstGames;
	private double secondGames;
	private double shortGames;
	private double thirdGames;
	private double leftGames;
	private double centerGames;
	private double rightGames;
	private double dhGames;
	private double positional;
	private double replacement = 19;
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
	
	public void setDef(double dRaa) {
		this.dWAR = this.dWAR + dRaa;
	}
	
	public void setDWAR(double average) {
		this.dWAR = this.dWAR - (average*(gamesPlayed/160));
    	this.positional = (-6 * (this.catcherGames/160)) + (-6 * (this.firstGames/160)) + (8 * (this.secondGames/160)) + (10 * (this.shortGames/160))
    					+ (2 * (this.thirdGames/160)) + (-2 * (this.leftGames/160)) + (-2 * (this.rightGames/160)) 
    					+ (5 * (this.centerGames/160)) + (-12 * this.dhGames/160);
	}
	
	public void setWAR() {
		this.tWAR = this.oWAR + this.dWAR + (this.replacement*(gamesPlayed/160)) + this.positional;
		this.tWAR = tWAR/9.5;
		this.dollarWAR = this.tWAR/this.salarY;
	}
	
	public void positionTally(String position) {
		if(position.equals("1B")) {
			this.firstGames++;
			this.gamesPlayed++;
			boolean match = false;
			for(int i = 0; i < multiPositions.size(); i++) {
				if(multiPositions.get(i).equals(position)) {
					match = true;
				}
			}
			if(match == false) {
				this.multiPositions.add(position);
			}
		}
		if(position.equals("C")) {
			this.catcherGames++;
			this.gamesPlayed++;
			boolean match = false;
			for(int i = 0; i < multiPositions.size(); i++) {
				if(multiPositions.get(i).equals(position)) {
					match = true;
				}
			}
			if(match == false) {
				this.multiPositions.add(position);
			}
		}
		if(position.equals("2B")) {
			this.secondGames++;
			this.gamesPlayed++;
			boolean match = false;
			for(int i = 0; i < multiPositions.size(); i++) {
				if(multiPositions.get(i).equals(position)) {
					match = true;
				}
			}
			if(match == false) {
				this.multiPositions.add(position);
			}
		}
		if(position.equals("SS")) {
			this.shortGames++;
			this.gamesPlayed++;
			boolean match = false;
			for(int i = 0; i < multiPositions.size(); i++) {
				if(multiPositions.get(i).equals(position)) {
					match = true;
				}
			}
			if(match == false) {
				this.multiPositions.add(position);
			}
		}
		if(position.equals("3B")) {
			this.thirdGames++;
			this.gamesPlayed++;
			boolean match = false;
			for(int i = 0; i < multiPositions.size(); i++) {
				if(multiPositions.get(i).equals(position)) {
					match = true;
				}
			}
			if(match == false) {
				this.multiPositions.add(position);
			}
		}
		if(position.equals("LF")) {
			this.leftGames++;
			this.gamesPlayed++;
			boolean match = false;
			for(int i = 0; i < multiPositions.size(); i++) {
				if(multiPositions.get(i).equals(position)) {
					match = true;
				}
			}
			if(match == false) {
				this.multiPositions.add(position);
			}
		}
		if(position.equals("CF")) {
			this.centerGames++;
			this.gamesPlayed++;
			boolean match = false;
			for(int i = 0; i < multiPositions.size(); i++) {
				if(multiPositions.get(i).equals(position)) {
					match = true;
				}
			}
			if(match == false) {
				this.multiPositions.add(position);
			}
		}
		if(position.equals("RF")) {
			this.rightGames++;
			this.gamesPlayed++;
			boolean match = false;
			for(int i = 0; i < multiPositions.size(); i++) {
				if(multiPositions.get(i).equals(position)) {
					match = true;
				}
			}
			if(match == false) {
				this.multiPositions.add(position);
			}
		}
		if(position.equals("DH")) {
			this.dhGames++;
			this.gamesPlayed++;
			boolean match = false;
			for(int i = 0; i < multiPositions.size(); i++) {
				if(multiPositions.get(i).equals(position)) {
					match = true;
				}
			}
			if(match == false) {
				this.multiPositions.add(position);
			}
		}
		if(dhGames > gamesPlayed/2) {
			this.playerPosition = "DH";
		}
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
	
	public String getPositions() {
		String positions = "";
		for(int i = 0; i < this.multiPositions.size(); i++ ) {
			if(i != 0) {
				positions = positions + ", " + this.multiPositions.get(i);
			}
			else if(i == 0) {
				positions = this.multiPositions.get(i);
			}
		}
		return positions;
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
	
	public double getGames() {
		return this.gamesPlayed;
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
