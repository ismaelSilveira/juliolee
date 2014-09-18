import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class SubirPala implements Behavior {
	static int DISTANCIA_PARED;
	UltrasonicSensor sonar_izq;
	UltrasonicSensor sonar_der;
	NXTRegulatedMotor motor;

	public SubirPala(NXTRegulatedMotor motor_pala, SensorPort puerto_sonar_izq, SensorPort puerto_sonar_der, int dist_pared) {
		motor = motor_pala;
		motor.resetTachoCount();
		sonar_izq = new UltrasonicSensor(puerto_sonar_izq);
		sonar_der = new UltrasonicSensor(puerto_sonar_der);
		DISTANCIA_PARED = dist_pared;
	}

	@Override
	public boolean takeControl() {
		return sonar_izq.getDistance() <= DISTANCIA_PARED || sonar_der.getDistance() <= DISTANCIA_PARED;
	}

	@Override
	public void action() {
		Sound.beep();
		motor.rotateTo(0);
		while(sonar_izq.getDistance() <= DISTANCIA_PARED)
			Thread.yield();
	}

	@Override
	public void suppress() {
	}

}
