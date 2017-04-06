//package startrekmini;

import java.util.ArrayList;
import java.util.Vector;
import java.math.*;
//import Galaxie.typeTerrain;

/**
 * Classe pour les diff鲥nts couloir qui compose une ligne
 * (morceau de ligne entre deux stations).
 * Le 31/12/2016
 */
public class Couloir {
	
	/**
	 *L'une des 2 stations ࠬ'extr魩t頤u couloir, dite de d鰡rt.
	 */
	private StationSpatiale stationDepart;
	
	/**
	 * L'une des 2 stations ࠬ'extr魩t頤u couloir, dite d'arriv饮
	 */
	private StationSpatiale stationArrivee;
	
	/**
	 * Liste des Vecteurs composant le trac頤u couloir.
	 */
	private ArrayList<Vecteur> trace;
	private Vecteur changementDirection;
	
	/**
	 * Liste des navettes spatiales pr鳥ntes dans le couloir.
	 */
	private ArrayList<NavetteSpatiale> listeDeNavettes;
	private NavetteSpatiale navetteCouloir;
	
	/**
	 * Liste des vecteur o񠳥 trouve un vortex sue la chemin parcouru
	 * le couloir.
	 */
	//private ArrayList<Vortex> listeVortex;
	
	/**
	 * Constructeur par d馡ut.
	 */
	public Couloir()
	{
		this.stationDepart = new StationSpatiale();
		this.stationArrivee = new StationSpatiale();
		this.trace = new ArrayList<Vecteur>();
		changementDirection = new Vecteur(0,0);
		this.listeDeNavettes = new ArrayList<NavetteSpatiale>();
	}
	
	/**
	 * Constructeur avec param贲es. Le param贲es qu'il re篩t sont une station de d鰡rt,
	 * et une station d'arriv饮Le calcul du trac頳e fait dans ce mꭥ constructeur.
	 * @param depart
	 * @param arrivee
	 */
	public Couloir(StationSpatiale depart, StationSpatiale arrivee)
	{
		this.stationDepart = depart;
		this.stationArrivee = arrivee;
		this.trace = new ArrayList<Vecteur>();
		changementDirection = new Vecteur(0,0);
		this.listeDeNavettes = new ArrayList<NavetteSpatiale>();
		this.navetteCouloir = new NavetteSpatiale();
	}
	
	/**
	 * Constructeur de recopie.
	 * @param c : couloir ࠲ecopier.
	 */
	public Couloir(Couloir c)
	{
		this.stationDepart = c.getStationDepart();
		this.stationArrivee = c.getStationArrivee();
		this.trace = new ArrayList<Vecteur>(c.getTrace());
		this.listeDeNavettes = new ArrayList<NavetteSpatiale>(c.getNavettesPresentes());
		//this.listeVortex = new ArrayList<Vortex>(c.getVortexPresents());
	}

	
	public NavetteSpatiale getNavette(){
		return this.navetteCouloir;
	}
	
	public void retireNavette(){
		this.navetteCouloir = null;
	}
	
	public void recupereNavetteDebut(NavetteSpatiale s){
		this.navetteCouloir = s;
		this.navetteCouloir.getPosition().setX(this.trace.get(0).getX());
		this.navetteCouloir.getPosition().setY(this.trace.get(0).getY());
		//modifier le tracer de la carte
	}
	
	public void recupereNavetteFin(NavetteSpatiale s){
		this.navetteCouloir = s;
		this.navetteCouloir.getPosition().setX(this.trace.get(trace.size()-1).getX());
		this.navetteCouloir.getPosition().setY(this.trace.get(trace.size()-1).getY());
		//modifier le tracer de la carte
	}
	
