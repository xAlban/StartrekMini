//package startrekmini;

import java.util.ArrayList;

/**
 * Classe pour les stations spatiales du jeu.
 * V 2.1 le 31/12/2016
 *
 */
public class StationSpatiale {
	
	/**
	 * Type de la station. Correspond � sa forme sur la carte.
	 */
	private TypeStation type;
	private int idStation;
	
	/**
	 * Position de la station sur la carte.
	 */
	private Vecteur position;
	
	/**
	 * Liste des passagers pr�sents dans la station.
	 */
	private ArrayList <Passager> usagers;
	
	/**
	 * Constructeur par d�faut.
	 */
	public StationSpatiale()
	{
		//this.type=TypeStation.ROND;
		this.position = new Vecteur();
		this.usagers = new ArrayList<Passager>();
		int i= (int)(Math.random()*20); //added here
		switch(i)
		{
			case 0:
				this.type = TypeStation.ETOILE;
				break;
			case 1:
				this.type = TypeStation.CROIX;
				break;
			case 2: case 3:
				this.type = TypeStation.LOSANGE;
				break;
			case 4: case 5: case 6:
			case 7:
				this.type = TypeStation.CARRE;
				break;
			case 8: case 9: case 10:
			case 11: case 12: case 13:
				this.type = TypeStation.TRIANGLE;
				break;
			default:
				this.type = TypeStation.ROND;
				break;
		}
	}
	
	/**
	 * Constructeur avec param�tres.
	 * @param nouveauType : type de la station cr��e.
	 * @param nouvellePosition : position ou placer la nouvelle station spatiale.
	 */
	public StationSpatiale(TypeStation nouveauType, Vecteur nouvellePosition)
	{
		this.type = nouveauType;
		this.position = nouvellePosition;
		this.usagers = new ArrayList<Passager>();
	}
	
	/**
	 * Cr�e une station spatiale dont le type est d�fini al�atoirement. Chaque type
	 * de station est associ� � une probabilit� (30% de ronds, 30% de triangles, 20% de carr�s,
	 * 10% de losanges, 5% de croix et 5% d'�toiles).
	 * @param nouvellePosition : position � laquelle est pl�c�e la station.
	 */
	public StationSpatiale(int posX, int posY)
	{
		this.position = new Vecteur(posX,posY);
		this.usagers = new ArrayList<Passager>();
		idStation = (int)Math.abs(Math.random()*1000);
		int i=(int)(Math.random()*20);
		switch(i)
		{
			case 0:
				this.type = TypeStation.ETOILE;
				break;
			case 1:
				this.type = TypeStation.CROIX;
				break;
			case 2: case 3:
				this.type = TypeStation.LOSANGE;
				break;
			case 4: case 5: case 6:
			case 7:
				this.type = TypeStation.CARRE;
				break;
			case 8: case 9: case 10:
			case 11: case 12: case 13:
				this.type = TypeStation.TRIANGLE;
				break;
			default:
				this.type = TypeStation.ROND;
				break;
		}
	}
	
	public int getID(){
		return this.idStation;
	}
	
	/**
	 * @return le type de la Station Spatiale.
	 */
	public TypeStation getType()
	{
		return this.type;
	}
	
	/**
	 * @return la position de la station spatiale sur la carte.
	 */
	public Vecteur getPosition()
	{
		return this.position;
	}
	
	/**
	 * @return la liste des Passager pr�sents dans la station.
	 */
	public ArrayList<Passager> getUsagers()
	{
		return this.usagers;
	}
	
	/**
	 * Ajoute entre 0 et 6 passagers dans la liste "usagers" de la station.
	 * Le nombre ainsi que le type des usagers est d�fini al�atoirement.
	 */
	public void genererUsagers(Galaxie g)
	{
		g.genererUsagers(this);
	}
	
	/**
	 * Ajoute un Passager avec l'un des type pr�sents dans la liste
	 * pass�e en param�tre.
	 * @param t : liste de typeStation (En th�orie ceux des stations 
	 * d�j� pr�sentes sur la carte).
	 */
	public void genererUsagers(ArrayList<TypeStation> t)
	{
		int i = (int)(Math.random()*7);
		for(int j=0; j<i; j++)
		{
			int k = (int)(Math.random()*(t.size()));
			this.usagers.add(new Passager(t.get(k)));
		}
	}
	
	/**
	public static void main(String[] args)
	{
		StationSpatiale s = new StationSpatiale(new Vecteur(3,2));
		ArrayList<TypeStation> t = new ArrayList<TypeStation>();
		t.add(TypeStation.CROIX);
		t.add(TypeStation.LOSANGE);
		t.add(TypeStation.TRIANGLE);
		System.out.println(s.getType());
		s.getPosition().printVecteur();
		s.genererUsagers(t);
		System.out.println(s.getUsagers().size());
		for(int i=0; i<s.getUsagers().size(); i++)
		{
			System.out.println(s.getUsagers().get(i).getDestination());
		}
	}
	*/
}
