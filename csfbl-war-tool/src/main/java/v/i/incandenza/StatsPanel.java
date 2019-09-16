package v.i.incandenza;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import org.jsoup.Jsoup;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.DefaultComboBoxModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.IOException;

public class StatsPanel extends JPanel {
	private ArrayList<String> yearList = new ArrayList<String>();
	private ArrayList<Team> teamList = new ArrayList<Team>();
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private ArrayList<Pitcher> pitcherList = new ArrayList<Pitcher>();
	ArrayList<ArrayList<String>> fieldingData;
	ArrayList<String> playerData;
	private Document doc;
	private Document xBat;
	private Document normPitch;
	private Document xPitch;
	private String leagueID;	
	private String defaultUrl; 
	private DataTable tableModel;
	private JTable table;
	private String gameID;
	private String currentYear;
	
	public void setYears(ArrayList<String> userYears) {		
		for(int i = 0; i < userYears.size(); i++) {
			yearList.add(userYears.get(i));			
		}
	}
	
	public void setLeague(String userLeague) {
		leagueID = userLeague;
	}
	
	public void refreshTable(DataTable model) {
		JTable temporaryTable = new JTable(tableModel);
		this.remove(table);
	    this.add(new JScrollPane(temporaryTable), BorderLayout.CENTER);
	    this.revalidate();	 
	}
	
