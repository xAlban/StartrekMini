import java.util.ArrayList;


public class StationsSurMesLignes {
	private StationSpatiale stationOrigine;
	ArrayList<StationSpatiale> destPossibles;
	
	public StationsSurMesLignes(StationSpatiale s){
		stationOrigine = s;
		destPossibles= new ArrayList<StationSpatiale>(); 
	}
	
	public StationSpatiale getStationOrigine(){
		return this.stationOrigine;
	}
	
	public ArrayList<StationSpatiale> getDestPossibles(){
		return this.destPossibles;
	}
	
	public void setDestPossibles(ArrayList<StationSpatiale> lstStat){
		this.destPossibles = lstStat;
	}
}
