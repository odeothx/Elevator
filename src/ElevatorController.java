import java.util.ArrayList;
import java.util.List;

public class ElevatorController {
	private int id ;
	private boolean active ;
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	private int currentFloor ;
	private int movingDistance ;

	private List<Integer> destinations = new ArrayList<Integer>() ;

	public void setDestinations(List<Integer> destinations) {
		this.destinations = destinations;
		System.out.println("Elevator " + id + " Destinations: " + destinations);
	}

	public List<Integer> getDestinations() {
		return destinations;
	}
	private SamsungElevatorDoor elevatorDoor ;
	private LGFloorDoor[] floorDoors ;
	private SamsungMotor motor ;
	
	private ElevatorLocationLamp[] locationLamps = new ElevatorLocationLamp[10] ;
	
	public ElevatorController(int id, LGFloorDoor[] floorDoors) {
		super();
		this.id = id;
		currentFloor = 1 ;
		movingDistance = 0 ;
		active = true ;
		
		elevatorDoor = new SamsungElevatorDoor() ;
		motor = new SamsungMotor() ;
		this.floorDoors = floorDoors ;
		
		for ( int i = 0 ; i < 10 ; i ++ )
			locationLamps[i] = new ElevatorLocationLamp(i+1) ;
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}

	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
		movingDistance ++ ;
	}

	public SamsungMotor getMotor() {
		return motor ;
	}

	public ElevatorLocationLamp getLamp() {
		return locationLamps[currentFloor-1] ;
	}

	public LGFloorDoor getFloorDoor(int floor) {
		return floorDoors[floor-1] ;
	}

	public SamsungElevatorDoor getElevatorDoor() {
		return elevatorDoor ;
	}
	
	public int getMovingDistance() {
		return movingDistance;
	}
	public void setMovingDistance(int movingDistance) {
		this.movingDistance = movingDistance ;
	}
	
}
