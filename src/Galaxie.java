import java.util.*;

public class Galaxie implements Observable{

    enum typeTerrain {
        NORMAL,
        OBSTACLE,
        STATION,
        COULOIR,
        NAVETTE,
        EFFACE,
        GAMEOVER;
    }
    
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
    private static int longueurBase = 45;
    private static int largeurBase = 30;
    private Vector<Vector<typeTerrain>> carte; //Liste 2D contenant le terrain, s'agrandit dynamiquement
    private Map<List<Integer>, StationSpatiale> listeStation;

    private int longueur;
    private int largeur;
    
    private ArrayList<Liaison> listeLiaisons;
    private ArrayList<StationSpatiale> listeStations;
    
    //Score : nombre de passager arriv鳠ࠤestination depuis le d颵t de la partie.
    private int nbPassagersDesservis;
    
    //StationParLignes contient 1 lisaison et une liste de stations
    //C'est la PREMIERE partie de notre table de routage.
    private ArrayList<StationsParLigne>  tableRoutageListeSPL;

    //StationsSurMaLigne contient 1 station d'origine et une liste de stations se trouvant sur la même ligne
    //C'est la DEUXIEME partie de notre table de routage.
    private ArrayList<StationsSurMesLignes> tableRoutageCorrespondances;
    

    public Galaxie(){
        longueur = longueurBase;
        largeur = largeurBase;
        listeLiaisons= new ArrayList<Liaison>();
        listeStations= new ArrayList<StationSpatiale>();
        tableRoutageListeSPL = new ArrayList<StationsParLigne>();
        tableRoutageCorrespondances = new ArrayList<StationsSurMesLignes>();

        carte = new Vector<Vector<typeTerrain>>(); //Initialisation de base, 1er niveau
        for (int i = 0; i < longueurBase; i++) {
            carte.add(new Vector<typeTerrain>()); //Initialisation de base, 2eme niveau
            for (int j = 0; j < largeurBase; j++) {
                carte.get(i).add(typeTerrain.NORMAL); //Remplit de terrain normal
            }
        }

        listeStation = new HashMap<List<Integer>, StationSpatiale>();

        //TODO: Génération des obstacles
        Random rng = new Random();
        int posX = 0, posY = largeur/2;
        int l;
        while (posX < longueur && posY > 0 && posY < largeur){
            l = rng.nextInt(3)-1; //Entre -1 et 1
            carte.get(posX).set(posY, typeTerrain.OBSTACLE);
            if (posY + l > 0 && posY +l < largeur) {
                carte.get(posX).set(posY + l, typeTerrain.OBSTACLE);
            }
            posY += l;
            posX++;
        }


    }

    public int getLongueur() {
        return longueur;
    }

    public int getLargeur() {
        return largeur;
    }

    public typeTerrain getPoint(int posX, int posY){
        //Récupère le point aux coordonnées données
        return carte.get(posX).get(posY);
    }
    
    public void resetCarte(){
        carte = new Vector<Vector<typeTerrain>>(); //Initialisation de base, 1er niveau
        for (int i = 0; i < longueurBase; i++) {
            carte.add(new Vector<typeTerrain>()); //Initialisation de base, 2eme niveau
            for (int j = 0; j < largeurBase; j++) {
                carte.get(i).add(typeTerrain.GAMEOVER); //Remplit de terrain gameover
            }
        }
        notifyObserver();
    }
    public typeTerrain[][] getCarte(){
        typeTerrain[][] tableauCarte = new typeTerrain[longueur][largeur];
        for (int i = 0; i < longueur; i++) {
            //tableauCarte[i] = carte.get(i).toArray(tableauCarte[i]);
            for (int j = 0; j < largeur; j++) {
                tableauCarte[i][j] = carte.get(i).get(j);
            }
        }
        return tableauCarte;
    }

