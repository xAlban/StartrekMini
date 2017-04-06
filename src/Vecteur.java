//package startrekmini;

/**
 * Classe pour vecteur en deux dimensions.
 * le 27/12/2016
 *
 */
public class Vecteur {
	
	/**
	 * Composante X du Vecteur.
	 */
	private int X;
	
	/**
	 * Composante X du Vecteur.
	 */
	private int Y;
	
	/**
	 * Constructeur par défaut.
	 */
	public Vecteur()
	{
		this.X=(int)(Math.random()*20);
		this.Y=(int)(Math.random()*20);
	}
	
	/**
	 * Constructeur avec paramètres x et y.
	 * @param x : composante x du Vecteur à construire.
	 * @param y : composante y du Vecteur à construire.
	 */
	public Vecteur(int x, int y)
	{
		this.X=x;
		this.Y=y;
	}
	
	/**
	 * Constructeur de recopie.
	 * @param v : Vecteur à copier.
	 */
	public Vecteur(Vecteur v)
	{
		this.X=v.getX();
		this.Y=v.getY();
	}
	
	/**
	 * @return la valeur de la composante X du Vecteur.
	 */
	public int getX()
	{
		return this.X;
	}
	
	/**
	 * Remplace la composante X du Vecteur.
	 * @param x : nouvelle valeur de X.
	 */
	public void setX(int x)
	{
		this.X=x;
	}
	
	/**
	 * @return la valeur de la composante Y du Vecteur.
	 */
	public int getY()
	{
		return this.Y;
	}
	
	/**
	 * Remplace la composante Y du Vecteur.
	 * @param y : nouvelle valeur de Y.
	 */
	public void setY(int y)
	{
		this.Y=y;
	}
	
	public void printVecteur()
	{
		System.out.println("(" + this.X + ";" + this.Y + ")");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vecteur other = (Vecteur) obj;
		if (X != other.X)
			return false;
		if (Y != other.Y)
			return false;
		return true;
	}
}
