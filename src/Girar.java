import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

import java.util.Random;

@SuppressWarnings("deprecation")
public class Girar implements Behavior {
	private DifferentialPilot pilot;
	private UltrasonicSensor sonar;
	private NXTRegulatedMotor pala;
	private Random r = new Random(6846515);
	private boolean hasControl = false;
	
	public Girar(NXTRegulatedMotor pala, DifferentialPilot pilot, UltrasonicSensor sonar) {
		this.pala = pala;
		this.pilot = pilot;
		this.sonar = sonar;
	}

	@Override
	public boolean takeControl() {
		return hasControl = hasControl || r.nextInt(1000) < 10;
	}

	@Override
	public void action() {
		pilot.setTravelSpeed(30);
		pala.rotateTo(0);
		pilot.rotate(-90);
		pala.rotate(-106);
		hasControl = false;
	}

	@Override
	public void suppress() {
		hasControl = false;
	}
}
