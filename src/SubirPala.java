import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;
import lejos.robotics.subsumption.Behavior;
import lejos.util.LogColumn;
import lejos.util.NXTDataLogger;

public class SubirPala implements Behavior {
	private NXTRegulatedMotor motorIzq, motorDer;
	final int DISTANCIA_PARED;
	final int DISTANCIA_ARRIBA_PARED_GRANDE;
	final int DISTANCIA_ARRIBA_ZM_PARED;

	private UltrasonicSensor sonar_izq;
	private NXTRegulatedMotor pala;
	private Comunicacion com;
	private SensoresJulioLee2 distancia_arriba;
	private CompassHTSensor compass;

	public SubirPala(NXTRegulatedMotor izq, NXTRegulatedMotor der,
			NXTRegulatedMotor motor_pala, UltrasonicSensor s_izq,
			int dist_pared, int dist_arriba, int dist_arr_zonamuerta_pared,
			Comunicacion com, SensoresJulioLee2 s_arriba,
			CompassHTSensor c) {
		distancia_arriba = s_arriba;
		pala = motor_pala;
		pala.resetTachoCount();
		pala.setSpeed(30);
		sonar_izq = s_izq;
		DISTANCIA_PARED = dist_pared;
		DISTANCIA_ARRIBA_PARED_GRANDE = dist_arriba;
		DISTANCIA_ARRIBA_ZM_PARED = dist_arr_zonamuerta_pared;
		motorIzq = izq;
		motorDer = der;
		this.com = com;
		compass = c;

	}

	@Override
	public boolean takeControl() {
		float orientacion = compass.getDegreesCartesian();
		boolean miro_hacia_otra_cancha = orientacion < 45 || orientacion > 315;
		int dist_arriba = distancia_arriba.getDistancia();
		int dist_abajo = sonar_izq.getDistance();

		return dist_abajo <= DISTANCIA_PARED
				&& (dist_arriba <= DISTANCIA_ARRIBA_PARED_GRANDE || (miro_hacia_otra_cancha && dist_arriba <= DISTANCIA_ARRIBA_ZM_PARED));
	}

	@Override
	public void action() {
		motorIzq.rotate(-180, true);
		motorDer.rotate(-180, false);
		pala.rotateTo(0);
		// com.setComunicando(true);
	}

	@Override
	public void suppress() {
	}
}
