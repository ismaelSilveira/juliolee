import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class Avanzar implements Behavior {
	private NXTRegulatedMotor motorDer, motorIzq;
	UltrasonicSensor sonar;
	final int DISTANCIA_PARED;
	boolean seguir;

	public Avanzar(NXTRegulatedMotor izq, NXTRegulatedMotor der, UltrasonicSensor s, int dist_pared) {
		sonar = s;
		motorDer = der;
		motorIzq = izq;
		DISTANCIA_PARED = dist_pared;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		seguir = true;
		motorIzq.setSpeed(700);
		motorDer.setSpeed(660);
		motorDer.forward();
		motorIzq.forward();
		
		while (seguir)
			Thread.yield();
		motorIzq.stop(true);
		motorDer.stop();
	}

	@Override
	public void suppress() {
		seguir = false;
	}

}
