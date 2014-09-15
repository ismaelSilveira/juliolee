import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class JulioLee {
	private static SensorPort PUERTO_COMPASS = SensorPort.S2;
	private static SensorPort PUERTO_SONAR = SensorPort.S3;

	public static void main(String[] args) {
		Behavior[] comportamientos = { new Avanzar(PUERTO_SONAR, PUERTO_COMPASS) };
		Arbitrator arbitro = new Arbitrator(comportamientos);
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
		}
		arbitro.start();
	}
}
