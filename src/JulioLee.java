import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class JulioLee {
	// private static SensorPort PUERTO_COMPASS = SensorPort.S2;
	private static SensorPort PUERTO_SONAR_IZQ = SensorPort.S3;
	// private static SensorPort PUERTO_SONAR_DER = SensorPort.S1;
	private static int DISTANCIA_PARED = 21;

	public static void main(String[] args) {
		// Inicializacion de sensores
		UltrasonicSensor sonar_izq = new UltrasonicSensor(PUERTO_SONAR_IZQ);
		CompassHTSensor compass = new CompassHTSensor(SensorPort.S2);
		compass.resetCartesianZero();
		
		Comunicacion com = new Comunicacion();
		com.start();
		
		sensorDistancia distancia = new sensorDistancia(com);
		distancia.start();

		// Inicializacion de comportamientos
		Behavior avanzar = new Avanzar(Motor.B, Motor.A, Motor.C, sonar_izq, DISTANCIA_PARED, com);
		Behavior subir_pala = new SubirPala(Motor.B, Motor.A, Motor.C, sonar_izq, DISTANCIA_PARED, compass, com);
		Behavior bajar_pala = new BajarPala(Motor.C, sonar_izq, DISTANCIA_PARED,com);
		Behavior girar = new Girar(Motor.B, Motor.A, Motor.C, compass, com);
		Behavior sensarYPatear = new SensarYPatear(com, compass, Motor.B, Motor.A);
		Behavior[] comportamientos = { avanzar, bajar_pala, girar, subir_pala, sensarYPatear };
	/*	Motor.B.setSpeed(50);
		Motor.A.setSpeed(50);
		compass.startCalibration();
		Motor.B.rotate(4560, true);
		Motor.A.rotate(-4560);
		compass.stopCalibration();
*/
		Arbitrator arbitro = new Arbitrator(comportamientos);
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
		}
		LCD.drawString("Arranca", 0, 0);
		arbitro.start();
	}
}
