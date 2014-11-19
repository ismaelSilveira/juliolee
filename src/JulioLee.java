import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class JulioLee {
	private static SensorPort PUERTO_COMPASS = SensorPort.S2;
	private static SensorPort PUERTO_SONAR_IZQ = SensorPort.S3;
	private static SensorPort PUERTO_SONAR_DER = SensorPort.S1;
	private static int DISTANCIA_PARED = 21;
	private static int DISTANCIA_ARRIBA = 296;
	private static int DISTANCIA_ARRIBA_ZM_PARED = 1303;
	private static int DISTANCIA_ARRIBA_ZM = 1093;
	private static int DISTANCIA_AGGREGATION_MAX = 40;
	private static int DISTANCIA_AGGREGATION_MIN = 35;
	private static int DISTANCIA_DISPERSION_MAX = 30;
	
	public static void main(String[] args) {
		// Inicializacion de sensores
		UltrasonicSensor sonar_izq = new UltrasonicSensor(PUERTO_SONAR_IZQ);
		UltrasonicSensor sonar_der = new UltrasonicSensor(PUERTO_SONAR_DER);
		CompassHTSensor compass = new CompassHTSensor(PUERTO_COMPASS);
		compass.resetCartesianZero();

		Comunicacion com = new Comunicacion();
		//com.start();

		SensoresJulioLee2 distancia = new SensoresJulioLee2(com);
		//distancia.start();

		// Inicializacion de comportamientos
		Behavior avanzar = new Avanzar(Motor.B, Motor.A);
		Behavior subir_pala = new SubirPala(Motor.B, Motor.A, Motor.C,
				sonar_izq, DISTANCIA_PARED, DISTANCIA_ARRIBA,
				DISTANCIA_ARRIBA_ZM_PARED, com, distancia, compass);
		Behavior bajar_pala = new BajarPala(Motor.C, sonar_izq,
				DISTANCIA_PARED, com);
		Behavior girar = new Girar(Motor.B, Motor.A, Motor.C, compass, com);
		Behavior sensarYPatear = new SensarYPatear(com, compass, Motor.B, Motor.A);
		Behavior dispersion = new Dispersion(Motor.B, Motor.A, sonar_izq, sonar_der, DISTANCIA_PARED, DISTANCIA_ARRIBA,
				DISTANCIA_ARRIBA_ZM, distancia, compass, DISTANCIA_DISPERSION_MAX);
		Behavior aggregation = new Aggregation(Motor.B, Motor.A, sonar_izq, sonar_der, DISTANCIA_PARED, DISTANCIA_ARRIBA,
				DISTANCIA_ARRIBA_ZM, distancia, compass, DISTANCIA_AGGREGATION_MAX, DISTANCIA_AGGREGATION_MIN);
		Behavior[] comportamientos = { avanzar, bajar_pala, girar, subir_pala, sensarYPatear };

		/*
		 * Motor.B.setSpeed(50); Motor.A.setSpeed(50);
		 * compass.startCalibration(); Motor.B.rotate(4560, true);
		 * Motor.A.rotate(-4560); compass.stopCalibration();
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
