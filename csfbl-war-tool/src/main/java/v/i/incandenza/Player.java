package v.i.incandenza;

import java.util.ArrayList;

public class Player {	
	private String playerName;
	private String playerPosition;
	private ArrayList<Team> playerTeams = new ArrayList<Team>();
	private ArrayList<Games> teamGames = new ArrayList<Games>();
	private String[][] outAverages;
	private String currentTeam = "";
	private boolean teamSwitch = false;
	private ArrayList<String> multiPositions = new ArrayList<String>();
	private String[] averageAverages = { "0", "15", "55", "30", "55", "2", "-12", "-10", "0" };
	private String playerYear;	
	private double gamesPlayed = 0;
	private double catcherGames = 0;
	private double firstGames = 0;
	private double secondGames = 0;
	private double shortGames = 0;
	private double thirdGames = 0;
	private double leftGames = 0;
	private double centerGames = 0;
	private double rightGames = 0;
	private double dhGames = 0;
	private double pitcherDef = 0;
	private double catcherDef = 0;
	private double firstDef = 0;
	private double secondDef = 0;
	private double thirdDef = 0;
	private double shortDef = 0;
	private double leftDef = 0;
	private double centerDef = 0;
	private double rightDef = 0;
	private double plateAppearances = 0;
	private double positional;
	private double replacement;
	private double leaguePA;
	private double runsPerWin;
	double groundFactor;
	double flyFactor;
	double groundOuts = 0.0;
	double flyOuts = 0.0;
	private double oWAR;
	private double dWAR;
	private double tWAR;
	private double dollarWAR = 0;
	private double salarY;
	private int assists = 0;
	private int extraBases = 0;
	private double range = 0;
	private double error = 0;
	private double arm = 0;
	
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
	
	public void setTeamGames(String games, Team team) {
		Games teamGame = new Games(games, team);
		this.teamGames.add(teamGame);
	}
	
