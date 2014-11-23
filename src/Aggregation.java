import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Behavior;

public class Aggregation implements Behavior {
	private NXTRegulatedMotor motorIzq, motorDer;
	private UltrasonicSensor sonar_izq, sonar_der;
	private SensoresJulioLee2 sensores;
	private CompassHTSensor compass;
	private Boolean active;
	private int dist_abajo = 0;
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
		int dist_der = sonar_der.getDistance();
		int dist_izq = sonar_izq.getDistance();
		dist_abajo = Math.min(dist_izq, dist_der);

		boolean take = ((dist_abajo >= Constante.DISTANCIA_AGGREGATION_MIN) && (dist_abajo < Constante.DISTANCIA_AGGREGATION_MAX))
				&& (
						(miro_hacia_otra_cancha && (dist_arriba / 10 - dist_abajo - Constante.DISTANCIA_ARRIBA_ZM / 10) > Constante.DIFERNECIA_ARRIBA_Y_ABAJO) 
						|| ((!miro_hacia_otra_cancha) && (dist_arriba / 10 - dist_abajo) > Constante.DIFERNECIA_ARRIBA_Y_ABAJO)
					);
		
		return take;
	}

	@Override
	public void action() {
		active = true;

		this.motorIzq.setSpeed(Constante.max_vel_izq);
		this.motorDer.setSpeed(Constante.max_vel_der);

		dist_abajo = Math.min(sonar_izq.getDistance(), sonar_der.getDistance());
		
		while (active && (dist_abajo >= Constante.DISTANCIA_AGGREGATION_MIN) && (dist_abajo < Constante.DISTANCIA_AGGREGATION_MAX) ){
			if ((dist_abajo > Constante.DISTANCIA_AGGREGATION_MIN)
					&& (dist_abajo < Constante.DISTANCIA_AGGREGATION_MAX)){
				
				this.motorIzq.forward();
				this.motorDer.forward();
				
				while ((dist_abajo > Constante.DISTANCIA_AGGREGATION_MIN)
					&& (dist_abajo < Constante.DISTANCIA_AGGREGATION_MAX)) {
					dist_abajo = Math.min(sonar_izq.getDistance(), sonar_der.getDistance());
				}
				
				this.motorIzq.stop(true);
				this.motorDer.stop();
			}
			
			dist_abajo = Math.min(sonar_izq.getDistance(), sonar_der.getDistance());
		}

	}

	@Override
	public void suppress() {
		active = false;
		this.motorIzq.setSpeed(Constante.avanzar_vel_izq);
		this.motorDer.setSpeed(Constante.avanzar_vel_der);
	}

}
