package v.i.incandenza;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class DataTable extends AbstractTableModel {
	private String[] playerColumns = { "Position", "Name", "Team", "Games", "oRAA", "dRAA", "WAR", "Salary", "$/WAR" };
	private String[] pitcherColumns = { "Position", "Name", "Team", "Games", "Starts", "IP", "ERA", "FIP", "dRAA", "WAR", "Salary", "$/WAR" };
	private String[] defensiveColumns = { "Position", "Name", "Team", "Games", "Range+-", "Error+-", "Arm+-", "Assists", "Extra Bases", "dRAA" };
	private String[] totalColumns = { "Team", "Offensive RAA", "Defensive RAA", "Position WAR", "Pitching WAR", "Total WAR" };
	private Object[][] playerData;
	private ArrayList<Player> playerList;
	private ArrayList<Pitcher> pitcherList;
	private ArrayList<String> tablePlayers;
	private ArrayList<String> tablePitchers;
	private ArrayList<Team> teamList;
	private ArrayList<ArrayList> tempTable;
	private ArrayList tempData;
	private String menuYear = "All";
	private String menuEnd = "-To Year";
	private String menuTeam = "All Teams";
	private String menuPosition = "Position Players";
	private String menuPlayer = "Individual players";
	private String menuTotal = "-";
	private String currentYear;

	public int getColumnCount() {
		if(menuTotal.contains("Team WAR Totals")) {
			return this.totalColumns.length;
		}
		else if (menuPosition.equals("All Pitchers") || menuPosition.equals("Relief Pitchers")) {
			return this.pitcherColumns.length;
		} 
		else if((!menuPosition.equals("All Pitchers") && !menuPosition.equals("Relief Pitchers")) 
				 && !menuPosition.equals("Defensive Stats")) {
			return this.playerColumns.length;
		}
		else {
			return this.defensiveColumns.length;
		}
	}

	public int getRowCount() {
		return this.playerData.length;
	}

	@Override
	public String getColumnName(int col) {
		if(menuTotal.contains("Team WAR Totals")) {
			return this.totalColumns[col];
		}
		else if (menuPosition.equals("All Pitchers") || menuPosition.equals("Relief Pitchers")) {
			return this.pitcherColumns[col];
		} 
		else if((!menuPosition.equals("All Pitchers") && !menuPosition.equals("Relief Pitchers")) 
				 && !menuPosition.equals("Defensive Stats")) {
			return this.playerColumns[col];
		}
		else {
			return this.defensiveColumns[col];
		}
	}

	public Object getValueAt(int row, int col) {
		if(playerData.length > row) {
			return this.playerData[row][col];
		}
		else {
			return "0";
		}
	}

	@Override
	public Class<?> getColumnClass(int c) {
		int columnCount = this.getColumnCount();
		if (columnCount <= 1) {
			return String.class;
		}
		return getValueAt(0, c).getClass();
	}

	// every time user switches menu selection, repopulate the table based on
	// parameters
	public void MenuFilter() {
		ArrayList<Integer> yearList = new ArrayList<Integer>();
		tempTable = new ArrayList<ArrayList>();
		boolean newYear = false;
		boolean newEnd = false;
		boolean newTeam = false;
		boolean newPos = false;
		boolean newPlayer = false;
		boolean teamTotal = false;
		String fakeString = "0";
		Team fakeTeam = new Team(" ", 0);
		// check year status
		if (!menuYear.equals("All")) {
			System.out.println("year select");
			newYear = true;
		}
		// check if a span of years is selected and if so create that span and store it
		// in an array list
		if (!menuEnd.equals("-To Year") && !menuYear.equals("All") && !menuEnd.equals("") && !menuYear.equals("")) {
			System.out.println("year range select");
			if (Integer.parseInt(menuYear) < Integer.parseInt(menuEnd)) {
				newEnd = true;
				yearList.clear();
				int tempYear = Integer.parseInt(menuYear);
				for (int i = 0; i < (Integer.parseInt(menuEnd) - Integer.parseInt(menuYear)); i++) {
					yearList.add(tempYear);
					tempYear++;
				}
			} else if (Integer.parseInt(menuEnd) < Integer.parseInt(menuYear)) {
				newEnd = true;
				yearList.clear();
				int tempYear = Integer.parseInt(menuEnd);
				for (int i = 0; i < (Integer.parseInt(menuYear) - Integer.parseInt(menuEnd)); i++) {
					yearList.add(tempYear);
					tempYear++;
				}
			}
		}
		if (!menuTeam.equals("All Teams")) {
			newTeam = true;
		}

		if (!menuPlayer.equals("Individual Players")) {
			newPlayer = true;
		}
		
		if(!menuTotal.equals("-")) {
			teamTotal = true;
		}

		// populate the table based on menu selections; checks first if pitchers or
		// position players
		// are selected, then proceeds through each flag that was checked earlier
		if ((!menuPosition.equals("All Pitchers") && !menuPosition.equals("Relief Pitchers")) 
			 && !menuPosition.equals("Defensive Stats") && teamTotal == false) {
			if (!menuPosition.equals("Position Players")) {
				newPos = true;
			}
			for (int a = 0; a < tablePlayers.size(); a++) {
				String fString = "0";
				Team fTeam = new Team(" ", 0);
				double fDouble = 0.0;
				String[][] fOuts = new String[24][3];
				Player tempPlayer = new Player(fString, fString, fTeam, fString, fString, fString, fString, fDouble, fDouble, fOuts);
				double owarCounter = 0;
				double dwarCounter = 0;
				double warCounter = 0;
				double gameCounter = 0;
				double salaryCounter = 0;
				double dollarCounter = 0;
				int dollarIndex = 1;
				boolean addPlayer = false;
				ArrayList<Team> tempTeams = new ArrayList<Team>();
				// uses alphabetically sorted and non-duplicated table of players that shows up
				// in menu to
				// search through the actual list of Player objects
				for (int b = 0; b < playerList.size(); b++) {
					boolean add = true;
					if (tablePlayers.get(a).equals(playerList.get(b).getName())) {
						tempPlayer = playerList.get(b);
						tempTeams = new ArrayList<Team>(tempPlayer.getTeam());					
						// looks through the year array list of each Player object to determine if that
						// object (year-specific)
						// matches the menu selection and flips the "add" flag to false if it doesn't
	
						// for year range selection
						if (newEnd == true && newYear == true) {
							boolean tempAdd = false;
							for (int c = 0; c < yearList.size(); c++) {
								if (Integer.parseInt(tempPlayer.getYear()) == yearList.get(c)) {
									tempAdd = true;
								}
							}
							if (tempAdd != true) {
								add = false;
							}
						}
						// for single year selection (any other permutation results in all years being
						// selected and thus no filtering)
						else if (newEnd == false && newYear == true) {
							if (!menuYear.equals(tempPlayer.getYear())) {
								add = false;
							}
						}
						// checks if any 1 team in Player object's team list matches menu selection
						if (newTeam == true) {
							boolean tempAdd = false;
							for (int c = 0; c < tempTeams.size(); c++) {
								if (menuTeam.equals(tempTeams.get(c).getName())) {
									tempAdd = true;
								}
							}
							if (tempAdd != true) {
								add = false;
							}
						}
						// check if position matches menu selection
						if (newPos == true) {
							if (!menuPosition.equals(tempPlayer.getPos())) {
								add = false;
							}
						}
						// if individual player selected from menu, check if current player is that
						// player
						if (newPlayer == true) {
							if (!menuPlayer.equals(tempPlayer.getName())) {
								add = false;
							}
						}
						// System.out.println(add + ": add");
						// if all filters bypassed add player to table
						if (add == true) {
							addPlayer = true;
							owarCounter = owarCounter + tempPlayer.getOWAR();
							dwarCounter = dwarCounter + tempPlayer.getDWAR();
							gameCounter = gameCounter + tempPlayer.getGames();
							warCounter = warCounter + tempPlayer.getTWAR();
							salaryCounter = salaryCounter + tempPlayer.getSalary();
							dollarCounter = (dollarCounter + tempPlayer.getDollars())/dollarIndex;
							dollarIndex++;
						}
					}
				}
				if (addPlayer == true) {
					tempData = new ArrayList();
					tempData.add(tempPlayer.getPositions());
					tempData.add(tempPlayer.getName());
					if(tempPlayer.getYear().equals(this.currentYear)) {
						tempData.add(tempPlayer.getCurrentTeam());
					}
					else {
						tempData.add(tempPlayer.getTeam().get(0));
					}
					tempData.add(gameCounter);
					tempData.add(owarCounter);
					tempData.add(dwarCounter);
					tempData.add(warCounter);
					tempData.add(salaryCounter);
					tempData.add(dollarCounter);
					tempTable.add(tempData);
				}
			}
			playerData = new Object[tempTable.size()][9];
			for (int a = 0; a < tempTable.size(); a++) {
				for (int b = 0; b < 9; b++) {
					playerData[a][b] = tempTable.get(a).get(b);
					if(b < 8) {
						System.out.print(tempTable.get(a).get(b) + "|");		
					}
					else if(b == 8) {
						System.out.println(tempTable.get(a).get(b));
					}
				}
			}
		}
		// repeat steps above for pitchers if applicable
		else if ((menuPosition.equals("All Pitchers") || menuPosition.equals("Relief Pitchers")) && teamTotal == false) {
			if (menuPosition.equals("Relief Pitchers")) {
				newPos = true;
			}
			for (int a = 0; a < tablePitchers.size(); a++) {
				Pitcher tempPlayer = new Pitcher(fakeString, fakeString, fakeTeam, fakeString, fakeString);
				double eraCounter = 0;
				double fipCounter = 0;
				double startCounter = 0;
				double gameCounter = 0;
				double tempERA = 0;
				double tempFIP = 0;
				double tempInnings = 0;
				double warCounter = 0;
				double dWARCounter = 0;
				double salaryCounter = 0;
				double dollarCounter = 0;
				int dollarIndex = 1;
				boolean addPlayer = false;
				ArrayList<Team> tempTeams = new ArrayList<Team>();
				for (int b = 0; b < pitcherList.size(); b++) {
					boolean add = true;
					if (tablePitchers.get(a).equals(pitcherList.get(b).getName())) {
						tempPlayer = pitcherList.get(b);
						tempTeams = new ArrayList<Team>(tempPlayer.getTeam());
						tempERA = tempPlayer.getERA();
						tempFIP = tempPlayer.getFIP();
						if (Double.toString(tempERA).equals("-")) {
							tempERA = 0;
						}
						if (Double.toString(tempFIP).equals("-")) {
							tempFIP = 0;
						}
						
						if (newEnd == true && newYear == true) {
							boolean tempAdd = false;
							for (int c = 0; c < yearList.size(); c++) {
								if (Integer.parseInt(tempPlayer.getYear()) == yearList.get(c)) {
									tempAdd = true;
								}
							}
							if (tempAdd != true) {
								add = false;
							}
						} else if (newEnd == false && newYear == true) {
							if (!menuYear.equals(tempPlayer.getYear())) {
								add = false;
							}
						}
						if (newTeam == true) {
							boolean tempAdd = false;
							for (int c = 0; c < tempTeams.size(); c++) {
								if (menuTeam.equals(tempTeams.get(c).getName())) {
									tempAdd = true;
								}
							}
							if (tempAdd != true) {
								add = false;
							}
						}
						if (newPos == true) {
							if (!tempPlayer.checkPos().equals("Relief Pitchers")) {
								add = false;
							}
						}
						if (newPlayer == true) {
							if (!menuPlayer.equals(tempPlayer.getName())) {
								add = false;
							}
						}
						if (add == true) {
							addPlayer = true;							
							double tempRuns = (eraCounter * (tempInnings / 9))
											+ (tempPlayer.getERA() * (tempPlayer.inningsTotal() / 9));
							eraCounter = (tempRuns / (tempInnings + tempPlayer.inningsTotal())) * 9;
							tempRuns = (fipCounter * (tempInnings / 9))
									 + ((tempPlayer.getFIP() * (tempPlayer.inningsTotal()) / 9));
							fipCounter = (tempRuns / (tempInnings + tempPlayer.inningsTotal())) * 9;
							startCounter = startCounter + tempPlayer.getStarts();
							gameCounter = gameCounter + tempPlayer.getGames();
							warCounter = warCounter + tempPlayer.getTWAR();
							dWARCounter = dWARCounter + tempPlayer.getDWAR();
							tempInnings = tempInnings + tempPlayer.inningsTotal();
							salaryCounter = salaryCounter + tempPlayer.getSalary();
							dollarCounter = (dollarCounter + tempPlayer.getDollars()) / dollarIndex;
							dollarIndex++;
						}
					}
				}
				if (addPlayer == true) {
					tempData = new ArrayList();
					tempData.add(tempPlayer.getPos());
					tempData.add(tempPlayer.getName());
					if(tempPlayer.getYear().equals(this.currentYear)) {
						tempData.add(tempPlayer.getCurrentTeam());
					}
					else {
						tempData.add(tempPlayer.getTeam().get(0));
					}
					tempData.add(gameCounter);
					tempData.add(startCounter);					
					tempData.add(tempInnings);
					tempData.add(eraCounter);
					tempData.add(fipCounter);
					tempData.add(dWARCounter);
					tempData.add(warCounter);
					tempData.add(salaryCounter);
					tempData.add(dollarCounter);
					tempTable.add(tempData);
				}
			}
			playerData = new Object[tempTable.size()][12];
			for (int a = 0; a < tempTable.size(); a++) {
				for (int b = 0; b < 12; b++) {
					playerData[a][b] = tempTable.get(a).get(b);
					if(b < 11) {
						System.out.print(tempTable.get(a).get(b) + "|");		
					}
					else if(b == 11) {
						System.out.println(tempTable.get(a).get(b));
					}
				}
			}
		}
		else if(menuPosition.equals("Defensive Stats") && teamTotal == false) {
			for (int a = 0; a < tablePlayers.size(); a++) {
				String fString = "0";
				Team fTeam = new Team(" ", 0);
				double fDouble = 0.0;
				String[][] fOuts = new String[24][3];
				Player tempPlayer = new Player(fString, fString, fTeam, fString, fString, fString, fString, fDouble, fDouble, fOuts);
				double gameCounter = 0;
				double rangeCounter = 0;
				double errorCounter = 0;
				double armCounter = 0;
				double defCounter = 0;
				double assistCounter = 0;
				double extraCounter = 0;
				boolean addPlayer = false;
				ArrayList<Team> tempTeams = new ArrayList<Team>();
				// uses alphabetically sorted and non-duplicated table of players that shows up
				// in menu to
				// search through the actual list of Player objects
				for (int b = 0; b < playerList.size(); b++) {
					boolean add = true;
					if (tablePlayers.get(a).equals(playerList.get(b).getName())) {
						tempPlayer = playerList.get(b);
						tempTeams = new ArrayList<Team>(tempPlayer.getTeam());					
						// looks through the year array list of each Player object to determine if that
						// object (year-specific)
						// matches the menu selection and flips the "add" flag to false if it doesn't
	
						// for year range selection
						if (newEnd == true && newYear == true) {
							boolean tempAdd = false;
							for (int c = 0; c < yearList.size(); c++) {
								if (Integer.parseInt(tempPlayer.getYear()) == yearList.get(c)) {
									tempAdd = true;
								}
							}
							if (tempAdd != true) {
								add = false;
							}
						}
						// for single year selection (any other permutation results in all years being
						// selected and thus no filtering)
						else if (newEnd == false && newYear == true) {
							if (!menuYear.equals(tempPlayer.getYear())) {
								add = false;
							}
						}
						// checks if any 1 team in Player object's team list matches menu selection
						if (newTeam == true) {
							boolean tempAdd = false;
							for (int c = 0; c < tempTeams.size(); c++) {
								if (menuTeam.equals(tempTeams.get(c).getName())) {
									tempAdd = true;
								}
							}
							if (tempAdd != true) {
								add = false;
							}
						}
						// check if position matches menu selection
						if (newPos == true) {
							if (!menuPosition.equals(tempPlayer.getPos())) {
								add = false;
							}
						}
						// if individual player selected from menu, check if current player is that
						// player
						if (newPlayer == true) {
							if (!menuPlayer.equals(tempPlayer.getName())) {
								add = false;
							}
						}
						// System.out.println(add + ": add");
						// if all filters bypassed add player to table
						if (add == true) {
							addPlayer = true;
							gameCounter = gameCounter + tempPlayer.getGames();
							rangeCounter = rangeCounter + tempPlayer.getRange();
							errorCounter = errorCounter + tempPlayer.getError();
							armCounter = armCounter + tempPlayer.getArm();
							defCounter = defCounter + tempPlayer.getDWAR();
							assistCounter = assistCounter + tempPlayer.getAssists();
							extraCounter = extraCounter + tempPlayer.getExtra();
						}
					}
				}
				if (addPlayer == true) {
					tempData = new ArrayList();
					tempData.add(tempPlayer.getPositions());
					tempData.add(tempPlayer.getName());
					if(tempPlayer.getYear().equals(this.currentYear)) {
						tempData.add(tempPlayer.getCurrentTeam());
					}
					else {
						tempData.add(tempPlayer.getTeam().get(0));
					}
					tempData.add(gameCounter);
					tempData.add(rangeCounter);
					tempData.add(errorCounter);
					tempData.add(armCounter);
					tempData.add(assistCounter);
					tempData.add(extraCounter);
					tempData.add(defCounter);
					tempTable.add(tempData);
				}
			}	
			for (int a = 0; a < tablePitchers.size(); a++) {
				String fString = "0";
				Team fTeam = new Team(" ", 0);
				Pitcher tempPlayer = new Pitcher(fString, fString, fTeam, fString, fString);
				double gameCounter = 0;
				double rangeCounter = 0;
				double errorCounter = 0;
				double armCounter = 0;
				double defCounter = 0;
				double assistCounter = 0;
				double extraCounter = 0;
				boolean addPlayer = false;
				ArrayList<Team> tempTeams = new ArrayList<Team>();
				// uses alphabetically sorted and non-duplicated table of players that shows up
				// in menu to
				// search through the actual list of Player objects
				for (int b = 0; b < pitcherList.size(); b++) {
					boolean add = true;
					if (tablePlayers.get(a).equals(playerList.get(b).getName())) {
						tempPlayer = pitcherList.get(b);
						tempTeams = new ArrayList<Team>(tempPlayer.getTeam());					
						// looks through the year array list of each Player object to determine if that
						// object (year-specific)
						// matches the menu selection and flips the "add" flag to false if it doesn't	
						// for year range selection
						if (newEnd == true && newYear == true) {
							boolean tempAdd = false;
							for (int c = 0; c < yearList.size(); c++) {
								if (Integer.parseInt(tempPlayer.getYear()) == yearList.get(c)) {
									tempAdd = true;
								}
							}
							if (tempAdd != true) {
								add = false;
							}
						}
						// for single year selection (any other permutation results in all years being
						// selected and thus no filtering)
						else if (newEnd == false && newYear == true) {
							if (!menuYear.equals(tempPlayer.getYear())) {
								add = false;
							}
						}
						// checks if any 1 team in Player object's team list matches menu selection
						if (newTeam == true) {
							boolean tempAdd = false;
							for (int c = 0; c < tempTeams.size(); c++) {
								if (menuTeam.equals(tempTeams.get(c).getName())) {
									tempAdd = true;
								}
							}
							if (tempAdd != true) {
								add = false;
							}
						}
						// check if position matches menu selection
						if (newPos == true) {
							if (!menuPosition.equals(tempPlayer.getPos())) {
								add = false;
							}
						}
						// if individual player selected from menu, check if current player is that
						// player
						if (newPlayer == true) {
							if (!menuPlayer.equals(tempPlayer.getName())) {
								add = false;
							}
						}
						// System.out.println(add + ": add");
						// if all filters bypassed add player to table
						if (add == true) {
							addPlayer = true;
							gameCounter = gameCounter + tempPlayer.getGames();
							rangeCounter = rangeCounter + tempPlayer.getRange();
							errorCounter = errorCounter + tempPlayer.getError();
							armCounter = armCounter + tempPlayer.getArm();
							defCounter = defCounter + tempPlayer.getDWAR();
						}
					}
				}
				if (addPlayer == true) {
					tempData = new ArrayList();
					tempData.add(tempPlayer.getPos());
					tempData.add(tempPlayer.getName());
					if(tempPlayer.getYear().equals(this.currentYear)) {
						tempData.add(tempPlayer.getCurrentTeam());
					}
					else {
						tempData.add(tempPlayer.getTeam().get(0));
					}
					tempData.add(gameCounter);
					tempData.add(rangeCounter);
					tempData.add(errorCounter);
					tempData.add(armCounter);
					tempData.add(assistCounter);
					tempData.add(extraCounter);
					tempData.add(defCounter);
					tempTable.add(tempData);
				}
			}	
			playerData = new Object[tempTable.size()][10];
			for (int a = 0; a < tempTable.size(); a++) {
				for (int b = 0; b < 10; b++) {
					playerData[a][b] = tempTable.get(a).get(b);
					if(b < 9) {
						System.out.print(tempTable.get(a).get(b) + "|");		
					}
					else if(b == 9) {
						System.out.println(tempTable.get(a).get(b));
					}
				}
			}
		}
		else if(teamTotal == true) {
			for (int a = 0; a < teamList.size(); a++) {
				System.out.println(teamList.get(a).getName());
				String fString = "0";
				Team fTeam = new Team(" ", 0);
				double fDouble = 0.0;
				String[][] fOuts = new String[24][3];
				Player tempPlayer = new Player(fString, fString, fTeam, fString, fString, fString, fString, fDouble, fDouble, fOuts);
				double owarCounter = 0;
				double dwarCounter = 0;
				double warCounter = 0;
				double pwarCounter = 0;
				ArrayList<Team> tempTeams;
				for(int z = 0; z < tablePlayers.size(); z++) {	
					// uses alphabetically sorted and non-duplicated table of players that shows up
					// in menu to
					// search through the actual list of Player objects
					for (int b = 0; b < playerList.size(); b++) {
						boolean add = true;
						if (tablePlayers.get(z).equals(playerList.get(b).getName())) {
							tempPlayer = playerList.get(b);
							tempTeams = new ArrayList<Team>(tempPlayer.getTeam());	
							boolean multiTeam = false;
							if(tempTeams.size() > 1) {
								multiTeam = true;
							}
							// looks through the year array list of each Player object to determine if that
							// object (year-specific)
							// matches the menu selection and flips the "add" flag to false if it doesn't
		
							// for year range selection
							if (newEnd == true && newYear == true) {
								boolean tempAdd = false;
								for (int c = 0; c < yearList.size(); c++) {
									if (Integer.parseInt(tempPlayer.getYear()) == yearList.get(c)) {
										tempAdd = true;
									}
								}
								if (tempAdd != true) {
									add = false;
								}
							}
							// for single year selection (any other permutation results in all years being
							// selected and thus no filtering)
							else if (newEnd == false && newYear == true) {
								if (!menuYear.equals(tempPlayer.getYear())) {
									add = false;
								}
							}
							boolean teamAdd = false;
							ArrayList<Integer> teamIndex = new ArrayList<Integer>();
							for(int c = 0; c < tempTeams.size(); c++) {						
								if(teamList.get(a).getName().equals(tempTeams.get(c).getName())) {
									teamAdd = true;
									teamIndex.add(c);
								}
							}
							if(teamAdd != true) {
								add = false;								
							}
							
							// System.out.println(add + ": add");
							// if all filters bypassed add player to table
							if (add == true && multiTeam == false) {															
								owarCounter = owarCounter + (tempPlayer.getOWAR());
								dwarCounter = dwarCounter + (tempPlayer.getDWAR());
								warCounter = warCounter + (tempPlayer.getTWAR());
							}
							if(add == true && multiTeam == true) {
								for(int d = 0; d < teamIndex.size(); d++) {
									owarCounter = owarCounter + (((tempPlayer.getOWAR()*(tempPlayer.getTeamGames(teamList.get(teamIndex.get(d))))/tempPlayer.getGames())));
									dwarCounter = dwarCounter + (((tempPlayer.getDWAR()*(tempPlayer.getTeamGames(teamList.get(teamIndex.get(d))))/tempPlayer.getGames())));
									warCounter = warCounter + (((tempPlayer.getTWAR()*(tempPlayer.getTeamGames(teamList.get(teamIndex.get(d))))/tempPlayer.getGames())));
								}
							}
						}
					}
				}		
				ArrayList<Team> pitcherTeams;
				for (int y = 0; y < tablePitchers.size(); y++) {
					boolean add = true;
					for(int z = 0; z <pitcherList.size(); z++) {
						if (tablePitchers.get(y).equals(pitcherList.get(z).getName())) {		
							Pitcher tempPitcher = pitcherList.get(z);
							pitcherTeams = new ArrayList<Team>(tempPitcher.getTeam());	
							boolean multiTeam = false;
							if(pitcherTeams.size() > 1) {
								multiTeam = true;
							}
							// looks through the year array list of each Player object to determine if that
							// object (year-specific)
							// matches the menu selection and flips the "add" flag to false if it doesn't
		
							// for year range selection
							if (newEnd == true && newYear == true) {
								boolean tempAdd = false;
								for (int c = 0; c < yearList.size(); c++) {
									if (Integer.parseInt(tempPitcher.getYear()) == yearList.get(c)) {
										tempAdd = true;
									}
								}
								if (tempAdd != true) {
									add = false;
								}
							}
							// for single year selection (any other permutation results in all years being
							// selected and thus no filtering)
							else if (newEnd == false && newYear == true) {
								if (!menuYear.equals(tempPitcher.getYear())) {
									add = false;
								}
							}
							boolean teamAdd = false;
							ArrayList<Integer> teamIndex = new ArrayList<Integer>();
							for(int c = 0; c < pitcherTeams.size(); c++) {						
								if(teamList.get(a).getName().equals(pitcherTeams.get(c).getName())) {
									teamAdd = true;
									teamIndex.add(c);
								}
							}
							if(teamAdd != true) {
								add = false;
							}
							// if all filters bypassed add player to table
							if (add == true && multiTeam == false) {
								pwarCounter = pwarCounter + tempPitcher.getTWAR();
							}
							if(add == true && multiTeam == true) {
								for(int d = 0; d < teamIndex.size(); d++) {
									pwarCounter = pwarCounter + (tempPitcher.getTWAR()*(tempPitcher.getTeamInnings(teamList.get(teamIndex.get(d)))/tempPitcher.inningsTotal()));
								}
							}
						}
					}
				}
				tempData = new ArrayList();
				tempData.add(teamList.get(a).getName());
				tempData.add(owarCounter);
				tempData.add(dwarCounter);
				tempData.add(warCounter);
				tempData.add(pwarCounter);				
				tempData.add(warCounter + pwarCounter);
				tempTable.add(tempData);
			}
			playerData = new Object[tempTable.size()][6];
			for (int a = 0; a < tempTable.size(); a++) {
				for (int b = 0; b < 6; b++) {
					playerData[a][b] = tempTable.get(a).get(b);
					if(b < 5) {
						System.out.print(tempTable.get(a).get(b) + "|");		
					}
					else if(b == 5) {
						System.out.println(tempTable.get(a).get(b));
					}					
				}
			}
		}
	}

	public void setYear(String year) {
		System.out.println(year);
		if (year.equals("ITEM_STATE_CHANGED,item=All,stateChange=SELECTED")) {
			menuYear = "All";
		}
		if (year.equals("All")) {
			menuYear = "All";
		} 
		else {
			menuYear = year.replaceAll("[^\\d]", "");
			System.out.println(menuYear);
		}
	}

	public void setEnd(String end) {
		System.out.println(end);
		if (end.equals("ITEM_STATE_CHANGED,item=-To Year,stateChange=SELECTED")) {
			menuEnd = "-To Year";
		} 
		if (end.equals("-To Year")) {
			menuEnd = "-To Year";
		} 
		else {
			menuEnd = end.replaceAll("[^\\d]", "");
			System.out.println(menuEnd);
		}
	}

	public void setTeam(String team) {
		if (team.equals("All Teams")) {
			menuTeam = team;
		} 
		else {
			String[] subString = team.split("=");
			subString = subString[1].split(",");
			menuTeam = subString[0];
		}
	}

	public void setPos(String pos) {
		if (pos.equals("Position Players") || pos.equals("All Pitchers") || pos.equals("Relief Pitchers")) {
			menuPosition = pos;
		} 
		else {
			String[] subString = pos.split("=");
			subString = subString[1].split(",");
			pos = subString[0];
			menuPosition = pos;
		}
	}

	public void setPlayer(String name) {
		if (name.contains("Individual Players,stateChange")) {
			name = "Individual Players";
		}
		if (name.equals("Individual Players")) {
			menuPlayer = name;
		} 
		else {
			String[] subString = name.split("=");
			subString = subString[1].split(",");
			name = subString[0] + "," + subString[1];
			menuPlayer = name;
		}
	}
	
	public void setTotal(String total) {
		if (total.contains("-,stateChange")) {
			total = "-";
		}
		if (total.equals("-")) {
			menuTotal = total;
		} 
		else {			
			menuTotal = "Team WAR Totals";
		}
	}

	public void initialize() {
		tempTable = new ArrayList<ArrayList>();
		//populate table by cycling through each player and collecting
		//data from matching players/years from player list
		for(int a = 0; a < tablePlayers.size(); a++) {
			String fString = "0";
			Team fTeam = new Team(" ", 0);
			double fDouble = 0.0;
			String[][] fOuts = new String[24][3];
			Player tempPlayer = new Player(fString, fString, fTeam, fString, fString, fString, fString, fDouble, fDouble, fOuts);
			double owarCounter = 0;
			double dwarCounter = 0;
			double warCounter = 0;
			double gameCounter = 0;
			double salaryCounter = 0;
			double dollarCounter = 0;
			int dollarIndex = 1;
			for(int b = 0; b < playerList.size(); b++) {
				if(tablePlayers.get(a) == playerList.get(b).getName()) {
					tempPlayer = playerList.get(b);
					owarCounter = owarCounter + tempPlayer.getOWAR();
					dwarCounter = dwarCounter + tempPlayer.getDWAR();
					warCounter = warCounter + tempPlayer.getTWAR();
					gameCounter = gameCounter + tempPlayer.getGames();
					salaryCounter = salaryCounter + tempPlayer.getSalary();
					dollarCounter = (dollarCounter + tempPlayer.getDollars())/dollarIndex;
					dollarIndex++;
				}				
			}
			tempData = new ArrayList();
			tempData.add(tempPlayer.getPos());
			tempData.add(tempPlayer.getName());
			if(tempPlayer.getYear().equals(this.currentYear)) {
				tempData.add(tempPlayer.getCurrentTeam());
			}
			else {
				tempData.add(tempPlayer.getTeam().get(0));
			}
			tempData.add(gameCounter);
			tempData.add(owarCounter);
			tempData.add(dwarCounter);
			tempData.add(warCounter);
			tempData.add(salaryCounter);
			tempData.add(dollarCounter);
			tempTable.add(tempData);
		}
		playerData = new Object[tempTable.size()][9];
		for(int a = 0; a < tempTable.size(); a++) {
			for(int b = 0; b < 9; b++) {
				playerData[a][b] = tempTable.get(a).get(b);				
			}
		}
	}
	
	public Object[][] getData() {
		return this.playerData;
	}

	public DataTable(ArrayList<Player> players, ArrayList<Pitcher> pitchers, ArrayList<Team> teams,
					 ArrayList<String> playerTable, ArrayList<String> pitcherTable, String[] menuSelection, String currentYear) {
		this.playerList = new ArrayList<Player>(players);
		this.pitcherList = new ArrayList<Pitcher>(pitchers);
		this.tablePlayers = new ArrayList<String>(playerTable);
		this.tablePitchers = new ArrayList<String>(pitcherTable);
		this.teamList = new ArrayList<Team>(teams);
		this.currentYear = currentYear;
		if (menuSelection[0].equals("default")) {
			initialize();
		} 
		else {
			setYear(menuSelection[0]);
			setEnd(menuSelection[1]);
			setTeam(menuSelection[2]);
			setPos(menuSelection[3]);
			setPlayer(menuSelection[4]);
			setTotal(menuSelection[5]);
			MenuFilter();
		}
	}
}