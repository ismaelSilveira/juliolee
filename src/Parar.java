import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.navigation.CompassPilot;
import lejos.robotics.subsumption.Behavior;

@SuppressWarnings("deprecation")
public class Parar implements Behavior {
	private CompassHTSensor compass;
	private CompassPilot pilot;
	UltrasonicSensor sonar_izq;
	UltrasonicSensor sonar_der;
	final int DISTANCIA_PARED;
	boolean seguir;

	public Parar(SensorPort puerto_sonar_izq, SensorPort puerto_sonar_der,
			SensorPort puerto_compass, int dist_pared) {
		sonar_izq = new UltrasonicSensor(puerto_sonar_izq);
		sonar_der = new UltrasonicSensor(puerto_sonar_der);
		compass = new CompassHTSensor(puerto_compass);
		pilot = new CompassPilot(compass, 33, 144, Motor.A, Motor.B, false);
		DISTANCIA_PARED = dist_pared;
	}

	@Override
	public boolean takeControl() {
		return pilot.isMoving() && (sonar_der.getDistance() <= DISTANCIA_PARED
				|| sonar_izq.getDistance() <= DISTANCIA_PARED);
	}

	@Override
	public void action() {
		seguir = true;
		pilot.stop();
		int dist_der = sonar_der.getDistance();
		int dist_izq = sonar_izq.getDistance();
		pilot.setRotateSpeed(20);
		if (dist_der < dist_izq) 
			pilot.rotate(90, true);
		else if (dist_der > dist_izq)
			pilot.rotate(-90, true);
		while (Math.abs(sonar_izq.getDistance() - sonar_der.getDistance()) > 1);
		pilot.stop();
	}

	@Override
	public void suppress() {
		seguir = false;
	}

}
