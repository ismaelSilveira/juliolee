import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class BajarPala implements Behavior {
	final int DISTANCIA_PARED;
	UltrasonicSensor sonar_izq;
	UltrasonicSensor sonar_der;
	NXTRegulatedMotor motor;

	public BajarPala(NXTRegulatedMotor motor_pala, SensorPort puerto_sonar_izq, SensorPort puerto_sonar_der, int dist_pared) {
		motor = motor_pala;
		motor.setSpeed(200);
		sonar_izq = new UltrasonicSensor(puerto_sonar_izq);
		sonar_der = new UltrasonicSensor(puerto_sonar_der);
		DISTANCIA_PARED = dist_pared;
	}

	@Override
	public boolean takeControl() {
		return motor.getTachoCount() == 0 && sonar_izq.getDistance() > DISTANCIA_PARED && sonar_der.getDistance() > DISTANCIA_PARED;
	}

	@Override
	public void action() {
		motor.rotateTo(-105);
	}

	@Override
	public void suppress() {
	}

}