    public void setPoint(int posX, int posY, typeTerrain type){
        //Modifie le point aux coordonnées données
        //Private car les classes externes ne sont pas supposées modifier la carte directement
        carte.get(posX).set(posY, type);
    }
    
    
    public void agrandir(int quantiteX, int quantiteY){
        if (quantiteX < 0 || quantiteY < 0) {
            return;
        }
        //Agrandit les lignes existantes
        for (int i = 0; i < longueur; i++) {
            for (int j = 0; j < quantiteY; j++) {
                carte.get(i).add(typeTerrain.NORMAL);
            }
        }

        //Créé de nouvelles lignes agrandies
        for (int i = 0; i < quantiteX; i++) {
            carte.add(new Vector<typeTerrain>());
            for (int j = 0; j < largeur + quantiteY; j++) {
                int ligne = j + longueur;
                carte.get(ligne).add(typeTerrain.NORMAL);
            }
        }

        //Configure les variables de longueur et largeur
        longueur += quantiteX;
        largeur += quantiteY;
    }

    public int[] placerStation(){
        Random rng = new Random(); //Générateur de nombre aléatoire
        boolean place = false;
        int posX = 0;
        int posY = 0;
        
        while (!place){
        	//Obtient 2 entiers pour les coordonnées
        	posX = rng.nextInt(longueur);
        	posY = rng.nextInt(largeur);

        	//Vérifie que le terrain est inutilisé pour pouvoir placer la station
        	if (getPoint(posX, posY) == typeTerrain.NORMAL
        			&& (posX-1 < 0 || getPoint(posX-1, posY) == typeTerrain.NORMAL)
        			&& (posX+1 >= longueur || getPoint(posX+1, posY) == typeTerrain.NORMAL)
        			&& (posY-1 < 0 || getPoint(posX, posY-1) == typeTerrain.NORMAL)
        			&& (posY+1 >= largeur || getPoint(posX, posY+1) == typeTerrain.NORMAL)
        			&& posX != 0
        			&& posY != 0){
        		setPoint(posX, posY, typeTerrain.STATION);
        		place = true;
        	}
        }
        List<Integer> coords = new ArrayList<Integer>();
        coords.add(posX);
        coords.add(posY);
        listeStation.put(coords, new StationSpatiale(posX, posY));
        listeStations.add(getStation(posX,posY));

        notifyObserver();
        return new int[]{posX, posY};
    }
    public ArrayList<StationSpatiale> getListeStations(){
    	return this.listeStations;
    }
    public StationSpatiale getStation(int posX, int posY){
        List<Integer> coords = new ArrayList<Integer>();
        coords.add(posX);
        coords.add(posY);

        if (! listeStation.containsKey(coords)){
            return null;
        }

        return listeStation.get(coords);
    }