	public int deplacerUneNavette(Galaxie g){
		if (this.navetteCouloir == null){
			return 0;
		}else{
			boolean vaAuTerminus = this.navetteCouloir.getVaAuTerminus();
			int typeDeplacement = 0; 
			boolean flag = true;
			int i=0;
	    	// 0 -> pas de navette déplacée sur le couloir
	    	// 1 -> navette déplacée en milieu de couloir
	    	// 2 -> navette arrivée en station d'arrivée (dans le sens vaAuTerminus=true)
	    	// 3 -> navette arrivée en station de départ (dans le sens vaAuTerminus=false)
			
			//recherche la position de la navette dans le tracé
			while (i<trace.size() && flag && navetteCouloir!=null){
			
				if ( trace.get(i).equals(this.navetteCouloir.getPosition())){
					g.setPoint(navetteCouloir.getPosition().getX(), navetteCouloir.getPosition().getY(), Galaxie.typeTerrain.EFFACE);
                    g.notifyObserver();
                    g.setPoint(navetteCouloir.getPosition().getX(), navetteCouloir.getPosition().getY(), Galaxie.typeTerrain.COULOIR);
                    g.notifyObserver();
					if (vaAuTerminus){
						//on teste si on arrive à la station de fin du couloir
						if(i == trace.size()-1){
							//ON ARRIVE A LA STATION D'ARRIVEE DU COULOIR
							this.navetteCouloir.setPosition(trace.get(trace.size()-1));
							typeDeplacement = 2; 
						}else
							{
							//sinon on avance d'une position sur le tracé
							this.navetteCouloir.setPosition(trace.get(i+1));
							typeDeplacement = 1;
							}
						flag=false;
					}
					else{
						//on teste si on revient à la station de début de couloir
						if ( i == 0){
							//ON ARRIVE A LA STATION DE DEPART DU COULOIR
							this.navetteCouloir.setPosition(trace.get(0));
							typeDeplacement = 3;
						}else
							{
							//on recule d'une position sur le tracé
							this.navetteCouloir.setPosition(trace.get(i-1));
							typeDeplacement = 1;
							}
						flag=false;
					}
				}
				i++;
			}
			
			return typeDeplacement;
			//il faut mettre à jour le typeTerrain de la carte
		}
		
	}
	
	
	/**
	 * Construit le tracé d'un couloir..
	 */
	public void traceCouloir(Galaxie gal){
		// StationSpatiale StatDep,StationSpatiale StatArr
		//récupération des coordonnées des deux stations spatiales
		int xDep = stationDepart.getPosition().getX();
		int yDep = stationDepart.getPosition().getY();
		int xArr = stationArrivee.getPosition().getX();
		int yArr = stationArrivee.getPosition().getY();
		
		//on calcule les distance à parcourir sur les deux axes.
		int deplX = xArr-xDep;
		int deplY = yArr-yDep;
		
		//on récupère le signe de deplX et deplY, en affectant la valeur des pas sur les deux axes : 1 ou -1.
		int pasX;
		int pasY;		
		if (deplX!=0) {pasX = deplX/Math.abs(deplX);} else {pasX = 0;}; 
        if (deplY!=0) {pasY = deplY/Math.abs(deplY);} else {pasY = 0;};
		
		//initialisation des points de départ
		int posCourX = xDep + pasX;
		int posCourY = yDep + pasY;
		while(posCourX!=xArr & posCourY!=yArr){
			//on se déplace sur les deux directions
			
			ajouteVecteur(posCourX, posCourY);
			posCourX += pasX;
			posCourY += pasY;	
		}
		
		if (posCourX != xArr){
			//on ne se déplace que sur l'axe des X
			
			// STOCKE LES COORDONNEES DU POINT DE CASSURE !!!!!!!!!
			changementDirection.setX(posCourX);
			changementDirection.setY(posCourY);
			
			while(posCourX!=xArr){
				ajouteVecteur(posCourX, posCourY);
				posCourX += pasX;	
			}
		}
		else{	
			if (posCourY != yArr){
				//Ou alors on ne se déplace que sur l'axe des Y
				
				// STOCKE LES COORDONNEES DU POINT DE CASSURE !!!!!!!!!
				changementDirection.setX(posCourX);
				changementDirection.setY(posCourY);
				
				while(posCourY!=yArr){
					ajouteVecteur(posCourX, posCourY);
					posCourY += pasY;
				}
			}	
		}
	
		majCarte(gal);
		
	}
	
	//rajout d'un point au tracé : appélée par traceCouloir
	public void ajouteVecteur(int posCourX, int posCourY){
		Vecteur vect = new Vecteur();
		vect.setX(posCourX);
		vect.setY(posCourY);
		trace.add(vect);
	}
	
	/*
	//on décrémente le nombre de vortex de l'inventaire : appélée par traceCouloir
	public void decrVortxInv(Inventory inv, Vecteur coordVortex, int posCourX, int posCourY){
		inv.setNombreDeVortex(inv.getNombreDeVortex()-1);
		//stockage des coordonnées du VORTEX
		coordVortex = new Vecteur();
		coordVortex.setX(posCourX);
		coordVortex.setY(posCourY);
	}
	*/
	
	//maj de la carte  : appélée par traceCouloir
	public void majCarte(Galaxie gal){
		//Galaxie.typeTerrain[][] carte = gal.getCarte();
		for (Vecteur v : trace){
			gal.setPoint(v.getX(), v.getY(), Galaxie.typeTerrain.COULOIR);
		}
		gal.notifyObserver();
	}
	
	/**
	 * @return la station dite de d鰡rt du couloir.
	 */
	public StationSpatiale getStationDepart()
	{
		return this.stationDepart;
	}
	
	/**
	 * @return la station dite d'arrivee du couloir.
	 */
	public StationSpatiale getStationArrivee()
	{
		return this.stationArrivee;
	}
	
	public void setStationDepart(StationSpatiale s){
		this.stationDepart = s;
	}
	
	public void setStationArrivee(StationSpatiale s){
		this.stationArrivee = s;
	}
	
	/**
	 * @return la liste des navettes pr鳥ntes dans le couloir (s'il y en a).
	 */
	public ArrayList<NavetteSpatiale> getNavettesPresentes()
	{
		return this.listeDeNavettes;
	}
	
	/**
	 * @Ajoute une navette à la liste des navettes presentes dans le couloir (s'il y en a).
	 */
	public void addNavette(NavetteSpatiale n)
	{
		this.listeDeNavettes.add(n);
		this.navetteCouloir = n;
	}
	
	
	/**
	 * @return la liste des vecteurs qui composent le trac頤u couloir. 
	 */
	public ArrayList<Vecteur> getTrace()
	{
		return this.trace;
	}
	
	/**
	 * @return la liste des vortex pr鳥nts dans le couloir (s'il y en a).
	 
	public ArrayList<Vortex> getVortexPresents()
	{
		return this.listeVortex;
	}	
	*/
}

