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
		motorIzq.setAcceleration(300);
		motorDer.setAcceleration(300);
		motorIzq.setSpeed(650);
		motorDer.setSpeed(715);
		motorDer.forward();
		motorIzq.forward();
		
		while (seguir)
			Thread.yield();
		pala.rotate(15);
		motorIzq.stop();
		motorDer.stop();
	}

	@Override
	public void suppress() {
		seguir = false;
	}

}
