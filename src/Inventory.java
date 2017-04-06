import java.util.ArrayList;

public class Inventory {
	private Integer nbrNavettes;
	private Integer nbrLiaisons;
	private Integer nbrVortex;

	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	
	public Inventory(){
		nbrNavettes=2;
		nbrLiaisons=3;
		nbrVortex=1;
	}
	
	public Integer getNombreDeLiaisons(){
		return this.nbrLiaisons;
	}

	public int getNombreDeNavettes(){
		return this.nbrNavettes;
	}
		
	public int getNombreDeVortex(){
		return this.nbrVortex;
	}
	
	public void setNombreDeLiaisons(Integer nbr){
		this.nbrLiaisons = nbr;
	}

	public void setNombreDeNavettes(Integer nbr){
		this.nbrNavettes = nbr;	
	}
	
	public void setNombreDeVortex(Integer nbr){
	
	}
}
