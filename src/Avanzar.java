import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.navigation.CompassPilot;
import lejos.robotics.subsumption.Behavior;

@SuppressWarnings("deprecation")
public class Avanzar implements Behavior {
	private CompassHTSensor compass;
	private CompassPilot pilot;
	UltrasonicSensor sonar;
	boolean seguir;

	public Avanzar(SensorPort puerto_sonar, SensorPort puerto_compass) {
		sonar = new UltrasonicSensor(puerto_sonar);
		compass = new CompassHTSensor(puerto_compass);
		pilot = new CompassPilot(compass, 33, 144, Motor.A, Motor.B, false);
		pilot.setTravelSpeed(300);
		pilot.setRotateSpeed(30);
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		seguir = true;
		LCD.drawString("Forward",0,1);
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
