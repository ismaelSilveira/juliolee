import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Behavior;

import java.util.Date;
import java.util.Random;

public class Girar implements Behavior {
	private NXTRegulatedMotor pala, motorIzq, motorDer;
	private Random r = new Random(System.currentTimeMillis());
	private boolean hasControl = false;
	private Comunicacion com;
	private CompassHTSensor compass;
	
	public Girar(NXTRegulatedMotor izq, NXTRegulatedMotor der, NXTRegulatedMotor pala, CompassHTSensor comp, Comunicacion com) {
		this.pala = pala;
		motorIzq = izq;
		motorDer = der;
		this.com = com;
		compass = comp;
	}

	@Override
	public boolean takeControl() {
		return ((hasControl = hasControl || r.nextInt(1000) == 500) && (com.getComunicandose() == Comunicacion.SIN_COMUNICACION));
	}

	@Override
	public void action() {
		Sound.beepSequenceUp();
		float degrees = compass.getDegreesCartesian();
		/*LCD.clearDisplay();
		LCD.drawInt((int) degrees, 0, 0);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if (degrees >= 315 || degrees < 45) {
			Sound.twoBeeps();
		}
		else if (degrees >= 45 && degrees < 135){
			Sound.twoBeeps();
			Sound.twoBeeps();
		}
		else if (degrees >= 135 && degrees < 225){
			Sound.twoBeeps();
			Sound.twoBeeps();
			Sound.twoBeeps();
		}
		else if (degrees >= 225 && degrees < 315){
			Sound.twoBeeps();
			Sound.twoBeeps();
			Sound.twoBeeps();
			Sound.twoBeeps();
		}
		pala.rotateTo(0);
		motorIzq.rotate(570, true);
		motorDer.rotate(-570, false);
		hasControl = false;
	}

	@Override
	public void suppress() {
		hasControl = false;
	}
}
