import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Behavior;

public class Aggregation implements Behavior {
	private NXTRegulatedMotor motorIzq, motorDer;
	private UltrasonicSensor sonar_izq, sonar_der;
	private SensoresJulioLee2 sensores;
	private CompassHTSensor compass;
	private Boolean active;
	private int velocidadAnteriorIzq, velocidadAnteriorDer;

	public Aggregation(NXTRegulatedMotor izq, NXTRegulatedMotor der,
			UltrasonicSensor s_izq, UltrasonicSensor s_der,
			SensoresJulioLee2 sensores, CompassHTSensor c) {
		this.sensores = sensores;
		this.sonar_izq = s_izq;
		this.sonar_der = s_der;
		this.compass = c;
		this.motorIzq = izq;
		this.motorDer = der;
	}

	@Override
	public boolean takeControl() {
		float orientacion = compass.getDegreesCartesian();
		boolean miro_hacia_otra_cancha = orientacion < 45 || orientacion > 315;
		int dist_arriba = sensores.getDistancia();
		float dist_abajo = sonar_izq.getDistance() /* + sonar_der.getDistance()) / 2*/;

		return ((dist_abajo >= Constante.DISTANCIA_AGGREGATION_MIN) && (dist_abajo < Constante.DISTANCIA_AGGREGATION_MAX));
			//	&& ((miro_hacia_otra_cancha && (dist_arriba - dist_abajo - Constante.DISTANCIA_ARRIBA_ZM) > Constante.DIFERNECIA_ARRIBA_Y_ABAJO) || (dist_arriba - dist_abajo) > Constante.DIFERNECIA_ARRIBA_Y_ABAJO);
	}

	@Override
	public void action() {
		active = true;
		Sound.beepSequence();
		velocidadAnteriorIzq = this.motorIzq.getSpeed();
		velocidadAnteriorDer = this.motorDer.getSpeed();
		
		this.motorIzq.setSpeed(900);
		this.motorDer.setSpeed(900);
		
		while(active && (sonar_izq.getDistance() >= Constante.DISTANCIA_AGGREGATION_MIN) && (sonar_izq.getDistance() < Constante.DISTANCIA_AGGREGATION_MAX)){
			this.motorIzq.forward();
			this.motorDer.forward();
		}
		
		this.motorIzq.stop(true);
		this.motorDer.stop();
		
	}

	@Override
	public void suppress() {
		active = false;
		this.motorIzq.setSpeed(velocidadAnteriorIzq);
		this.motorDer.setSpeed(velocidadAnteriorDer);
	}

}