    public StationSpatiale getStation(int[] pos){
        if (pos.length < 2){
            throw new IllegalArgumentException();
        }
        return getStation(pos[0], pos[1]);
    }
    
    
    //Fonction de pilotage des deplacements des Navettes
    public void deplacerLesNavettes(){
    	int typeDeplacement = 0; 
    	// 0 -> pas de navette deplacee sur le couloir
    	// 1 -> navette deplacee en milieu de couloir
    	// 2 -> navette arrivee en station d'arrivee (dans le sens vaAuTerminus=true)
    	// 3 -> navette arrivee en station de depart (dans le sens vaAuTerminus=false)
    	int i=0,j;
    	
    	//On part du principe qu'il n'y a qu'une seule Navette par LIGNE. Donc des qu'on en 
    	//a deplacee une, on arrete le traitement....

    	boolean flagCouloir = true;
    	
    	//on parcours la liste des liaisons de la carte
    	while (i<listeLiaisons.size()){
    		//on parcourt les couloirs de la liaison pour deplacer la navette
    		j=0;
    		while (j<listeLiaisons.get(i).getItineraire().size() && flagCouloir){
    			
    			typeDeplacement=listeLiaisons.get(i).getItineraire().get(j).deplacerUneNavette(this);
    			
    			if (typeDeplacement == 2){
    				//LA NAVETTE EST ARRIVEE A UNE STATION d'arrivee
    				this.dechargerPassagers(listeLiaisons.get(i).getNavettesPresentes().get(0), listeLiaisons.get(i).getItineraire().get(j).getStationArrivee());
    				this.chargerPassagers(listeLiaisons.get(i).getNavettesPresentes().get(0), listeLiaisons.get(i).getItineraire().get(j).getStationArrivee());
    				listeLiaisons.get(i).getItineraire().get(j).getStationArrivee();
    				//ET ON LA TRANSMETS AU COULOIR SUIVANT OU ON FAIT DEMI-TOUR
    				if(j<listeLiaisons.get(i).getItineraire().size()-1){
    					//COULOIR SUIVANT
    					listeLiaisons.get(i).passeCouloirSuivant(listeLiaisons.get(i).getItineraire().get(j),listeLiaisons.get(i).getItineraire().get(j+1));
    					this.setPoint(listeLiaisons.get(i).getNavettesPresentes().get(0).getPosition().getX(), listeLiaisons.get(i).getNavettesPresentes().get(0).getPosition().getY(), typeTerrain.NAVETTE);
    					this.setPoint(listeLiaisons.get(i).getItineraire().get(j).getTrace().get(0).getX(),listeLiaisons.get(i).getItineraire().get(j).getTrace().get(0).getY(), typeTerrain.COULOIR);
    					System.out.println("placer type navette");
    					notifyObserver();
    				}
    				else{
    					//ON FAIT DEMI-TOUR
    					listeLiaisons.get(i).demiTour(listeLiaisons.get(i).getItineraire().get(j));
    					this.setPoint(listeLiaisons.get(i).getNavettesPresentes().get(0).getPosition().getX(), listeLiaisons.get(i).getNavettesPresentes().get(0).getPosition().getY(), typeTerrain.NAVETTE);
    					this.setPoint(listeLiaisons.get(i).getItineraire().get(j).getTrace().get(0).getX(),listeLiaisons.get(i).getItineraire().get(j).getTrace().get(0).getY(), typeTerrain.COULOIR);
    					System.out.println("placer type navette");
    					notifyObserver();
    				}
    				//ON STOPPE LE TRAITEMENT
    		    	flagCouloir = false;
    			}
    			else
    				{
    				if (typeDeplacement == 3){
        				//LA NAVETTE EST ARRIVEE A UNE STATION de depart
        				//ON DECHARGE ET CHARGE LES PASSAGERS
        				this.dechargerPassagers(listeLiaisons.get(i).getNavettesPresentes().get(0), listeLiaisons.get(i).getItineraire().get(j).getStationArrivee());
        				this.chargerPassagers(listeLiaisons.get(i).getNavettesPresentes().get(0), listeLiaisons.get(i).getItineraire().get(j).getStationArrivee());
        				//ET ON LA TRANSMETS AU COULOIR PRECEDENT OU ON FAIT DEMI-TOUR
    					if (j>0){
    						//COULOIR PRECEDENT
    						listeLiaisons.get(i).passeCouloirPrecedent(listeLiaisons.get(i).getItineraire().get(j-1),listeLiaisons.get(i).getItineraire().get(j));
    						this.setPoint(listeLiaisons.get(i).getNavettesPresentes().get(0).getPosition().getX(), listeLiaisons.get(i).getNavettesPresentes().get(0).getPosition().getY(), typeTerrain.NAVETTE);
    						System.out.println("placer type navette");
    						notifyObserver();
    					}
    					else{
    						//DEMI-TOUR
    						listeLiaisons.get(i).demiTour(listeLiaisons.get(i).getItineraire().get(j));
    						this.setPoint(listeLiaisons.get(i).getNavettesPresentes().get(0).getPosition().getX(), listeLiaisons.get(i).getNavettesPresentes().get(0).getPosition().getY(), typeTerrain.NAVETTE);
    						System.out.println("placer type navette");
    						notifyObserver();
    					}
    					
        		    	flagCouloir = false;
        				}else
    						{
        					if(typeDeplacement == 1){
        						//la navette est déplacée le long d'un couloir, pas la peine de continuer 
        						this.setPoint(listeLiaisons.get(i).getNavettesPresentes().get(0).getPosition().getX(), listeLiaisons.get(i).getNavettesPresentes().get(0).getPosition().getY(), typeTerrain.NAVETTE);
        				    	flagCouloir = false;
        					}
    					}
    				}
    			j++;
    				
    		}
    		flagCouloir = true;
    		i++;
    	}
    	notifyObserver();
    }
    
    
    
    
  //construction de la première partie de la table de routage : la liste des stations pour chaque ligne
    public void creeTableRoutageListeSPL(){
    
    	//La table est reconstruite entièrement à chaque fois, donc on la vide à chaque fois
    	tableRoutageListeSPL.clear();
    	
    	for ( Liaison l : listeLiaisons){
    		
    		//StationParLignes contient 1 lisaison et une liste de station 
        	StationsParLigne STPL = new StationsParLigne(l);
    		ArrayList<Couloir> listeCouloirs = l.getItineraire();
    		
    		STPL.lstStat.add(listeCouloirs.get(0).getStationDepart());
    		
    		for (Couloir c : listeCouloirs){STPL.lstStat.add(c.getStationArrivee());}

    		tableRoutageListeSPL.add(STPL);	
    	}
    	
    }
    
