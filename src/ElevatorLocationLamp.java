
public class ElevatorLocationLamp {
	private int floor ;
	
	public ElevatorLocationLamp(int floor) {
		super();
		this.floor = floor;
	}

	public void turn(int flag) {
		if ( flag == 0 )
			System.out.println("Elevator Lamp at " + floor + " turn ON") ;
		else
			System.out.println("Elevator Lamp at " + floor + " turn OFF") ;			
	}

}
