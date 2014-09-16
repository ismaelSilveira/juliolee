import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class SubirPala implements Behavior {
	static int DISTANCIA_PARED;
	UltrasonicSensor sonar;
	NXTRegulatedMotor motor;

	public SubirPala(NXTRegulatedMotor motor_pala, SensorPort puerto_sonar, int dist_pared) {
		motor = motor_pala;
		motor.resetTachoCount();
		sonar = new UltrasonicSensor(puerto_sonar);
		DISTANCIA_PARED = dist_pared;
	}

	@Override
	public boolean takeControl() {
		LCD.clear();
		LCD.drawInt(sonar.getDistance(),0,2);
		return sonar.getDistance() <= DISTANCIA_PARED;
	}

	@Override
	public void action() {
		Sound.beep();
		motor.rotateTo(0);
		while(sonar.getDistance() <= DISTANCIA_PARED)
			Thread.yield();
	}

	@Override
	public void suppress() {
	}

}
