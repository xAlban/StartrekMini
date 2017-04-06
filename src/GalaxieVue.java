import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;


public class GalaxieVue implements Observer{
    private GridPane gridjeu;
    private Scene sceneJeu;
    private Group root2;
    private Scene scene2;
    private ControlJeu controljeu = new ControlJeu();
    private Galaxie galaxie;
    private controlbstation controlstation;

    
    public GalaxieVue(ControlJeu ctrlj, Galaxie g){
        this.controljeu = ctrlj;
        this.galaxie = g;
        galaxie.addObserver(this);
        
    }
    //Cette methode va etre appeler une seul fois lorsqu'on lance le jeu depuis le menu ensuite on appelera update
    //qui rafraichira la vue et qui fait presque la meme chose que creer_scene_jeu sans l'initialisation de debut
    public void creer_scene_jeu(){
    	root2 = new Group();
    	scene2 = new Scene(root2, 1024, 768);
    	scene2.setFill(Color.BLACK);
    	gridjeu = new GridPane();
    	gridjeu.setBackground(new Background(new BackgroundImage(new Image("/starback.jpg"), null, null, null, null)));
    	int largeur = galaxie.getLargeur();
    	int longueur = galaxie.getLongueur();
        for (int i = 0; i < longueur; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setMinWidth(20);
            gridjeu.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < largeur; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setMinHeight(20);
            gridjeu.getRowConstraints().add(rowConst);         
        }
        gridjeu.setPrefSize(1024, 768);
        gridjeu.setGridLinesVisible(false);
        
        //placement des 3 premieres stations
        galaxie.placerStation();
        galaxie.placerStation();
        galaxie.placerStation();
        Galaxie.typeTerrain[][] tableau = galaxie.getCarte();
  
        for (int i = 0; i < longueur; i++){
            for (int j = 0; j < largeur; j++){
            	
            	switch (tableau[i][j]){
            		case NORMAL:

            			break;
            		case OBSTACLE:
            			Rectangle rectangle1 = new Rectangle(18,18);
            			rectangle1.setFill(Color.BROWN);
            			GridPane.setRowIndex(rectangle1, j);
            			GridPane.setColumnIndex(rectangle1, i);
            	        gridjeu.getChildren().add(rectangle1);
            			break;
            		case STATION:
            			switch (galaxie.getStation(i,j).getType()){
        				case ROND:
        					Circle cercle = new Circle();
        					controlstation = new controlbstation(galaxie.getStation(i, j), galaxie);
        					cercle.setRadius(9);
        					// creation du controleur pour cette station
        					cercle.setOnMouseClicked(controlstation); 
        					//placement de l'objet dans la grille
        					GridPane.setRowIndex(cercle, j);      
        					GridPane.setColumnIndex(cercle, i);
        					gridjeu.getChildren().add(cercle);
        					break;
        				case CARRE:
                			Rectangle rectangle2 = new Rectangle(18,18);
                			controlstation = new controlbstation(galaxie.getStation(i, j), galaxie);
                			rectangle2.setOnMouseClicked(controlstation);
                			rectangle2.setFill(Color.WHITE);
                			GridPane.setRowIndex(rectangle2, j);
                			GridPane.setColumnIndex(rectangle2, i);
                	        gridjeu.getChildren().add(rectangle2);
                			break;
        				case LOSANGE:
        					Polygon losange = new Polygon(10,0, 20,10 ,10,20 ,0,10);
        					controlstation = new controlbstation(galaxie.getStation(i, j), galaxie);
        					losange.setFill(Color.WHITE);
        					losange.setOnMouseClicked(controlstation);
        					GridPane.setRowIndex(losange, j);
        					GridPane.setColumnIndex(losange, i);
        					gridjeu.getChildren().add(losange);
        					break;
        				case CROIX:
        					Polygon croix = new Polygon(7,0 ,13,0 ,13,7 ,20,7 ,20,13 ,13,13 ,13,20 ,7,20 ,7,13 ,0,13 ,0,7 ,7,7);
        					controlstation = new controlbstation(galaxie.getStation(i, j), galaxie);
        					croix.setFill(Color.WHITE);
        					croix.setOnMouseClicked(controlstation);
        					GridPane.setRowIndex(croix, j);
        					GridPane.setColumnIndex(croix, i);
        					gridjeu.getChildren().add(croix);
							
							break;
        				case TRIANGLE:
        					Polygon triangle = new Polygon(10,0, 20,20 ,0,20);
        					controlstation = new controlbstation(galaxie.getStation(i, j), galaxie);
        					triangle.setFill(Color.WHITE);
        					triangle.setOnMouseClicked(controlstation);
        					GridPane.setRowIndex(triangle, j);
        					GridPane.setColumnIndex(triangle, i);
        					gridjeu.getChildren().add(triangle);
        					break;
        					
        			}		
            			
            			break;
            			
            			
            	}
            }
        }
        
        

        root2.getChildren().add(gridjeu);
        sceneJeu = scene2;
    	
    }

