//package startrekmini;

/**
 * Classe pour les passagers des navettes spatiales.
 * le 26/12/2016
 *
 */

public class Passager {
	
	/**
	 * Type de stationSpatiale que souhaite atteindre le passager.
	 */
	private TypeStation destination;
	
	/**
	 * Constructeur par d�faut, d�fini une destination al�atoirement.
	 */
	public Passager()
	{
		int i= (int)(Math.random()*20); //added here
		switch(i)
		{
			case 0:
				this.destination = TypeStation.ETOILE;
				break;
			case 1:
				this.destination = TypeStation.CROIX;
				break;
			case 2: case 3:
				this.destination = TypeStation.LOSANGE;
				break;
			case 4: case 5: case 6:
			case 7:
				this.destination = TypeStation.CARRE;
				break;
			case 8: case 9: case 10:
			case 11: case 12: case 13:
				this.destination = TypeStation.TRIANGLE;
				break;
			default:
				this.destination = TypeStation.ROND;
				break;
		}
	}
	
	/**
	 * Cr�e un Passager avec pour destination le type de station pass� en param�tre.
	 * @param t : destination du nouveau Passager.
	 */
	public Passager(TypeStation t)
	{
		this.destination=t;
	}
	
	/**
	 * Constructeur de recopie.
	 * @param p : Passager � recopier.
	 */
	public Passager(Passager p)
	{
		this.destination=p.getDestination();
	}
	
	/**
	 * Retourne la destination  d'un passager.
	 * @return le destination du Passager.
	 */
	public TypeStation getDestination()
	{
		return this.destination;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Passager other = (Passager) obj;
		if (destination != other.destination)
			return false;
		return true;
	}
}
