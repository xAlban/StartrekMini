import java.lang.Runnable;
import javafx.application.Platform;


public class TimerNavette implements Runnable {
	
	private Galaxie galaxie;
	
	
	public TimerNavette(Galaxie g){
		this.galaxie = g;
	}
	
	//lancement de l'horloge
	public void run() {
		
		while (true){
		
			try {
			    Thread.sleep(1000);               
			    } 
			 catch(InterruptedException ex) {
			Thread.currentThread().interrupt();} 
			//Meme principe que la classe Timer
			//on deplace les navettes apres avoir donner la main au prochain thread
			Platform.runLater(() -> {
				Galaxie.typeTerrain[][] tableau = galaxie.getCarte();
				if (tableau[0][0] == Galaxie.typeTerrain.GAMEOVER){
					Thread.currentThread().stop();
				}else{
					galaxie.deplacerLesNavettes();
				}
				
	        });
			
			
		}
		
	}
	
}