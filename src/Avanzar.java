import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.CompassPilot;
import lejos.robotics.subsumption.Behavior;

@SuppressWarnings("deprecation")
public class Avanzar implements Behavior {
	private CompassPilot pilot;
	UltrasonicSensor sonar;
	final int DISTANCIA_PARED;
	boolean seguir;

	public Avanzar(CompassPilot p, UltrasonicSensor s, int dist_pared) {
		sonar = s;
		pilot = p;		
		DISTANCIA_PARED = dist_pared;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		seguir = true;
		pilot.resetCartesianZero();
		pilot.setHeading(0);
		pilot.setTravelSpeed(300);
		pilot.forward();
		while (seguir)
			Thread.yield();
		pilot.stop();
	}

	@Override
	public void suppress() {
		seguir = false;
	}

}
