import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Behavior;

public class SensarYPatear implements Behavior {
	private Comunicacion com;
	private CompassHTSensor compass;
	private NXTRegulatedMotor motorIzq;
	private NXTRegulatedMotor motorDer;

	public static int AZUL = 1;
	public static int NARANJA = 2;
	public static int NADA = 6;

	public SensarYPatear(Comunicacion com, CompassHTSensor compass,
			NXTRegulatedMotor motorIzq, NXTRegulatedMotor motorDer) {
		this.com = com;
		this.compass = compass;
		this.motorDer = motorDer;
		this.motorIzq = motorIzq;
	}

	@Override
	public boolean takeControl() {
		return com.getComunicando();
	}

	@Override
	public void action() {
		LCD.clear();

		int girarRuedaIzquierda = -1;
		Boolean giro = false;

		// Mientras tenga pelotas arriba
		while (com.getComunicando()) {
			com.comunicar(Comunicacion.SENSAR);
			com.leer();

			// Si no tengo ninguna pelota, termino el comportamiento
			if (com.getLectura() == NADA) {
				com.setComunicando(false);
				continue;
			}

			// Si tengo una pelota naranja, me acomodo para tirar
			if (com.getLectura() == NARANJA) {
				giro = true;
				float angulo = compass.getDegreesCartesian();

				if ((angulo < 45) || (angulo > 315)) { // esta en 0
					girarRuedaIzquierda = 180;
				} else if ((angulo >= 45) && (angulo < 135)) { // esta en 90
					girarRuedaIzquierda = 90;
				} else if ((angulo >= 135) && (angulo < 225)) { // esta en 180
					girarRuedaIzquierda = 0;
				} else {
					girarRuedaIzquierda = 270;
				}
				motorIzq.rotate(Math.round(girarRuedaIzquierda / 90) * 560,
						true);
				motorDer.rotate(Math.round(girarRuedaIzquierda / 90) * (-560),
						false);
				if (girarRuedaIzquierda != 0) {
					motorIzq.rotate(180, true);
					motorDer.rotate(180);
				}
			}

			// Comunico que quiero que patee y espero respuesta
			com.comunicar(Comunicacion.PATEAR);
			com.leer();

			// "bailo" para bajar las pelotas que quedaron trancadas
			motorIzq.rotate(180, true);
			motorDer.rotate(-180);
			motorIzq.rotate(-180, true);
			motorDer.rotate(180);
		}

		// me acomodo para seguir
		if (giro) {
			switch (girarRuedaIzquierda) {
			case 0: // estaba en 180
				girarRuedaIzquierda = 90;
				break;
			case 90: // estaba en 90
				girarRuedaIzquierda = 0;
				break;
			case 180: // estaba en 0
				girarRuedaIzquierda = 270;
				break;
			case 270: // estaba en 270
				girarRuedaIzquierda = 180;
				break;
			}
		} else {
			girarRuedaIzquierda = 90;
		}

		motorIzq.rotate(Math.round(girarRuedaIzquierda / 90) * 570, true);
		motorDer.rotate(Math.round(girarRuedaIzquierda / 90) * (-570), false);
	}

	@Override
	public void suppress() {
	}

}
