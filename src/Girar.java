import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.subsumption.Behavior;

import java.util.Random;

public class Girar implements Behavior {
	private NXTRegulatedMotor pala, motorIzq, motorDer;
	private Random r = new Random(System.currentTimeMillis());
	private boolean hasControl = false;
	private int rand = r.nextInt();
	
	public Girar(NXTRegulatedMotor izq, NXTRegulatedMotor der, NXTRegulatedMotor pala) {
		this.pala = pala;
		motorIzq = izq;
		motorDer = der;
	}

	@Override
	public boolean takeControl() {
		rand = r.nextInt(rand+1);
		return (hasControl = hasControl || (rand >= 400 && rand < 405));
	}

	@Override
	public void action() {
		pala.rotateTo(0);
		motorIzq.rotate(560, true);
		motorDer.rotate(-560, false);
		hasControl = false;
	}

	@Override
	public void suppress() {
		hasControl = false;
	}
}
