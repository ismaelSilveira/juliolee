import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.subsumption.Behavior;

import java.util.Random;

public class Girar implements Behavior {
	private NXTRegulatedMotor pala, motorIzq, motorDer;
	private Random r = new Random(6846515);
	private boolean hasControl = false;
	
	public Girar(NXTRegulatedMotor izq, NXTRegulatedMotor der, NXTRegulatedMotor pala) {
		this.pala = pala;
		motorIzq = izq;
		motorDer = der;
	}

	@Override
	public boolean takeControl() {
		return hasControl = hasControl || r.nextInt(1000) < 10;
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
