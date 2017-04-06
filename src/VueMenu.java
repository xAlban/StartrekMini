
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class VueMenu {
    private App app;
    private ControlMenu controleur;
    private Galaxie modele;
    private Scene sceneMenu;

    public VueMenu(App app){
        this.app = app;
        controleur = new ControlMenu(this, app);
        buildScene();
    }

    private void buildScene() {
        // la racine du sceneGraph est le root
        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);

        // cr�ation deux panneau pour title et subtitle
        Group title = new Group();
        title.setTranslateX(250);
        title.setTranslateY(300);
        Group subtitle = new Group();
        subtitle.setTranslateX(245);
        subtitle.setTranslateY(350);

        // Attention les coordonn�es sont celles du panneau, pas de la sc�ne
        Text text = new Text(0, 30, "StarTrek MINI");
        text.setFont(new Font(80));
        text.setFill(Color.WHITE);

        Text subtext = new Text(0, 100, "Cliquer pour lancer une partie");
        subtext.setFont(new Font(40));
        subtext.setFill(Color.WHITE);

        // composer l'�l�ment plus complexe

        title.getChildren().add(text);
        subtitle.getChildren().add(subtext);
        // ajout de tous les �l�ments de la sc�ne
        root.getChildren().add(title);
        root.getChildren().add(subtitle);
        // ajout de la sc�ne sur l'estrade

        scene.setOnMouseClicked(controleur);
        sceneMenu = scene;
    }


    public Scene getScene(){
        return sceneMenu;
    }
    
    

}
