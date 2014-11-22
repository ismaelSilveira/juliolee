import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class Avanzar implements Behavior {
	private NXTRegulatedMotor motorDer, motorIzq;
	private boolean seguir;

	public Avanzar(NXTRegulatedMotor izq, NXTRegulatedMotor der) {
		motorDer = der;
		motorIzq = izq;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		Sound.twoBeeps();
		Sound.twoBeeps();
		seguir = true;
		motorIzq.setSpeed(Constante.avanzar_vel_izq);
		motorDer.setSpeed(Constante.avanzar_vel_der);
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
