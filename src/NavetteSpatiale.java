//package startrekmini;

import java.util.ArrayList;

/**
 * Classe pour les navettes spatiales.
 * Le 30/12/2016
 *
 */
public class NavetteSpatiale {
	
	/**
	 * Sens de circulation de la rame :
	 * true : du d鰡rt vers le terminus
	 * false : du terminus vers le d鰡rt.
	 */
	private boolean vaAuTeminus;
	
	/**
	 * Ensemble des modules tract鳠par la Navette.
	 */
	private ArrayList <Module> rame;
	
	/**
	 * Position de la Navette sur la carte.
	 */
	private Vecteur position;
	
	/**
	 * Constructeur par d馡ut.
	 */
	public NavetteSpatiale()
	{
		this.position = new Vecteur();
		this.rame = new ArrayList<Module>();
		this.rame.add(new Module());
		this.vaAuTeminus = true;
	}
	
	/**
	 * Constructeur avec position pass饠en param贲e.
	 * @param nouvellePosition : Position de la nouvelle navette spatiale.
	 */
	public NavetteSpatiale(Vecteur nouvellePosition)
	{
		this.position = nouvellePosition;
		this.vaAuTeminus = true;
		this.rame = new ArrayList<Module>();
		this.rame.add(new Module());
	}
	
	/**
	 * Constructeur de recopie.
	 * @param n : navette ࠲ecopier.
	 */
	public NavetteSpatiale(NavetteSpatiale n)
	{
		this.position = n.getPosition();
		this.vaAuTeminus = n.vaTElleAuTerminus();
		this.rame = new ArrayList<Module>(n.getRame());
	}
	
	/**
	 * @return la liste de module de la navette.
	 */
	public ArrayList<Module> getRame()
	{
		return this.rame;
	}
	
	/**
	 * @return le vecteur position de la navette.
	 */
	public Vecteur getPosition(){
		return this.position;
	}
	
	/**
	 * @return vrai ou faux en fonction de la valeur de vaAuTerminus.
	 */
	public boolean vaTElleAuTerminus()
	{
		return this.vaAuTeminus;
	}
	
	/**
	 * Change la position de la navette.
	 * @param v : nouvelle position.
	 */
	public void setPosition(Vecteur v)
	{
		this.position = v;
	}
	
	
	public boolean getVaAuTerminus(){
		return this.vaAuTeminus;
	}
	
	/**
	 * Change la valeur du sens de circulation (remplace par false si true et inversement).
	 */
	public void changerSensCirculation()
	{
		if(this.vaAuTeminus == true)
		{
			this.vaAuTeminus = false;
		}
		else
		{
			this.vaAuTeminus = true;
		}
	}
	
	/**
	 * Ajoute un passager dans l'un des modules.
	 * @param p : passager ࠡjouter.
	 */
	public void chargerPassager(Passager p)
	{
		for(int i=0; i<this.rame.size(); i++)
		{
			if(this.rame.get(i).estComplet()==false)
			{
				this.rame.get(i).getPassagers().add(p);
			}
		}
	}
	
	/**
	 * Supprime un passager de l'un des module.
	 * @param p ; passager ࠳upprimer.
	 */
	public void dechargerPassager(Passager p)
	{
		int i=0;
		boolean estDecharge = false;
		while(i<this.rame.size() && estDecharge == false)
		{
			if(this.rame.get(i).getPassagers().contains(p))
			{
				this.rame.get(i).getPassagers().remove(p);
				estDecharge = true;
			}
			i++;
		}
	}
	
	/**
	 * V鲩fie si une navette est pleine et ne peut plus recevoir de passagers.
	 * @return true si pleine, false s'il reste de la place.
	 */
	public boolean estPleine()
	{
		int encoreUnePlace = 0;
		int i = 0;
		while(i<this.rame.size() && encoreUnePlace==0)
		{
			if(this.rame.get(i).estComplet()==false)
			{
				encoreUnePlace=1;
			}
				i++;
		}
		if(encoreUnePlace==1)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NavetteSpatiale other = (NavetteSpatiale) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (rame == null) {
			if (other.rame != null)
				return false;
		} else if (!rame.equals(other.rame))
			return false;
		if (vaAuTeminus != other.vaAuTeminus)
			return false;
		return true;
	}
}

