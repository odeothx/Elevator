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
		// 현재 층에 멈춰 있는 Elevator를 찾는다.
		for ( int i = 0 ; i < 3 ; i ++ ) {
			int curFloor = controllers[i].getCurrentFloor() ;
			if ( curFloor == floor && controllers[i].isActive()) {
				foundElevator = i ;
				break ;
			}
		}
		int selectedElevator = -1 ;

		if ( foundElevator != -1 ) { // 현재 층에 있는 Elevator의 문을 열고, 3초후에 문을 닫는다.
			selectedElevator = foundElevator ;
			System.out.println("Elevator " + selectedElevator + " Selected at " + controllers[selectedElevator].getCurrentFloor());

			
			LGFloorDoor curFloorDoor = controllers[foundElevator].getFloorDoor(floor) ;
			curFloorDoor.control(1);
			SamsungElevatorDoor elevatorDoor = controllers[foundElevator].getElevatorDoor() ;
			elevatorDoor.control(0);
			
			SamsungMotor motor = controllers[foundElevator].getMotor() ;
			motor.setDoorStatus(1);
			
			try {
				Thread.sleep(3000) ;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			curFloorDoor.control(0);
			elevatorDoor.control(1);
			
			motor.setDoorStatus(0);
		}
		else {
			// 가장 가까운 Elevator를 찾아서 이동시킨다.
			// 만약 가장 가까운 Elevator가 복수개이면 Elevator의 전체 이동거리(movingDistance)가 작은 Elevator를 선택한다.
			
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
			// 선택된 Elevator의 목적지 층에 요청 층을 추가
			List<Integer> destinations = controllers[selectedElevator].getDestinations() ;
			destinations.add(floor) ;
			controllers[selectedElevator].setDestinations(destinations) ;
			
			// 가장 가까운 Elevator를 요청 층으로 이동시킨다.
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
		// Elevator의 기존 층 Lamp를 끈다.
		ElevatorLocationLamp lamp1 = controllers[elevatorNo].getLamp() ;
		lamp1.turn(1);
		
		// Elevator의 현재 층을 갱신시킨다.
		controllers[elevatorNo].setCurrentFloor(floor);
		
		// Elevator의 현재 챙의 Lamp를 켠다.
		ElevatorLocationLamp lamp2 = controllers[elevatorNo].getLamp() ;
		lamp2.turn(0);
		
		// MovingDistance를 update시킨다.
		int distance = controllers[elevatorNo].getMovingDistance() ;
		distance ++ ;
		controllers[elevatorNo].setMovingDistance(distance);
		
		// 정지할 필요가 있으면 정지하고, 문을 열고 2초 후에 문을 닫는다.
		
		List<Integer> destinations = controllers[elevatorNo].getDestinations() ;
		if ( destinations.contains(floor)) {
			SamsungMotor motor = controllers[elevatorNo].getMotor() ;
			motor.stop(); 
			
			LGFloorDoor curFloorDoor = controllers[elevatorNo].getFloorDoor(floor) ;
			curFloorDoor.control(1);
			SamsungElevatorDoor elevatorDoor = controllers[elevatorNo].getElevatorDoor() ;
			elevatorDoor.control(0);
			
			motor.setDoorStatus(1);

			// 안내 방송
			System.out.println(floor + " 층에 도착하였습니다.");

			try {
				Thread.sleep(2000) ;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			curFloorDoor.control(0);
			elevatorDoor.control(1);
			
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
			elevatorDoor.control(0);;
			floorDoor.control(1);
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
			elevatorDoor.control(1);;
			floorDoor.control(0);
		}
		else {
			System.out.println("Elevator " + elevatorNo + " Already closed: cannot close Door");
		}
	}
}