import javafx.application.Application;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

/**
 * Created by Jonathan on 30/12/2016.
 */
public class App extends Application {

    private Stage stage;
    private VueMenu vueMenu;
    private Galaxie g;
    private Inventory inventaire;
    private GalaxieVue galaxieVue;
    private ControlJeu controlJeu;
    private Thread time;
    private Thread timerpassager;
    private Thread timernavette;
    public static StationSpatiale stationdepart = new StationSpatiale();

    public static void main(String[] args) {
        System.out.println( "Main method inside Thread : " +  Thread.currentThread().getName());

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        // définit la largeur et la hauteur de la fenêtre
        // en pixels, le (0, 0) se situe en haut à gauche de la fenêtre
        primaryStage.setWidth(1024);
        primaryStage.setHeight(768);
        // met un titre dans la fenêtre
        primaryStage.setTitle("StarTrek Mini");
        
        vueMenu = new VueMenu(this);
        stage.setScene(vueMenu.getScene());
        stage.show();
    }

    public void restart(){
        stage.setScene(vueMenu.getScene());
    }

    public void lancerJeu() {
        System.out.println("Click menu");
        inventaire = new Inventory();
        g = new Galaxie();
        time = new Thread(new Timer(g));
        timerpassager = new Thread(new TimerPassager(g));
        timernavette = new Thread(new TimerNavette(g));
        controlJeu = new ControlJeu(g);
        galaxieVue = new GalaxieVue(controlJeu, g);
        galaxieVue.creer_scene_jeu();
        stage.setScene(galaxieVue.getScene());
        stage.show();
        time.start();
        timerpassager.start();
        timernavette.start();
    }
    
   /* public void refresh(){
    	stage.setScene(galaxieVue.getScene());
    	stage.show();
    	
    }
   */ 

}
