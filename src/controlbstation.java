import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;

public class controlbstation implements EventHandler<MouseEvent>{
	private StationSpatiale station;
	private Couloir couloir;
	private Galaxie galaxie;
	
	public controlbstation(StationSpatiale s, Galaxie g){
		this.station = s;
		this.galaxie = g;
	}
	@Override
    public void handle(MouseEvent event) {
    	System.out.println("clicked button");
    	//on prend ControlJeu de maniere statique pour pouvoir comparer les deux station pour ensuite tracer une ligne
    	ControlJeu.getControlJeu(galaxie).tracer(station);
    	
    }
	

    
}
