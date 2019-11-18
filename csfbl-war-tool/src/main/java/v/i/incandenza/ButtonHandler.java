package v.i.incandenza;

import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.*;

public class ButtonHandler implements ActionListener {	
	private ArrayList<String> yearList;
	private static String userLeague;
	private static String userYears;
	private StatsPanel sPanel;	
	private ProgressBar consolePanel;
	
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
		//JTextArea ta = new JTextArea();
        //TextAreaOutputStream taos = new TextAreaOutputStream(ta, 60);
        //System.setOut(taos);
        //System.setErr(taos);
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
			}
		}			
		/*this.consolePanel = new ProgressBar();
		Window.addProgress(this.consolePanel);
		System.out.println("added");
		if(userLeague != null && userYears != null) {
			Window.showProgress();
		}*/
		this.sPanel = new StatsPanel(yearList, userLeague);
		Window.addPanel(this.sPanel);
		if(userLeague != null && userYears != null) {
			Window.switchPanel();	
		}			
	}		            
};		
	       