    //méthode de test de la table de routage
    public void test1ereTable(){
    	int i = 1;
    	System.out.println("Nombre de laisons dans la galaxie : " + listeLiaisons.size());
    	System.out.println("Nombre de STPL dans la table : " + tableRoutageListeSPL.size());
    	for(StationsParLigne STPL: tableRoutageListeSPL){
    		for (StationSpatiale s : STPL.lstStat){
    		System.out.println("Ligne  : " + i + " - " + s.getType() + " id = " + s.getID());
    		}
    		i++;
    		System.out.println("--------");
    	}
    }
    
    public void creeTableRoutageCorrespondances(){
    	
    	//La table est reconstruite entièrement à chaque fois, donc on la vide à chaque fois
    	tableRoutageCorrespondances.clear();

    	//L'algorithme part de la liste des stations spatiales de la galaxie.
    	for (StationSpatiale s : listeStations){
    		
    		//pour chaque station spatiale dans la galaxie, on crée un objet de type StationSurMesLignes
    		//cette objet contient la station elle-même en station d'origine
    		StationsSurMesLignes SSML = new StationsSurMesLignes(s);
    		
    		//on va ensuite chercher ttes les stations se trouvant sur les mêmes lignes qu'elle
    		//que l'on rajoutera dans listStat de l'objet StationSurMesLignes
    		//pour cela on va se servir de la première table de routage précédemment construite, contenant la liste des lignes et leurs stations
    		rechercheStationsSurMesLignes(SSML);
    		
    		tableRoutageCorrespondances.add(SSML);
    	}
    	
    }
    
