
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
				
		// 3������ ����
		int selectedElevator = ui.pressed(3);
		
		// 1���� �ִ� Elevator�� ���� �̵�
		if ( selectedElevator != -1 ) {
			// 2���� ����
			ui.processingApproaching(selectedElevator, 2);
			// 3���� ����
			ui.processingApproaching(selectedElevator, 3);
		}

	}
}
