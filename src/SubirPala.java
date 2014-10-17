import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Behavior;

public class SubirPala implements Behavior {
	private NXTRegulatedMotor motorIzq, motorDer;
	private CompassHTSensor compass;
	static int DISTANCIA_PARED;
	UltrasonicSensor sonar_izq;
	NXTRegulatedMotor pala;
	private Comunicacion com;

	public SubirPala(NXTRegulatedMotor izq, NXTRegulatedMotor der, NXTRegulatedMotor motor_pala, UltrasonicSensor s_izq, int dist_pared, CompassHTSensor comp, Comunicacion com) {
		pala = motor_pala;
		pala.resetTachoCount();
		pala.setSpeed(30);
		sonar_izq = s_izq;
		DISTANCIA_PARED = dist_pared;
		motorIzq = izq;
		motorDer = der;
		this.com = com;
		compass = comp;
	}

	@Override
	public boolean takeControl() {
		return sonar_izq.getDistance() <= DISTANCIA_PARED;
	}

	@Override
	public void action() {
		//Sound.beep();
		pala.rotateTo(0);
		motorIzq.rotate(-180, true);
		motorDer.rotate(-180, false);
		motorIzq.rotate(395, true);
		motorDer.rotate(-395, false);
		float degrees = compass.getDegreesCartesian(); 
		if (degrees > 75 && degrees < 105) {
			//Sound.beep();
		}
		else if (degrees > 165 && degrees < 195){
			//Sound.twoBeeps();
		}
		else if (degrees > 255 && degrees < 285){
			//Sound.beepSequenceUp();
		}
		else if (degrees > 345 && degrees < 15){
			//Sound.beepSequence();
		}
		com.setComunicandose(Comunicacion.GET_CONEXION);
	}

	@Override
	public void suppress() { }

}
