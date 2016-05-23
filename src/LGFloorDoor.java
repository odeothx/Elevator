
public class LGFloorDoor implements IDoor {
	private int floor ;
	private int status ; // 0 for Closed, 1 for Open
	
	@Override
    public int getStatus() {
		return status;
	}

	public LGFloorDoor(int floor) {
		this.floor = floor ;
		status = 0 ;	
	}
	
	public void control(int open) {
		if ( open == 1 ) {
			status = 1 ;
			System.out.println("LGFloorDoor at " + floor + " OPEN");
		}
		else {
			status = 0 ;
			System.out.println("LGFloorDoor at " + floor + " CLOSED");
		}
	}
	@Override
    public void open(){
        status = 1 ;
        System.out.println("LGFloorDoor at " + floor + " OPEN");
    }
    @Override
    public void close(){
        status = 0 ;
        System.out.println("LGFloorDoor at " + floor + " CLOSED");
    }
}