    //recherche de station dans la table de routage Stations Par ligne.
    public void rechercheStationsSurMesLignes(StationsSurMesLignes SSML){
    	
    	//contiendra la liste des stations trouvées sur les mêmes lignes que la station d'origine.
    	ArrayList<StationSpatiale> lstStatTrouvees = new ArrayList<StationSpatiale>();
    	boolean recupListe=false;
    	
    	for (StationsParLigne STPL : tableRoutageListeSPL){
    		ArrayList<StationSpatiale> listStat = STPL.getListeStations();
    		for (StationSpatiale s : listStat){
    			//on teste si la station en cours d'évaluation  se trouve sur cette ligne
    			if (s == SSML.getStationOrigine()){
    				//la station se trouve sur la ligne, donc il faut récupérer toutes les stations de la liste.
    				recupListe = true;
    				}
    			}
    		if (recupListe){
				for (StationSpatiale statTrouvee : listStat){
					if(SSML.getStationOrigine() != statTrouvee) lstStatTrouvees.add(statTrouvee);
				}
    		}
    		recupListe = false;
    		//boucle pour supprimer les occurrences qui contiennent la station d'origine elle-même
    		/**
    		for (StationSpatiale s : lstStatTrouvees){
    			if (s != SSML.getStationOrigine()){
    				lstStatTrouveesDedoubl.add(s);
    			}
    			else{
    				System.out.println("Station dédoubloonée !");
    			}
    		}
    		*/
    		//listStat.clear();
    	}
    	// On ajoute la liste des stations qui sont sur les mêmes lignes que la station d'origine
    	SSML.setDestPossibles(lstStatTrouvees);
    }
    
    
    void test2iemeTable(){
    	for ( StationsSurMesLignes SSML: tableRoutageCorrespondances) {
    		System.out.println("Station Origine : " + SSML.getStationOrigine().getType()+ " id : " +SSML.getStationOrigine().getID());
    		for (StationSpatiale s : SSML.getDestPossibles()){
    			System.out.println("Station dest : " + s.getType()+ " id : " +s.getID());
    		}
    		System.out.println("----------------");
    	}
    }
    
    
    //Fonction appelée par la classe ContrôleJeu, lorsque deux stations ont été sélectionnées pour être reliées.
    public void rechercheLiaisonDesStations(StationSpatiale s1, StationSpatiale s2){
    	Couloir nouveauCouloir = new Couloir();
    	boolean nouvelleLigne = true;
    	
    	/* TROIS CAS DE FIGURE MAJEURS : UNE DES DEUX STATIONS EST LA PREMIERE STATION DE LA LIGNE
    	 * OU LA DERNIERE. DANS CES CAS ON DOIT AGRANDIR LA LIGNE ET Y AJOUTER LE NOUVEAU COULOIR, SOIT AU 
    	 * DEBUT SOIT A LA FIN DE LA LISTE DES COULOIRS.
    	 * SINON ON CREE UNE NOUVELLE LIGNE AVEC UN SEUL COULOIR
    	 */
    	
    	//On récupère le premier couloir, pour tester si s1 ou s2 est la première station de la ligne
    	if (listeLiaisons.size()>0) {
    		
    		//recopie de la liste des liaisons
    		ArrayList<Liaison> listeL = new ArrayList<Liaison>();
    		for (Liaison l : listeLiaisons) {listeL.add(l);}
    		System.out.println("Taille de Liste L : " + listeL.size());
    		for (Liaison l : listeL){
    		System.out.println("je passe dans le for");
    		Couloir c = l.getItineraire().get(0);
    		if(c.getStationDepart() == s1 ) {
    			//s1 sera la station d'arrivée du nouveau couloir
    			//le nouveau couloir devra être ajouté au début de la liste des couloirs de la liaison
    			nouveauCouloir.setStationDepart(s2);
    			nouveauCouloir.setStationArrivee(s1);
    			nouveauCouloir.traceCouloir(this);
    			//Ajout du couloir rn debut Debut de ligne;
    			l.ajouteCouloirDebut(nouveauCouloir);
    			nouvelleLigne = false;
    		}
    		else{ 
    			if (c.getStationDepart() == s2 ) {
    				//s2 sera la station d'arrivée du nouveau couloir
    				//le nouveau couloir devra être ajouté au début de la liste des couloirs de la laison
    				nouveauCouloir.setStationDepart(s1);
    				nouveauCouloir.setStationArrivee(s2);
    				nouveauCouloir.traceCouloir(this);
    				//Ajout du couloir rn debut Debut de ligne;
    				l.ajouteCouloirDebut(nouveauCouloir);
    				nouvelleLigne = false;

    			}
    			else{
    	
    				//On récupère le dernier couloir, pour tester si s1 ou s2 est la dernière station de la ligne
    				//Liaison drLiaison = listeLiaisons.get(listeLiaisons.size()-1);
    				int lg = l.getItineraire().size();
    				c = l.getItineraire().get(lg-1);
    			
    				if(c.getStationArrivee() == s1 ) {
    					//s1 sera la station de départ du nouveau couloir
    					//le nouveau couloir devra être ajouté à la fin de la liste des couloirs de la laison
    					nouveauCouloir.setStationDepart(s1);
    					nouveauCouloir.setStationArrivee(s2);
    					nouveauCouloir.traceCouloir(this);
    					l.ajouteCouloirFin(nouveauCouloir);
    					nouvelleLigne = false;
    				}
    				else{ 
    					if (c.getStationArrivee() == s2 ) {
    						//s2 sera la station dde départ du nouveau couloir
    						//le nouveau couloir devra être ajouté à la fin de la liste des couloirs de la laison
    						nouveauCouloir.setStationDepart(s2);
    						nouveauCouloir.setStationArrivee(s1);
    						nouveauCouloir.traceCouloir(this);
    						l.ajouteCouloirFin(nouveauCouloir);
    						nouvelleLigne = false;
    					}
    				}
    			}
    		}
    		}//fin de la boucle for
    		//
    		if (nouvelleLigne){
    			Liaison L = new Liaison();
    			nouveauCouloir.setStationDepart(s1);
        		nouveauCouloir.setStationArrivee(s2);
        		nouveauCouloir.traceCouloir(this);
    			L.ajouteCouloirFin(nouveauCouloir);
    			//on ajoute la nouvele liaison à la liste des liaisons de la galaxie
    			listeLiaisons.add(L);
    			nouveauCouloir.addNavette(new NavetteSpatiale());
    			Vecteur pos = new Vecteur();    			
    			pos = nouveauCouloir.getTrace().get(0);
    			nouveauCouloir.getNavette().setPosition(pos);
    			this.setPoint(nouveauCouloir.getTrace().get(0).getX(),nouveauCouloir.getTrace().get(0).getY(),typeTerrain.NAVETTE);
    			notifyObserver();
    		}
    	}
    	else{
			Liaison L = new Liaison();
			nouveauCouloir.setStationDepart(s1);
    		nouveauCouloir.setStationArrivee(s2);
    		nouveauCouloir.traceCouloir(this);
			L.ajouteCouloirFin(nouveauCouloir);
			//on ajoute la nouvele liaison à la liste des liaisons de la galaxie
			listeLiaisons.add(L);
			nouveauCouloir.addNavette(new NavetteSpatiale());
			Vecteur pos = new Vecteur();    			
			pos = nouveauCouloir.getTrace().get(0);
			nouveauCouloir.getNavette().setPosition(pos);
			this.setPoint(nouveauCouloir.getTrace().get(0).getX(),nouveauCouloir.getTrace().get(0).getY(),typeTerrain.NAVETTE);
			notifyObserver();
    	}
    	
    }
    
