package v.i.incandenza;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Window extends JFrame {
	private JFrame frame;
	private static JTabbedPane tabbedPane;
	
	public Window() {
		initialize();
	}
	
	public static void switchPanel() {
		tabbedPane.setSelectedIndex(1);	
	}
	
	public static void addPanel(StatsPanel sp) {		
		StatsPanel card2 = sp;
		tabbedPane.addTab("Stats", card2);
	}
	
	private void initialize() {
		frame = new JFrame();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	frame.setSize(screenSize.width-100, screenSize.height-100);    	
		frame.getContentPane().setForeground(Color.DARK_GRAY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		tabbedPane = new JTabbedPane();
		MainScreen card1 = new MainScreen();				
		tabbedPane.addTab("League/Year", card1);		
		tabbedPane.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		frame.add(tabbedPane);	
		frame.pack();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);		
		frame.setVisible(true);
	}
}


