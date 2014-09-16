import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class JulioLee {
	private static SensorPort PUERTO_COMPASS = SensorPort.S2;
	private static SensorPort PUERTO_SONAR = SensorPort.S3;

	public static void main(String[] args) {
		Behavior avanzar = new Avanzar(PUERTO_SONAR, PUERTO_COMPASS);
		Behavior subir_pala = new SubirPala(Motor.C, PUERTO_SONAR);
		Behavior[] comportamientos = { avanzar, subir_pala };
		Arbitrator arbitro = new Arbitrator(comportamientos);
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
		}
		LCD.drawString("Arranca", 0, 0);
		arbitro.start();
	}
}