    /**
     * Fonction pour le d飨argement des passager d'une navette. Elle v鲩fie qu'elle peut l'amener
     * ࠤestination, ou au moins ࠵ne correspondance, avant d'effectuer le transfert de passager
     * de la station ࠬa navette, si la navette n'est pas pleine. 
     * @param n : navette ࠣharger.
     * @param s : station spatiale desservie.
     */
    public void chargerPassagers(NavetteSpatiale n, StationSpatiale s)
    {
    	int i = 0;
    	int k = 0;
    	boolean prendTON = false;
    	boolean estDesservie = false;
    	boolean liaisonTrouvee = false;
    	StationsParLigne liaisonQuiDessert;
    	StationsSurMesLignes station;
    	station = this.chercherStation(s);
    	//On balaye tous les usagers de la station tant que la navette n'est pas pleine.
    	while(i<s.getUsagers().size() && n.estPleine()==false)
    	{
			//On verifie ensuite toutes les destinations possibles a�partir de s.
			while(k<station.getDestPossibles().size())
			{
				//S'il y en a une qui correspond a�la destination du passager, il peut �tre pris dans une navette. 
				if(station.getDestPossibles().get(k).getType() == s.getUsagers().get(i).getDestination())
				{
					prendTON = true;
					switch(s.getUsagers().get(i).getDestination()){
					case ROND:
						this.setPoint(s.getPosition().getX()-1, s.getPosition().getY(), typeTerrain.EFFACE);
						break;
					case CARRE:
						this.setPoint(s.getPosition().getX()-1, s.getPosition().getY()+1, typeTerrain.EFFACE);
						break;
					case LOSANGE:
						this.setPoint(s.getPosition().getX(), s.getPosition().getY()+1, typeTerrain.EFFACE);
						break;
					case CROIX:
						this.setPoint(s.getPosition().getX(), s.getPosition().getY()+1, typeTerrain.EFFACE);
						break;
					case TRIANGLE:
						this.setPoint(s.getPosition().getX()+1, s.getPosition().getY(), typeTerrain.EFFACE);
						break;
					case ETOILE:
						this.setPoint(s.getPosition().getX()+1, s.getPosition().getY()+1, typeTerrain.EFFACE);
						break;
					}
					notifyObserver();
				}
				k++;
			}
			//Mais si la ligne de la navette ne dessert pas directement cette ligne
			//et qu'une autre ligne passant par la station si, le passager sera alors laiss� a l'autre ligne.
			
			//On verifie dans une premier temps si la ligne de la navette dessert la destination du passager.
			k=0;
			//On cherche la ligne de la navette parmi les lignes de la Galaxie.
			while(k<this.listeLiaisons.size() && liaisonTrouvee == false)
			{ 
				//On regarde si c'est la bonne ligne
				if(this.listeLiaisons.get(k).getNavettesPresentes().contains(n))
				{
					//Si oui, on aura pas besoin  de rapasser dans le while.
					liaisonTrouvee = true;
					//On regarde si elle dessert le type que veut le passager.
					if(this.estDesservi(s.getUsagers().get(i).getDestination(), this.listeLiaisons.get(k)))
					{
						estDesservie = true;	//Si oui on passe estDesservie � true.
					}	
				}
				k++;
			}	
			//Si ce n'est pas le cas on regarde les autres lignes desservant la station.
			if(estDesservie = false)				
			{
				k=0;
				//pour toutes les liaisons de la carte
				while(k<this.listeLiaisons.size() && prendTON == true)
				{
					liaisonQuiDessert = this.chercherLigne(this.listeLiaisons.get(k));
					//Si elle dessert la station o� l'on se trouve
					if(liaisonQuiDessert.getListeStations().contains(s))
					{
						//Et si cette ligne dessert �galement le type de station voulu par le passager.
						if(this.estDesservi(s.getUsagers().get(i).getDestination(), liaisonQuiDessert.getLiaison()))
						{
							//Alors on ne prendra pas ce passager pour le laisser � cette ligne.
							prendTON = false;
						}
					}
					k++;
				}
			}	
			//Une fois que l'on a trait� toutes les correspondances possibles, on teste la valeur de prendTON.
			if(prendTON == true)
			{	
				switch(s.getUsagers().get(i).getDestination()){
				case ROND:
					this.setPoint(s.getPosition().getX()-1, s.getPosition().getY(), typeTerrain.EFFACE);
					break;
				case CARRE:
					this.setPoint(s.getPosition().getX()-1, s.getPosition().getY()+1, typeTerrain.EFFACE);
					break;
				case LOSANGE:
					this.setPoint(s.getPosition().getX(), s.getPosition().getY()+1, typeTerrain.EFFACE);
					break;
				case CROIX:
					this.setPoint(s.getPosition().getX(), s.getPosition().getY()+1, typeTerrain.EFFACE);
					break;
				case TRIANGLE:
					this.setPoint(s.getPosition().getX()+1, s.getPosition().getY(), typeTerrain.EFFACE);
					break;
				case ETOILE:
					this.setPoint(s.getPosition().getX()+1, s.getPosition().getY()+1, typeTerrain.EFFACE);
					break;
				}
				notifyObserver();
				n.chargerPassager(s.getUsagers().get(i));
				s.getUsagers().remove(i);
			}
			i++;
    	}
    }
    
