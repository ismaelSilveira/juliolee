import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Behavior;

public class SensarYPatear implements Behavior {
	private Comunicacion com;
	private int lectura;
	private CompassHTSensor compass;
	private NXTRegulatedMotor motorIzq;
	private NXTRegulatedMotor motorDer;
	
	public static int AZUL = 1;
	public static int NARANJA = 2;

	public SensarYPatear(Comunicacion com, CompassHTSensor compass, NXTRegulatedMotor motorIzq, NXTRegulatedMotor motorDer) {
		this.com = com;
		this.compass = compass;
		this.motorDer = motorDer;
		this.motorIzq = motorIzq;
	}

	@Override
	public boolean takeControl() {
		return com.getComunicandose() == Comunicacion.GET_CONEXION;
	}

	@Override
	public void action() {
		//Sound.beep();
		LCD.clear();
		
		int girarRuedaIzquierda = -1;
		while (com.getComunicandose() == Comunicacion.GET_CONEXION) {
			com.comunicar(Comunicacion.SENSAR);
			lectura = com.leer();
			while ((com.getComunicandose() == Comunicacion.GET_CONEXION)
					&& (lectura != AZUL) && (lectura != NARANJA)) {
				lectura = com.leer();
				// LCD.drawInt(lectura, 0, i);
			}

			if (lectura == NARANJA) {
				// me acomodo para tirar
				Sound.playTone(440, 500);
				float angulo = compass.getDegreesCartesian();
				
				if((angulo < 190) && (angulo > 170)){
					girarRuedaIzquierda = 180 - Math.round(angulo);
					motorIzq.rotate((girarRuedaIzquierda % 90) * 570, true);
					motorDer.rotate((girarRuedaIzquierda % 90) * (-570), false);
				}
			}

			com.comunicar(Comunicacion.PATEAR);
			lectura = com.leer();
			while ((com.getComunicandose() == Comunicacion.GET_CONEXION)
					&& (lectura != Comunicacion.PATEAR)) {
				lectura = com.leer();
				// LCD.drawInt(lectura, 0, i);
			}

			// LCD.drawInt(lectura, 1, 0);
			//Sound.twoBeeps();
			//Sound.twoBeeps();
		}
		
		Sound.beepSequenceUp();
		// me acomodo para seguir
		girarRuedaIzquierda = 90 - girarRuedaIzquierda;
		motorIzq.rotate((girarRuedaIzquierda % 90) * 570, true);
		motorDer.rotate((girarRuedaIzquierda % 90) * (-570), false);
	}

	@Override
	public void suppress() {
	}

}
