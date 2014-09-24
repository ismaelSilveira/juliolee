import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

import java.util.Date;
import java.util.Random;

@SuppressWarnings("deprecation")
public class Girar implements Behavior {
	public Girar() {
		
	}

	@Override
	public boolean takeControl() {
		UltrasonicSensor distancia = new UltrasonicSensor(SensorPort.getInstance(3));
		Random r = new Random(distancia.getDistance());
		if (r.nextInt(100) <= 70)
			return true;
		else
			return false;
	}

	@Override
	public void action() {
		DifferentialPilot pilot = new DifferentialPilot(33, 144, Motor.getInstance(0), Motor.getInstance(1));
		pilot.rotateRight();
	}

	@Override
	public void suppress() {
		
	}

}
