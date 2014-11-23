import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Behavior;

public class SubirPala implements Behavior {
	private NXTRegulatedMotor motorIzq, motorDer;

	private UltrasonicSensor sonar_izq;
	private NXTRegulatedMotor pala;
	private Comunicacion com;
	private SensoresJulioLee2 distancia_arriba;
	private CompassHTSensor compass;

	public SubirPala(NXTRegulatedMotor izq, NXTRegulatedMotor der,
			NXTRegulatedMotor motor_pala, UltrasonicSensor s_izq,
			Comunicacion com, SensoresJulioLee2 s_arriba,
			CompassHTSensor c) {
		distancia_arriba = s_arriba;
		pala = motor_pala;
		pala.resetTachoCount();
		pala.setSpeed(30);
		sonar_izq = s_izq;
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

		return dist_abajo <= Constante.DISTANCIA_PARED
				&& (miro_hacia_otra_cancha && dist_arriba <= Constante.DISTANCIA_ARRIBA_ZM_PARED) || dist_arriba <= Constante.DISTANCIA_ARRIBA;
	}

	@Override
	public void action() {
		motorIzq.rotate(-180, true);
		motorDer.rotate(-180, false);
		pala.rotateTo(0);
		com.setComunicandoSP(true);
	}

	@Override
	public void suppress() {
	}
}
