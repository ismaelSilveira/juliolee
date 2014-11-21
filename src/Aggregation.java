import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;
import lejos.robotics.subsumption.Behavior;
import lejos.util.LogColumn;
import lejos.util.NXTDataLogger;

public class Aggregation implements Behavior {
	private NXTRegulatedMotor motorIzq, motorDer;
	private UltrasonicSensor sonar_izq, sonar_der;
	private SensoresJulioLee2 sensores;
	private CompassHTSensor compass;
	private Boolean active;
	private int velocidadAnteriorIzq, velocidadAnteriorDer;
	private float dist_abajo = 0;
	private boolean miro_hacia_otra_cancha = false;

	/************************ loggin **************************/
	static NXTDataLogger logger = new NXTDataLogger();
	static LogColumn r = new LogColumn("Miro a ZM", LogColumn.DT_BOOLEAN);
	static LogColumn g = new LogColumn("Dist der", LogColumn.DT_INTEGER);
	static LogColumn b = new LogColumn("Dist izq", LogColumn.DT_INTEGER);
	static LogColumn a = new LogColumn("Dist arr", LogColumn.DT_INTEGER);
	static LogColumn c1 = new LogColumn("d_abjo >= D_AGG_MIN", LogColumn.DT_BOOLEAN);
	static LogColumn c2 = new LogColumn("d_ab < D_AGG_MAX", LogColumn.DT_BOOLEAN);
	static LogColumn c3 = new LogColumn("d_ar / 10 - d_ab - D_ARR_ZM / 10 > 3", LogColumn.DT_BOOLEAN);
	static LogColumn c4 = new LogColumn("d_ar / 10 - d_ab > D_ARR_Y_AB", LogColumn.DT_BOOLEAN);
	static LogColumn c5 = new LogColumn("agregue", LogColumn.DT_BOOLEAN);
	static LogColumn[] columnDefs = new LogColumn[] { r, g, b, a, c1, c2, c3,
			c4, c5 };
	
	/************************ loggin **************************/

	public Aggregation(NXTRegulatedMotor izq, NXTRegulatedMotor der,
			UltrasonicSensor s_izq, UltrasonicSensor s_der,
			SensoresJulioLee2 sensores, CompassHTSensor c) {

		/************************ loggin **************************/
		NXTConnection connection = USB.waitForConnection();
		try {
			logger.startRealtimeLog(connection);
		} catch (Exception e) {
		}

		logger.setColumns(columnDefs);
		/************************ loggin **************************/

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
		dist_abajo = Math.min(dist_der, dist_der);

		/************************ loggin **************************/
		logger.writeLog(miro_hacia_otra_cancha);
		logger.writeLog(dist_der);
		logger.writeLog(dist_izq);
		logger.writeLog(dist_arriba);
		logger.writeLog(dist_abajo >= Constante.DISTANCIA_AGGREGATION_MIN);
		logger.writeLog(dist_abajo < Constante.DISTANCIA_AGGREGATION_MAX);
		logger.writeLog((dist_arriba / 10 - dist_abajo - Constante.DISTANCIA_ARRIBA_ZM / 10) > 3);
		logger.writeLog((dist_arriba / 10 - dist_abajo) > Constante.DIFERNECIA_ARRIBA_Y_ABAJO);

		/************************ loggin **************************/

		boolean take = ((dist_abajo >= Constante.DISTANCIA_AGGREGATION_MIN) && (dist_abajo < Constante.DISTANCIA_AGGREGATION_MAX))
				&& ((miro_hacia_otra_cancha && (dist_arriba / 10 - dist_abajo - Constante.DISTANCIA_ARRIBA_ZM / 10) > 3) || (dist_arriba / 10 - dist_abajo) > Constante.DIFERNECIA_ARRIBA_Y_ABAJO);
		logger.writeLog(take);
		logger.finishLine();
		return take;
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

//		while (active && (dist_abajo >= Constante.DISTANCIA_AGGREGATION_MIN)
//				&& (dist_abajo < Constante.DISTANCIA_AGGREGATION_MAX)) {
//			this.motorIzq.forward();
//			this.motorDer.forward();
//		}

		this.motorIzq.stop(true);
		this.motorDer.stop();

	}

	@Override
	public void suppress() {
		active = false;
		this.motorIzq.setSpeed(Constante.avanzar_vel_izq);
		this.motorDer.setSpeed(Constante.avanzar_vel_der);
	}

	@Override
	protected void finalize() {
		logger.stopLogging();
	}

}
