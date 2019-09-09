package v.i.incandenza;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class DataTable extends AbstractTableModel {
	private String[] playerColumns = {"Position", "Name", "Team", "oRAA", "dRAA", "WAR", "Salary", "$/WAR"};
	private String[] pitcherColumns = {"Position", "Name", "Team", "ERA", "FIP", "WAR", "Salary", "$/WAR" };
	private Object[][] playerData;
	private ArrayList<Player> playerList;
	private ArrayList<Pitcher> pitcherList;
	private ArrayList<Team> teamList;
	private ArrayList<String> tablePlayers; 
	private ArrayList<String> tablePitchers;
	private ArrayList<ArrayList> tempTable;
	private ArrayList tempData;
	private String menuYear = "All";
	private String menuEnd = "-To Year";
	private String menuTeam = "All Teams";
	private String menuPosition = "Position Players";
	private String menuPlayer = "Individual Players";
	
	public int getColumnCount() {
		if(!menuPosition.equals("All Pitchers") && !menuPosition.equals("Relief Pitchers")) {		
			return this.playerColumns.length;
		}
		else {
			return this.pitcherColumns.length;
		}
	}
	
	public int getRowCount() {
		if(!menuPosition.equals("All Pitchers")&& !menuPosition.equals("Relief Pitchers")) {		
			return this.tablePlayers.size();
		}
		else {
			return this.tablePitchers.size();
		}
	}
	
	@Override
	public String getColumnName(int col) {
		if(!menuPosition.equals("All Pitchers") && !menuPosition.equals("Relief Pitchers")) {
		    return playerColumns[col];
		}
		else {
			return pitcherColumns[col];
		}
	}
	
	public Object getValueAt(int row, int col) {
		return this.playerData[row][col];
	}
	
	@Override
	public Class<?> getColumnClass(int c) {
		   return getValueAt(0, c).getClass();
	}
	
	//every time user switches menu selection, repopulate the table based on parameters
	public void MenuFilter() {
		System.out.println("Menu Filtering");
		ArrayList<Integer> yearList = new ArrayList<Integer>();
		tempTable = new ArrayList<ArrayList>();
		boolean newYear = false;
		boolean newEnd = false;
		boolean newTeam = false;
		boolean newPos = false;
		boolean newPlayer = false;
		String fakeString = "0";
		Team fakeTeam = new Team();
		//check year status
		if(!menuYear.equals("All")) {
			newYear = true;
		}
		//check if a span of years is selected and if so create that span and store it in an array list
		if(!menuEnd.equals("-To Year") && !menuYear.equals("All")) {
			System.out.println("year range select");
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
		if(!menuTeam.equals("All Teams")) {
			System.out.println("team switch");
			newTeam = true;
		}
		
		if(!menuPlayer.equals("Individual Players")) {
			System.out.println("player switch");
			newPlayer = true;
		}
		
		//populate the table based on menu selections; checks first if pitchers or position players
		//are selected, then proceeds through each flag that was checked earlier
		boolean positionPlayer = false;		
		if(!menuPosition.equals("All Pitchers") && !menuPosition.equals("Relief Pitchers")) {
			if(!menuPosition.equals("Position Players")) {
				System.out.println("position switch");
				newPos = true;
			}
			for(int a = 0; a < tablePlayers.size(); a++) {
				String fString = "0";
				Team fTeam = new Team();
				Player tempPlayer = new Player(fString, fString, fTeam, fString, fString, fString);
				double owarCounter = 0;
				double dwarCounter = 0;
				double warCounter = 0;
				double salaryCounter = 0;
				double dollarCounter = 0;
				int dollarIndex = 1;
				boolean addPlayer = false;
				ArrayList<Team> tempTeams = new ArrayList<Team>();
				//uses alphabetically sorted and non-duplicated table of players that shows up in menu to
				//search through the actual list of Player objects
				for(int b = 0; b < playerList.size(); b++) {
					boolean add = true;
					if(tablePlayers.get(a).equals(playerList.get(b).getName())){
						tempPlayer = playerList.get(b);		
						tempTeams = new ArrayList<Team>(tempPlayer.getTeam());
					}
					//looks through the year array list of each Player object to determine if that object (year-specific)
					//matches the menu selection and flips the "add" flag to false if it doesn't
					
					//for year range selection
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
					//for single year selection (any other permutation results in all years being selected and thus no filtering)
					else if(newEnd == false && newYear == true) {
						if(!menuYear.equals(tempPlayer.getYear())) {
							add = false;
						}
					}
					//checks if any 1 team in Player object's team list matches menu selection
					if(newTeam == true) {
						boolean tempAdd = false;
						for(int c = 0; c < tempTeams.size(); c++) {
							if(menuTeam.equals(tempTeams.get(c).getName())) {
								tempAdd = true;
							}
						}	
						if(tempAdd != true) {
							add = false;
						}
					}
					//check if position matches menu selection
					if(newPos == true) {
						if(menuPosition != tempPlayer.getPos()) {
							add = false;
						}
					}
					//if individual player selected from menu, check if current player is that player
					if(newPlayer == true) {
						if(menuPlayer != tempPlayer.getName()) {
							add = false;
						}
					}		
					//if all filters bypassed add player to table
					if(add == true) {
						addPlayer = true;
						owarCounter = owarCounter + tempPlayer.getOWAR();
						dwarCounter = dwarCounter + tempPlayer.getDWAR();
						warCounter = warCounter + tempPlayer.getTWAR();
						salaryCounter = salaryCounter + tempPlayer.getSalary();
						dollarCounter = (dollarCounter + tempPlayer.getDollars())/dollarIndex;
						dollarIndex++;
					}
				}
				if(addPlayer == true) {
					tempData = new ArrayList();
					tempData.add(tempPlayer.getPos());
					tempData.add(tempPlayer.getName());
					tempData.add(tempTeams.get(tempTeams.size()-1).getName());
					tempData.add(owarCounter);
					tempData.add(dwarCounter);
					tempData.add(warCounter);
					tempData.add(salaryCounter);
					tempData.add(dollarCounter);
					tempTable.add(tempData);
				}
			}
			playerData = new Object[tempTable.size()][8];
			for(int a = 0; a < tempTable.size(); a++) {
				for(int b = 0; b < 8; b++) {
					playerData[a][b] = tempTable.get(a).get(b);
				}
			}
		}
		//repeat steps above for pitchers
		else {
			if(!menuPosition.equals("All Pitchers")) {
				newPos = true;
			}
			for(int a = 0; a < tablePitchers.size(); a++) {				
				Pitcher tempPlayer = new Pitcher(fakeString, fakeString, fakeTeam, fakeString, fakeString);
				double eraCounter = 0;
				double fipCounter = 0;
				double tempERA = 0;
				double tempFIP = 0;
				double tempInnings = 0;
				int gameWeight = 0;
				int rateIndex = 1;
				double warCounter = 0;
				double salaryCounter = 0;
				double dollarCounter = 0;
				int dollarIndex = 1;
				boolean addPlayer = false;
				ArrayList<Team> tempTeams = new ArrayList<Team>();				
				for(int b = 0; b < pitcherList.size(); b++) {
					boolean add = true;
					if(tablePitchers.get(a).equals(pitcherList.get(b).getName())) {
						tempPlayer = pitcherList.get(b);		
						tempTeams = new ArrayList<Team>(tempPlayer.getTeam());						
						gameWeight = tempPlayer.getGames()/160;
						tempERA = tempPlayer.getERA();
						tempFIP = tempPlayer.getFIP();
						if(Double.toString(tempERA).equals("-")) {
							tempERA = 0;
						}							
						if(Double.toString(tempFIP).equals("-")) {
							tempFIP = 0;
						}							
					}
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
							if(menuTeam.equals(tempTeams.get(c).getName())) {
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
					if(add == true) {
						addPlayer = true;
						double tempRuns = (eraCounter*tempPlayer.inningsTotal()/9) + ((tempPlayer.getERA()*tempInnings)/9);
						eraCounter = (tempRuns/(tempInnings+tempPlayer.inningsTotal())*9);
						tempRuns = (fipCounter*tempPlayer.inningsTotal()/9) + ((tempPlayer.getFIP()*tempInnings)/9);
						fipCounter = (tempRuns/(tempInnings+tempPlayer.inningsTotal())*9);
						tempInnings = tempInnings + tempPlayer.inningsTotal();
						warCounter = warCounter + tempPlayer.getTWAR();
						salaryCounter = salaryCounter + tempPlayer.getSalary();
						dollarCounter = (dollarCounter + tempPlayer.getDollars())/dollarIndex;
						rateIndex++;
						dollarIndex++;							
					}
				}
				if(addPlayer == true) {
					tempData = new ArrayList();
					tempData.add(tempPlayer.getPos());
					tempData.add(tempPlayer.getName());
					tempData.add(tempTeams.get(tempTeams.size()-1).getName());
					tempData.add(eraCounter);
					tempData.add(fipCounter);
					tempData.add(warCounter);
					tempData.add(salaryCounter);
					tempData.add(dollarCounter);
					tempTable.add(tempData);
				}
			}
			playerData = new Object[tempTable.size()][8];
			for(int a = 0; a < tempTable.size(); a++) {
				for(int b = 0; b < 8; b++) {
					playerData[a][b] = tempTable.get(a).get(b);
				}
			}	
		}				
	}	
	
	public void setYear(String year) {
		if(!year.equals("ITEM_STATE_CHANGED,item=All,stateChange=SELECTED")) {
			menuYear = year.replaceAll("[^\\d]", "" );
		}
		System.out.println(menuYear);
		MenuFilter();
	}
	
	public void setEnd(String end) {
		if(!end.equals("ITEM_STATE_CHANGED,item=-To Year,stateChange=SELECTED")) {
			menuEnd = end.replaceAll("[^\\d]", "" );
		}
		System.out.println(menuEnd);
		MenuFilter();
	}
	
	public void setTeam(String team) {
		String[] subString = team.split("=");
		subString = subString[1].split(",");
		menuTeam = subString[0];
		System.out.println(menuTeam);
		MenuFilter();
	}
	
	public void setPos(String pos) {
		String[] subString = pos.split("=");
		subString = subString[1].split(",");
		pos = subString[0];
		System.out.println(pos);
		menuPosition = pos;
		MenuFilter();
	}
	
	public void setPlayer(String name) {
		String[] subString = name.split("=");
		subString = subString[1].split(",");
		name = subString[0] + "," + subString[1];
		System.out.println(name);
		menuPlayer = name;
		MenuFilter();
	}
	
	public void initialize() {
		tempTable = new ArrayList<ArrayList>();
		//populate table by cycling through each player and collecting
		//data from matching players/years from player list
		for(int a = 0; a < tablePlayers.size(); a++) {
			String fString = "0";
			Team fTeam = new Team();
			Player tempPlayer = new Player(fString, fString, fTeam, fString, fString, fString);
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
			tempData = new ArrayList();
			tempData.add(tempPlayer.getPos());
			tempData.add(tempPlayer.getName());
			tempData.add(tempTeams.get(tempTeams.size()-1).getName());
			tempData.add(owarCounter);
			tempData.add(dwarCounter);
			tempData.add(warCounter);
			tempData.add(salaryCounter);
			tempData.add(dollarCounter);
			tempTable.add(tempData);
		}
		playerData = new Object[tempTable.size()][8];
		for(int a = 0; a < tempTable.size(); a++) {
			for(int b = 0; b < 8; b++) {
				playerData[a][b] = tempTable.get(a).get(b);
			}
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