import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.CompassPilot;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

@SuppressWarnings("deprecation")
public class Avanzar implements Behavior {
	private DifferentialPilot pilot;
	UltrasonicSensor sonar;
	final int DISTANCIA_PARED;
	boolean seguir;

	public Avanzar(DifferentialPilot p, UltrasonicSensor s, int dist_pared) {
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
		pilot.setTravelSpeed(300);
		pilot.forward();
		while (seguir)
			Thread.yield();
		pilot.quickStop();
	}

	@Override
	public void suppress() {
		seguir = false;
	}

}
