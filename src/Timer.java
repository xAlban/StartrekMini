import java.lang.Runnable;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;

import java.lang.Exception;
import java.io.IOException;

public class Timer implements Runnable {
	
	private Galaxie galaxie;
	
	
	public Timer(Galaxie g){
		this.galaxie = g;
	}
	
	//lancement de l'horloge
	public void run() {
		
		while (true){
		
			try {
			    Thread.sleep(45000);               
			    } 
			 catch(InterruptedException ex) {
			//toute les XX ms on arrete le thread principale pour ensuite indiquer que l'on place une station
			//lorsque le prochain thread prend la main
			Thread.currentThread().interrupt();} 
			Platform.runLater(() -> {
	            galaxie.placerStation();
	        });
			
			
		}
		
	}
	
}
