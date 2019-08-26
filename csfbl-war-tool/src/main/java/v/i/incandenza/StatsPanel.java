package v.i.incandenza;

import java.util.ArrayList;
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
	private Document doc;
	private Document xBat;
	private Document normPitch;
	private Document xPitch;
	private String leagueID;	
	private String defaultUrl; 
	private DataTable tableModel;
	
	public void setYears(ArrayList<String> userYears) {		
		for(int i = 0; i < userYears.size(); i++) {
			yearList.add(userYears.get(i));			
		}
	}
	
	public void setLeague(String userLeague) {
		leagueID = userLeague;
	}
	
	public StatsPanel(ArrayList<String> years, String league) {
		this.setYears(years);
		this.setLeague(league);		
		this.setLayout(new BorderLayout());
		JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));       	
        this.add(menuPanel, BorderLayout.PAGE_START); 
        
        defaultUrl = "https://www.csfbl.com/league/view.asp?leagueid=";
		if(leagueID != null) {
			try {
				String url = defaultUrl.concat(leagueID);
				doc = Jsoup.connect(url).get();					
				Elements parents = doc.select("tbody > tr > td > a");				
				for(int a = 0; a < parents.size(); a += 2) {
					Element parent = parents.get(a);
					String x = parent.text();
					String y = parent.attr("href");
					String digits = y.replaceAll("[^\\d]", "" );
					int teamID = Integer.parseInt(digits);
					Team z = new Team();
					z.setValues(x, teamID);					
					teamList.add(z);
				}
			}
			catch(IOException e) {
		       e.getMessage();
		       e.printStackTrace();
			}			
		}
        
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
            		System.out.println(yearItem);
            	}
            }
        };
        yearSelect.addItemListener(yearListener);
	    menuPanel.add(yearSelect);
	    
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
                	System.out.println(endYear);
            	}
            }
        };
        yearEnd.addItemListener(endListener);        
	    menuPanel.add(yearEnd);
	    
	    
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
            		System.out.println(team);
            	}
            }
        };
        teamSelect.addItemListener(teamListener); 
        menuPanel.add(teamSelect);
        	    
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
            		System.out.println(position);
            		tableModel.setPos(position);
            	}
            }
        };
        typeSelect.addItemListener(positionListener);
        menuPanel.add(typeSelect);
        
        DefaultComboBoxModel<String> playerModel = new DefaultComboBoxModel<>();
        playerModel.addElement("Individual Players");
        
        for(int i = 0; i < yearList.size(); i++) {
        	for(int x = 0; x < teamList.size(); x++) {        		
        		String baseURL = "https://www.csfbl.com/team/stats.asp?teamid=";
                String extBat = "&type=4";
                String extPit = "&type=5";
                String normPit = "&type=2";
                String yearURL = "&status=" + yearList.get(i);
                Team tEam = teamList.get(x);                
                int iD = tEam.getID();
                
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
        			
        			Elements batParents = xBat.select("tbody > tr > td");        			
        			Elements batNames = xBat.select("span[class].visible-xs-inline");        			
        			Elements xpitParents = xPitch.select("tbody > tr > td");        			
        			Elements xpitNames = xPitch.select("span[class].visible-xs-inline");
        			Elements pitchParents = normPitch.select("tbody > tr > td");      			
        			Elements pitchNames = normPitch.select("span[class].visible-xs-inline"); 
        			
        			for(int a = 0; a < batParents.size(); a+=18) {        				
        				Element posNode = batParents.get(a);
        				String playerPos = posNode.text();       				
        				Element nameNode = batNames.get(a/18);
        				String playerName = nameNode.attr("title");        				
        				Element oNode = batParents.get(a+17);
        				String xRaa = oNode.text();       				
        				String playerYear = yearList.get(i);       				      				
        				boolean match = false;        				
        				boolean switchTeam = false;        				
        				int matchNum = 0;
        				//check against existing list
        				for(int b = 0; b < playerList.size(); b++) {        					
        					Player playerCheck = playerList.get(b);
        					String yearCheck = playerCheck.getYear();
        					String nameCheck = playerCheck.getName();        					
        					ArrayList<Team> teamCheck = playerCheck.getTeam();
        					for (int c = 0; c < teamCheck.size(); c++) {
        						if(playerName.equals(nameCheck) && playerYear.equals(yearCheck)) {
            						if(!(teamCheck.get(c).equals(tEam))) {
            							match = true;
            							switchTeam = true;            							
            							matchNum = c;
            						}
            						else {
            							match = true;
                						matchNum = c;
            						}        						
            					}
        					}
        					        					
        				}
        				if(match == false && switchTeam == false) {        					
        					Player newPlayer = new Player(playerName, playerPos, tEam, xRaa, playerYear);
        					playerList.add(newPlayer);    						
        				}
        				else if(match == true && switchTeam == true) {
        					playerList.get(matchNum).setTeam(tEam);
        				}
        			}
        			for(int a = 0; a < xpitParents.size(); a+=19) {        				
        				Element posNode = xpitParents.get(a);
        				String playerPos = posNode.text();        				
        				Element nameNode = xpitNames.get(a/19);
        				String playerName = nameNode.attr("title");        				
;        				Element fIP = xpitParents.get(a+18);        				
        				String playerFIP = fIP.text();
        				if(playerFIP.equals("-")) {
        					playerFIP = "0";
        				}        				
        				String playerYear = yearList.get(i);       				      				
        				boolean match = false;
        				boolean switchTeam = false;
        				int matchNum = 0;
        				//check for team switch
        				for(int b = 0; b < pitcherList.size(); b++) {
        					Pitcher pitcherCheck = pitcherList.get(b);
        					String yearCheck = pitcherCheck.getYear();
        					String nameCheck = pitcherCheck.getName();
        					ArrayList<Team> teamCheck = pitcherCheck.getTeam();
        					for (int c = 0; c < teamCheck.size(); c++) {
	        					if(playerName.equals(nameCheck) && playerYear.equals(yearCheck)) {
	        						if(!(teamCheck.get(c).equals(tEam))) {
            							match = true;
            							switchTeam = true;            							
            							matchNum = c;
            						}
            						else {
            							match = true;
                						matchNum = c;
            						}        						
	        					}  
        					}
        				}
        				if(match == false && switchTeam == false) {    					
        					Pitcher newPitcher = new Pitcher(playerName, playerPos, tEam, playerYear);
        					newPitcher.setFIP(playerFIP);
        					pitcherList.add(newPitcher);
        				}  
        				else if(match == true && switchTeam == true) {
        					pitcherList.get(matchNum).setTeam(tEam);
        				}
        				else if(match == true && switchTeam == false) {
        					pitcherList.get(matchNum).setFIP(playerFIP);        					
        				}        			
        			}
        			for(int a = 0; a < pitchParents.size(); a+=21) {        				
        				Element posNode = pitchParents.get(a);
        				String playerPos = posNode.text();       				
        				Element nameNode = pitchNames.get(a/21);
        				String playerName = nameNode.attr("title");        				
        				Element gamesStart = pitchParents.get(a+15);
        				String playerStarts = gamesStart.text();
        				Element gamesTotal = pitchParents.get(a+2);
        				String playerGames = gamesTotal.text();
        				String playerYear = yearList.get(i);       				      				
        				boolean match = false;
        				boolean switchTeam = false;
        				int matchNum = 0;
        				//check against existing list
        				for(int b = 0; b < pitcherList.size(); b++) {
        					Pitcher pitcherCheck = pitcherList.get(b);
        					String yearCheck = pitcherCheck.getYear();
        					String nameCheck = pitcherCheck.getName();
        					ArrayList<Team> teamCheck = pitcherCheck.getTeam();
        					for (int c = 0; c < teamCheck.size(); c++) {
	        					if(playerName.equals(nameCheck) && playerYear.equals(yearCheck)) {
	        						if(!(teamCheck.get(c).equals(tEam))) {
            							match = true;
            							switchTeam = true;            							
            							matchNum = c;
            						}
            						else {
            							match = true;
                						matchNum = c;                						
            						}        						
	        					}  
        					}
        				}
        				if(match == false && switchTeam == false) {    					
        					Pitcher newPitcher = new Pitcher(playerName, playerPos, tEam, playerYear);
        					newPitcher.setGames(playerGames);
        					newPitcher.setStarts(playerStarts);
        					pitcherList.add(newPitcher);    						
        				}  
        				else if(match == true && switchTeam == true) {
        					pitcherList.get(matchNum).setTeam(tEam);
        				}
        				else if(match == true && switchTeam == false) {
        					pitcherList.get(matchNum).setGames(playerGames);
        					pitcherList.get(matchNum).setStarts(playerStarts);
        				}
        			}
                }       	
        		catch(IOException e) {
        			e.getMessage();
        			e.printStackTrace();
        		}
        	}
        }
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
               
        for(int i = 0; i < sortedPlayers.size(); i++) {        	      	
        	playerModel.addElement(sortedPlayers.get(i));
        }            	
        JComboBox<String> playerSelect = new JComboBox<String>(playerModel);
        ItemListener playerListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
            		String player = itemEvent.paramString();
            		tableModel.setPlayer(player);
            		System.out.println(player);
            	}
            }
        };
        playerSelect.addItemListener(playerListener); 
	    menuPanel.add(playerSelect);	    
	    menuPanel.revalidate();
	    
	    this.tableModel = new DataTable(playerList, pitcherList, teamList, tablePlayers, tablePitchers);	   
	    JTable table = new JTable(tableModel);
	    this.add(new JScrollPane(table), BorderLayout.CENTER);
	    this.revalidate();	    
		this.setVisible(true);						
    }
}
