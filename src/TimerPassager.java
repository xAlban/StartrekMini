import java.lang.Runnable;

import javafx.application.Platform;


public class TimerPassager implements Runnable {
	
	private Galaxie galaxie;
	
	
	public TimerPassager(Galaxie g){
		this.galaxie = g;
	}
	
	//lancement de l'horloge
	public void run() {
		
		while (true){
		
			try {
			    Thread.sleep(25000);               
			    } 
			 catch(InterruptedException ex) {
			Thread.currentThread().interrupt();} 

			//on place sur une station 0,1 ou 2 passager apres avoir redonner la main au prochain thread
			//en testant d'abord si la station est saturee. Si elle l'est on arrete le jeu
			Platform.runLater(() -> {
				int r = (int)(Math.random()*(galaxie.getListeStations().size()));
				if (galaxie.getListeStations().get(r).getUsagers().size() > 15){
					galaxie.resetCarte();				
				}else{
					galaxie.getListeStations().get(r).genererUsagers(galaxie);
					System.out.println("usager cree");
				}

	        });
			
			
		}
		
	}
	
}