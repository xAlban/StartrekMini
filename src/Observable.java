
public interface Observable {
	public void addObserver(Observer Obs);
	public void removeObserver(Observer Obs);
	public void notifyObserver();
	
}
