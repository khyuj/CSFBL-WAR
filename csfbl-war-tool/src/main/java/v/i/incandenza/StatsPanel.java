package v.i.incandenza;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.*;
import java.io.IOException;

public class StatsPanel extends JPanel {
	private ArrayList<String> yearList = new ArrayList<String>();
	private ArrayList<Team> teamList = new ArrayList<Team>();
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private ArrayList<Pitcher> pitcherList = new ArrayList<Pitcher>();
	private ArrayList<ArrayList<String>> fieldingData;
	private ArrayList<String> playerData;
	private ArrayList<Player> identicalPlayers = new ArrayList<Player>();
	private String[][] teamAverages = new String[24][3];
	private Document doc;
	private Document xBat;
	private Document normBat;
	private Document normPitch;
	private Document xPitch;
	private String leagueID;	
	private String defaultUrl; 
	private DataTable tableModel;
	private JTable table;
	private String gameID;
	private String currentYear;
	private String currentDate;
	private double runsPerWin;
	private double leagueFIP;
	private double leagueRA9;
	private double leaguePA;
	private double leagueInnings;
	private double leagueRuns;
    double groundOuts = 0;
    double flyOuts = 0;
	private String menuYear = "All";
	private String menuEnd = "-To Year";
	private String menuTeam = "All Teams";
	private String menuPosition = "Position Players";
	private String menuPlayer = "Individual Players";
	private String menuTotal = "-";
	
	public void setYears(ArrayList<String> userYears) {		
		for(int i = 0; i < userYears.size(); i++) {
			yearList.add(userYears.get(i));			
		}
	}
	
	public void setLeague(String userLeague) {
		leagueID = userLeague;
	}
	
	public void refreshTable(DataTable model) {
		model.fireTableDataChanged();
		table.setModel(model);		
	    this.revalidate();
	    this.repaint();
	}
	
	public void assignFielding(String name, double runs, String year, String position, String type) {
		String duplicate = "";
		for(int i = 0; i < identicalPlayers.size(); i++) {
			if(name.equals(identicalPlayers.get(i).getName()) && !position.equals("pitcher")) {
				duplicate = "player";
			}
			else if(name.equals(identicalPlayers.get(i).getName()) && position.equals("pitcher")) {
				duplicate = "pitcher";
			}
		}
		for(int i = 0; i < playerList.size(); i++) {
			if(playerList.get(i).getName().equals(name) && playerList.get(i).getYear().equals(year)
			   && (duplicate.equals("player") || duplicate.equals(""))) {
				playerList.get(i).setDef(runs, position, type);
				System.out.println(name + ": " + runs);	
			}
		}
		for(int i = 0; i < pitcherList.size(); i++) {
			if(pitcherList.get(i).getName().equals(name) && pitcherList.get(i).getYear().equals(year)
			   && (duplicate.equals("pitcher") || duplicate.equals(""))) {
				pitcherList.get(i).setDef(runs);
				System.out.println(name + ": " + runs);
			}
		}				
	}
	
	public void assignArm(String name, String type, String year, String position) {
		if(!position.equals("pitcher")) {
			for(int i = 0; i < playerList.size(); i++) {
				if(playerList.get(i).getName().equals(name) && playerList.get(i).getYear().equals(year)) {
					playerList.get(i).setArm(type);
					System.out.println(name);
				}
			}
		}
		else {
			for(int i = 0; i < pitcherList.size(); i++) {
				if(pitcherList.get(i).getName().equals(name) && pitcherList.get(i).getYear().equals(year)) {
					pitcherList.get(i).setArm(type);
					System.out.println(name);
				}
			}
		}
	}
	
	public void assignRange(String name, String year, String position) {
		if(!position.equals("pitcher")) {
			for(int i = 0; i < playerList.size(); i++) {
				if(playerList.get(i).getName().equals(name) && playerList.get(i).getYear().equals(year)) {
					playerList.get(i).setRange();
					System.out.println(playerList.get(i).getName());
				}
			}
		}
		else {
			for(int i = 0; i < pitcherList.size(); i++) {
				if(pitcherList.get(i).getName().equals(name) && pitcherList.get(i).getYear().equals(year)) {
					pitcherList.get(i).setRange();
					System.out.println(pitcherList.get(i).getName());
				}
			}
		}
	}
	
	public void assignError(String name, String year, String position) {
		if(!position.equals("pitcher")) {
			for(int i = 0; i < playerList.size(); i++) {
				if(playerList.get(i).getName().equals(name) && playerList.get(i).getYear().equals(year)) {
					playerList.get(i).setError();
					System.out.println(playerList.get(i).getName());
				}
			}
		}
		else {
			for(int i = 0; i < pitcherList.size(); i++) {
				if(pitcherList.get(i).getName().equals(name) && pitcherList.get(i).getYear().equals(year)) {
					pitcherList.get(i).setError();
					System.out.println(pitcherList.get(i).getName());
				}
			}
		}
	}
		
	public String positionFormat(String position) {
		if(position.equals("P")) {
			position = "pitcher";
		}
		else if(position.equals("C")) {
			position = "to the catcher";
		}
		else if(position.equals("1B")) {
			position = "to first";
		}
		else if(position.equals("2B")) {
			position = "to second";
		}
		else if(position.equals("SS")) {
			position = "to short";
		}
		else if(position.equals("3B")) {
			position = "to third";
		}
		else if(position.equals("LF")) {
			position = "to left";
		}
		else if(position.equals("CF")) {
			position = "to center";
		}
		else if(position.equals("RF")) {
			position = "to right";
		}
		return position;
	}
	
