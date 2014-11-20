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
	private float dist_abajo = 0;
	private boolean miro_hacia_otra_cancha = false;

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
		miro_hacia_otra_cancha = orientacion < 45 || orientacion > 315;
		int dist_arriba = sensores.getDistancia();
		dist_abajo = sonar_izq.getDistance() + sonar_der.getDistance() / 2;

		return ((dist_abajo >= Constante.DISTANCIA_AGGREGATION_MIN) && (dist_abajo < Constante.DISTANCIA_AGGREGATION_MAX))
				&& ((miro_hacia_otra_cancha && (dist_arriba/10 - dist_abajo - Constante.DISTANCIA_ARRIBA_ZM/10) > 3) || (dist_arriba/10 - dist_abajo) > Constante.DIFERNECIA_ARRIBA_Y_ABAJO);
	}

	@Override
	public void action() {
		Sound.beep();
		active = true;
		velocidadAnteriorIzq = this.motorIzq.getSpeed();
		velocidadAnteriorDer = this.motorDer.getSpeed();
		Sound.beepSequence();
		
		this.motorIzq.setSpeed(Constante.max_vel_izq);
		this.motorDer.setSpeed(Constante.max_vel_der);
		
		while(active && (dist_abajo >= Constante.DISTANCIA_AGGREGATION_MIN) && (dist_abajo < Constante.DISTANCIA_AGGREGATION_MAX)){
			this.motorIzq.forward();
			this.motorDer.forward();
		}
		
		this.motorIzq.stop(true);
		this.motorDer.stop();
		
	}

	@Override
	public void suppress() {
		active = false;
		this.motorIzq.setSpeed(Constante.avanzar_vel_izq);
		this.motorDer.setSpeed(Constante.avanzar_vel_der);
	}

}
