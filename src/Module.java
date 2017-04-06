//package startrekmini;

import java.util.ArrayList;

/**
 * Classe pour les différents wagons (modules) de la navette spatiale.
 * Le 28/12/2016
 *
 */
public class Module {
	
	/**
	 * Passagers présents dans le module. Le module peut en contenir jusqu'à 6.
	 */
	private ArrayList <Passager> passagers;
	
	/**
	 * Constructeur pas défaut. Inialise le tableau de Passager à 6.
	 */
	public Module()
	{
		passagers = new ArrayList<Passager>();
	}
	
	/**
	 * Constructeur de recopie.
	 * @param m : module à recopier.
	 */
	public Module(Module m)
	{
		passagers = new ArrayList<Passager>(m.getPassagers());
	}
	
	/**
	 * @return Retourne le tableau de Passager présents dans le module.
	 */
	public ArrayList<Passager> getPassagers()
	{
		return this.passagers;
	}
	
	/**
	 * Vérifie si la liste de passager du module n'a pas atteint la limite de 6.
	 * @return : vraie si la limite est atteinte, faus sinon.
	 */
	public boolean estComplet()
	{
		if(this.passagers.size()>=6)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
