import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Behavior;

public class SubirPala implements Behavior {
	private NXTRegulatedMotor motorIzq, motorDer;
	private CompassHTSensor compass;
	static int DISTANCIA_PARED;
	UltrasonicSensor sonar_izq;
	NXTRegulatedMotor pala;
	private Comunicacion com;

	public SubirPala(NXTRegulatedMotor izq, NXTRegulatedMotor der, NXTRegulatedMotor motor_pala, UltrasonicSensor s_izq, int dist_pared, CompassHTSensor comp, Comunicacion com) {
		pala = motor_pala;
		pala.resetTachoCount();
		pala.setSpeed(30);
		sonar_izq = s_izq;
		DISTANCIA_PARED = dist_pared;
		motorIzq = izq;
		motorDer = der;
		this.com = com;
		compass = comp;
	}

	@Override
	public boolean takeControl() {
		return sonar_izq.getDistance() <= DISTANCIA_PARED;
	}

	@Override
	public void action() {
		motorIzq.rotate(-180, true);
		motorDer.rotate(-180, false);
		pala.rotateTo(0);
		
		com.setComunicando(true);
	}

	@Override
	public void suppress() { }
}
