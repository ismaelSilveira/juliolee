import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.subsumption.Behavior;

import java.util.Random;

public class Girar implements Behavior {
	private NXTRegulatedMotor pala, motorIzq, motorDer;
	private Random r = new Random(6846515);
	private boolean hasControl = false;
	private Comunicacion com;
	
	public Girar(NXTRegulatedMotor izq, NXTRegulatedMotor der, NXTRegulatedMotor pala, Comunicacion com) {
		this.pala = pala;
		motorIzq = izq;
		motorDer = der;
		this.com = com;
	}

	@Override
	public boolean takeControl() {
		return ((hasControl = hasControl || r.nextInt(1000) < 10) && (com.getComunicandose() == Comunicacion.SIN_COMUNICACION));
	}

	@Override
	public void action() {
		pala.rotateTo(0);
		motorIzq.rotate(1440, true);
		motorDer.rotate(-1440, false);
		hasControl = false;
	}

	@Override
	public void suppress() {
		hasControl = false;
	}
}
