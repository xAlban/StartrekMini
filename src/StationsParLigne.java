import java.util.ArrayList;

public class StationsParLigne{
    	private Liaison liaison;
    	ArrayList<StationSpatiale> lstStat;
    	
    	public StationsParLigne(Liaison l){
    		liaison=l;
    		lstStat = new ArrayList<StationSpatiale>(); 
    		
    	}
    	
    	public Liaison getLiaison(){
    		return this.liaison;
    	}
    	
    	public ArrayList<StationSpatiale> getListeStations(){
    		return this.lstStat;
    	}
    }