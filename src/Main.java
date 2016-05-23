
public class Main {

	public static void main(String args[]) {
		
		ElevatorController[] controllers = new ElevatorController[3] ;
		for ( int i = 0 ; i < 3 ; i ++ ) {
			LGFloorDoor[] floorDoors = new LGFloorDoor[10] ;
			for ( int f = 0 ; f < 10 ; f ++ )
				floorDoors[f] = new LGFloorDoor(f+1) ;
				
			controllers[i] = new ElevatorController(i, floorDoors) ;
			controllers[i].setMovingDistance(10-i);
		}
		
		ElevatorUI ui = new ElevatorUI(controllers) ;
				
		// 3층에서 선택
		int selectedElevator = ui.pressed(3);
		
		// 1층에 있는 Elevator가 위로 이동
		if ( selectedElevator != -1 ) {
			// 2층에 접근
			ui.processingApproaching(selectedElevator, 2);
			// 3층에 접근
			ui.processingApproaching(selectedElevator, 3);
		}

	}
}
