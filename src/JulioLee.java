import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class JulioLee {
	public static void main(String[] args) {
		// Inicializacion de sensores
		UltrasonicSensor sonar_izq = new UltrasonicSensor(Constante.PUERTO_SONAR_IZQ);
		UltrasonicSensor sonar_der = new UltrasonicSensor(Constante.PUERTO_SONAR_DER);
		CompassHTSensor compass = new CompassHTSensor(Constante.PUERTO_COMPASS);
		compass.resetCartesianZero();
		
		Comunicacion com = new Comunicacion();
		com.start();
		
		SensoresJulioLee2 distancia = new SensoresJulioLee2(com);

		// Inicializacion de comportamientos
		Behavior avanzar = new Avanzar(Motor.B, Motor.A);
		Behavior subir_pala = new SubirPala(Motor.B, Motor.A, Motor.C,
				sonar_izq, com, distancia, compass);
		Behavior bajar_pala = new BajarPala(Motor.C, sonar_izq, com);
		Behavior girar = new Girar(Motor.B, Motor.A, Motor.C, compass, com);
		Behavior sensarYPatear = new SensarYPatear(com, compass, Motor.B, Motor.A);
		//Behavior dispersion = new Dispersion(Motor.B, Motor.A, sonar_izq, sonar_der, distancia, compass);
		Behavior aggregation = new Aggregation(Motor.B, Motor.A, sonar_izq, sonar_der, distancia, compass);
//		Behavior[] comportamientos = { avanzar, bajar_pala, girar, subir_pala, aggregation, sensarYPatear };
		Behavior[] comportamientos = {aggregation};
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
		
		distancia.start();
		
		arbitro.start();
		
	}
}
