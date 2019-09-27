package v.i.incandenza;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.JButton;

public class MainScreen extends JPanel {	
	private JTextField league;
	private JTextField years;
	private static String userYears;
	private static String userLeague;
	private ButtonHandler bhandler;	
	
	public static String getYears() {
		return userYears;		
	}
	
	public static String getLeague() {
		return userLeague;
	}
			
	public MainScreen() {				
		this.setLayout(new BorderLayout());
		this.setVisible(true);		
		
		JLabel title = new JLabel("CSFBL WAR Tool");
		title.setBackground(new Color(255, 255, 0));
		title.setFont(new Font("Scheherazade", Font.PLAIN, 70));		
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setVerticalAlignment(JLabel.CENTER);
		this.add(title, BorderLayout.NORTH);

		JPanel center = new JPanel();		
		center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
		
		JLabel leagueSelect = new JLabel("Enter League ID:");
		leagueSelect.setBackground(SystemColor.info);
		leagueSelect.setFont(new Font("Scheherazade", Font.PLAIN, 23));
		leagueSelect.setAlignmentX(CENTER_ALIGNMENT);
		
		this.league = new JTextField();
		this.league.setMaximumSize(new Dimension(150,60));
		this.league.setAlignmentX(CENTER_ALIGNMENT);		
		this.league.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent f) {				
				char a = f.getKeyChar();
				if(!(Character.isDigit(a) || a==KeyEvent.VK_BACK_SPACE || a==KeyEvent.VK_DELETE)) {
					f.consume();
				}
				else {					
					userLeague = league.getText();
					userLeague = userLeague.replaceAll("\\s","");
					bhandler.setLeague(userLeague);
				}
			}
		});				
		
		JLabel yearSelect = new JLabel("Enter Year(s):");
		yearSelect.setBackground(SystemColor.info);
		yearSelect.setFont(new Font("Scheherazade", Font.PLAIN, 23));			
		yearSelect.setAlignmentX(CENTER_ALIGNMENT);
		
		JLabel comma = new JLabel("(separate by comma)");
		comma.setBackground(SystemColor.info);
		comma.setFont(new Font("SCheherazade", Font.PLAIN, 23));
		comma.setAlignmentX(CENTER_ALIGNMENT);
		
		this.years = new JTextField();	
		this.years.setMaximumSize(new Dimension(150,60));
		this.years.setAlignmentX(CENTER_ALIGNMENT);		
		this.years.addKeyListener(new KeyAdapter() {
			@Override
	        public void keyReleased(KeyEvent e) {
				char b = e.getKeyChar();				
				if(!(Character.isDigit(b) || b==KeyEvent.VK_BACK_SPACE || b==KeyEvent.VK_COMMA || b==KeyEvent.VK_DELETE)) {
					e.consume();
				}
			 	else {					
					userYears = years.getText();					
					userYears = userYears.replaceAll("\\s","");
					bhandler.setYears(userYears);
				}
	        }			
		});		
		
		JButton button1 = new JButton("Generate Data");
		button1.setBackground(SystemColor.activeCaption);
		button1.setAlignmentX(CENTER_ALIGNMENT);	
		this.bhandler = new ButtonHandler();		
		button1.addActionListener(this.bhandler);			
		
		center.add(Box.createVerticalGlue());
		center.add(leagueSelect);		
		center.add(this.league);
		center.add(Box.createVerticalGlue());
		center.add(yearSelect);		
		center.add(comma);		
		center.add(this.years);		
		center.add(Box.createVerticalGlue());
		center.add(button1);
		center.add(Box.createVerticalGlue());
		this.add(center, BorderLayout.CENTER);
				
		//JLabel image1 = new JLabel("New label");
		//ImageIcon transcendTwo = new ImageIcon(getClass().getResource("D8LtkYzXUAE_8oe.jpg"));
		//image1.setIcon(transcendTwo);		
		//this.add(image1, BorderLayout.WEST);
		
		//JLabel image2 = new JLabel("New label");
		//ImageIcon transcendOne = new ImageIcon(getClass().getResource("D8LxpqxXkAYyxHw.jpg"));
		//image2.setIcon(transcendOne);		
		//this.add(image2, BorderLayout.EAST);	
		
		JLabel hermetic = new JLabel("THE ONCE HERMETIC KNOWLEDGE OF THE ORACLES...");
		hermetic.setFont(new Font("Scheherazade", Font.ITALIC, 23));		
		hermetic.setHorizontalAlignment(JLabel.CENTER);
		hermetic.setVerticalAlignment(JLabel.CENTER);
		this.add(hermetic, BorderLayout.SOUTH);	
		
	}
}