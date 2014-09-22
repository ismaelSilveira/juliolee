import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.CompassPilot;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

@SuppressWarnings("deprecation")
public class SubirPala implements Behavior {
	static int DISTANCIA_PARED;
	UltrasonicSensor sonar_izq;
	UltrasonicSensor sonar_der;
	DifferentialPilot pilot;
	NXTRegulatedMotor pala;

	public SubirPala(NXTRegulatedMotor motor_pala, UltrasonicSensor s_izq,
			UltrasonicSensor s_der, DifferentialPilot p, int dist_pared) {
		pala = motor_pala;
		pala.resetTachoCount();
		pala.setSpeed(30);
		sonar_izq = s_izq;
		sonar_der = s_der;
		pilot = p;
		DISTANCIA_PARED = dist_pared;
	}

	@Override
	public boolean takeControl() {
		return sonar_izq.getDistance() <= DISTANCIA_PARED;
	}

	@Override
	public void action() {
		pilot.setTravelSpeed(30);
		pilot.travel(-20);
		pala.rotateTo(0);
	}

	@Override
	public void suppress() {
	}

}
