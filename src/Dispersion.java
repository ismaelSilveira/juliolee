import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Behavior;


public class Dispersion implements Behavior {
	final int DISTANCIA_PARED;
	final int DISTANCIA_ARRIBA_PARED_GRANDE;
	final int DISTANCIA_ARRIBA_ZM;
	final int DISTANCIA_DISPERSION_MAX;

	private NXTRegulatedMotor motorIzq, motorDer;
	private UltrasonicSensor sonar_izq, sonar_der;
	private SensoresJulioLee2 sensores;
	private CompassHTSensor compass;
	private Boolean active;
	
	public Dispersion(NXTRegulatedMotor izq, NXTRegulatedMotor der, UltrasonicSensor s_izq, UltrasonicSensor s_der,
			int dist_pared, int dist_arriba, int dist_arr_zonamuerta_pared, SensoresJulioLee2 sensores,
			CompassHTSensor c, int max_distancia) {
		this.sensores = sensores;
		this.sonar_izq = s_izq;
		this.sonar_der = s_der;
		DISTANCIA_PARED = dist_pared;
		DISTANCIA_ARRIBA_PARED_GRANDE = dist_arriba;
		DISTANCIA_ARRIBA_ZM = dist_arr_zonamuerta_pared;
		this.compass = c;
		DISTANCIA_DISPERSION_MAX = max_distancia;
		this.motorIzq = izq;
		this.motorDer = der;
	}

	@Override
	public boolean takeControl() {
		float orientacion = compass.getDegreesCartesian();
		boolean miro_hacia_otra_cancha = orientacion < 45 || orientacion > 315;
		int dist_arriba = sensores.getDistancia();
		float dist_abajo = (sonar_izq.getDistance() + sonar_der.getDistance()) / 2;
		
		return dist_abajo <= DISTANCIA_DISPERSION_MAX
				&& ((miro_hacia_otra_cancha && (dist_arriba - dist_abajo - DISTANCIA_ARRIBA_ZM) > 15) || (dist_arriba - dist_abajo) > 15);
	}

	@Override
	public void action() {
		active = true;
		motorDer.backward();
		motorIzq.backward();
		
		while(active && (sonar_izq.getDistance() <= DISTANCIA_DISPERSION_MAX) && (sensores.getBoton() == 0 ))
			Thread.yield();
		
		motorIzq.stop(true);
		motorDer.stop();
	
		// si sali del while porque el boton de atras estaba apretado, espero sin hacer nada
		if(sensores.getBoton() == 1){
			while(active && (sonar_izq.getDistance() <= DISTANCIA_DISPERSION_MAX))
				Thread.yield();
		}
		
	}

	@Override
	public void suppress() {
		active = false;
	}

}