	public int getTeamGames(Team team) {
		int games = 0;
		for(int a = 0; a < this.teamGames.size(); a++) {
			if(team.getName().equals(teamGames.get(a).getTeam().getName())) {
				games = this.teamGames.get(a).getGames();
			}
		}
		return games;
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
	
	public void setArm(String type) {
		if(type.equals("assist")) {
			this.assists++;
			this.arm = this.arm + 0.44;
		}
		else if(type.equals("extra base")) {
			this.extraBases++;
			this.arm = this.arm - 0.38;
		}
		else if(type.equals("double play")) {
			this.arm = this.arm + 0.07;
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
	
	public void setDef(double dRaa, String position, String type) {		
		if(type.equals("ground")) {
			System.out.println("Ground factor: " + this.groundFactor);
			this.dWAR = this.dWAR + (dRaa/this.groundFactor);
		}
		else if(type.equals("fly")) {
			System.out.println("Fly factor: " + this.flyFactor);
			this.dWAR = this.dWAR + (dRaa/this.flyFactor);
		}
		else {
			this.dWAR = this.dWAR + dRaa;
		}
		if(position.contains("pitcher")) {
			this.pitcherDef = this.pitcherDef + dRaa;
		}
		if(position.contains("catcher")) {
			this.catcherDef = this.catcherDef + dRaa;
		}
		if(position.contains("first")) {
			this.firstDef = this.firstDef + dRaa;
		}
		if(position.contains("second")) {
			this.secondDef = this.secondDef + dRaa;
		}
		if(position.contains("short")) {
			this.shortDef = this.shortDef + dRaa;
		}
		if(position.contains("third")) {
			this.thirdDef = this.thirdDef + dRaa;
		}
		if(position.contains("left")) {
			this.leftDef = this.leftDef + dRaa;
		}
		if(position.contains("center")) {
			this.centerDef = this.centerDef + dRaa;
		}
		if(position.contains("right")) {
			this.rightDef = this.rightDef + dRaa;
		}
	}
	
	public void setDWAR(ArrayList<Double> averages, ArrayList<Double> components) {
		for(int a = 0; a < averages.size(); a++) {
			if(Double.isNaN(averages.get(a)) || Double.isInfinite(averages.get(a))) {
				averages.set(a, Double.parseDouble(averageAverages[a]));
			}
		}
		for(int a = 0; a < components.size(); a++) {
			if(Double.isNaN(components.get(a)) || Double.isInfinite(components.get(a))) {
				components.set(a, 0.0);
			}
		}
		double average = ((this.catcherGames/160)*averages.get(0)) + ((this.firstGames/160)*averages.get(1)) + ((this.secondGames/160)*averages.get(2)) 
						 + ((this.thirdGames/160)*averages.get(3)) + ((this.shortGames/160)*averages.get(4))  + ((this.leftGames/160)*averages.get(5))
						 + ((this.centerGames/160)*averages.get(6)) + ((this.rightGames/160)*averages.get(7));
		double rangeComponent = ((this.catcherGames/160)*components.get(1)) + ((this.firstGames/160)*components.get(4)) + ((this.secondGames/160)*components.get(7)) 
				 				+ ((this.thirdGames/160)*components.get(10)) + ((this.shortGames/160)*components.get(13))  + ((this.leftGames/160)*components.get(16))
				 				+ ((this.centerGames/160)*components.get(19)) + ((this.rightGames/160)*components.get(22));
		double armComponent = ((this.catcherGames/160)*components.get(0)) + ((this.firstGames/160)*components.get(3)) + ((this.secondGames/160)*components.get(6)) 
							  + ((this.thirdGames/160)*components.get(9)) + ((this.shortGames/160)*components.get(12))  + ((this.leftGames/160)*components.get(15))
							  + ((this.centerGames/160)*components.get(18)) + ((this.rightGames/160)*components.get(21));
		double errorComponent = ((this.catcherGames/160)*components.get(2)) + ((this.firstGames/160)*components.get(5)) + ((this.secondGames/160)*components.get(8)) 
								+ ((this.thirdGames/160)*components.get(11)) + ((this.shortGames/160)*components.get(14))  + ((this.leftGames/160)*components.get(17))
								+ ((this.centerGames/160)*components.get(20)) + ((this.rightGames/160)*components.get(23));
		this.dWAR = this.dWAR - average;
		if(Double.isNaN(this.dWAR)) {
			this.dWAR = 0;
		}
		this.arm = this.arm - armComponent;
		this.range = this.range - rangeComponent;
		this.error = this.error - errorComponent;
    	this.positional = (5 * (this.catcherGames/160)) + (-6 * (this.firstGames/160)) + (8 * (this.secondGames/160)) + (11 * (this.shortGames/160))
    					+ (2 * (this.thirdGames/160)) + (-2 * (this.leftGames/160)) + (-2 * (this.rightGames/160)) 
    					+ (5 * (this.centerGames/160)) + (-12 * this.dhGames/160);
	}
	
	public void setRPW(double RPW) {
		this.runsPerWin = RPW;
	}
	
	public void setPA(double playerPlates) {
		this.plateAppearances = this.plateAppearances + playerPlates;
	}
	
	public void setLeaguePA(double leaguePlates) {
		this.leaguePA = leaguePlates;
	}
	
	public void setWAR() {
		this.replacement = 420 * this.runsPerWin * (this.plateAppearances/this.leaguePA);
		this.tWAR = this.oWAR + this.dWAR + (this.replacement*(gamesPlayed/160)) + this.positional;
		this.tWAR = tWAR/runsPerWin;
		this.dollarWAR = Math.round(this.salarY/this.tWAR);
		if(this.dollarWAR <= 0) {
			this.dollarWAR = Double.NaN;
		}
	}
	
	public void setOutAverages(double groundOuts, double flyOuts, String[][] teamAverages) {
		this.groundOuts = groundOuts;
		this.flyOuts = flyOuts;
		this.outAverages = teamAverages;			
	}
	
	public void weightOuts() {
		this.groundFactor = 0;
		this.flyFactor = 0;
		for(int a = 0; a < this.playerTeams.size(); a++) {
			double games = this.getTeamGames(playerTeams.get(a));
			System.out.println(this.playerName);
			System.out.println(games + ": games");
			System.out.println(this.gamesPlayed + ": total games");
			for(int b = 0; b < outAverages.length; b++) {
				if(this.playerTeams.get(a).getName().equals(outAverages[b][0])) {
					this.groundFactor = this.groundFactor + ((((Double.parseDouble(outAverages[b][1])/groundOuts) + 1)/2) * (games/this.gamesPlayed));
					this.flyFactor = this.flyFactor + ((((Double.parseDouble(outAverages[b][2])/flyOuts) + 1)/2) * (games/this.gamesPlayed));					
				}
			}
		}
		if(this.groundFactor < 0.5 || this.groundFactor > 2 || this.flyFactor < 0.5 || this.flyFactor > 2) {
			this.groundFactor = 1;
			this.flyFactor = 1;
		}
		System.out.println(this.groundFactor + "||" + this.flyFactor);
	}
	
	public void setGames(double games) {
		this.gamesPlayed = this.gamesPlayed + games;
	}
	
	public void positionTally(String position) {
		if(position.equals("1B")) {
			this.firstGames++;
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
		if(dhGames > (gamesPlayed*0.5)) {
			this.playerPosition = "DH";
		}
		if(catcherGames > (gamesPlayed*0.5)) {
			this.playerPosition = "C";
		}
		if(firstGames > (gamesPlayed*0.5)) {
			this.playerPosition = "1B";
		}
		if(secondGames > (gamesPlayed*0.5)) {
			this.playerPosition = "2B";
		}
		if(thirdGames > (gamesPlayed*0.5)) {
			this.playerPosition = "3B";
		}
		if(shortGames > (gamesPlayed*0.5)) {
			this.playerPosition = "SS";
		}
		if(leftGames > (gamesPlayed*0.5)) {
			this.playerPosition = "LF";
		}
		if(centerGames > (gamesPlayed*0.5)) {
			this.playerPosition = "CF";
		}
		if(rightGames > (gamesPlayed*0.5)) {
			this.playerPosition = "RF";
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
	
	public double getRange() {
		return this.range;
	}
	
	public double getError() {
		return this.error;
	}
	
	public double getArm() {
		return this.arm;
	}
	
	public int getAssists() {
		return this.assists;
	}
	
	public int getExtra() {
		return this.extraBases;
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
	public double getBins(String position) {
		double bin = 0;
		if(position.equals("C")) {
			bin = this.catcherDef/(this.catcherGames/160.0);
			if(Double.isNaN(bin)) {
				bin = 0.0;
			}
		}
		if(position.contains("1B")) {
			bin = this.firstDef/(this.firstGames/160.0);
			if(Double.isNaN(bin)) {
				bin = 15.0*(this.firstGames/160.0);
			}
		}
		if(position.contains("2B")) {
			bin = this.secondDef/(this.secondGames/160.0);			
			if(Double.isNaN(bin)) {
				bin = 55.0*(this.secondGames/160.0);
			}
		}
		if(position.contains("SS")) {
			bin = this.shortDef/(this.shortGames/160.0);			
			if(Double.isNaN(bin)) {
				bin = 55.0*(this.firstGames/160.0);
			}
		}
		if(position.contains("3B")) {
			bin = this.thirdDef/(this.thirdGames/160.0);
			if(Double.isNaN(bin)) {
				bin = 30.0*(this.thirdGames/160.0);
			}
		}
		if(position.contains("LF")) {
			bin = this.leftDef/(this.leftGames/160.0);
			if(Double.isNaN(bin)) {
				bin = 2.0*(this.leftGames/160.0);
			}
		}
		if(position.contains("CF")) {
			bin = this.centerDef/(this.centerGames/160.0);
			if(Double.isNaN(bin)) {
				bin = -10.0*(this.centerGames/160.0);
			}
		}
		if(position.contains("RF")) {
			bin = this.rightDef/(this.rightGames/160.0);
			if(Double.isNaN(bin)) {
				bin = -12.0*(this.rightGames/160.0);
			}
		}
		return bin;
	}
	
	public double getPosGames(String position) {
		double bin = 0;
		if(position.equals("C")) {
			bin = this.catcherGames/160;
		}
		if(position.contains("1B")) {
			bin = this.firstGames/160;
		}
		if(position.contains("2B")) {
			bin = this.secondGames/160;
		}
		if(position.contains("SS")) {
			bin = this.shortGames/160;
		}
		if(position.contains("3B")) {
			bin = this.thirdGames/160;
		}
		if(position.contains("LF")) {
			bin = this.leftGames/160;
		}
		if(position.contains("CF")) {
			bin = this.centerGames/160;
		}
		if(position.contains("RF")) {
			bin = this.rightGames/160;
		}		
		return bin;
	}
	
	public ArrayList<Team> getTeam() {
		return this.playerTeams;
	}
	
	public String getCurrentTeam() {
		return this.currentTeam;
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
	
	public double getRPW() {
		return this.runsPerWin;
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
	
	public Player(String name, String position, Team team, String xRaa, String year, String salary, String games, double groundOuts, double flyOuts, String[][] teamAverages) {
		this.setName(name);
		this.setPos(position);
		this.setTeam(team);
		this.setXRAA(xRaa);
		this.setYear(year);
		this.setSalary(salary);
		this.setTeamGames(games, team);
		this.setOutAverages(groundOuts, flyOuts, teamAverages);
	}		
}
