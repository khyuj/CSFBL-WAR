package v.i.incandenza;

import java.util.ArrayList;

public class Pitcher {
	private String playerName;
	private String playerPosition;
	private ArrayList<Team> playerTeams = new ArrayList<Team>();
	private String currentTeam = "";
	private boolean teamSwitch = false;
	private String playerYear;
	private int gameTotal;
	private int gameStarted;
	private double fIP;
	private double eRA;
	private double dWAR;
	private ArrayList<Innings> inningsTotal = new ArrayList<Innings>();
	private double tWAR;
	private double battedBallWAR;
	private double runsPerWin;
	private double leagueFIP;
	private double dollarWAR;
	private int salarY;
	private double arm;
	private double range;
	private double error;
	
	public void setTeam(Team teamName) {
		this.playerTeams.add(teamName);
		if(teamSwitch == false) {
			this.currentTeam = teamName.getName();
		}
	}
	
	public void setCurrentTeam(String teamName) {
		this.currentTeam = teamName;
		teamSwitch = true;
	}
	
	public void setName(String name) {
		this.playerName = name;
	}
	
	public void setPos(String pos) {
		this.playerPosition = pos;
	}
	
	public void setStarts(String starts) {
		this.gameStarted = this.gameStarted + Integer.parseInt(starts);
	}
	
	public void setGames(String games) {
		this.gameTotal = this.gameTotal + Integer.parseInt(games);
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
		if(!salary.equals("")) {
			this.salarY = Integer.parseInt(salary);
		}
	}
	
	public void setInnings(String innings, String team) {
		Innings newInnings = new Innings(innings, team);
		this.inningsTotal.add(newInnings);
	}
	
	public void setRPW(double rpw, double fip) {
		this.runsPerWin = rpw;
		this.leagueFIP = fip;
	}
	
	public void setArm(String type) {
		if(type.equals("double play")) {
			this.arm = this.arm + 0.06;
		}
		else if(type.equals("CS")) {
			this.arm = this.arm + 0.2;
		}
		else if(type.equals("SB")) {
			this.arm = this.arm - 0.2;
		}
	}
	
	public void setRange() {
		this.range = this.range + 0.08;
	}
	
	public void setError() {
		this.error = this.error - 0.5;
	}
	
	public void setDWAR(double average, ArrayList<Double> components) {
		double defAverage = average * (this.inningsTotal()/200);
		this.dWAR = this.dWAR - average;
		if(Double.isNaN(this.dWAR)) {
			this.dWAR = 0;
		}		
		double armComponent = components.get(24) * (this.inningsTotal()/200);
		double rangeComponent = components.get(25) * (this.inningsTotal()/200);
		double errorComponent = components.get(26) * (this.inningsTotal()/200);
		this.arm = this.arm - armComponent;
		this.range = this.range - rangeComponent;
		this.error = this.error - errorComponent;
	}	
	
	public void setWAR(double leagueRA9, double leagueInnings) {
		double fipAverage = this.leagueFIP - this.fIP;
		fipAverage = fipAverage/this.runsPerWin;
		double runsAverage = leagueRA9 - this.eRA;
		runsAverage = runsAverage/this.runsPerWin;
		double weightedAverage = (0.67*fipAverage) + (0.33*runsAverage);
		double replacement = 0.12*(gameStarted/gameTotal) + (0.08*(1-(gameStarted/gameTotal)));
		weightedAverage = weightedAverage + replacement;
		this.tWAR = (weightedAverage * (this.inningsTotal()/9));
		this.battedBallWAR = this.battedBallWAR/this.runsPerWin;
		this.tWAR = this.tWAR + (this.dWAR/this.runsPerWin);
		this.dollarWAR = Math.round(this.salarY/this.tWAR);
		if(this.dollarWAR <= 0) {
			this.dollarWAR = Double.NaN;
		}
	}
	
	public void addWAR(double runs) {
		this.battedBallWAR = this.battedBallWAR + runs;
	}
	
	public String getName() {
		return this.playerName;
	}
	
	public String checkPos() {
		if(gameTotal/5 < gameStarted) {
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
	
	public String getCurrentTeam() {
		return this.currentTeam;
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
	
	public double getDWAR() {
		return this.dWAR;
	}
	
	public double getWeightDef() {
		double weightedDef = this.getDWAR()/(this.inningsTotal()/200);
		return weightedDef;
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
	
	public double getDollars() {
		return this.dollarWAR;
	}
	
	public Innings getInnings(int index) {
		return this.inningsTotal.get(index);
	}
	
	public double getTeamInnings(Team team) {
		double innings = 0;
		for(int a = 0; a < inningsTotal.size(); a++) {
			if(team.getName().equals(inningsTotal.get(a).getTeam())) {
				innings = inningsTotal.get(a).getInnings();
			}
		}
		return innings;
	}
	
	public double inningsTotal() {
		double innings = 0;
		for(int i = 0; i < inningsTotal.size(); i++) {
			innings = innings + inningsTotal.get(i).getInnings();
		}
		return innings;
	}
	
	public double getRange() {
		return this.range;
	}
	
	public double getError() {
		return this.error;
	}
	
	public double getArm() {
		return this.arm;
	}
	
	public double getRPW() {
		return this.runsPerWin;
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

