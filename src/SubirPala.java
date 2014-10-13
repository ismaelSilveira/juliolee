import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class SubirPala implements Behavior {
	private NXTRegulatedMotor motorIzq, motorDer;
	static int DISTANCIA_PARED;
	UltrasonicSensor sonar_izq;
	NXTRegulatedMotor pala;
	private Comunicacion com;

	public SubirPala(NXTRegulatedMotor izq, NXTRegulatedMotor der, NXTRegulatedMotor motor_pala, UltrasonicSensor s_izq, int dist_pared, Comunicacion com) {
		pala = motor_pala;
		pala.resetTachoCount();
		pala.setSpeed(30);
		sonar_izq = s_izq;
		DISTANCIA_PARED = dist_pared;
		motorIzq = izq;
		motorDer = der;
		this.com = com;
	}

	@Override
	public boolean takeControl() {
		return sonar_izq.getDistance() <= DISTANCIA_PARED;
	}

	@Override
	public void action() {
		Sound.beep();
		pala.rotateTo(0);
		motorIzq.rotate(-720, true);
		motorDer.rotate(-720, false);
		motorIzq.rotate(1440, true);
		motorDer.rotate(-1440, false);
		com.setComunicandose(Comunicacion.GET_CONEXION);
	}

	@Override
	public void suppress() { }

}
