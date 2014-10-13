import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class Avanzar implements Behavior {
	private NXTRegulatedMotor motorDer, motorIzq, pala;
	UltrasonicSensor sonar;
	final int DISTANCIA_PARED;
	boolean seguir;
	Comunicacion com;

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
		return (true && (com.getComunicandose() == Comunicacion.SIN_COMUNICACION));
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
		pala.rotate(5, true);
		motorIzq.stop(true);
		motorDer.stop();
	}

	@Override
	public void suppress() {
		seguir = false;
	}

}
