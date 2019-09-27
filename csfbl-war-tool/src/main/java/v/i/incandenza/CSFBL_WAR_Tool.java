package v.i.incandenza;

import java.awt.EventQueue;

public class CSFBL_WAR_Tool {
	//launch application
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Window();					
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});		
	}	
}