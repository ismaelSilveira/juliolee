import lejos.nxt.NXTRegulatedMotor;
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
	
	public Girar(NXTRegulatedMotor pala, DifferentialPilot pilot, UltrasonicSensor sonar) {
		this.pala = pala;
		this.pilot = pilot;
		this.sonar = sonar;
	}

	@Override
	public boolean takeControl() {
		return r.nextInt(100) < 5;
	}

	@Override
	public void action() {
		pala.rotateTo(0);
		pilot.rotate(-90);
		pala.rotate(-106);
	}

	@Override
	public void suppress() {
		
	}
}
