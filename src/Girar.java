import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Behavior;

import java.util.Random;

public class Girar implements Behavior {
	private NXTRegulatedMotor pala, motorIzq, motorDer;
	private Random r = new Random(6846515);
	private boolean hasControl = false;
	private CompassHTSensor compass;
	
	public Girar(NXTRegulatedMotor izq, NXTRegulatedMotor der, NXTRegulatedMotor pala, CompassHTSensor comp) {
		this.pala = pala;
		motorIzq = izq;
		motorDer = der;
		compass = comp;
	}

	@Override
	public boolean takeControl() {
		return hasControl = hasControl || r.nextInt(1000) < 10;
	}

	@Override
	public void action() {
		pala.rotateTo(0);
		motorIzq.rotate(395, true);
		motorDer.rotate(-395, false);
		float degrees = compass.getDegreesCartesian(); 
		if (degrees > 75 && degrees < 105) {
			Sound.beep();
		}
		else if (degrees > 165 && degrees < 195){
			Sound.twoBeeps();
		}
		else if (degrees > 255 && degrees < 285){
			Sound.beepSequenceUp();
		}
		else if (degrees > 345 && degrees < 15){
			Sound.beepSequence();
		}
		hasControl = false;
	}

	@Override
	public void suppress() {
		hasControl = false;
	}
}