	public void assignFielding(String name, double runs) {
		System.out.println(name + ": " + runs);		
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
				doc = Jsoup.connect(url).get();					
				Elements parents = doc.select("tbody > tr > td");
				Elements leagueDate = doc.select("div > dl > dd");
				currentYear = leagueDate.get(0).text();
				currentYear = currentYear.split("\\s|/")[2];
				int getPBP = 0;
				for(int a = 0; a < parents.size(); a += 2) {
					Element parent = parents.get(a);
					String x = parent.child(0).text();
					String y = parent.child(0).attr("href");
					String digits = y.replaceAll("[^\\d]", "");
					int teamID = Integer.parseInt(digits);
					Team z = new Team();
					z.setValues(x, teamID);					
					teamList.add(z);
					if(getPBP == 0) {
						gameID = "https://www.csfbl.com/team/news.asp?teamid=" + teamID;
					}
					getPBP++;					
				}
			}
			catch(IOException e) {
		       e.getMessage();
		       e.printStackTrace();
			}			
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
            		String yearItem = itemEvent.paramString();
            		tableModel.setYear(yearItem);
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
            		String endYear = itemEvent.paramString();
                	tableModel.setEnd(endYear);
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
            		String team = itemEvent.paramString();
            		tableModel.setTeam(team);
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
        typeModel.addElement("C");
        typeModel.addElement("1B");
        typeModel.addElement("2B");
        typeModel.addElement("SS");
        typeModel.addElement("3B");
        typeModel.addElement("LF");
        typeModel.addElement("CF");
        typeModel.addElement("RF"); 
        JComboBox<String> typeSelect = new JComboBox<String>(typeModel);
        ItemListener positionListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
            		String position = itemEvent.paramString();
            		tableModel.setPos(position);
            		refreshTable(tableModel);
            	}
            }
        };
        typeSelect.addItemListener(positionListener);
        menuPanel.add(typeSelect);
        
        //create selection menu based on all players in the league, at the same time 
        //create that list of players and add their stats where applicable
        DefaultComboBoxModel<String> playerModel = new DefaultComboBoxModel<>();
        playerModel.addElement("Individual Players");
        //cycles through each year specified
        for(int i = 0; i < yearList.size(); i++) {
        	//cycles through each team in the league in that year
        	for(int x = 0; x < teamList.size(); x++) {        		
        		String baseURL = "https://www.csfbl.com/team/stats.asp?teamid=";
        		String salaryURL = "https://www.csfbl.com/player/history.asp?playerid=";
                String extBat = "&type=4";
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
        			xBat = Jsoup.connect(url).get();	
        			url = baseURL.concat(Integer.toString(iD));
        			url = url.concat(extPit);
        			url = url.concat(yearURL);        			
        			xPitch = Jsoup.connect(url).get();
        			url = baseURL.concat(Integer.toString(iD));
        			url = url.concat(normPit);
        			url = url.concat(yearURL);         			
        			normPitch = Jsoup.connect(url).get();
        			//get desired HTML elements from these pages
        			Elements batParents = xBat.select("tbody > tr > td");        			
        			Elements batNames = xBat.select("span[class].visible-xs-inline");        			
        			Elements xpitParents = xPitch.select("tbody > tr > td");        			
        			Elements xpitNames = xPitch.select("span[class].visible-xs-inline");
        			Elements pitchParents = normPitch.select("tbody > tr > td");      			
        			Elements pitchNames = normPitch.select("span[class].visible-xs-inline"); 
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
        				//Element playerID = batParents.get(a+1);
        				//String playerLink = playerID.child(0).attr("href");
        				//playerLink = playerLink.replaceAll("[^\\d]", "" );
        				//String playerURL = salaryURL + playerLink;
        				//Document salaryPage = Jsoup.connect(playerURL).get();
        				String salaryString = "0";
        				//for(int z = 0; z < salaryParents.size(); z+=13) {
        					//if(salaryParents.get(z).text().equals(playerYear)) {
        						//salaryString = salaryParents.get(z+12).text();
        						//salaryString = salaryString.replaceAll("[^\\d]", "" );
        						//System.out.println(salaryString);
        					//}
        				//}		      				
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
        					playerList.get(matchNum).setXRAA(xRaa);
        				}
        				if(match == false && teamSwitch == false) {        					
        					Player newPlayer = new Player(playerName, playerPos, tEam, xRaa, playerYear, salaryString);
        					playerList.add(newPlayer);
        				}        				
        			}
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
        				//String playerURL = salaryURL + playerLink;
        				//Document salaryPage = Jsoup.connect(playerURL).get();
        				//Elements salaryParents = salaryPage.select("tbody > tr > td");
        				String salaryString = "0";
        				//for(int z = 0; z < salaryParents.size(); z+=13) {
        					//if(salaryParents.get(z).text().equals(playerYear)) {
        						//salaryString = salaryParents.get(z+12).text();
        						//salaryString = salaryString.replaceAll("[^\\d]", "" );
        					//}
        				//}
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
        				}
        			}
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
                }       	
        		catch(IOException e) {
        			e.getMessage();
        			e.printStackTrace();
        		}
        	}
        }
        //calculate games played this current season regardless of seasons specified, in order to calculate scraping start page
        int pageNum = 1;
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
				int currentGames = (currentWins + currentLosses)/2;
		        pageNum = 1 + Math.abs((Integer.parseInt(currentYear) - Integer.parseInt(yearList.get(yearList.size() - 1))) * ((currentGames/1920)*12));
	        }
	        catch(IOException e) {
				e.getMessage();
				e.printStackTrace();
			} 
        }
        //get PBP url range for most recent season
        for(int u = Integer.parseInt(yearList.get(yearList.size() - 1)) - Integer.parseInt(yearList.get(0)); u >= 0; u--) {
        	try {
        		//get total games played this season
        		int totalGames = 0;
        		if(yearList.get(u).equals(currentYear)) {
        			String standingsURL = "https://www.csfbl.com/league/standings.asp?leagueid=" + leagueID + "&season=" + yearList.get(u);
        			Document standingsPage = Jsoup.connect(standingsURL).get();
        			Elements winLoss = standingsPage.select("tbody > tr > td");
        			int wins = 0;
        			int losses = 0;
        			for(int z = 0; z < winLoss.size(); z+=13) {
        				wins = wins + Integer.parseInt(winLoss.get(z+2).text());
        				losses = losses + Integer.parseInt(winLoss.get(z+3).text());
        			}
        			totalGames = (wins + losses)/2;
        		}
        		else {
        			totalGames = 1920;
        		}
            	//get most recent game played
    			boolean lastGame = false;
    			int upperLink = 0;
    			String pbpURL = "https://www.csfbl.com/playbyplay.asp?gameid=";
    			Elements gameList = new Elements();
    			//check team news page for playoff appearance, if none detected then get last game played
    			while(lastGame == false) {
    				Document teamNews = Jsoup.connect(gameID + "&page=" + pageNum).get();
    				gameList = teamNews.select("tbody > tr > td");
   					boolean endPage = false;
   					while(endPage == false) {
    					for(int a = 0; a < gameList.size(); a++) {
    						if(gameList.get(a).text().contains("Player of the Game")) {
    							String gameLink = gameList.get(a).child(4).attr("href");
    			    			gameLink = gameLink.split("&")[0];
    			    			gameLink = gameLink.replaceAll("[^\\d]", "" );
    			    			int lastIndex = Integer.parseInt(gameLink);
    			    			Document playByPlay = Jsoup.connect(pbpURL + lastIndex).get();
    			    			Elements metas = playByPlay.select("head > meta");
    			    			Element date = metas.get(3);
    			    			String dateString = date.attr("content");
    			    			dateString = dateString.split(",")[1];
    			    			dateString = dateString.split(":")[0];
    			    			String year = dateString.split("/")[2];
    			    			String month = dateString.split("/")[0];
    			    			String day = dateString.split("/")[1];
    			    			if(year.equals(yearList.get(u)) && !month.contains(" 10") && !(month.contains("9") && day.contains("30"))) {
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
	    			Document playByPlay = Jsoup.connect(pbpURL + upperLink).get();
	    			Elements metas = playByPlay.select("head > meta");
	    			Element date = metas.get(3);
	    			String dateString = date.attr("content");
	    			dateString = dateString.split(",")[1];
	    			dateString = dateString.split(":")[0];
	    			String year = dateString.split("/")[2];
	    			String month = dateString.split("/")[0];
	    			String day = dateString.split("/")[1];
	    			if(!year.equals(yearList.get(u)) || month.contains("10") || month.contains("4") || (month.equals(" 9") && day.contains("30"))){
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
    			upperLink = upperLink - 1;
    			totalGames = totalGames - 1;
    			int firstID = upperLink - totalGames;
    			for(int y = firstID; y < upperLink; y++) {
    				Document playByPlay = Jsoup.connect(pbpURL + y).get();
    				Element div = playByPlay.selectFirst("div[class=col-sm-8]");
    				String playText = div.text();
    				//split PbP into list of separate plays and remove extraneous strings
    				ArrayList<String> playList = new ArrayList<>(Arrays.asList(playText.split("\\.|:")));
    				for (int f = 0; f < playList.size(); f++) {
    					if(playList.get(f).contains("SCORE:") || playList.get(f).contains("hits.)")) {
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
    					String last = name.split(" ")[1];
    					String first = name.split(" ")[0];
    					name = last + ", " + first;
    					playerData.add(name);
    					fieldingData.add(playerData);
    					System.out.println(name);
    					for(int p = 0; p < fieldingData.size(); p++) {
    						System.out.println(fieldingData.get(p).get(0) );
    					}
    				}
    				//go through list of plays, count fielding events, and assign run value to player object. check run values
    				int startIndex = 0;
    				int endIndex = 0;
    				int playIndex = 0;
    				for(int d = 0; d < pbp.size(); d++) {
    					if(pbp.get(d).is("br")) {
    						startIndex = endIndex;
    						endIndex = d;
    						if(playList.get(playIndex).contains("hits a fly ball")) {
    							if(playList.get(playIndex).contains("error")) {
    								String[] playElements = playList.get(playIndex).split(",");
    								System.out.println("error");
    								String player = pbp.get(d-2).text();
    								int playerIndex = 0;
    								for(int i = 0; i < fieldingData.size(); i++) {
    									if(fieldingData.get(i).get(0).contains(player)) {
    										playerIndex = i;
    									}
    								}
    								player = fieldingData.get(playerIndex).get(0);
    								assignFielding(player, -0.6);
    							}
    							else if(playList.get(playIndex).contains("tags up")) {
    								System.out.println("sac fly");
    								String player = pbp.get(d-4).text();
    								int playerIndex = 0;
    								for(int i = 0; i < fieldingData.size(); i++) {
    									if(fieldingData.get(i).get(0).contains(player)) {
    										playerIndex = i;
    									}
    								}
    								player = fieldingData.get(playerIndex).get(0);
    								assignFielding(player, 0.7);
    							}
    							else if(playList.get(playIndex).contains("thrown out")) {
    								System.out.println("outfield assist");
    								String player = pbp.get(d-4).text();
    								int playerIndex = 0;
    								for(int i = 0; i < fieldingData.size(); i++) {
    									if(fieldingData.get(i).get(0).contains(player)) {
    										playerIndex = i;
    									}
    								}
    								player = fieldingData.get(playerIndex).get(0);
    								assignFielding(player, 0.7);
    								//re-check this, temporary
    								player = pbp.get(d-1).text();
    								playerIndex = 0;
    								for(int i = 0; i < fieldingData.size(); i++) {
    									if(fieldingData.get(i).get(0).contains(player)) {
    										playerIndex = i;
    									}
    								}
    								player = fieldingData.get(playerIndex).get(0);
    								assignFielding(player, 0.5);
    							}
    							else {
    								String player = pbp.get(d-1).text();
    								System.out.println("fly ball catch");
    								int playerIndex = 0;
    								for(int i = 0; i < fieldingData.size(); i++) {
    									if(fieldingData.get(i).get(0).contains(player)) {
    										playerIndex = i;
    									}
    								}
    								player = fieldingData.get(playerIndex).get(0);
    								assignFielding(player, 0.7);
    							}
    						}
    						else if(playList.get(playIndex).contains("hits a ground ball")) {
    							if(playList.get(playIndex).contains("error")) {
    								System.out.println("ground ball error");
    								String player = pbp.get(d-2).text();
    								int playerIndex = 0;
    								for(int i = 0; i < fieldingData.size(); i++) {
    									if(fieldingData.get(i).get(0).contains(player)) {
    										playerIndex = i;
    									}
    								}
    								player = fieldingData.get(playerIndex).get(0);
    								assignFielding(player, -0.4);
    							}
    							if(playList.get(playIndex).contains("double play")) {
    								System.out.println("DP");
    								String player = pbp.get(d-2).text();
    								int playerIndex = 0;
    								for(int i = 0; i < fieldingData.size(); i++) {
    									if(fieldingData.get(i).get(0).contains(player)) {
    										playerIndex = i;
    									}
    								}
    								player = fieldingData.get(playerIndex).get(0);
    							}
    							else {
    								String player = pbp.get(d-2).text();
    								System.out.println("ground ball out");
    								int playerIndex = 0;
    								for(int i = 0; i < fieldingData.size(); i++) {
    									if(fieldingData.get(i).get(0).contains(player)) {
    										playerIndex = i;
    									}
    								}
    								player = fieldingData.get(playerIndex).get(0);
    							}
    						}
    						playIndex++;
    					}
    				}
    			}
    			pageNum += 12;
            }
    		catch(IOException e) {
    		    e.getMessage();
    		    e.printStackTrace();
    		}  
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
        //create selection menu for individual players
        JComboBox<String> playerSelect = new JComboBox<String>(playerModel);
        ItemListener playerListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
            		String player = itemEvent.paramString();
            		tableModel.setPlayer(player);
            		refreshTable(tableModel);
            	}
            }
        };
        playerSelect.addItemListener(playerListener); 
	    menuPanel.add(playerSelect);	    
	    menuPanel.revalidate();
	    
	    //create JTable with all existing info
	    this.tableModel = new DataTable(playerList, pitcherList, teamList, tablePlayers, tablePitchers);	   
	    table = new JTable(tableModel);
	    table.setAutoCreateRowSorter(true);
	    this.add(new JScrollPane(table), BorderLayout.CENTER);
	    this.revalidate();	    
		this.setVisible(true);						
    }
}
