import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class BajarPala implements Behavior {
	final int DISTANCIA_PARED;
	UltrasonicSensor sonar_izq;
	NXTRegulatedMotor motor;
	private Comunicacion com;

	public BajarPala(NXTRegulatedMotor motor_pala, UltrasonicSensor s_izq, int dist_pared, Comunicacion com) {
		motor = motor_pala;
		motor.setSpeed(200);
		sonar_izq = s_izq;
		DISTANCIA_PARED = dist_pared;
		this.com = com;
	}

	@Override
	public boolean takeControl() {
		return ((motor.getTachoCount() == 0 && sonar_izq.getDistance() > DISTANCIA_PARED));
	}

	@Override
	public void action() {
		motor.rotate(-106,false);
	}

	@Override
	public void suppress() {
	}

}

