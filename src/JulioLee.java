import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class JulioLee {
	private static SensorPort PUERTO_COMPASS = SensorPort.S2;
	private static SensorPort PUERTO_SONAR_IZQ = SensorPort.S3;
	private static SensorPort PUERTO_SONAR_DER = SensorPort.S1;
	private static int DISTANCIA_PARED = 25;

	public static void main(String[] args) {
		Behavior avanzar = new Avanzar(PUERTO_SONAR_IZQ, PUERTO_COMPASS,
				DISTANCIA_PARED);
		Behavior parar = new Parar(PUERTO_SONAR_IZQ, PUERTO_SONAR_DER,
				PUERTO_COMPASS, DISTANCIA_PARED);
		Behavior subir_pala = new SubirPala(Motor.C, PUERTO_SONAR_IZQ,
				PUERTO_SONAR_DER, DISTANCIA_PARED);
		Behavior bajar_pala = new BajarPala(Motor.C, PUERTO_SONAR_IZQ,
				PUERTO_SONAR_DER, DISTANCIA_PARED);
		// Behavior[] comportamientos = { avanzar, subir_pala, bajar_pala };
		Behavior[] comportamientos = { avanzar, subir_pala, bajar_pala, parar };
		Arbitrator arbitro = new Arbitrator(comportamientos);
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
		}
		LCD.drawString("Arranca", 0, 0);
		arbitro.start();
	}
}
