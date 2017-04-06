//package startrekmini;

import java.util.ArrayList;

/**
 * Classe pour les liaison (lignes) qui composent le r鳥au
 * de transport spatiale.
 * Le 07/01/2017
 */
public class Liaison {
	
	/**
	 * Couleur de la liaison sur la carte.
	 */
	private CouleursLignes couleur;
	
	/**
	 * Ensemble des couloirs constituant la liaison.
	 */
	private ArrayList <Couloir> listeCouloirs;
	
	/**
	 * Constructeur par defaut.
	 */
	public Liaison()
	{
		this.couleur = CouleursLignes.ROUGE;
		this.listeCouloirs = new ArrayList<Couloir>();
	}
	
	/**
	 * Constructeur prenant en param�tre la couleur de la ligne � construire
	 * ainsi qu'un couloir (Une ligne ne peut pas exister sans au moins un
	 * couloir).
	 * @param couleur : couleur de la ligne a creer.
	 * @param couloir : premier couloir formant la ligne.
	 */
	public Liaison(CouleursLignes couleur)
	{
		this.couleur = couleur;
		this.listeCouloirs = new ArrayList<Couloir>();
	}
	
	/**
	 * Constructeur de recopie.
	 * @param l : liaison a recopier.
	 */
	public Liaison(Liaison l)
	{
		this.couleur = l.couleur;
		this.listeCouloirs = new ArrayList<Couloir>(l.listeCouloirs);
	}
	
	/**
	 * @return la couleur de la liaison.
	 */
	public CouleursLignes getCouleur()
	{
		return this.couleur;
	}
	
	/**
	 * @return la liste des couloirs composant la ligne. 
	 */
	public ArrayList<Couloir> getItineraire()
	{
		return this.listeCouloirs;
	}
	
	
	public void ajouteCouloirFin(Couloir c){
		listeCouloirs.add(c);
	}
	
	public void ajouteCouloirDebut(Couloir c){
		listeCouloirs.add(0,c);
	}
	
	
	/**
	 * Fait faire demi-tour a la NavetteSpatiale passee en parametre.
	 * @param n : navette a laquelle il faut faire faire demi-tour.
	 */
	public void demiTour(Couloir c)
	{
		if (c.getNavette().getVaAuTerminus() == true){
			c.getNavette().getPosition().setX(c.getTrace().get(c.getTrace().size()-1).getX());
			c.getNavette().getPosition().setY(c.getTrace().get(c.getTrace().size()-1).getY());
		}else
			{
			c.getNavette().getPosition().setX(c.getTrace().get(0).getX());
			c.getNavette().getPosition().setY(c.getTrace().get(0).getY());
			}
		c.getNavette().changerSensCirculation();
	}
	
	
	
	
	
	/**
	 * Passe une navette spatiale d'un couloir donnee au couloir suivant,
	 * si celui-ci existe, sinon elle lui fait faire demi-tour.
	 * @param n : navette spaciale qui arrive au bout du couloir.
	 * @param c : couloir ou se trouve actuellement la navette.
	 */
	
	public void passeCouloirSuivant(Couloir c1, Couloir c2){
		c2.recupereNavetteDebut(c1.getNavette());
		c1.retireNavette();	
	}
	
	public void passeCouloirPrecedent(Couloir c1, Couloir c2){
		c1.recupereNavetteFin(c2.getNavette());
		c2.retireNavette();	
	}
	
	
	/**
	 * Retourne une liste des navettes spatiales pr鳥ntes sur la ligne.
	 * @return : liste des NavetteSpatialesPr鳥ntes sur la Liaison.
	 */
	public ArrayList<NavetteSpatiale> getNavettesPresentes()
	{
		ArrayList<NavetteSpatiale> listeNavettes = new ArrayList<NavetteSpatiale>();
		for(Couloir c : this.getItineraire())
		{
			listeNavettes.addAll(c.getNavettesPresentes());
		}
		return listeNavettes;
	}
}
