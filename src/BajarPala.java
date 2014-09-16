import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class BajarPala implements Behavior {
	final int DISTANCIA_PARED;
	UltrasonicSensor sonar;
	NXTRegulatedMotor motor;

	public BajarPala(NXTRegulatedMotor motor_pala, SensorPort puerto_sonar, int dist_pared) {
		motor = motor_pala;
		motor.setSpeed(200);
		sonar = new UltrasonicSensor(puerto_sonar);
		DISTANCIA_PARED = dist_pared;
	}

	@Override
	public boolean takeControl() {
		return motor.getTachoCount() == 0 && sonar.getDistance() > DISTANCIA_PARED;
	}

	@Override
	public void action() {
		motor.rotateTo(-105);
	}

	@Override
	public void suppress() {
	}

}