    public void dechargerPassagers(NavetteSpatiale n, StationSpatiale s)
    {
    	int i=0, j=0;
    	boolean ligneTrouvee = false;
    	boolean stationTrouvee = false;
    	StationsParLigne ligneTable = null;
    	StationsSurMesLignes station;
    	for(Module m : n.getRame())	//Pour tous les modules de la navette.
    	{
    		for(Passager p : m.getPassagers())	//pour tous les passagers de ce module.
    		{
    			if(s.getType()==p.getDestination())	//On regarde si le type de la station et la destination du passager correspodent
    			{
    				n.dechargerPassager(p);	//Si oui, on d飨arge le passager.
    				this.nbPassagersDesservis++;
    			}
    			else	//Sinon
    			{
    				//On cherche la ligne su laquelle est la la navette.
    				while(i<this.listeLiaisons.size() && ligneTrouvee == false)
    				{
    					if(this.listeLiaisons.get(i).getNavettesPresentes().contains(n))
    					{
    						ligneTable = this.chercherLigne(this.listeLiaisons.get(i));
    						ligneTrouvee = true;
    					}
    					i++;
    				}    				
    				i=0;
    				//Puis on v鲩fie s'il existe une station avec un type correspondant ࠬa destination du passager.
    				//On regarde toutes les station desservies par la ligne jusqu'ࠣe que l'on en trouve une. (Auquel cas on gardera le passager dans cette Navette))
    				while(i<ligneTable.getListeStations().size() && stationTrouvee == false)
    				{
    					if(ligneTable.getListeStations().get(i).getType() == p.getDestination())
    					{
    						stationTrouvee = true;
    					}
    					i++;
    				}
    				//Si l'on en trouve pas.
    				if(stationTrouvee==false)
    				{
    					//On v鲩fie que cette station a des correspondances avec une station de type voulu.
    					station = this.chercherStation(s);
    					if(station.getDestPossibles().contains(s))
    					{
    						//Si oui on d飨arge les passagers.
    						s.getUsagers().add(p);
    						n.dechargerPassager(p);
    					}
    				}
    			}
    		}
    	}
    }
    
