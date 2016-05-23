import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ElevatorUI {
	private ElevatorController[] controllers ;

	
	public ElevatorUI(ElevatorController[] controllers) {
		super();
		this.controllers = controllers ;
	}
	
	// by User
	public int pressed(int floor) {
		
		int foundElevator = -1 ;
		// ���� ���� ���� �ִ� Elevator�� ã�´�.
		for ( int i = 0 ; i < 3 ; i ++ ) {
			int curFloor = controllers[i].getCurrentFloor() ;
			if ( curFloor == floor && controllers[i].isActive()) {
				foundElevator = i ;
				break ;
			}
		}
		int selectedElevator = -1 ;

		if ( foundElevator != -1 ) { // ���� ���� �ִ� Elevator�� ���� ����, 3���Ŀ� ���� �ݴ´�.
			selectedElevator = foundElevator ;
			System.out.println("Elevator " + selectedElevator + " Selected at " + controllers[selectedElevator].getCurrentFloor());

			
			LGFloorDoor curFloorDoor = controllers[foundElevator].getFloorDoor(floor) ;
			curFloorDoor.open();
			SamsungElevatorDoor elevatorDoor = controllers[foundElevator].getElevatorDoor() ;
			elevatorDoor.open();
			
			SamsungMotor motor = controllers[foundElevator].getMotor() ;
			motor.setDoorStatus(1);
			
			try {
				Thread.sleep(3000) ;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			curFloorDoor.close();
			elevatorDoor.close();
			
			motor.setDoorStatus(0);
		}
		else {
			// ���� ����� Elevator�� ã�Ƽ� �̵���Ų��.
			// ���� ���� ����� Elevator�� �������̸� Elevator�� ��ü �̵��Ÿ�(movingDistance)�� ���� Elevator�� �����Ѵ�.
			
			List<Integer> nearestElevators = new ArrayList<Integer>() ;
			int minDistance = 1000 ;
						
			for ( int i = 0 ; i < 3 ; i ++ ) {
				if ( ! controllers[i].isActive() ) continue ;
				
				int distance = Math.abs(controllers[1].getCurrentFloor() - floor) ;
				if ( distance < minDistance ) {
					nearestElevators.clear();
					nearestElevators.add(i) ;
					minDistance = distance ;
				}
				else if ( distance == minDistance ) {
					nearestElevators.add(i) ;
				}
			}
			
			if ( nearestElevators.size() == 1)
				selectedElevator = nearestElevators.get(0) ;
			else if ( nearestElevators.size() > 1) {
				int minMovingDistance = controllers[nearestElevators.get(0)].getMovingDistance() ;
				selectedElevator = nearestElevators.get(0) ;
				
				for ( int i = 1 ; i < nearestElevators.size() ; i ++ ) {
					int movingDistance = controllers[nearestElevators.get(i)].getMovingDistance() ;
					if ( movingDistance < minMovingDistance ) {
						selectedElevator = nearestElevators.get(i) ;
						minMovingDistance = movingDistance ;
					}
				}
			}
			if ( selectedElevator == -1 )
				return -1 ;

			System.out.println("Elevator " + selectedElevator + " Selected at " + controllers[selectedElevator].getCurrentFloor());
			// ���õ� Elevator�� ������ ���� ��û ���� �߰�
			List<Integer> destinations = controllers[selectedElevator].getDestinations() ;
			destinations.add(floor) ;
			controllers[selectedElevator].setDestinations(destinations) ;
			
			// ���� ����� Elevator�� ��û ������ �̵���Ų��.
			SamsungMotor motor = controllers[selectedElevator].getMotor() ;
			if ( floor > controllers[selectedElevator].getCurrentFloor() )
				motor.move(1);
			else
				motor.move(0);		
		}
		return selectedElevator ;		
	}
	
	// By Sensor
	public void processingApproaching(int elevatorNo, int floor) {
		System.out.println("Elevator " + elevatorNo + " Approaching " + floor);
		// Elevator�� ���� �� Lamp�� ����.
		ElevatorLocationLamp lamp1 = controllers[elevatorNo].getLamp() ;
		lamp1.turn(1);
		
		// Elevator�� ���� ���� ���Ž�Ų��.
		controllers[elevatorNo].setCurrentFloor(floor);
		
		// Elevator�� ���� ì�� Lamp�� �Ҵ�.
		ElevatorLocationLamp lamp2 = controllers[elevatorNo].getLamp() ;
		lamp2.turn(0);
		
		// MovingDistance�� update��Ų��.
		int distance = controllers[elevatorNo].getMovingDistance() ;
		distance ++ ;
		controllers[elevatorNo].setMovingDistance(distance);
		
		// ������ �ʿ䰡 ������ �����ϰ�, ���� ���� 2�� �Ŀ� ���� �ݴ´�.
		
		List<Integer> destinations = controllers[elevatorNo].getDestinations() ;
		if ( destinations.contains(floor)) {
			SamsungMotor motor = controllers[elevatorNo].getMotor() ;
			motor.stop(); 
			
			LGFloorDoor curFloorDoor = controllers[elevatorNo].getFloorDoor(floor) ;
			curFloorDoor.open();
			SamsungElevatorDoor elevatorDoor = controllers[elevatorNo].getElevatorDoor() ;
			elevatorDoor.open();
			
			motor.setDoorStatus(1);

			// �ȳ� ���
			System.out.println(floor + " ���� �����Ͽ����ϴ�.");

			try {
				Thread.sleep(2000) ;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			curFloorDoor.close();
			elevatorDoor.close();
			
			motor.setDoorStatus(0);

			destinations.remove(new Integer(floor)) ;
			controllers[elevatorNo].setDestinations(destinations);
		}
		
	}
	
	// by Administrator
	public void clearDestinations(int elevatorNo) {
		List<Integer> destinations = new ArrayList<Integer>() ;
		controllers[elevatorNo].setDestinations(destinations);
	}
	// by Administrator
	public void controlElevatorActivation(int elevatorNo, boolean active) {
		controllers[elevatorNo].setActive(active);
	}
	// by Administrator	
	public void controlElevatorActivation(boolean active) {
		for ( int i = 0 ; i < 3 ; i ++ )
			controllers[i].setActive(active);
	}
	
	// by User, Administrator
	public void openDoor(int elevatorNo) {
		SamsungElevatorDoor elevatorDoor = controllers[elevatorNo].getElevatorDoor() ;
		int floor = controllers[elevatorNo].getCurrentFloor() ;
		LGFloorDoor floorDoor = controllers[elevatorNo].getFloorDoor(floor) ;
		
		SamsungMotor motor = controllers[elevatorNo].getMotor() ;
		if ( motor.getStatus() == 0 ) {
			elevatorDoor.open();;
			floorDoor.open();
		}
		else {
			System.out.println("Elevator " + elevatorNo + " Moving: cannot open Door");
		}
	}
	
	// by User, Administrator
	public void closeDoor(int elevatorNo) {
		SamsungElevatorDoor elevatorDoor = controllers[elevatorNo].getElevatorDoor() ;
		int floor = controllers[elevatorNo].getCurrentFloor() ;
		LGFloorDoor floorDoor = controllers[elevatorNo].getFloorDoor(floor) ;
		
		if ( elevatorDoor.getStatus() == 1 && floorDoor.getStatus() == 1 ) {
			elevatorDoor.close();;
			floorDoor.close();
		}
		else {
			System.out.println("Elevator " + elevatorNo + " Already closed: cannot close Door");
		}
	}
}