	public StatsPanel(ArrayList<String> years, String league) {
		this.setYears(years);
		this.setLeague(league);		
		this.setLayout(new BorderLayout());
		JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));       	
        this.add(menuPanel, BorderLayout.PAGE_START); 
        
        //create the list of teams from the league page submitted by user
        defaultUrl = "https://www.csfbl.com/league/view.asp?leagueid=";
		if(leagueID != null) {
			try {
				String url = defaultUrl.concat(leagueID);
				doc = Jsoup.connect(url).timeout(0).get();					
				Elements parents = doc.select("tbody > tr > td");
				Elements leagueDate = doc.select("div > dl > dd");
				currentDate = leagueDate.get(0).text().split("\\s")[0];				
				currentYear = currentDate.split("/")[2];
				for(int a = 0; a < parents.size(); a += 2) {
					Element parent = parents.get(a);
					String x = parent.child(0).text();
					String y = parent.child(0).attr("href");
					String digits = y.replaceAll("[^\\d]", "");
					int teamID = Integer.parseInt(digits);
					Team z = new Team(x, teamID);	
					teamList.add(z);
					if(a == 0) {
						gameID = "https://www.csfbl.com/team/news.asp?teamid=" + teamID;
					}				
				}
			}
			catch(IOException e) {
		       e.getMessage();
		       e.printStackTrace();
			}			
		}
		
        //create selection menu based on all players in the league, at the same time 
        //create that list of players and add their stats where applicable
        DefaultComboBoxModel<String> playerModel = new DefaultComboBoxModel<>();
        playerModel.addElement("Individual Players");
        //cycles through each year specified
        for(int i = 0; i < yearList.size(); i++) {            
            //get Runs Per Win from league stats page
            String leagueStats = "https://www.csfbl.com/league/teamstats.asp?leagueid=";
            String yearStats = "&type=2&show=";
            String fipStats = "&type=5&show=";            
            try {
	            Document teamStats = Jsoup.connect(leagueStats + leagueID + yearStats + yearList.get(i)).timeout(0).get();
	            Elements runsInnings = teamStats.select("tbody > tr > td");	            
	            ArrayList<String> teamInnings = new ArrayList<String>();
	            for(int e = 0; e < runsInnings.size(); e+=20) {
	            	String innings = runsInnings.get(e+2).text();
	            	teamInnings.add(innings);
	            	innings = innings.replaceAll(",", "");
	            	this.leagueInnings = this.leagueInnings + Double.parseDouble(innings.split("\\.")[0]);
	            	if(innings.split("\\.")[1].equals(".1")) {
	            		this.leagueInnings = this.leagueInnings + 0.33;
	            	}
	            	else if(innings.split("\\.")[1].equals(".2")) {
	            		this.leagueInnings = this.leagueInnings + 0.67;
	            	}
	            	String runs = runsInnings.get(e+9).text();
	            	runs = runs.replaceAll(",", "");
	            	this.leagueRuns = this.leagueRuns + Double.parseDouble(runs);
	            }
	            this.leagueRA9 = (this.leagueRuns/this.leagueInnings) * 9;
	            this.runsPerWin = 2 * Math.pow((this.leagueRuns/this.leagueInnings)*18, 0.71);	            
	            //get league FIP
	            Document teamFIPs = Jsoup.connect(leagueStats + leagueID + fipStats + yearList.get(i)).timeout(0).get();
	            Elements fips = teamFIPs.select("tbody > tr > td");
	            this.leagueFIP = 0;	            
	            for(int e = 0; e < fips.size(); e+=18) {
	            	String tempInnings = teamInnings.get(e/18);
	            	tempInnings = tempInnings.replaceAll(",", "");
	            	double innings = Double.parseDouble(tempInnings.split("\\.")[0]);
	            	if(tempInnings.split("\\.")[1].equals(".1")) {
	            		innings = innings + 0.33;
	            	}
	            	else if(tempInnings.split("\\.")[1].equals(".2")) {
	            		innings = innings + 0.67;
	            	}
	            	this.leagueFIP = this.leagueFIP + (Double.parseDouble(fips.get(e+17).text())*(innings/9));
	            	String teamName = fips.get(e).text();	            	
	            	this.groundOuts = this.groundOuts + Double.parseDouble(fips.get(e+4).text().replaceAll(",", ""));
	            	this.flyOuts = this.flyOuts + Double.parseDouble(fips.get(e+5).text().replaceAll(",", ""));	 
	            	String[] tempOuts = new String[3];
	            	tempOuts[0] = teamName;
	            	tempOuts[1] = fips.get(e+4).text().replaceAll(",", "");
	            	tempOuts[2] = fips.get(e+5).text().replaceAll(",", "");
	            	this.teamAverages[e/18] = tempOuts;
	            }
	            this.groundOuts = groundOuts/24;
	            this.flyOuts = flyOuts/24;
	            this.leagueFIP = (this.leagueFIP/this.leagueInnings)*9;
	            System.out.println(this.leagueFIP + ": league FIP");	
	            System.out.println(this.leagueRA9 + ": league RA9");
	            System.out.println(this.groundOuts + ": average groundouts/team");
	            System.out.println(this.flyOuts + ": average flyouts/team");
	            ProgressBar.setValue(1);
            }
            catch(IOException e) {
    			e.getMessage();
    			e.printStackTrace();
    		}
        	//cycles through each team in the league in that year
        	for(int x = 0; x < teamList.size(); x++) {        		
        		String baseURL = "https://www.csfbl.com/team/stats.asp?teamid=";
        		String salaryURL = "https://www.csfbl.com/player/history.asp?playerid=";
        		String teamURL = "https://www.csfbl.com/player/view.asp?playerid=";
                String extBat = "&type=4";
                String normBat = "&type=1";
                String extPit = "&type=5";
                String normPit = "&type=2";
                String yearURL = "&status=" + yearList.get(i);
                Team tEam = teamList.get(x);                
                int iD = tEam.getID();
                //create stat page URLs
                try {
        			String url = baseURL.concat(Integer.toString(iD));
        			url = url.concat(extBat);
        			url = url.concat(yearURL);        			
        			this.xBat = Jsoup.connect(url).timeout(0).get();	
        			url = baseURL.concat(Integer.toString(iD));
        			url = url.concat(normBat);
        			url = url.concat(yearURL);    
        			this.normBat = Jsoup.connect(url).timeout(0).get();	        			
        			url = baseURL.concat(Integer.toString(iD));
        			url = url.concat(extPit);
        			url = url.concat(yearURL);        			
        			this.xPitch = Jsoup.connect(url).timeout(0).get();
        			url = baseURL.concat(Integer.toString(iD));
        			url = url.concat(normPit);
        			url = url.concat(yearURL);         			
        			this.normPitch = Jsoup.connect(url).timeout(0).get();
        			//get desired HTML elements from these pages
        			Elements batParents = this.xBat.select("tbody > tr > td");        			
        			Elements batNames = this.xBat.select("span[class].visible-xs-inline");  
        			Elements batGames = this.normBat.select("tbody > tr > td");
        			Elements xpitParents = this.xPitch.select("tbody > tr > td");        			
        			Elements xpitNames = this.xPitch.select("span[class].visible-xs-inline");
        			Elements pitchParents = this.normPitch.select("tbody > tr > td");      			
        			Elements pitchNames = this.normPitch.select("span[class].visible-xs-inline"); 
        			//go through each player on the stats page, extract stats/info, and
        			//check against existing list to see if player should have existing Player object
        			//updated or if a new player should be added to the list
        			for(int a = 0; a < batParents.size(); a+=18) {
        				Element posNode = batParents.get(a);
        				String playerPos = posNode.text();       				
        				Element nameNode = batNames.get(a/18);
        				String playerName = nameNode.attr("title"); 
        				Element oNode = batParents.get(a+17);
        				String xRaa = oNode.text();       				
        				String playerYear = yearList.get(i);
        				Element gameNode = batGames.get(a+2);
        				String playerGames = gameNode.text();
        				Element playerID = batParents.get(a+1);
        				String playerLink = playerID.child(0).attr("href");
        				playerLink = playerLink.replaceAll("[^\\d]", "" );
        				String playerURL = salaryURL + playerLink;
        				String salaryString = "0";
						if(!yearList.get(i).equals(currentYear)) {
							Document salaryPage = Jsoup.connect(playerURL).timeout(0).get();
							Elements salaryParents = salaryPage.select("tbody > tr > td");
							for(int z = 0; z < salaryParents.size(); z+=13) {
								if(salaryParents.get(z).text().equals(playerYear)) {
									salaryString = salaryParents.get(z+12).text();
									salaryString = salaryString.replaceAll("[^\\d]", "" );
									System.out.println(salaryString + ": " + playerID);
								}
							}		
						}
        				boolean match = false; 
        				boolean teamSwitch = false;
        				int matchNum = 0;
        				//check against existing list
        				for(int b = 0; b < playerList.size(); b++) {        					
        					Player playerCheck = playerList.get(b);
        					String yearCheck = playerCheck.getYear();
        					String nameCheck = playerCheck.getName();        					
        					ArrayList<Team> teamCheck = playerCheck.getTeam();
        					if(playerName.equals(nameCheck) && playerYear.equals(yearCheck)) {
	        					for (int c = 0; c < teamCheck.size(); c++) {	        					
	            					if(!(teamCheck.get(c).equals(tEam))) {
	            						match = true;            						
	            						teamSwitch = true;
	            						matchNum = b;
	            					}
	            					else {
	            						match = true;
	            						matchNum = b;
	            					}        						
	            				}
	        				}        					        					
        				}
        				if(match == true && teamSwitch == true) {
        					playerList.get(matchNum).setTeam(tEam);
        					String getTeam = teamURL + playerLink;
            				Document getTeamPage = Jsoup.connect(getTeam).timeout(0).get();
            				Elements teamElements = getTeamPage.select("div[class=col-xs-6 col-sm-4] > dl > dd > a");
            				String currentTeam = teamElements.get(1).text();
        					playerList.get(matchNum).setCurrentTeam(currentTeam);
        					playerList.get(matchNum).setXRAA(xRaa);
        					playerList.get(matchNum).setTeamGames(playerGames, tEam);
        				}
        				if(match == false && teamSwitch == false) {        					
        					Player newPlayer = new Player(playerName, playerPos, tEam, xRaa, playerYear, salaryString, playerGames, groundOuts, flyOuts, teamAverages);
        					playerList.add(newPlayer);
        				}        				
        			}
        			 ProgressBar.setValue(2);
        			//go through pitchers on stats page
        			for(int a = 0; a < pitchParents.size(); a+=21) {        				
        				Element posNode = pitchParents.get(a);
        				String playerPos = posNode.text();       				
        				Element nameNode = pitchNames.get(a/21);
        				String playerName = nameNode.attr("title");        				
        				Element gamesStart = pitchParents.get(a+15);
        				String playerStarts = gamesStart.text();
        				Element gamesTotal = pitchParents.get(a+2);        				
        				String playerGames = gamesTotal.text();
        				Element inningsTotal = pitchParents.get(a+3);        				
        				String playerInnings = inningsTotal.text();
        				Element eRA = pitchParents.get(a+19);
        				String playerERA = eRA.text();
        				if(playerERA.equals("-")) {
        					playerERA = "0";
        				}        
        				String playerYear = yearList.get(i);
        				Element playerID = pitchParents.get(a+1);
        				String playerLink = playerID.child(0).attr("href");
        				playerLink = playerLink.replaceAll("[^\\d]", "" );
        				String playerURL = salaryURL + playerLink;
        				String salaryString = "0";
						if(!yearList.get(i).equals(currentYear)) {
							Document salaryPage = Jsoup.connect(playerURL).timeout(0).get();
							Elements salaryParents = salaryPage.select("tbody > tr > td");
							for(int z = 0; z < salaryParents.size(); z+=13) {
								if(salaryParents.get(z).text().equals(playerYear)) {
									salaryString = salaryParents.get(z+12).text();
									salaryString = salaryString.replaceAll("[^\\d]", "" );
									System.out.println(salaryString + ": " + playerID);
								}
							}
						}
        				boolean match = false;
        				boolean switchTeam = false;
        				int matchNum = 0;
        				//check against existing list
        				for(int b = 0; b < pitcherList.size(); b++) {
        					Pitcher pitcherCheck = pitcherList.get(b);
        					String yearCheck = pitcherCheck.getYear();
        					String nameCheck = pitcherCheck.getName();
        					ArrayList<Team> teamCheck = pitcherCheck.getTeam();
        					if(playerName.equals(nameCheck) && playerYear.equals(yearCheck)) {
        						for (int c = 0; c < teamCheck.size(); c++) {	        					
	        						if(!(teamCheck.get(c).equals(tEam))) {
            							match = true;
            							switchTeam = true;
            							matchNum = b;
            						}
            						else {
            							match = true;
            						}        						
	        					}  
        					}
        				}
        				if(match == false && switchTeam == false) {    					
        					Pitcher newPitcher = new Pitcher(playerName, playerPos, tEam, playerYear, salaryString);
        					newPitcher.setGames(playerGames);
        					newPitcher.setStarts(playerStarts);
        					newPitcher.setInnings(playerInnings, tEam.getName());
        					newPitcher.setERA(playerERA);
        					pitcherList.add(newPitcher);
        				}  
        				else if(match == true && switchTeam == true) {
        					double tempInnings = 0;
        					//collect year's innings total from pitcher object
        					for(int d = 0; d < pitcherList.get(matchNum).getInningsArray().size(); d++) {
        						tempInnings = tempInnings + pitcherList.get(matchNum).getInnings(d).getInnings();
        					}
        					//update Player's season ERA weighted by innings
        					double tempRuns = ((Double.parseDouble(playerERA)*Double.parseDouble(playerInnings))/9) + 							((pitcherList.get(matchNum).getERA()*tempInnings)/9);
        					double tempERA = (tempRuns/(tempInnings+Double.parseDouble(playerInnings)))*9;
        					pitcherList.get(matchNum).setERA(Double.toString(tempERA));
        					pitcherList.get(matchNum).setGames(playerGames);
        					pitcherList.get(matchNum).setStarts(playerStarts);
        					pitcherList.get(matchNum).setInnings(playerInnings, tEam.getName());
        					pitcherList.get(matchNum).setTeam(tEam);
        					String getTeam = teamURL + playerLink;
            				Document getTeamPage = Jsoup.connect(getTeam).timeout(0).get();
            				Elements teamElements = getTeamPage.select("div[class=col-xs-6 col-sm-4] > dl > dd > a");
            				String currentTeam = teamElements.get(1).text();
        					playerList.get(matchNum).setCurrentTeam(currentTeam);
        				}
        			}
        			ProgressBar.setValue(3);
        			for(int a = 0; a < xpitParents.size(); a+=19) {
        				//get data from extended pitching stats page on pitcher
        				Element nameNode = xpitNames.get(a/19);
        				String playerName = nameNode.attr("title");        				
        				Element fIP = xpitParents.get(a+18);        				
        				String playerFIP = fIP.text();
        				if(playerFIP.equals("-")) {
        					playerFIP = "0";
        				}        				
        				String playerYear = yearList.get(i);       				      				
        				boolean match = false;
        				boolean switchTeam = false;
        				int matchNum = 0;
        				//add or update pitcher by going through existing pitcherList
        				for(int b = 0; b < pitcherList.size(); b++) {
        					Pitcher pitcherCheck = pitcherList.get(b);
        					String yearCheck = pitcherCheck.getYear();
        					String nameCheck = pitcherCheck.getName();
        					ArrayList<Team> teamCheck = pitcherCheck.getTeam();
        					//go through the team list of each matched player, checks for in-season team switches
        					if(playerName.equals(nameCheck) && playerYear.equals(yearCheck)) {
        						for (int c = 0; c < teamCheck.size(); c++) {	        					
	        						if(!(teamCheck.get(c).equals(tEam))) {
            							match = true; 
            							switchTeam = true;						
            							matchNum = b;
            						}
            						else {
            							match = true;
            							matchNum = b;
            						}
        						}
	        				}
        				}
        				//create or modify player based on match status        					
        				if(match == true && switchTeam == true) {
       						double tempInnings = 0;
       						double playerInnings = 0;
           					//collect year's innings total from pitcher object
           					for(int d = 0; d < pitcherList.get(matchNum).getInningsArray().size(); d++) {
           						if(pitcherList.get(matchNum).getInnings(d).getTeam() == tEam.getName()) {
           							playerInnings = pitcherList.get(matchNum).getInnings(d).getInnings();
           						}
           						else {
           							tempInnings = tempInnings + pitcherList.get(matchNum).getInnings(d).getInnings();
           						}
           					}
           					double tempRuns = ((Double.parseDouble(playerFIP)*playerInnings)/9) + ((pitcherList.get(matchNum).getFIP()*tempInnings)/9);
           					double tempFIP = (tempRuns/(tempInnings+playerInnings))*9;
	               			pitcherList.get(matchNum).setFIP(Double.toString(tempFIP)); 
	               			pitcherList.get(matchNum).setTeam(tEam);
        				}
        				else if(match == true && switchTeam == false) {
           					pitcherList.get(matchNum).setFIP(playerFIP);
        				}    				      			
        			}
        			ProgressBar.setValue(4);
                }       	
        		catch(IOException e) {
        			e.getMessage();
        			e.printStackTrace();
        		}
				if(yearList.get(i).equals(currentYear)) {
					try {
				    	Document salaryPage = Jsoup.connect("https://www.csfbl.com/team/roster.asp?teamid=" + iD).timeout(0).get();
				    	String salaryString = "";
						Elements salaryParents = salaryPage.select("tbody > tr > td");
						for(int z = 0; z < salaryParents.size(); z+=19) {
							String playerName = salaryParents.get(z+1).text();
							String firstName = playerName.split(" ")[0];
							String lastName = playerName.split(" ")[1];
							playerName = lastName + ", " + firstName;
							salaryString = salaryParents.get(z+18).attr("data-value");
							int salary = Integer.parseInt(salaryString) * 1000;
							salaryString = Integer.toString(salary);
							System.out.println(salaryString + ": " + playerName);
							for(int p = 0; p < playerList.size(); p++) {
								if(playerName.equals(playerList.get(p).getName())) {
									playerList.get(p).setSalary(salaryString);
								}
							}
							for(int p = 0; p < pitcherList.size(); p++) {
								if(playerName.equals(pitcherList.get(p).getName())) {
									pitcherList.get(p).setSalary(salaryString);
								}
							}
						}
				    }                	
					catch(IOException e) {
						e.getMessage();
						e.printStackTrace();
					}
				}
        	}
        	//check if there are any players with the same name (can occur only once per class of batter/pitcher)
        	for(int a = 0; a < playerList.size(); a++) {
        		for(int b = 0; b < pitcherList.size(); b++) {
        			if(playerList.get(a).getName().equals(pitcherList.get(b).getName())) {
        				this.identicalPlayers.add(playerList.get(a));
        			}
        		}
        	}
        	
        	//set plate appearances for player to calculate replacement
        	try {
	        	String battingStats = "&type=4&show=";
	        	String gameStats = "&type=1&show=";
	            String playerStats = "https://www.csfbl.com/league/stats.asp?leagueid=";
	            Document playerPage = Jsoup.connect(playerStats + leagueID + battingStats + yearList.get(i)).timeout(0).get();
	            Elements plateAppearances = playerPage.select("tbody > tr > td");
	            Document gamePage = Jsoup.connect(playerStats + leagueID + gameStats + yearList.get(i)).timeout(0).get();
	            Elements gamesPlayed = gamePage.select("tbody > tr > td");
	            for(int e = 0; e < plateAppearances.size(); e+=18) {
	            	String playerPA = plateAppearances.get(e+2).text();
	            	String playerName = plateAppearances.get(e+1).text();
	            	String playerGames = gamesPlayed.get(e+2).text();
	            	String deleteName = playerName.split(",")[0];
	            	playerName = playerName.split(",")[1];
	            	playerName = playerName.replaceAll(deleteName, "");
	            	playerName = deleteName + "," + playerName;
	            	this.leaguePA = this.leaguePA + Double.parseDouble(playerPA);
	            	for(int f = 0; f < playerList.size(); f++) {
	            		if(playerName.equals(playerList.get(f).getName()) && yearList.get(i).equals(playerList.get(f).getYear())) {
	            			playerList.get(f).setPA(Double.parseDouble(playerPA));
	            			playerList.get(f).setGames(Double.parseDouble(playerGames));
	            		}
	            	}
	            	for(int f = 0; f < playerList.size(); f++) {
	            		playerList.get(f).weightOuts();	            		
	            	}
	            }	            
        	}
	        catch(IOException e) {
	        	e.getMessage();
	        	e.printStackTrace();
        	}     
        }
        //calculate games played this current season regardless of seasons specified, in order to calculate scraping start page
        double pageNum = 1;
        int currentGames = 0;
        if(!currentYear.equals(yearList.get(yearList.size() - 1))) {
	        try {
		        String currentURL = "https://www.csfbl.com/league/standings.asp?leagueid=" + leagueID + "&season=" + currentYear;
				Document currentStandings = Jsoup.connect(currentURL).get();
				Elements currentWL = currentStandings.select("tbody > tr > td");
				int currentWins = 0;
				int currentLosses = 0;
				for(int z = 0; z < currentWL.size(); z+=13) {
					currentWins = currentWins + Integer.parseInt(currentWL.get(z+2).text());
					currentLosses = currentLosses + Integer.parseInt(currentWL.get(z+3).text());
				}
				currentGames = (currentWins + currentLosses)/2;
				double doubleGames = currentGames;
		        pageNum = 1 + (Math.abs(Integer.parseInt(currentYear) - Integer.parseInt(yearList.get(yearList.size() - 1))) * 12);
		        double adjustment = Math.round((12.0 - ((doubleGames/1920.0)*12.0)));
		        pageNum = pageNum - adjustment;
		        System.out.println(adjustment + ": adjustment");
		        System.out.println(currentGames + ": this season's games played");	       
		        System.out.println(pageNum + ": start page");
	        }
	        catch(IOException e) {
				e.getMessage();
				e.printStackTrace();
			} 
        }
        //get PBP url range for most recent season
        for(int u = yearList.size() - 1; u >= 0; u--) {
        	try {
        		//get total games played for selected season
        		int totalGames = 0;        		
        		System.out.println(currentYear + ": current year");
        		if(yearList.get(u).equals(currentYear)) {
        			String currentURL = "https://www.csfbl.com/league/standings.asp?leagueid=" + leagueID + "&season=" + currentYear;
     				Document currentStandings = Jsoup.connect(currentURL).timeout(0).get();
     				Elements currentWL = currentStandings.select("tbody > tr > td");
     				int currentWins = 0;
     				int currentLosses = 0;
     				for(int z = 0; z < currentWL.size(); z+=13) {
     					currentWins = currentWins + Integer.parseInt(currentWL.get(z+2).text());
     					currentLosses = currentLosses + Integer.parseInt(currentWL.get(z+3).text());
     				}
     				totalGames = ((currentWins + currentLosses)/2) - 1;
        		}
        		else {
        			totalGames = 1919;
        		}
            	//get most recent game played
    			boolean lastGame = false;
    			int upperLink = 0;
    			String pbpURL = "https://www.csfbl.com/playbyplay.asp?gameid=";
    			Elements gameList = new Elements();
    			//check team news page for playoff appearance, if none detected then get last game played
    			while(lastGame == false) {
    				Document teamNews = Jsoup.connect(gameID + "&page=" + pageNum).timeout(0).get();
    				gameList = teamNews.select("tbody > tr > td");
   					boolean endPage = false;
   					while(endPage == false) {
    					for(int a = 0; a < gameList.size(); a++) {
    						if(gameList.get(a).text().contains("Player of the Game") && !gameList.get(a).text().contains("not yet awarded")) {
    							String gameLink = gameList.get(a).child(4).attr("href");
    			    			gameLink = gameLink.split("&")[0];
    			    			gameLink = gameLink.replaceAll("[^\\d]", "");
    			    			int lastIndex = Integer.parseInt(gameLink);
    			    			Document playByPlay = Jsoup.connect(pbpURL + lastIndex).timeout(0).get();
    			    			System.out.println(pbpURL + lastIndex);
    			    			Elements metas = playByPlay.select("head > meta");
    			    			Elements teams = playByPlay.select("div > h2 > a");
    			    			String firstTeam = teams.get(0).text();
    			    			String firstClean = firstTeam.replaceAll("\\.|,", " ");
    			    			String secondTeam = teams.get(1).text();
    			    			String secondClean = secondTeam.replaceAll("\\.|,", " ");
    			    			Element date = metas.get(3);
    			    			String dateString = date.attr("content");
    			    			dateString = dateString.replaceAll(firstTeam, firstClean);
    			    			dateString = dateString.replaceAll(secondTeam, secondClean);
    			    			dateString = dateString.replaceAll(" ", "");
    			    			dateString = dateString.split(",")[1];
    			    			dateString = dateString.split(":")[0];
    			    			String year = dateString.split("/")[2].replaceAll(" ", "");
    			    			String month = dateString.split("/")[0].replaceAll(" ", "");
    			    			String day = dateString.split("/")[1].replaceAll(" ", "");   			    			
    			    			if(year.equals(yearList.get(u)) && !month.contains("10") && !(month.contains("9") && day.contains("30"))) {
            						upperLink = lastIndex;
        							lastGame = true;
        							endPage = true;
        							break;
       							}								
    						}
    						if(a == gameList.size() - 1) {
    							endPage = true;
    						}
    					}
    				}
    				pageNum++;
    			}
    			//get last game played league-wide, beginning search from last game of team above
    			boolean endGame = false;
    			while(endGame == false) {
	    			Document playByPlay = Jsoup.connect(pbpURL + upperLink).timeout(0).get();
	    			System.out.println(pbpURL + upperLink);
	    			Elements teams = playByPlay.select("div > h2 > a");
	    			String firstTeam = teams.get(0).text();
	    			String firstClean = firstTeam.replaceAll("\\.|,", " ");
	    			String secondTeam = teams.get(1).text();
	    			String secondClean = secondTeam.replaceAll("\\.|,", " ");
	    			Elements metas = playByPlay.select("head > meta");
	    			Element date = metas.get(3);
	    			String dateString = date.attr("content");
	    			dateString = dateString.replaceAll(firstTeam, firstClean);
	    			dateString = dateString.replaceAll(secondTeam, secondClean);
	    			dateString = dateString.replaceAll(" ", "");
	    			dateString = dateString.split(",")[1];
	    			dateString = dateString.split(":")[0];
	    			String year = dateString.split("/")[2];
	    			String month = dateString.split("/")[0];
	    			String day = dateString.split("/")[1];
	    			int dDay = Integer.parseInt(day);
	    			String currentYear = currentDate.split("/")[2].replaceAll(" ", "");
	    			String currentMonth = currentDate.split("/")[0].replaceAll(" ", "");
	    			String currentDay = currentDate.split("/")[1].replaceAll(" ", "");
	    			int cDay = Integer.parseInt(currentDay);
	    			if(year.equals(currentYear) && month.equals(currentMonth) && (dDay > cDay)) {
	    				if(endGame == false) {
							System.out.println(upperLink + ": last league game, season in progress");
	    				}
						endGame = true;
	    			}
	    			if(!year.equals(yearList.get(u)) || month.equals("10") || (month.equals("9") && day.equals("30"))) {
	    				if(endGame == false) {
							System.out.println(upperLink + ": last league game");
	    				}
						endGame = true;
					}    			
	    			if(endGame == false) {
		    			upperLink++;
	    			}
    			}
    			//get PbP text
    			ProgressBar.setValue(5);
    			upperLink = upperLink - 1;
    			int firstID = upperLink - totalGames;
    			int oldRange = (upperLink - firstID);  
        		int newRange = 94;  
    			System.out.println(firstID + ": first league game");
    			for(int y = firstID; y < upperLink; y++) {    				
    				System.out.println(pbpURL + y);
    				Document playByPlay = Jsoup.connect(pbpURL + y).timeout(0).get();
    				Elements teams = playByPlay.select("div > table > caption > a");
        			String teamOne = teams.get(0).text();
        			String cleanOne = teamOne.replaceAll("\\.|,", " ");
    				String teamTwo = teams.get(1).text();
    				String cleanTwo = teamTwo.replaceAll("\\.|,", " ");
    				Element div = playByPlay.selectFirst("div[class=col-sm-8]");
    				String playText = div.text();
    				playText = playText.replaceAll(teamOne, cleanOne);
    				playText = playText.replaceAll(teamTwo, cleanTwo);
    				//split PbP into list of separate plays and remove extraneous strings
    				ArrayList<String> playList = new ArrayList<>(Arrays.asList(playText.split("\\.|:")));
    				for (int f = 0; f < playList.size(); f++) {
    					if(playList.get(f).contains("runs,")) {
    						playList.remove(f);
    					}
    					if(playList.get(f).contains("Bottom of") && f != 0) {
    						playList.remove(f);
    					}
    					if(playList.get(f).contains("Top of") && f != 0) {
    						playList.remove(f);
    					}
    				}
    				Elements pbp = div.children();
    				Elements players = playByPlay.select("tbody > tr > td");
    				fieldingData = new ArrayList<ArrayList<String>>();
    				//create temporary list of player involved in game
    				for(int c = 0; c < players.size(); c+=3) {
    					playerData = new ArrayList<String>();
    					String name = players.get(c+1).child(0).text();
    					String position = players.get(c+2).text();
    					String playerID = players.get(c+1).child(0).attr("href");
    					String formattedPosition = positionFormat(position);
    					String last = name.split(" ")[1];
    					String first = name.split(" ")[0];
    					name = last + ", " + first;
    					playerData.add(name);
    					playerData.add(formattedPosition);
    					if(c < 10) {
    						playerData.add(teams.get(0).text());
    					}
    					else {
    						playerData.add(teams.get(1).text());
    					}
    					playerData.add(playerID);
    					fieldingData.add(playerData);
    					//add position tally to player object
    					for(int p = 0; p < playerList.size(); p++) {
    						if(name.equals(playerList.get(p).getName()) && yearList.get(u).equals(playerList.get(p).getYear())) {
    							position = position.replaceAll(" ", "");
    							playerList.get(p).positionTally(position);
    							System.out.println(position + " " + name);
    						}
    					}
    				}
    				//go through list of plays, count fielding events, and assign run value to player object. check run values
    				int startIndex = 0;
    				int endIndex = 0;
    				int playIndex = 0;
    				boolean topInning = true;
    				if(pbp.get(1).text().equals(teamOne)) {
    					 topInning = false;
    				}
    				else if(pbp.get(1).text().equals(teamTwo)) {
    					topInning = true;
    				}
    				String pitcherTop = fieldingData.get(10).get(0);
    				String pitcherBottom = fieldingData.get(0).get(0);
    				for(int d = 0; d < pbp.size(); d++) {
    					System.out.println(pbp.get(d));
    					if(pbp.get(d).is("p")) {
    						if(topInning == true) {
    							topInning = false;
    							System.out.println("Bottom of the Inning ");
    						}
    						else if(topInning == false) {
    							topInning = true;
    							System.out.println("Top of the Inning ");
    						}
    					}
    					if(pbp.get(d).is("br") || d == pbp.size() - 1) {
    						boolean identified = false;
    						endIndex = d - startIndex - 1;
    						System.out.println(playList.get(playIndex));
    						String[] plays = playList.get(playIndex).split(",");
    						if(playList.get(playIndex).contains("comes in to pitch")) {
    							if(topInning == true) {
    								String name = pbp.get(d - 1).text();
    								for(int p = 0; p < pitcherList.size(); p++) {
    									if(pitcherList.get(p).getName().contains(name) && pitcherList.get(p).getYear().equals(yearList.get(u))) {
    										ArrayList<Team> pitcherTeams = new ArrayList<Team>(pitcherList.get(p).getTeam());
    										for(int r = 0; r < pitcherTeams.size(); r++) {
    											if(pitcherTeams.get(r).getName().equals(fieldingData.get(10).get(2))) {
    												name = pitcherList.get(p).getName();
    											}
    										}
    									}
    								}
    								pitcherTop = name;
    								System.out.println(name + " comes in to pitch");
    							}
    							else if(topInning == false) {
    								String name = pbp.get(d - 1).text();
    								for(int p = 0; p < pitcherList.size(); p++) {
    									if(pitcherList.get(p).getName().contains(name) && pitcherList.get(p).getYear().equals(yearList.get(u))) {
    										ArrayList<Team> pitcherTeams = new ArrayList<Team>(pitcherList.get(p).getTeam());
    										for(int r = 0; r < pitcherTeams.size(); r++) {
    											if(pitcherTeams.get(r).getName().equals(fieldingData.get(0).get(2))) {
    												name = pitcherList.get(p).getName();
    											}
    										}
    									}
    								}
    								pitcherBottom = name;
    								System.out.println(name + " comes in to pitch");
    							}
    						}
    						if(playList.get(playIndex).contains("hits a fly ball")) {
    							if(playList.get(playIndex).contains("error")) {
    								identified = true;
    								System.out.println("error");
    								String name = "";
    								boolean pitcher = true;
    								for(int z = 0; z < endIndex; z++) {
    									if(plays[z].contains("commits an error")) {
    										name = pbp.get(d - (endIndex - z)).text();
    									}
    								}    								
    								String player = "";
    								String position = "";
    								if(topInning == true) {
    									for(int i = fieldingData.size()/2; i < fieldingData.size(); i++) {
	    									if(fieldingData.get(i).get(0).contains(name) && plays[0].contains(fieldingData.get(i).get(1))) {
	    	    								player = fieldingData.get(i).get(0);
	    	    								position = fieldingData.get(i).get(1);
	    	    								pitcher = false;
	    									}
	    								}
    								}
    								else if(topInning == false) {
    									for(int i = 0; i < fieldingData.size()/2; i++) {
	    									if(fieldingData.get(i).get(0).contains(name) && plays[0].contains(fieldingData.get(i).get(1))) {
	    	    								player = fieldingData.get(i).get(0);
	    	    								position = fieldingData.get(i).get(1);
	    	    								pitcher = false;
	    									}
	    								}
    								}
    								if(pitcher == true) {
    									if(topInning == true) {
    										for(int p = 0; p < pitcherList.size(); p++) {
	    										if(pitcherList.get(p).getName().equals(pitcherTop)) {
	    											player = pitcherList.get(p).getName();
	    											position = "pitcher";
	    										}
	    									}
    									}
    									else if (topInning == false) {
	    									for(int p = 0; p < pitcherList.size(); p++) {
	    										if(pitcherList.get(p).getName().equals(pitcherBottom)) {
	    											player = pitcherList.get(p).getName();
	    											position = "pitcher";
	    										}
	    									}
    									}
    								}
    								assignFielding(player, -0.5, yearList.get(u), position, "fly");    
    								assignError(player, yearList.get(u), position);
    							}
    							else if(playList.get(playIndex).contains("tags up") && identified == false) {
    								identified = true;
    								System.out.println("sac fly");
    								String name = "";
    								for(int z = 0; z < endIndex; z++) {
    									if(plays[z].contains("caught by")) {
    										name = pbp.get(d - (endIndex - z)).text();
    									}
    								}
    								String player = "";
    								String position = "";
    								if(topInning == true) {
    									for(int i = fieldingData.size()/2; i < fieldingData.size(); i++) {
	    									if(fieldingData.get(i).get(0).contains(name) && plays[0].contains(fieldingData.get(i).get(1))) {
	    	    								player = fieldingData.get(i).get(0);
	    	    								position = fieldingData.get(i).get(1);
	    									}
	    								}
    									for(int p = 0; p < pitcherList.size(); p++) {
    										if(pitcherList.get(p).getName().equals(pitcherTop)) {
    											pitcherList.get(p).addWAR(0.03);
    										}
    									}
    								}
    								else if(topInning == false) {
    									for(int i = 0; i < fieldingData.size()/2; i++) {
	    									if(fieldingData.get(i).get(0).contains(name) && plays[0].contains(fieldingData.get(i).get(1))) {
	    	    								player = fieldingData.get(i).get(0);
	    	    								position = fieldingData.get(i).get(1);
	    									}
	    								}
    									for(int p = 0; p < pitcherList.size(); p++) {
    										if(pitcherList.get(p).getName().equals(pitcherBottom)) {
    											pitcherList.get(p).addWAR(0.03);
    										}
    									}
    								}
    								assignFielding(player, -0.3, yearList.get(u), position, "fly");
    								assignArm(player, "extra base", yearList.get(u), position);
    								assignRange(player, yearList.get(u), position);
    							}
    							else if(playList.get(playIndex).contains("thrown out") && identified == false) {
    								identified = true;
    								System.out.println("baserunning out");
    								String name = "";
    								for(int z = 0; z < endIndex; z++) {
    									if(plays[z].contains("caught by")) {
    										name = pbp.get(d - (endIndex - z)).text();
    										break;
    									}
    								}
    								String player = "";
    								String position = "";
    								if(topInning == true) {
    									for(int i = fieldingData.size()/2; i < fieldingData.size(); i++) {
	    									if(fieldingData.get(i).get(0).contains(name) && plays[0].contains(fieldingData.get(i).get(1))) {
	    	    								player = fieldingData.get(i).get(0);
	    	    								position = fieldingData.get(i).get(1);
	    									}
	    								}
    									for(int p = 0; p < pitcherList.size(); p++) {
    										if(pitcherList.get(p).getName().equals(pitcherTop)) {
    											pitcherList.get(p).addWAR(0.03);
    										}
    									}
    								}
    								else if(topInning == false) {
    									for(int i = 0; i < fieldingData.size()/2; i++) {
	    									if(fieldingData.get(i).get(0).contains(name) && plays[0].contains(fieldingData.get(i).get(1))) {
	    	    								player = fieldingData.get(i).get(0);
	    	    								position = fieldingData.get(i).get(1);
	    									}
	    								}
    									for(int p = 0; p < pitcherList.size(); p++) {
    										if(pitcherList.get(p).getName().equals(pitcherBottom)) {
    											pitcherList.get(p).addWAR(0.03);
    										}
    									}
    								}
    								assignFielding(player, 0.52, yearList.get(u), position, "fly");
    								assignArm(player, "assist", yearList.get(u), position);
    								assignRange(player, yearList.get(u), position);
    							}
    							else if(playList.get(playIndex).contains("foul territory") && identified == false) {
    								identified = true;
    								String url = "";
    								for(int z = 0; z < endIndex; z++) {
    									if(plays[z].contains("caught by")) {
    										url = pbp.get(d - (endIndex - z)).attr("href");
    									}
    								}
    								String partName = "";
    								for(int z = 0; z < endIndex; z++) {
    									if(plays[z].contains("caught by")) {
    										partName = pbp.get(d - (endIndex - z)).text();
    									}
    								}
    								if(d == pbp.size() - 1) {
    									url = pbp.get(d).attr("href");
    									partName = pbp.get(d).attr("href");
    								}
    								String sPlayer = "";
    								String sPosition = "";
    								int nameCount = 0;
    								if(topInning == true) {
    									for(int i = fieldingData.size()/2; i < fieldingData.size(); i++) {
	    									if(fieldingData.get(i).get(0).contains(partName)) {
	    	    								sPlayer = fieldingData.get(i).get(0);
	    	    								sPosition = fieldingData.get(i).get(1);
	    	    								nameCount++;
	    									}
	    								}
    								}
    								else if(topInning == false) {
    									for(int i = 0; i < fieldingData.size()/2; i++) {
	    									if(fieldingData.get(i).get(0).contains(partName)) {
	    	    								sPlayer = fieldingData.get(i).get(0);
	    	    								sPosition = fieldingData.get(i).get(1);
	    	    								nameCount++;
	    									}
	    								}
    								}
    								if(nameCount > 1 || nameCount == 0) {
	    								Document playerName= Jsoup.connect("https://www.csfbl.com" + url).timeout(0).get();
	    								Elements metas = playerName.select("head > meta");
	    								String name = metas.get(3).attr("content");
	    								String firstName = name.split("\\s+")[0];
	    								String lastName = name.split("\\s+")[1];
	    								name = lastName + ", " + firstName;
	    								for(int i = 0; i < fieldingData.size(); i++) {
	    									if(fieldingData.get(i).get(0).equals(name)) {
	    	    								sPlayer = fieldingData.get(i).get(0);
	    	    								sPosition = fieldingData.get(i).get(1);
	    									}    									
	    								}
    								}
    								assignFielding(sPlayer, 0.08, yearList.get(u), sPosition,"fly");
    								assignRange(sPlayer, yearList.get(u), sPosition);    								
    								if(topInning == true) {
    									for(int p = 0; p < pitcherList.size(); p++) {
    										if(pitcherList.get(p).getName().equals(pitcherTop)) {
    											pitcherList.get(p).addWAR(0.03);
    										}
    									}
    								}
    								else if(topInning == false) {
    									for(int p = 0; p < pitcherList.size(); p++) {
    										if(pitcherList.get(p).getName().equals(pitcherBottom)) {
    											pitcherList.get(p).addWAR(0.03);
    										}
    									}
    								}
    							}
    							else if(playList.get(playIndex).contains("caught by") && identified == false){
    								identified = true;
    								String name = "";
    								for(int z = 0; z < endIndex; z++) {
    									if(plays[z].contains("caught by")) {
    										name = pbp.get(d - (endIndex - z)).text();
    									}
    								}
    								String player = "";
    								String position = "";
    								boolean pitcher = true;
    								if(topInning == true) {
    									for(int i = fieldingData.size()/2; i < fieldingData.size(); i++) {
	    									if(fieldingData.get(i).get(0).contains(name) && plays[0].contains(fieldingData.get(i).get(1))) {
	    	    								player = fieldingData.get(i).get(0);
	    	    								position = fieldingData.get(i).get(1);
	    	    								pitcher = false;
	    									}
	    								}
    									for(int p = 0; p < pitcherList.size(); p++) {
    										if(pitcherList.get(p).getName().equals(pitcherTop)) {
    											pitcherList.get(p).addWAR(0.03);
    										}
    									}
    								}
    								else if(topInning == false) {
    									for(int i = 0; i < fieldingData.size()/2; i++) {
	    									if(fieldingData.get(i).get(0).contains(name) && plays[0].contains(fieldingData.get(i).get(1))) {
	    	    								player = fieldingData.get(i).get(0);
	    	    								position = fieldingData.get(i).get(1);
	    	    								pitcher = false;
	    									}
	    								}
    									for(int p = 0; p < pitcherList.size(); p++) {
    										if(pitcherList.get(p).getName().equals(pitcherBottom)) {
    											pitcherList.get(p).addWAR(0.03);
    										}
    									}
    								}
    								if(pitcher == true) {
    									if(topInning == true) {
    										for(int p = 0; p < pitcherList.size(); p++) {
	    										if(pitcherList.get(p).getName().equals(pitcherTop)) {
	    											player = pitcherList.get(p).getName();
	    											position = "pitcher";
	    										}
	    									}
    									}
    									else if (topInning == false) {
	    									for(int p = 0; p < pitcherList.size(); p++) {
	    										if(pitcherList.get(p).getName().equals(pitcherBottom)) {
	    											player = pitcherList.get(p).getName();
	    											position = "pitcher";
	    										}
	    									}
    									}
    								}
    								assignFielding(player, 0.08, yearList.get(u), position, "fly");    	
    								assignRange(player, yearList.get(u), position);
    							}
    						}
    						else if(playList.get(playIndex).contains("hits a ground ball") && identified == false) {
    							int doublePlay = 0;
    							for(int a = 0; a < plays.length; a++) {
    								if(plays[a].contains("out at")) {
    									doublePlay++;
    								}
    							}
    							if(doublePlay == 2) {
    								identified = true;
    								System.out.println("double play");
    								String name = "";
    								int match = 0;
    								for(int z = 0; z < endIndex; z++) {
    									if(plays[z].contains("fielded by")) {
    										name = pbp.get(d - (endIndex - z)).text();
    										match = z;
    										break;
    									}
    								}
    								String player = "";
    								String url = "";
    								String position = "";
    								boolean pitcher = true;
    								if(topInning == true) {
    									for(int i = fieldingData.size()/2; i < fieldingData.size(); i++) {
	    									if(fieldingData.get(i).get(0).contains(name) && plays[0].contains(fieldingData.get(i).get(1))) {
	    	    								player = fieldingData.get(i).get(0);
	    	    								position = fieldingData.get(i).get(1);
	    	    								pitcher = false;
	    									}
	    								}
    									for(int p = 0; p < pitcherList.size(); p++) {
    										if(pitcherList.get(p).getName().equals(pitcherTop)) {
    											pitcherList.get(p).addWAR(0.03);
    										}
    									}
    								}
    								else if(topInning == false) {
    									for(int i = 0; i < fieldingData.size()/2; i++) {
	    									if(fieldingData.get(i).get(0).contains(name) && plays[0].contains(fieldingData.get(i).get(1))) {
	    	    								player = fieldingData.get(i).get(0);
	    	    								position = fieldingData.get(i).get(1);
	    	    								pitcher = false;
	    									}
	    								}
    									for(int p = 0; p < pitcherList.size(); p++) {
    										if(pitcherList.get(p).getName().equals(pitcherTop)) {
    											pitcherList.get(p).addWAR(0.03);
    										}
    									}
    								}
    								if(pitcher == true) {
    									if(topInning == true) {
    										for(int p = 0; p < pitcherList.size(); p++) {
	    										if(pitcherList.get(p).getName().equals(pitcherTop)) {
	    											player = pitcherList.get(p).getName();
	    											position = "pitcher";
	    										}
	    									}
    									}
    									else if (topInning == false) {
	    									for(int p = 0; p < pitcherList.size(); p++) {
	    										if(pitcherList.get(p).getName().equals(pitcherBottom)) {
	    											player = pitcherList.get(p).getName();
	    											position = "pitcher";
	    										}
	    									}
    									}
    								}
    								assignFielding(player, 0.15, yearList.get(u), position, "ground");
    								assignRange(player, yearList.get(u), position);
    								assignArm(player, "double play", yearList.get(u), position);
    								
    								for(int z = 0; z < endIndex; z++) {
    									if(plays[z].contains("fielded by") && z != match) {
    										url = pbp.get(d - (endIndex - z)).attr("href");
    									}
    								}
    								String partName = "";
    								for(int z = 0; z < endIndex; z++) {
    									if(plays[z].contains("fielded by") && z != match) {
    										partName = pbp.get(d - (endIndex - z)).text();
    									}
    								}
    								String sPlayer = "";
    								String sPosition = "";
    								int nameCount = 0;
    								if(topInning == true) {
    									for(int i = fieldingData.size()/2; i < fieldingData.size(); i++) {
	    									if(fieldingData.get(i).get(0).contains(partName)) {
	    	    								sPlayer = fieldingData.get(i).get(0);
	    	    								sPosition = fieldingData.get(i).get(1);
	    	    								nameCount++;
	    									}
	    								}
    								}
    								else if(topInning == false) {
    									for(int i = 0; i < fieldingData.size()/2; i++) {
	    									if(fieldingData.get(i).get(0).contains(partName)) {
	    	    								sPlayer = fieldingData.get(i).get(0);
	    	    								sPosition = fieldingData.get(i).get(1);
	    	    								nameCount++;
	    									}
	    								}
    								}
    								if(nameCount > 1) {
	    								Document playerName= Jsoup.connect("https://www.csfbl.com" + url).timeout(0).get();
	    								Elements metas = playerName.select("head > meta");
	    								name = metas.get(3).attr("content");
	    								String firstName = name.split("\\s+")[0];
	    								String lastName = name.split("\\s+")[1];
	    								name = lastName + ", " + firstName;
	    								for(int i = 0; i < fieldingData.size(); i++) {
	    									if(fieldingData.get(i).get(0).equals(name)) {
	    	    								sPlayer = fieldingData.get(i).get(0);
	    	    								sPosition = fieldingData.get(i).get(1);
	    									}    									
	    								}
    								}
    								assignFielding(sPlayer, 0.07, yearList.get(u), sPosition, "ground");
    								assignArm(sPlayer, "double play", yearList.get(u), sPosition);    								
    							}
    							if(playList.get(playIndex).contains("error") && identified == false) {
    								identified = true;
    								System.out.println("ground ball error");
    								String url = "";
    								String partName = "";
    								for(int z = 0; z < endIndex; z++) {
    									if(plays[z].contains("commits an error")) {
    										url = pbp.get(d - (endIndex - z)).attr("href");
    										partName = pbp.get(d - (endIndex - z)).text();
    									}
    								}
    								String sPlayer = "";
    								String sPosition = "";
    								int nameCount = 0;
    								boolean pitcher = true;
    								if(topInning == true) {
    									for(int i = fieldingData.size()/2; i < fieldingData.size(); i++) {
	    									if(fieldingData.get(i).get(0).contains(partName)) {
	    	    								sPlayer = fieldingData.get(i).get(0);
	    	    								sPosition = fieldingData.get(i).get(1);
	    	    								nameCount++;
	    	    								pitcher = false;
	    									}
	    								}
    								}
    								else if(topInning == false) {
    									for(int i = 0; i < fieldingData.size()/2; i++) {
	    									if(fieldingData.get(i).get(0).contains(partName)) {
	    	    								sPlayer = fieldingData.get(i).get(0);
	    	    								sPosition = fieldingData.get(i).get(1);
	    	    								nameCount++;
	    	    								pitcher = false;
	    									}
	    								}
    								}
    								if(nameCount > 1) {
	    								Document playerName= Jsoup.connect("https://www.csfbl.com" + url).timeout(0).get();
	    								Elements metas = playerName.select("head > meta");
	    								String name = metas.get(3).attr("content");
	    								String firstName = name.split("\\s+")[0];
	    								String lastName = name.split("\\s+")[1];
	    								name = lastName + ", " + firstName;
	    								for(int i = 0; i < fieldingData.size(); i++) {
	    									if(fieldingData.get(i).get(0).equals(name)) {
	    	    								sPlayer = fieldingData.get(i).get(0);
	    	    								sPosition = fieldingData.get(i).get(1);
	    									} 									
	    								}
    								}
    								if(pitcher == true) {
    									if(topInning == true) {
    										for(int p = 0; p < pitcherList.size(); p++) {
	    										if(pitcherList.get(p).getName().equals(pitcherTop)) {
	    											sPlayer = pitcherList.get(p).getName();
	    											sPosition = "pitcher";
	    										}
	    									}
    									}
    									else if (topInning == false) {
	    									for(int p = 0; p < pitcherList.size(); p++) {
	    										if(pitcherList.get(p).getName().equals(pitcherBottom)) {
	    											sPlayer = pitcherList.get(p).getName();
	    											sPosition = "pitcher";
	    										}
	    									}
    									}
    								}
    								assignFielding(sPlayer, -0.5, yearList.get(u), sPosition, "ground");
    								assignError(sPlayer, yearList.get(u), sPosition);
    							}
    							else if(playList.get(playIndex).contains("hits a ground ball to") && identified == false) {
    								identified = true;
    								System.out.println("ground ball out");
    								String name = "";
    								String url = "";
    								boolean noPlayer = true;
    								boolean pitcher = true;
    								for(int z = 0; z < endIndex; z++) {
    									if(plays[z].contains("fielded by")) {
    										name = pbp.get(d - (endIndex - z)).text();
    										noPlayer = false;
    									}
    								}
    								for(int z = 0; z < endIndex; z++) {
    									if(noPlayer == true) {
        									if(plays[z].contains("hits a ground ball to first")) {
        										if(topInning == true) {
	        										for(int h = fieldingData.size()/2; h < fieldingData.size(); h++) {
	        											if(fieldingData.get(h).get(1).equals("to first")) {
	        												url = fieldingData.get(h).get(3);
	        												System.out.println("no player: " + url);
	        											}
	        										}
        										}
        										if(topInning == false) {
        											for(int h = 0; h < fieldingData.size()/2; h++) {
	        											if(fieldingData.get(h).get(1).equals("to first")) {
	        												url = fieldingData.get(h).get(3);
	        												System.out.println("no player: " + url);
	        											}
	        										}
        										}
        									}
        								}
    								}
    								String player = "";
    								String position = "";
    								if(noPlayer == true) {
	    								Document playerName= Jsoup.connect("https://www.csfbl.com" + url).timeout(0).get();
	    								Elements metas = playerName.select("head > meta");
	    								name = metas.get(3).attr("content");
	    								String firstName = name.split("\\s+")[0];
	    								String lastName = name.split("\\s+")[1];
	    								name = lastName + ", " + firstName;
	    								if(topInning == true) {
    										for(int i = fieldingData.size()/2; i < fieldingData.size(); i++) {
    	    									if(fieldingData.get(i).get(0).contains(name)) {
    	    	    								player = fieldingData.get(i).get(0);
    	    	    								position = fieldingData.get(i).get(1);
    	    	    								pitcher = false;
    	    									}
    	    								}
    										for(int p = 0; p < pitcherList.size(); p++) {
        										if(pitcherList.get(p).getName().equals(pitcherTop)) {
        											pitcherList.get(p).addWAR(0.04);
        										}
        									}
        								}
        								else if(topInning == false) {
        									for(int i = 0; i < fieldingData.size()/2; i++) {
    	    									if(fieldingData.get(i).get(0).contains(name)) {
    	    	    								player = fieldingData.get(i).get(0);
    	    	    								position = fieldingData.get(i).get(1);
    	    	    								pitcher = false;
    	    									}
    	    								}
        									for(int p = 0; p < pitcherList.size(); p++) {
        										if(pitcherList.get(p).getName().equals(pitcherBottom)) {
        											pitcherList.get(p).addWAR(0.04);
        										}
        									}
        								}
    								}
    								else if(noPlayer == false) {
    									if(topInning == true) {
    										for(int i = fieldingData.size()/2; i < fieldingData.size(); i++) {
    	    									if(fieldingData.get(i).get(0).contains(name) && plays[0].contains(fieldingData.get(i).get(1))) {
    	    	    								player = fieldingData.get(i).get(0);
    	    	    								position = fieldingData.get(i).get(1);
    	    	    								pitcher = false;
    	    									}
    	    								}
    										for(int p = 0; p < pitcherList.size(); p++) {
        										if(pitcherList.get(p).getName().equals(pitcherTop)) {
        											pitcherList.get(p).addWAR(0.04);
        										}
        									}
        								}
        								else if(topInning == false) {
        									for(int i = 0; i < fieldingData.size()/2; i++) {
    	    									if(fieldingData.get(i).get(0).contains(name) && plays[0].contains(fieldingData.get(i).get(1))) {
    	    	    								player = fieldingData.get(i).get(0);
    	    	    								position = fieldingData.get(i).get(1);
    	    	    								pitcher = false;
    	    									}
    	    								}
        									for(int p = 0; p < pitcherList.size(); p++) {
        										if(pitcherList.get(p).getName().equals(pitcherBottom)) {
        											pitcherList.get(p).addWAR(0.04);
        										}
        									}
        								}
    								}
    								if(pitcher == true) {
    									if(topInning == true) {
    										for(int p = 0; p < pitcherList.size(); p++) {
	    										if(pitcherList.get(p).getName().equals(pitcherTop)) {
	    											player = pitcherList.get(p).getName();
	    											position = "pitcher";
	    										}
	    									}
    									}
    									else if (topInning == false) {
	    									for(int p = 0; p < pitcherList.size(); p++) {
	    										if(pitcherList.get(p).getName().equals(pitcherBottom)) {
	    											player = pitcherList.get(p).getName();
	    											position = "pitcher";
	    										}
	    									}
    									}
    								}
    								assignFielding(player, 0.08, yearList.get(u), position, "ground");
    								assignRange(player, yearList.get(u), position);
    							}
    						}
    						else if(playList.get(playIndex).contains("thrown out") && identified == false) {
    							identified = true;
    							int assistIndex = endIndex - 1;
    							System.out.println("outfield assist");
    							String name = "";
								for(int z = 0; z < assistIndex; z++) {
									if(plays[z].contains("fielded by")) {
										name = pbp.get(d - (assistIndex - z)).text();
									}
									break;
								}
								String player = "";
    							String position = "";
    							if(topInning == true) {
    								for(int j = fieldingData.size()/2; j < fieldingData.size(); j++) {
    									if(fieldingData.get(j).get(0).contains(name) && plays[0].contains(fieldingData.get(j).get(1))) {
    										player = fieldingData.get(j).get(0);
    										position = fieldingData.get(j).get(1);
    									}
    								}
    							}
    							else if(topInning == false) {
    								for(int j = 0; j < fieldingData.size()/2; j++) {
    									if(fieldingData.get(j).get(0).contains(name) && plays[0].contains(fieldingData.get(j).get(1))) {
    										player = fieldingData.get(j).get(0);
    										position = fieldingData.get(j).get(1);
    									}
    								}
    							}
								assignFielding(player, 0.44, yearList.get(u), position, "fly");
								assignArm(player, "assist", yearList.get(u), position);
    						}
    						else if(playList.get(playIndex).contains("gets caught attempting") && identified == false) {
    							identified = true;
    							String player = "";
    							if(topInning == true) {
    								for(int j = fieldingData.size()/2; j < fieldingData.size(); j++) {
    									if(fieldingData.get(j).get(1).contains("catcher")) {
    										player = fieldingData.get(j).get(0);
    									}
    								}
    								assignFielding(pitcherTop, 0.15, yearList.get(u), "P", "none");
    								assignArm(pitcherTop, "CS", yearList.get(u), "pitcher");
    							}
    							else if(topInning == false) {
    								for(int j = 0; j < fieldingData.size()/2; j++) {
    									if(fieldingData.get(j).get(1).contains("catcher")) {
    										player = fieldingData.get(j).get(0);
    									}
    								}
    								assignFielding(pitcherBottom, 0.15, yearList.get(u), "P", "none");
    								assignArm(pitcherBottom, "CS", yearList.get(u), "pitcher");
    							}
    							assignFielding(player, 0.2, yearList.get(u), "C", "none");
    							assignArm(player, "CS", yearList.get(u), "C");
    						}
    						else if(playList.get(playIndex).contains("steals") && identified == false) {
    							identified = true;
    							String player = "";
    							if(topInning == true) {
    								for(int j = fieldingData.size()/2; j < fieldingData.size(); j++) {
    									if(fieldingData.get(j).get(1).contains("catcher")) {
    										player = fieldingData.get(j).get(0);
    									}
    								}
    								assignFielding(pitcherTop, -0.15, yearList.get(u), "P", "none");
    								assignArm(pitcherTop, "SB", yearList.get(u), "pitcher");
    							}
    							else if(topInning == false) {
    								for(int j = 0; j < fieldingData.size()/2; j++) {
    									if(fieldingData.get(j).get(1).contains("catcher")) {
    										player = fieldingData.get(j).get(0);
    									}
    								}
    								assignFielding(pitcherBottom, -0.15, yearList.get(u), "P", "none");
    								assignArm(pitcherBottom, "SB", yearList.get(u), "pitcher");
    							}
    							assignFielding(player, -0.2, yearList.get(u), "C", "none");
    							assignArm(player, "SB", yearList.get(u), "C");
    						}
    						else if(playList.get(playIndex).contains("advances") && identified == false) {
    							System.out.println("extra bases");
    							identified = true;
    							int assistIndex = endIndex - 1;
    							String name = "";
								for(int z = 0; z < assistIndex; z++) {
									if(plays[z].contains("fielded by")) {
										name = pbp.get(d - (assistIndex - z)).text();
									}
									break;
								}
    							String player = "";
    							String position = "";
    							if(topInning == true) {
    								for(int j = fieldingData.size()/2; j < fieldingData.size(); j++) {
    									if(fieldingData.get(j).get(0).contains(name) && plays[0].contains(fieldingData.get(j).get(1))) {
    										player = fieldingData.get(j).get(0);
    										position = fieldingData.get(j).get(1);
    									}
    								}
    							}
    							else if(topInning == false) {
    								for(int j = 0; j < fieldingData.size()/2; j++) {
    									if(fieldingData.get(j).get(0).contains(name) && plays[0].contains(fieldingData.get(j).get(1))) {
    										player = fieldingData.get(j).get(0);
    										position = fieldingData.get(j).get(1);
    									}
    								}
    							}
    							assignFielding(player, -0.38, yearList.get(u), position, "none");    							
    							assignArm(player, "extra bases", yearList.get(u), position);
    						}
    						playIndex++;
    						startIndex = d;
    					}
    				}
    				ProgressBar.setValue((((y - firstID) * newRange) / oldRange) + 6);
    			}
    			pageNum += 12;
            }
    		catch(IOException e) {
    		    e.getMessage();
    		    e.printStackTrace();
    		}
        	double catcherIndex = 0;
        	double catcherTotal = 0;
        	double catcherArm = 0;
        	double catcherRange = 0;
        	double catcherError = 0;
        	double firstIndex = 0;
        	double firstTotal = 0;
        	double firstArm = 0;
        	double firstRange = 0;
        	double firstError = 0;
        	double secondIndex = 0;
        	double secondTotal = 0;
        	double secondArm = 0;
        	double secondRange = 0;
        	double secondError = 0;
        	double thirdIndex = 0;
        	double thirdTotal = 0;
        	double thirdArm = 0;
        	double thirdRange = 0;
        	double thirdError = 0;
        	double shortIndex = 0;
        	double shortTotal = 0;
        	double shortArm = 0;
        	double shortRange = 0;
        	double shortError = 0;
        	double leftIndex = 0;
        	double leftTotal = 0;
        	double leftArm = 0;
        	double leftRange = 0;
        	double leftError = 0;
        	double centerIndex = 0;
        	double centerTotal = 0;
        	double centerArm = 0;
        	double centerRange = 0;
        	double centerError = 0;
        	double rightIndex = 0;
        	double rightTotal = 0;
        	double rightArm = 0;
        	double rightRange = 0;
        	double rightError = 0;
        	double pitcherIndex = 0;
        	double pitcherTotal = 0;
        	double pitcherArm = 0;
        	double pitcherRange = 0;
        	double pitcherError = 0;
        	for(int i = 0; i < playerList.size(); i++) {
        		if(playerList.get(i).getYear().equals(yearList.get(u))) {
        			String position = playerList.get(i).getPos();
        			position.replaceAll(" ", "");
            		if(position.equals("C")) {
            			catcherTotal = catcherTotal + playerList.get(i).getBins(position);
            			catcherIndex++;
            			catcherArm = catcherArm + (playerList.get(i).getArm()/playerList.get(i).getPosGames(position));
                    	catcherRange = catcherRange + (playerList.get(i).getRange()/playerList.get(i).getPosGames(position));
           	        	catcherError = catcherError + (playerList.get(i).getError()/playerList.get(i).getPosGames(position));
           			}
           			if(position.equals("1B")) {
           				firstTotal = firstTotal + playerList.get(i).getBins(position);
           				firstIndex++;
           				firstArm = firstArm + (playerList.get(i).getArm()/playerList.get(i).getPosGames(position));
           	        	firstRange = firstRange + (playerList.get(i).getRange()/playerList.get(i).getPosGames(position));
           	        	firstError = firstError + (playerList.get(i).getError()/playerList.get(i).getPosGames(position));
           			}
           			if(position.equals("2B")) {
           				secondTotal = secondTotal + playerList.get(i).getBins(position);
           				secondIndex++;            				
           				secondArm = secondArm + (playerList.get(i).getArm()/playerList.get(i).getPosGames(position));
            	       	secondRange = secondRange + (playerList.get(i).getRange()/playerList.get(i).getPosGames(position));
            	       	secondError = secondError + (playerList.get(i).getError()/playerList.get(i).getPosGames(position));
           			}            			
            		if(position.equals("SS")) {
            			shortTotal = shortTotal + playerList.get(i).getBins(position);
            			shortIndex++;
           				shortArm = shortArm + (playerList.get(i).getArm()/playerList.get(i).getPosGames(position));
           				shortRange = shortRange + (playerList.get(i).getRange()/playerList.get(i).getPosGames(position));
           				shortError = shortError + (playerList.get(i).getError()/playerList.get(i).getPosGames(position));
           			}
           			if(position.equals("3B")) {
           				thirdTotal = thirdTotal + playerList.get(i).getBins(position);
           				thirdIndex++;
           				thirdArm = thirdArm + (playerList.get(i).getArm()/playerList.get(i).getPosGames(position));
           				thirdRange = thirdRange + (playerList.get(i).getRange()/playerList.get(i).getPosGames(position));
           				thirdError = thirdError + (playerList.get(i).getError()/playerList.get(i).getPosGames(position));
           			}
           			if(position.equals("LF")) {
           				leftTotal = leftTotal + playerList.get(i).getBins(position);
           				leftIndex++;
           				leftArm = leftArm + (playerList.get(i).getArm()/playerList.get(i).getPosGames(position));
           				leftRange = leftRange + (playerList.get(i).getRange()/playerList.get(i).getPosGames(position));
           				leftError = leftError + (playerList.get(i).getError()/playerList.get(i).getPosGames(position));
           			}
           			if(position.equals("CF")) {
           				centerTotal = centerTotal + playerList.get(i).getBins(position);
           				System.out.println(centerTotal + "CF");
           				centerIndex++;
           				centerArm = centerArm + (playerList.get(i).getArm()/playerList.get(i).getPosGames(position));
           				centerRange = centerRange + (playerList.get(i).getRange()/playerList.get(i).getPosGames(position));
           				centerError = centerError + (playerList.get(i).getError()/playerList.get(i).getPosGames(position));
           			}
           			if(position.equals("RF")) {
           				rightTotal =  rightTotal + playerList.get(i).getBins(position);
           				rightIndex++;
           				rightArm = rightArm + (playerList.get(i).getArm()/playerList.get(i).getPosGames(position));
           				rightRange = rightRange + (playerList.get(i).getRange()/playerList.get(i).getPosGames(position));
           				rightError = rightError + (playerList.get(i).getError()/playerList.get(i).getPosGames(position));
           			}
       			}
       		}         	
        	for(int i = 0; i < pitcherList.size(); i++) {
        		if(pitcherList.get(i).getYear().equals(yearList.get(u))) {
					pitcherTotal =  pitcherTotal + pitcherList.get(i).getWeightDef();
					pitcherIndex++;
					pitcherArm = pitcherArm + (pitcherList.get(i).getArm()/(pitcherList.get(i).inningsTotal()/200));
					pitcherRange = pitcherRange + (pitcherList.get(i).getRange()/(pitcherList.get(i).inningsTotal()/200));
					pitcherError = pitcherError + (pitcherList.get(i).getError()/(pitcherList.get(i).inningsTotal()/200));
				}        		
        	}
        	ArrayList<Double> defAverages = new ArrayList<Double>();
        	ArrayList<Double> componentAverages = new ArrayList<Double>();
        	double catcherAverage = catcherTotal/catcherIndex;
        	double catcherArmA = catcherArm/catcherIndex;
        	double catcherRangeA = catcherRange/catcherIndex;
        	double catcherErrorA = catcherError/catcherIndex;
        	defAverages.add(catcherAverage);
        	componentAverages.add(catcherArmA);
        	componentAverages.add(catcherRangeA);
        	componentAverages.add(catcherErrorA);
        	double firstAverage = firstTotal/firstIndex;
        	double firstArmA = firstArm/firstIndex;
        	double firstRangeA = firstRange/firstIndex;
        	double firstErrorA = firstError/firstIndex;
        	defAverages.add(firstAverage);
        	componentAverages.add(firstArmA);
        	componentAverages.add(firstRangeA);
        	componentAverages.add(firstErrorA);
        	double secondAverage = secondTotal/secondIndex;
        	double secondArmA = secondArm/secondIndex;
        	double secondRangeA = secondRange/secondIndex;
        	double secondErrorA = secondError/secondIndex;
        	defAverages.add(secondAverage);
        	componentAverages.add(secondArmA);
        	componentAverages.add(secondRangeA);
        	componentAverages.add(secondErrorA);
        	double thirdAverage = thirdTotal/thirdIndex;
        	double thirdArmA = thirdArm/thirdIndex;
        	double thirdRangeA = thirdRange/thirdIndex;
        	double thirdErrorA = thirdError/thirdIndex;
        	defAverages.add(thirdAverage);
        	componentAverages.add(thirdArmA);
        	componentAverages.add(thirdRangeA);
        	componentAverages.add(thirdErrorA);
        	double shortAverage = shortTotal/shortIndex;
        	double shortArmA = shortArm/shortIndex;
        	double shortRangeA = shortRange/shortIndex;
        	double shortErrorA = shortError/shortIndex;
        	defAverages.add(shortAverage);
        	componentAverages.add(shortArmA);
        	componentAverages.add(shortRangeA);
        	componentAverages.add(shortErrorA);
        	double leftAverage = leftTotal/leftIndex;
        	double leftArmA = leftArm/leftIndex;
        	double leftRangeA = leftRange/leftIndex;
        	double leftErrorA = leftError/leftIndex;
        	defAverages.add(leftAverage);
        	componentAverages.add(leftArmA);
        	componentAverages.add(leftRangeA);
        	componentAverages.add(leftErrorA);
        	double centerAverage = centerTotal/centerIndex;
        	double centerArmA = centerArm/centerIndex;
        	double centerRangeA = centerRange/centerIndex;
        	double centerErrorA = centerError/centerIndex;
        	defAverages.add(centerAverage);
        	componentAverages.add(centerArmA);
        	componentAverages.add(centerRangeA);
        	componentAverages.add(centerErrorA);
        	double rightAverage = rightTotal/rightIndex;
        	double rightArmA = rightArm/rightIndex;
        	double rightRangeA = rightRange/rightIndex;
        	double rightErrorA = rightError/rightIndex;
        	defAverages.add(rightAverage);
        	componentAverages.add(rightArmA);
        	componentAverages.add(rightRangeA);
        	componentAverages.add(rightErrorA);
        	double pitcherAverage = pitcherTotal/pitcherIndex;
        	double pitcherArmA = pitcherArm/pitcherIndex;
        	double pitcherRangeA = pitcherRange/pitcherIndex;
        	double pitcherErrorA = pitcherError/pitcherIndex;
        	defAverages.add(pitcherAverage);       
        	componentAverages.add(pitcherArmA);
        	componentAverages.add(pitcherRangeA);
        	componentAverages.add(pitcherErrorA);
        	String[] componentPos = { "C", "1B", "2B", "3B", "SS", "LF", "CF", "RF", "P" };
        	
        	for(int i = 0; i < playerList.size(); i++) {
            	playerList.get(i).setDWAR(defAverages, componentAverages);
            	playerList.get(i).setLeaguePA(leaguePA);
            	playerList.get(i).setRPW(this.runsPerWin);
            	playerList.get(i).setWAR();
            }
        	for(int i = 0; i < pitcherList.size(); i++) {
        		pitcherList.get(i).setDWAR(pitcherAverage, componentAverages);
        		pitcherList.get(i).setRPW(this.runsPerWin, this.leagueFIP);
        		pitcherList.get(i).setWAR(this.leagueRA9, this.leagueInnings);
        	}
        	for(int i = 0; i < defAverages.size(); i++) {
        		System.out.println(defAverages.get(i) + ": position average");        	
        	}
        	for(int i = 0; i < componentAverages.size(); i+=3) {
        		System.out.println(componentPos[i/3]);
        		System.out.println(componentAverages.get(i) + ": arm average"); 
        		System.out.println(componentAverages.get(i+1) + ": range average");     
        		System.out.println(componentAverages.get(i+2) + ": error average"); 
        	}
        	System.out.println(runsPerWin + ": runs per win");
        }
        
        //sort list of non-duplicated players for drop-down menu selection
        ArrayList<String> tablePlayers = new ArrayList<String>();
        ArrayList<String> sortedPlayers = new ArrayList<String>();
        for(int i =0; i < playerList.size(); i++) {
        	sortedPlayers.add(playerList.get(i).getName());
        	tablePlayers.add(playerList.get(i).getName());
        }        
        ArrayList<String> tablePitchers = new ArrayList<String>();        
        for(int i =0; i < pitcherList.size(); i++) {        	
        	sortedPlayers.add(pitcherList.get(i).getName());
        	tablePitchers.add(pitcherList.get(i).getName());
        }        
        Set<String> playerSet = new HashSet<>(sortedPlayers);
        sortedPlayers.clear();
        sortedPlayers.addAll(playerSet);      
        Collections.sort(sortedPlayers);
        Set<String> playerTable = new HashSet<>(tablePlayers);
        tablePlayers.clear();
        tablePlayers.addAll(playerTable);        
        Set<String> pitcherTable = new HashSet<>(tablePitchers);
        tablePitchers.clear();
        tablePitchers.addAll(pitcherTable);        
        
        //populate menu model with list
        for(int i = 0; i < sortedPlayers.size(); i++) {        	      	
        	playerModel.addElement(sortedPlayers.get(i));
        }
        
        //create selection menu based on sorted year list
      	DefaultComboBoxModel<String> yearModel = new DefaultComboBoxModel<>();		   
        Collections.sort(yearList);
        yearModel.addElement("All");
      	for(int i = 0; i < yearList.size(); i++) {
      		yearModel.addElement(yearList.get(i));	        
      	}
        JComboBox<String> yearSelect = new JComboBox<String>(yearModel);
        ItemListener yearListener = new ItemListener() {
        	public void itemStateChanged(ItemEvent itemEvent) {
        		if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
        			menuYear = itemEvent.paramString();
                 	String[] menuSelection = new String[6];
              		menuSelection[0] = menuYear;
               		menuSelection[1] = menuEnd;
               		menuSelection[2] = menuTeam;
               		menuSelection[3] = menuPosition;
               		menuSelection[4] = menuPlayer;
               		menuSelection[5] = menuTotal;
               		tableModel = new DataTable(playerList, pitcherList, teamList, tablePlayers, tablePitchers, menuSelection, currentYear);
               		refreshTable(tableModel);
               	}
        	}
        };
        yearSelect.addItemListener(yearListener);
        menuPanel.add(yearSelect);
        
        //create selection menu allowing user to specify a certain span of years in the given range
	    DefaultComboBoxModel<String> endModel = new DefaultComboBoxModel<>();
	    endModel.addElement("-To Year");
	    for(int i = 0; i < yearList.size(); i++) {
	        endModel.addElement(yearList.get(i));	        
	    }	    
	    JComboBox<String> yearEnd = new JComboBox<String>(endModel);
	    ItemListener endListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
            		menuEnd = itemEvent.paramString();
            		String[] menuSelection = new String[6];
              		menuSelection[0] = menuYear;
               		menuSelection[1] = menuEnd;
               		menuSelection[2] = menuTeam;
               		menuSelection[3] = menuPosition;
               		menuSelection[4] = menuPlayer;
               		menuSelection[5] = menuTotal;
            		tableModel = new DataTable(playerList, pitcherList, teamList, tablePlayers, tablePitchers, menuSelection, currentYear);
                	refreshTable(tableModel);
            	}
            }
        };
        yearEnd.addItemListener(endListener);        
	    menuPanel.add(yearEnd);
	    
	    //create selection menu based on team list
	    DefaultComboBoxModel<String> teamModel = new DefaultComboBoxModel<>();
	    teamModel.addElement("All Teams");
	    ArrayList<String> sortedTeams = new ArrayList<String>();
	    for(int i = 0; i < teamList.size(); i++) {
	    	sortedTeams.add(teamList.get(i).getName());	    	        
	    }
	    Collections.sort(sortedTeams); 
        for(int i = 0; i < sortedTeams.size(); i++) {	    	
        	teamModel.addElement(sortedTeams.get(i));        	
        }        
	    JComboBox<String> teamSelect = new JComboBox<String>(teamModel);
	    ItemListener teamListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
            		menuTeam = itemEvent.paramString();
            		String[] menuSelection = new String[6];
              		menuSelection[0] = menuYear;
               		menuSelection[1] = menuEnd;
               		menuSelection[2] = menuTeam;
               		menuSelection[3] = menuPosition;
               		menuSelection[4] = menuPlayer;
               		menuSelection[5] = menuTotal;
            		tableModel = new DataTable(playerList, pitcherList, teamList, tablePlayers, tablePitchers, menuSelection, currentYear);
            		refreshTable(tableModel);
            	}
            }
        };
        teamSelect.addItemListener(teamListener); 
        menuPanel.add(teamSelect);        
        
        //create selection menu based on position
        DefaultComboBoxModel<String> typeModel = new DefaultComboBoxModel<>();             
        typeModel.addElement("Position Players");
        typeModel.addElement("All Pitchers");
        typeModel.addElement("Relief Pitchers");
        typeModel.addElement("Defensive Stats");
        typeModel.addElement("C");
        typeModel.addElement("1B");
        typeModel.addElement("2B");
        typeModel.addElement("SS");
        typeModel.addElement("3B");
        typeModel.addElement("LF");
        typeModel.addElement("CF");
        typeModel.addElement("RF"); 
        typeModel.addElement("DH");
        JComboBox<String> typeSelect = new JComboBox<String>(typeModel);
        ItemListener positionListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
            		menuPosition = itemEvent.paramString();
            		String[] menuSelection = new String[6];
              		menuSelection[0] = menuYear;
               		menuSelection[1] = menuEnd;
               		menuSelection[2] = menuTeam;
               		menuSelection[3] = menuPosition;
               		menuSelection[4] = menuPlayer;
               		menuSelection[5] = menuTotal;
            		tableModel = new DataTable(playerList, pitcherList, teamList, tablePlayers, tablePitchers, menuSelection, currentYear);
            		refreshTable(tableModel);
            	}
            }
        };
        typeSelect.addItemListener(positionListener);
        menuPanel.add(typeSelect);
        
        //create selection menu for individual players
        JComboBox<String> playerSelect = new JComboBox<String>(playerModel);
        ItemListener playerListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
            		menuPlayer = itemEvent.paramString();
            		String[] menuSelection = new String[6];
              		menuSelection[0] = menuYear;
               		menuSelection[1] = menuEnd;
               		menuSelection[2] = menuTeam;
               		menuSelection[3] = menuPosition;
               		menuSelection[4] = menuPlayer;
               		menuSelection[5] = menuTotal;
            		tableModel = new DataTable(playerList, pitcherList, teamList, tablePlayers, tablePitchers, menuSelection, currentYear);
            		refreshTable(tableModel);
            	}
            }
        };
        playerSelect.addItemListener(playerListener); 
	    menuPanel.add(playerSelect);	
	    
	    //Team totals
        DefaultComboBoxModel<String> totalModel = new DefaultComboBoxModel<>();             
        totalModel.addElement("-");
        totalModel.addElement("Team WAR Totals");
        JComboBox<String> totalSelect = new JComboBox<String>(totalModel);
        ItemListener totalsListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
            		menuTotal = itemEvent.paramString();
            		String[] menuSelection = new String[6];
              		menuSelection[0] = menuYear;
               		menuSelection[1] = menuEnd;
               		menuSelection[2] = menuTeam;
               		menuSelection[3] = menuPosition;
               		menuSelection[4] = menuPlayer;
               		menuSelection[5] = menuTotal;
            		tableModel = new DataTable(playerList, pitcherList, teamList, tablePlayers, tablePitchers, menuSelection, currentYear);
            		refreshTable(tableModel);
            	}
            }
        };
        totalSelect.addItemListener(totalsListener);
        menuPanel.add(totalSelect);
	    menuPanel.revalidate();
	    
	    //create JTable with all existing info
	    String[] defaultMenu = new String[1];
	    defaultMenu[0] = "default";
	    this.tableModel = new DataTable(playerList, pitcherList, teamList, tablePlayers, tablePitchers, defaultMenu, currentYear);	   
	    table = new JTable(tableModel);
	    table.setAutoCreateRowSorter(true);
	    this.add(new JScrollPane(table), BorderLayout.CENTER);
	    this.revalidate();	    
		this.setVisible(true);

		JButton button1 = new JButton("Export XML");
		button1.setBackground(SystemColor.activeCaption);
		XMLWriter exportXML = new XMLWriter(this.tableModel, this.yearList, this.leagueID);
		button1.addActionListener(exportXML);
		menuPanel.add(button1);
		menuPanel.revalidate();	    
    }
}
