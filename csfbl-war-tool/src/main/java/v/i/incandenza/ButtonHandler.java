package v.i.incandenza;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ButtonHandler implements ActionListener {	
	private ArrayList<String> yearList;
	private static String userLeague;
	private static String userYears;
	private StatsPanel sPanel;	
	
	public void setLeague(String league) {
		userLeague = league;
	}
	
	public void setYears(String years) {
		userYears = years;
	}
	
	
	public static String getLeague() {
		return userLeague;
	}
	
	public ArrayList<String> getYears() {
		return yearList;
	}
	
	//upon user clicking "generate" button, parse years and team entered, populate lists, 
	//and create new tabbed pane for data
	public void actionPerformed(ActionEvent e) {
		yearList = new ArrayList<String>();
		userYears = MainScreen.getYears();		
		userLeague = MainScreen.getLeague();		
		if(userYears != null) {
			for(int i =0; i < userYears.length(); i++) {
				String y;
				char a = userYears.charAt(i);				
				if(a == ',') {
					y = userYears.substring(i-4, i);					
					yearList.add(y);					
				}	
				else if(i == (userYears.length() - 1)) {
					y = userYears.substring(i-3, i+1);					
					yearList.add(y);					
				}
				else {					
				}
			}
		}			
		this.sPanel = new StatsPanel(yearList, userLeague);
		Window.addPanel(this.sPanel);
		if(userLeague != null && userYears != null) {
			Window.switchPanel();	
		}			
	}		            
};		
	       


