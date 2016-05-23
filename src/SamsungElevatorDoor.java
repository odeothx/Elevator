
// STATE PATTERN
public class SamsungElevatorDoor implements IDoor{
	private int status ; // 0 for Closed, 1 for Open

	@Override
	public int getStatus() {
		return status;
	}

	public SamsungElevatorDoor() {
		status = 0 ;	
	}
	
	public void control(int close) {
		if ( close == 1 ) {
			status = 0 ;
			System.out.println("SamsungElevatorDoor CLOSED");
		}
		else {
			status = 1 ;
			System.out.println("SamsungElevatorDoor OPEN");
		}
	}
	@Override
 	public void open()	{
		status = 1 ;
		System.out.println("SamsungElevatorDoor OPEN");
	}
	@Override
	public void close(){
		status = 0 ;
		System.out.println("SamsungElevatorDoor CLOSED");
	}

}