    public Scene getScene(){
        return sceneJeu;
    }
    
	// méthode appelée par le modèle lorsque la vue doit se mettre à jour

	@Override
	public void update(){
		// on recharge tout le tableau et verifie les cases une par une
		Galaxie.typeTerrain[][] tableau = galaxie.getCarte();
        for (int i = 0; i < galaxie.getLongueur(); i++){
            for (int j = 0; j < galaxie.getLargeur(); j++){
            	//en fonction de chaque valeur dans notre grille(carte) on cree un objet a afficher
            	switch (tableau[i][j]){
            		case NORMAL:

            			break;
            		case GAMEOVER:
            			Rectangle rectangle4 = new Rectangle(20,20);
            			rectangle4.setFill(Color.RED);
            			GridPane.setRowIndex(rectangle4, j);
            			GridPane.setColumnIndex(rectangle4, i);
            	        gridjeu.getChildren().add(rectangle4);
                        break;
            		case EFFACE :
            			Rectangle rectangle3 = new Rectangle(20,20);
            			rectangle3.setFill(Color.BLACK);
            			GridPane.setRowIndex(rectangle3, j);
            			GridPane.setColumnIndex(rectangle3, i);
            	        gridjeu.getChildren().add(rectangle3);
                        break;
            	    	
            		case OBSTACLE:
            			Rectangle rectangle1 = new Rectangle(18,18);
            			rectangle1.setFill(Color.BROWN);
            			GridPane.setRowIndex(rectangle1, j);
            			GridPane.setColumnIndex(rectangle1, i);
            	        gridjeu.getChildren().add(rectangle1);
            			break;
            		case COULOIR:
            			
    					Circle ligne = new Circle();
    					ligne.setRadius(4);
    					ligne.setFill(Color.AQUA);
    					GridPane.setRowIndex(ligne, j);
    					GridPane.setColumnIndex(ligne, i);
    					gridjeu.getChildren();
    					gridjeu.getChildren().add(ligne);
            			break;
            			
            		case NAVETTE:
            			
            			Polygon navette = new Polygon(10,0 ,13,3 ,13,10 ,20,15 ,20,18 ,13,18 ,13,20 ,7,20 ,7,18 ,0,18 ,0,15 ,7,10 ,7,3 ,10 ,0);
                        navette.setFill(Color.GREENYELLOW);
                        GridPane.setRowIndex(navette,j);
                        GridPane.setColumnIndex(navette, i);
                        gridjeu.getChildren().add(navette);
                        break;
    					
            		case STATION:
            			//afficher passager station
        				for (Passager passager : galaxie.getStation(i,j).getUsagers()){
        					// si c'est une station on affiche les passagers autour de celle ci
        					switch(passager.getDestination()){
        						case ROND:
        							Circle cerclep = new Circle();
        							cerclep.setRadius(3);
        							cerclep.setFill(Color.RED);
        							GridPane.setRowIndex(cerclep, j);
                					GridPane.setColumnIndex(cerclep, i-1);
                					gridjeu.getChildren().add(cerclep);
                					break;
        						case CARRE:
        							Rectangle rectanglep = new Rectangle(5,5);
                        			rectanglep.setFill(Color.RED);
                        			GridPane.setRowIndex(rectanglep, j+1);
                        			GridPane.setColumnIndex(rectanglep, i-1);
                        	        gridjeu.getChildren().add(rectanglep);
                        	        break;
        						case LOSANGE:
                					Polygon losangep = new Polygon(15,0, 20,5 ,15,10 ,10,5);
                					losangep.setFill(Color.RED);
                					GridPane.setRowIndex(losangep, j+1);
                					GridPane.setColumnIndex(losangep, i);
                					gridjeu.getChildren().add(losangep);
        						case CROIX:
                					Polygon croixp = new Polygon(3.5,0 ,6.5,0 ,6.5,3.5 ,10,3.5 ,10,6.5 ,6.5,6.5 ,6.5,10 ,3.5,10 ,3.5,6.5 ,0,6.5 ,0,6.5 ,3.5,3.5);
                					croixp.setFill(Color.RED);
                					GridPane.setRowIndex(croixp, j);
                					GridPane.setColumnIndex(croixp, i+1);
                					gridjeu.getChildren().add(croixp);
        							break;
        						case TRIANGLE:
                					Polygon trianglep = new Polygon(15,0, 20,10 ,10,10);
                					trianglep.setFill(Color.RED);
                					trianglep.setBlendMode(null);
                					GridPane.setRowIndex(trianglep, j+1);
                					GridPane.setColumnIndex(trianglep, i+1);        					
                					gridjeu.getChildren().add(trianglep);
                					break;
                				case ETOILE:
                					Polygon etoilep = new Polygon(15,0,17,3,20,5,17,7,15,10,13,7,10,5,13,3);
                					etoilep.setFill(Color.RED);
                					GridPane.setRowIndex(etoilep,j-1);
                					GridPane.setColumnIndex(etoilep, i+1);        					
                					gridjeu.getChildren().add(etoilep);
                					break;
                					
        					}
        				}
            			switch (galaxie.getStation(i,j).getType()){
            			// on  affiche les stations en fonction de leurs type
        				case ROND:
        					//afficher station ROND
        					Circle cercle = new Circle();
        					controlstation = new controlbstation(galaxie.getStation(i, j), galaxie);
        					cercle.setRadius(9);
        					cercle.setFill(Color.WHITE);
        					cercle.setOnMouseClicked(controlstation);
        					GridPane.setRowIndex(cercle, j);
        					GridPane.setColumnIndex(cercle, i);
        					gridjeu.getChildren().add(cercle);
        					
        					break;
        				case CARRE:
                			Rectangle rectangle2 = new Rectangle(18,18);
                			controlstation = new controlbstation(galaxie.getStation(i, j), galaxie);
                			rectangle2.setOnMouseClicked(controlstation);
                			rectangle2.setFill(Color.WHITE);
                			GridPane.setRowIndex(rectangle2, j);
                			GridPane.setColumnIndex(rectangle2, i);
                	        gridjeu.getChildren().add(rectangle2);
                			break;
        				case LOSANGE:
        					Polygon losange = new Polygon(10,0, 20,10 ,10,20 ,0,10);
        					controlstation = new controlbstation(galaxie.getStation(i, j), galaxie);
        					losange.setFill(Color.WHITE);
        					losange.setOnMouseClicked(controlstation);
        					GridPane.setRowIndex(losange, j);
        					GridPane.setColumnIndex(losange, i);
        					gridjeu.getChildren().add(losange);
        					break;
        				case CROIX:
        					Polygon croix = new Polygon(7,0 ,13,0 ,13,7 ,20,7 ,20,13 ,13,13 ,13,20 ,7,20 ,7,13 ,0,13 ,0,7 ,7,7);
        					controlstation = new controlbstation(galaxie.getStation(i, j), galaxie);
        					croix.setFill(Color.WHITE);
        					croix.setOnMouseClicked(controlstation);
        					GridPane.setRowIndex(croix, j);
        					GridPane.setColumnIndex(croix, i);
        					gridjeu.getChildren().add(croix);
							
							break;
        				case TRIANGLE:
        					Polygon triangle = new Polygon(10,0, 20,20 ,0,20);
        					controlstation = new controlbstation(galaxie.getStation(i, j), galaxie);
        					triangle.setFill(Color.WHITE);
        					triangle.setOnMouseClicked(controlstation);
        					GridPane.setRowIndex(triangle, j);
        					GridPane.setColumnIndex(triangle, i);        					
        					gridjeu.getChildren().add(triangle);
        					break;
        				case ETOILE:
        					Polygon etoile = new Polygon(0,0 ,7,7 ,10,0 ,13,7 ,20,10 ,13,13 ,10,20 ,7,13 ,0,10 ,7,7);
        					controlstation = new controlbstation(galaxie.getStation(i, j), galaxie);
        					etoile.setFill(Color.WHITE);
        					etoile.setOnMouseClicked(controlstation);
        					GridPane.setRowIndex(etoile, j);
        					GridPane.setColumnIndex(etoile, i);        					
        					gridjeu.getChildren().add(etoile);
        					break;
        					
        			}	

            	}
            	
				
            } 

        }
        
      
}
}





