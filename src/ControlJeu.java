import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.util.*;


public class ControlJeu implements EventHandler<MouseEvent> {
    private StationSpatiale s1;
    private StationSpatiale s2;
    private static ControlJeu controljeu;
    private Galaxie galaxie;
    // private Inventaire inventaire;
    
    public ControlJeu(){
    	
    }
    public ControlJeu(Galaxie g){
		this.galaxie = g;

    }

    @Override
    public void handle(MouseEvent event) {
    	System.out.println("clicked jeu"); // test pour voir si on obtien le clic dans la fenetre du jeu
    }
    
    public static ControlJeu getControlJeu(Galaxie g){
    	// creation d'un controleur de maniere statique pour l'utiliser dans le controlbstation
    	if(controljeu==null)
    	{
    		controljeu = new ControlJeu(g);
    	}
    	
    	return controljeu;
    }
    
    public void tracer(StationSpatiale s){
    	// on viens dans cette fonction depuis un clic sur une station (controlbstation)
    	// on va stocker lorsqu'on clic une premiere fois la station sur laquel on a clique
    	// puis si on clic sur une autre station une deuxieme fois on trace une ligne
    	if(this.s1==null){
    		this.s1=s;	
    	}else if(this.s2==null)
    	{
    		this.s2=s;
    		
    		System.out.println("Ligne tracer");
    		galaxie.rechercheLiaisonDesStations(s1,s2);
    		   		
    		this.s1=null;
    		this.s2=null;
    		galaxie.creeTableRoutageListeSPL();
    		//galaxie.test1ereTable();
    		galaxie.creeTableRoutageCorrespondances();
    		galaxie.test2iemeTable();
    	}
    		
    	
    }
        
}
