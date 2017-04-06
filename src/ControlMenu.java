import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Created by Jonathan on 22/12/2016.
 */
public class ControlMenu implements EventHandler<MouseEvent> {
    private VueMenu vueMenu;
    private App app;

    public ControlMenu(VueMenu vue, App app){
        vueMenu = vue;
        this.app = app;
    }

    @Override
    public void handle(MouseEvent event) {
        app.lancerJeu();
    }

}
