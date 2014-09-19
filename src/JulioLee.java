import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.navigation.CompassPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

@SuppressWarnings("deprecation")
public class JulioLee {
	private static SensorPort PUERTO_COMPASS = SensorPort.S2;
	private static SensorPort PUERTO_SONAR_IZQ = SensorPort.S3;
//	private static SensorPort PUERTO_SONAR_DER = SensorPort.S1;
	private static int DISTANCIA_PARED = 24;

	public static void main(String[] args) {
		// Inicializacion de sensores
		UltrasonicSensor sonar_izq = new UltrasonicSensor(PUERTO_SONAR_IZQ);
		CompassHTSensor compass = new CompassHTSensor(PUERTO_COMPASS);

		// Inicializacion de actuadores
		CompassPilot pilot = new CompassPilot(compass, 33, 144, Motor.A,
				Motor.B, false);

		// Inicializacion de comportamientos
		Behavior avanzar = new Avanzar(pilot, sonar_izq, DISTANCIA_PARED);
		Behavior subir_pala = new SubirPala(Motor.C, sonar_izq, null, pilot,
				DISTANCIA_PARED);
		Behavior bajar_pala = new BajarPala(Motor.C, sonar_izq,
				null, DISTANCIA_PARED);
		Behavior[] comportamientos = { avanzar, bajar_pala, subir_pala };
//		Behavior[] comportamientos = { bajar_pala, subir_pala };

		Arbitrator arbitro = new Arbitrator(comportamientos);
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
		}
		LCD.drawString("Arranca", 0, 0);
		arbitro.start();
	}
}
