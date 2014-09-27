import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

@SuppressWarnings("deprecation")
public class JulioLee {
	// private static SensorPort PUERTO_COMPASS = SensorPort.S2;
	private static SensorPort PUERTO_SONAR_IZQ = SensorPort.S3;
	// private static SensorPort PUERTO_SONAR_DER = SensorPort.S1;
	private static int DISTANCIA_PARED = 24;

	public static void main(String[] args) {
		// Inicializacion de sensores
		UltrasonicSensor sonar_izq = new UltrasonicSensor(PUERTO_SONAR_IZQ);
		// CompassHTSensor compass = new CompassHTSensor(PUERTO_COMPASS);

		// Inicializacion de actuadores
		DifferentialPilot pilot = new DifferentialPilot(40, 170, Motor.B, Motor.A);

		// Inicializacion de comportamientos
		Behavior avanzar = new Avanzar(pilot, sonar_izq, DISTANCIA_PARED);
		Behavior subir_pala = new SubirPala(Motor.C, sonar_izq, pilot, DISTANCIA_PARED);
		Behavior bajar_pala = new BajarPala(Motor.C, sonar_izq, DISTANCIA_PARED);
		Behavior girar = new Girar(Motor.C, pilot, sonar_izq);
		Behavior[] comportamientos = { avanzar, girar, bajar_pala, subir_pala };

		Arbitrator arbitro = new Arbitrator(comportamientos);
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
		}
		LCD.drawString("Arranca", 0, 0);
		arbitro.start();
	}
}
