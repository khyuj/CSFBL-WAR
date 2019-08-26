package v.i.incandenza;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class DataTable extends AbstractTableModel {
	String[] playerColumns = {"Position", "Name", "Team", "dWAR", "oWAR", "WAR", "Salary", "$/WAR"};
	String[] pitcherColumns = {"Position", "Name", "Team", "ERA", "FIP", "WAR", "Salary", "$/WAR" };
	String[][] playerData;
	ArrayList<Player> playerList;
	ArrayList<Pitcher> pitcherList;
	ArrayList<Team> teamList;
	ArrayList<String> tablePlayers; 
	ArrayList<String> tablePitchers;
	String menuYear;
	String menuEnd;
	String menuTeam;
	String menuPosition;
	String menuPlayer;
	
	public int getColumnCount() {
		if(menuPosition != "All Pitchers" && menuPosition != "Relief Pitchers") {		
			return this.playerColumns.length;
		}
		else {
			return this.pitcherColumns.length;
		}
	}
	
	public int getRowCount() {
		if(menuPosition != "All Pitchers" && menuPosition != "Relief Pitchers") {		
			return this.tablePlayers.size();
		}
		else {
			return this.tablePitchers.size();
		}
	}
	
	public String getValueAt(int row, int col) {
		return this.playerData[row][col];
	}
	
	public void MenuFilter() {
		ArrayList<Integer> yearList = new ArrayList<Integer>();
		boolean newYear = false;
		boolean newEnd = false;
		boolean newTeam = false;
		boolean newPos = false;
		boolean newPlayer = false;
		String fakeString = "0";
		Team fakeTeam = new Team();
		if(menuYear != "All") {
			newYear = true;
		}
		if(menuEnd != "-Year") {			
			if(Integer.parseInt(menuYear) < Integer.parseInt(menuEnd)) {
				newEnd = true;
				yearList.clear();
				int tempYear = Integer.parseInt(menuYear);
				for(int i = 0; i < (Integer.parseInt(menuEnd) - Integer.parseInt(menuYear)); i++) {
					yearList.add(tempYear);
					tempYear++;
				}
			}
			else if(Integer.parseInt(menuEnd) < Integer.parseInt(menuYear)) {
				newEnd = true;
				yearList.clear();
				int tempYear = Integer.parseInt(menuEnd);
				for(int i = 0; i < (Integer.parseInt(menuYear) - Integer.parseInt(menuEnd)); i++) {
					yearList.add(tempYear);
					tempYear++;
				}
			}			
		}
		if(menuTeam != "All Teams") {
			newTeam = true;
		}
		
		if(menuPlayer != "All Players") {
			newPlayer = true;
		}
		
		boolean positionPlayer = false;		
		if(menuPosition != "All Pitchers" && menuPosition != "Relief Pitchers") {
			if(menuPosition != "Position Players") {
				newPos = true;
			}
			for(int a = 0; a < tablePlayers.size(); a++) {
				String fString = "0";
				Team fTeam = new Team();
				Player tempPlayer = new Player(fString, fString, fTeam, fString, fString);
				double owarCounter = 0;
				double dwarCounter = 0;
				double warCounter = 0;
				double salaryCounter = 0;
				double dollarCounter = 0;
				int dollarIndex = 1;
				ArrayList<Team> tempTeams = new ArrayList<Team>();				
				for(int b = 0; b < playerList.size(); b++) {
					if(tablePlayers.get(a) == playerList.get(b).getName()) {
						tempPlayer = playerList.get(b);		
						tempTeams = new ArrayList<Team>(tempPlayer.getTeam());
					}
				
					boolean add = true;
					if(newEnd == true && newYear == true) {
						boolean tempAdd = false;
						for(int c = 0; c < yearList.size(); c++) {
							if(Integer.parseInt(tempPlayer.getYear()) == yearList.get(c)) {
								tempAdd = true;
							}						
						}
						if(tempAdd != true) {
							add = false;
						}
					}
					else if(newEnd == false && newYear == true) {
						if(menuYear != tempPlayer.getYear()) {
							add = false;
						}
					}
					if(newTeam == true) {
						boolean tempAdd = false;
						for(int c = 0; c < tempTeams.size(); c++) {
							if(menuTeam == tempTeams.get(c).getName()) {
								tempAdd = true;
							}
						}	
						if(tempAdd != true) {
							add = false;
						}
					}
					if(newPos == true) {
						if(menuPosition != tempPlayer.getPos()) {
							add = false;
						}
					}
					if(newPlayer == true) {
						if(menuPlayer != tempPlayer.getName()) {
							add = false;
						}
					}			
					if(add = true) {
						owarCounter = owarCounter + tempPlayer.getOWAR();
						dwarCounter = dwarCounter + tempPlayer.getDWAR();
						warCounter = warCounter + tempPlayer.getTWAR();
						salaryCounter = salaryCounter + tempPlayer.getSalary();
						dollarCounter = (dollarCounter + tempPlayer.getDollars())/dollarIndex;
						dollarIndex++;
					}
				}
				playerData[a][0] = tempPlayer.getPos();
				playerData[a][1] = tempPlayer.getName();
				playerData[a][2] = tempTeams.get(tempTeams.size()-1).getName();
				playerData[a][3] = Double.toString(owarCounter);
				playerData[a][4] = Double.toString(dwarCounter);
				playerData[a][5] = Double.toString(warCounter);
				playerData[a][6] = Double.toString(salaryCounter);
				playerData[a][7] = Double.toString(dollarCounter);
			}	
		}
		else {
			if(menuPosition != "All Pitchers") {
				newPos = true;
			}
			for(int a = 0; a < tablePitchers.size(); a++) {				
				Pitcher tempPlayer = new Pitcher(fakeString, fakeString, fakeTeam, fakeString);
				double eraCounter = 0;
				double fipCounter = 0;
				double tempERA = 0;
				double tempFIP = 0;
				int gameWeight = 0;
				int rateIndex = 1;
				double warCounter = 0;
				double salaryCounter = 0;
				double dollarCounter = 0;
				int dollarIndex = 1;
				ArrayList<Team> tempTeams = new ArrayList<Team>();				
				for(int b = 0; b < pitcherList.size(); b++) {
					if(tablePitchers.get(a) == pitcherList.get(b).getName()) {
						tempPlayer = pitcherList.get(b);		
						tempTeams = new ArrayList<Team>(tempPlayer.getTeam());						
						gameWeight = tempPlayer.getGames()/160;
						tempERA = tempPlayer.getERA();
						tempFIP = tempPlayer.getFIP();
						if(Double.toString(tempERA) == "-") {
							tempERA = 0;
						}							
						if(Double.toString(tempFIP) == "-") {
							tempFIP = 0;
						}							
					}									
					boolean add = true;
					if(newEnd == true && newYear == true) {
						boolean tempAdd = false;
						for(int c = 0; c < yearList.size(); c++) {
							if(Integer.parseInt(tempPlayer.getYear()) == yearList.get(c)) {
								tempAdd = true;
							}						
						}
						if(tempAdd != true) {
							add = false;
						}
					}
					else if(newEnd == false && newYear == true) {
						if(menuYear != tempPlayer.getYear()) {
							add = false;
						}
					}
					if(newTeam == true) {
						boolean tempAdd = false;
						for(int c = 0; c < tempTeams.size(); c++) {
							if(menuTeam == tempTeams.get(c).getName()) {
								tempAdd = true;
							}
						}	
						if(tempAdd != true) {
							add = false;
						}
					}
					if(newPos == true) {
						if(menuPosition != tempPlayer.checkPos()) {
							add = false;
						}
					}
					if(newPlayer == true) {
						if(menuPlayer != tempPlayer.getName()) {
							add = false;
						}
					}			
					if(add = true) {	
						eraCounter = (eraCounter + (tempERA*gameWeight)/rateIndex);
						fipCounter = (fipCounter + (tempFIP*gameWeight)/rateIndex);					
						warCounter = warCounter + tempPlayer.getTWAR();
						salaryCounter = salaryCounter + tempPlayer.getSalary();
						dollarCounter = (dollarCounter + tempPlayer.getDollars())/dollarIndex;
						rateIndex++;
						dollarIndex++;							
					}
				}
				playerData[a][0] = tempPlayer.getPos();
				playerData[a][1] = tempPlayer.getName();
				playerData[a][2] = tempTeams.get(tempTeams.size()-1).getName();
				playerData[a][3] = Double.toString(eraCounter);
				playerData[a][4] = Double.toString(fipCounter);
				playerData[a][5] = Double.toString(warCounter);
				playerData[a][6] = Double.toString(salaryCounter);
				playerData[a][7] = Double.toString(dollarCounter);
			}
		}				
	}	
	
	public void setYear(String year) {		
		menuYear = year;
		MenuFilter();
	}
	
	public void setEnd(String end) {
		menuEnd = end;
		MenuFilter();
	}
	
	public void setTeam(String team) {
		menuTeam = team;
		MenuFilter();
	}
	
	public void setPos(String pos) {
		menuPosition = pos;
		MenuFilter();
	}
	
	public void setPlayer(String name) {
		menuPlayer = name;
		MenuFilter();
	}
	
	public void initialize() {	
		for(int a = 0; a < tablePlayers.size(); a++) {
			String fString = "0";
			Team fTeam = new Team();
			Player tempPlayer = new Player(fString, fString, fTeam, fString, fString);
			double owarCounter = 0;
			double dwarCounter = 0;
			double warCounter = 0;
			double salaryCounter = 0;
			double dollarCounter = 0;
			int dollarIndex = 1;
			ArrayList<Team> tempTeams = new ArrayList<Team>();				
			for(int b = 0; b < playerList.size(); b++) {
				if(tablePlayers.get(a) == playerList.get(b).getName()) {
					tempPlayer = playerList.get(b);		
					tempTeams = new ArrayList<Team>(tempPlayer.getTeam());
					owarCounter = owarCounter + tempPlayer.getOWAR();
					dwarCounter = dwarCounter + tempPlayer.getDWAR();
					warCounter = warCounter + tempPlayer.getTWAR();
					salaryCounter = salaryCounter + tempPlayer.getSalary();
					dollarCounter = (dollarCounter + tempPlayer.getDollars())/dollarIndex;
					dollarIndex++;
				}				
			}
			playerData[a][0] = tempPlayer.getPos();
			playerData[a][1] = tempPlayer.getName();
			playerData[a][2] = tempTeams.get(tempTeams.size()-1).getName();
			playerData[a][3] = Double.toString(owarCounter);
			playerData[a][4] = Double.toString(dwarCounter);
			playerData[a][5] = Double.toString(warCounter);
			playerData[a][6] = Double.toString(salaryCounter);
			playerData[a][7] = Double.toString(dollarCounter);
		}
	}
	
	public DataTable(ArrayList<Player> players, ArrayList<Pitcher> pitchers, ArrayList<Team> teams, 
			   	     ArrayList<String> playerTable, ArrayList<String> pitcherTable) {	
		this.playerList = new ArrayList<Player>(players);
		this.pitcherList = new ArrayList<Pitcher>(pitchers);
		this.teamList = new ArrayList<Team>(teams);
		this.tablePlayers = new ArrayList<String>(playerTable);
		this.tablePitchers = new ArrayList<String>(pitcherTable);
		initialize();
	}
}