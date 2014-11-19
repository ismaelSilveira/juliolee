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
	private UltrasonicSensor sonar_izq;
	private SensoresJulioLee2 sensores;
	private CompassHTSensor compass;
	private Boolean active;
	
	public Aggregation(NXTRegulatedMotor izq, NXTRegulatedMotor der, UltrasonicSensor s_izq,
			int dist_pared, int dist_arriba, int dist_arr_zonamuerta_pared, SensoresJulioLee2 sensores,
			CompassHTSensor c, int max_distancia, int min_distancia) {
		this.sensores = sensores;
		this.sonar_izq = s_izq;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
