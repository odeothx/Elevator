
// STATE PATTERN
public class SamsungMotor {
	private int doorStatus ; // 0 for Closed, 1 for Open

	private int status ; // 0 for STOP, 1 for MovingUp, 2 for MovingDown
	
	public SamsungMotor() {
		doorStatus = 0 ;
		status = 0 ;
	}
	public void stop() {
		status = 0 ;
		System.out.println("Elevator Stop");

	}
	public void move(int down) {
		if ( doorStatus == 1 ) return ;
		if ( down == 0 ) {
			status = 1 ;
			System.out.println("Elevator Moving Up");
		}
		else {
			status = 2 ;
			System.out.println("Elevator Moving Down");
		}
	}
	
	public int getStatus() {
		return status;
	}
	public void setDoorStatus(int doorStatus) {
		this.doorStatus = doorStatus;
	}
}
