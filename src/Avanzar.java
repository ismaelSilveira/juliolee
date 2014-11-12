import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class Avanzar implements Behavior {
	private NXTRegulatedMotor motorDer, motorIzq, pala;
	private UltrasonicSensor sonar;
	private final int DISTANCIA_PARED;
	private boolean seguir;
	private Comunicacion com;

	public Avanzar(NXTRegulatedMotor izq, NXTRegulatedMotor der, NXTRegulatedMotor pala, UltrasonicSensor s, int dist_pared, Comunicacion com) {
		sonar = s;
		motorDer = der;
		motorIzq = izq;
		DISTANCIA_PARED = dist_pared;
		this.pala = pala;
		this.com = com;
	}

	@Override
	public boolean takeControl() {
		return (true);
	}

	@Override
	public void action() {
		seguir = true;
		motorIzq.setSpeed(655);
		motorDer.setSpeed(650);
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