    public boolean estDesservi(TypeStation t, Liaison l)
    {
    	int i=0;
    	boolean stationTrouvee = false;
    	StationsParLigne stations;
    	stations = this.chercherLigne(l);
    	while(i<stations.getListeStations().size() && stationTrouvee == false)
    	{
    		if(stations.getListeStations().get(i).getType() == t)
    		{
    			stationTrouvee = true;
    		}
    		i++;
    	}
    	if(stationTrouvee == true)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    /**
     * Fonction qui renvoie la StationsSurMesLigne correspondante dans la table tableRoutageCorrespondances
     * pour une StationSpatiale pass饠en param贲e.
     * @param s : StationSpatiale ࠴rouver dans la table.
     * @return la StationsSurMesLignes correspondante dans tableRoutageCorrespondances.
     */
    public StationsSurMesLignes chercherStation(StationSpatiale s)
    {
    	int k = 0;
    	boolean stationTrouvee = false;
    	StationsSurMesLignes station = null;
    	//On cherche la station voulue dans la table des correspondances.
    	//On parcours la liste de la table tant que l'on a pas de station correspondante.
    	while(k<this.tableRoutageCorrespondances.size() && stationTrouvee==false)
    	{
    		//Si on trouve la station dans la table.
    		if(s.equals(tableRoutageCorrespondances.get(k).getStationOrigine()))
    		{
    			//On stocke le r鳵ltat dans station.
    			station=this.tableRoutageCorrespondances.get(k);
    			stationTrouvee = true;
    		}
    		k++;
    	}
    	if(stationTrouvee==false)
    	{
    		System.err.println("Station introuvable dans dans la table des correspondaces.");
    	}
    	return station;	//on retourne la StationsSurMesLignes trouvee.
    }
    
    /**
     * Fonction qui renvoie la StationsParLigne correspondante dans la table tableRoutageListeSPL
     * pour une liaison pass饠en param贲e.
     * @param l : Liaison ࠴rouver dans la table.
     * @return la StationsParLigne correspondante dans la table tableRoutageListeSPL.
     */
    public StationsParLigne chercherLigne(Liaison l)
    {
    	int i=0;
    	boolean ligneTrouvee = false;
    	StationsParLigne liaison = null;
    	//On cherche la liaison dans la table de Routage des Lignes.
    	//On parcours la liste de la table tant que l'on a pas de correspondance.
    	while(i<this.tableRoutageListeSPL.size() && ligneTrouvee == false)
		{
    		//Si on trouve la liaison dans la table.
			if(this.tableRoutageListeSPL.get(i).getLiaison().equals(l))
			{
				//On r飵p貥 la StationParLigne correspondante.
				liaison = this.tableRoutageListeSPL.get(i);
				ligneTrouvee = true;	//On arr괥 le while.
			}
			i++;
		}
    	if(ligneTrouvee==false)
    	{
    		System.err.println("Ligne introuvable dans la table de routage des lignes.");
    	}
    	return liaison;	//On la retourne.
    }   
	public void genererUsagers(StationSpatiale s)
	{
		int i = (int)(Math.random()*2);
		for(int j=0; j<i; j++)
		{
			s.getUsagers().add(new Passager());
			notifyObserver();
		}
	}
    
	public void addObserver(Observer obs){
		this.listObserver.add(obs);	
	}

	public void notifyObserver(){
		for(Observer obs : listObserver){
			obs.update();
		}
	}
	
	public void removeObserver(Observer obs){
		listObserver = new ArrayList<Observer>();
	}

}
