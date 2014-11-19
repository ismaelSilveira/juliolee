import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Behavior;

public class Aggregation implements Behavior {
	final int DISTANCIA_PARED;
	final int DISTANCIA_ARRIBA_PARED_GRANDE;
	final int DISTANCIA_ARRIBA_ZM;
	final int DISTANCIA_AGGREGATION_MAX;
	final int DISTANCIA_AGGREGATION_MIN;

	private NXTRegulatedMotor motorIzq, motorDer;
	private UltrasonicSensor sonar_izq, sonar_der;
	private SensoresJulioLee2 sensores;
	private CompassHTSensor compass;
	private Boolean active;
	private int velocidadAnteriorIzq, velocidadAnteriorDer;
	
	public Aggregation(NXTRegulatedMotor izq, NXTRegulatedMotor der, UltrasonicSensor s_izq, UltrasonicSensor s_der,
			int dist_pared, int dist_arriba, int dist_arr_zonamuerta_pared, SensoresJulioLee2 sensores,
			CompassHTSensor c, int max_distancia, int min_distancia) {
		this.sensores = sensores;
		this.sonar_izq = s_izq;
		this.sonar_der = s_der;
		DISTANCIA_PARED = dist_pared;
		DISTANCIA_ARRIBA_PARED_GRANDE = dist_arriba;
		DISTANCIA_ARRIBA_ZM = dist_arr_zonamuerta_pared;
		this.compass = c;
		DISTANCIA_AGGREGATION_MAX = max_distancia;
		DISTANCIA_AGGREGATION_MIN = min_distancia;
		this.motorIzq = izq;
		this.motorDer = der;
	}

	@Override
	public boolean takeControl() {
		float orientacion = compass.getDegreesCartesian();
		boolean miro_hacia_otra_cancha = orientacion < 45 || orientacion > 315;
		int dist_arriba = sensores.getDistancia();
		float dist_abajo = (sonar_izq.getDistance() + sonar_der.getDistance()) / 2;
		
		return ((dist_abajo >= DISTANCIA_AGGREGATION_MIN) || (dist_abajo < DISTANCIA_AGGREGATION_MAX))
				&& ((miro_hacia_otra_cancha && (dist_arriba - dist_abajo - DISTANCIA_ARRIBA_ZM) > 15) || (dist_arriba - dist_abajo) > 15);
	}

	@Override
	public void action() {
		this.motorIzq.setSpeed(900);
		this.motorDer.setSpeed(900);
		velocidadAnteriorIzq = this.motorIzq.getSpeed();
		velocidadAnteriorDer = this.motorDer.getSpeed();
	}

	@Override
	public void suppress() {
		this.motorIzq.setSpeed(velocidadAnteriorIzq);
		this.motorDer.setSpeed(velocidadAnteriorDer);
	}

}
