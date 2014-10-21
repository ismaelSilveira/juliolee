import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.addon.CompassHTSensor;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;
import lejos.robotics.pathfinding.GridNode;
import lejos.robotics.subsumption.Behavior;
import lejos.util.LogColumn;
import lejos.util.NXTDataLogger;

public class SensarYPatear implements Behavior {
	private Comunicacion com;
	private int lectura;
	private CompassHTSensor compass;
	private NXTRegulatedMotor motorIzq;
	private NXTRegulatedMotor motorDer;
	
	/* TEST */
	/*
	static NXTDataLogger logger = new NXTDataLogger();
	static LogColumn r = new LogColumn("Grados",
			LogColumn.DT_FLOAT);
	static LogColumn[] columnDefs = new LogColumn[] { r };
	*/
	/* TEST */
	
	public static int AZUL = 1;
	public static int NARANJA = 2;

	public SensarYPatear(Comunicacion com, CompassHTSensor compass, NXTRegulatedMotor motorIzq, NXTRegulatedMotor motorDer) {
		this.com = com;
		this.compass = compass;
		this.motorDer = motorDer;
		this.motorIzq = motorIzq;
		/*
		NXTConnection connection = USB.waitForConnection();
		try {
			logger.startRealtimeLog(connection);
		} catch (Exception e) {
		}
		
		logger.setColumns(columnDefs); // must be after startRealtimeLog()
		*/
	}

	@Override
	public boolean takeControl() {
		return com.getComunicandose() == Comunicacion.GET_CONEXION;
	}

	@Override
	public void action() {
		//Sound.beep();
		LCD.clear();
		
		int girarRuedaIzquierda = -1;
		while (com.getComunicandose() == Comunicacion.GET_CONEXION) {
			com.comunicar(Comunicacion.SENSAR);
			lectura = com.leer();
			while ((com.getComunicandose() == Comunicacion.GET_CONEXION)
					&& (lectura != AZUL) && (lectura != NARANJA)) {
				lectura = com.leer();
				// LCD.drawInt(lectura, 0, i);
			}
			LCD.drawInt(lectura, 0, 1);
			if (lectura == NARANJA) {
				// me acomodo para tirar
				//Sound.playTone(440, 1000);
				float angulo = compass.getDegreesCartesian();
				
				if((angulo < 45) || (angulo > 315)){ // esta en 0
					girarRuedaIzquierda = 180;
				}else if((angulo >= 45) && (angulo < 135)){ // esta en 90
					girarRuedaIzquierda = 90;
				}else if((angulo >= 135) && (angulo < 225)){ // esta en 180
					girarRuedaIzquierda = 0;
				}else{
					girarRuedaIzquierda = 270;
				}
				/*
				logger.writeLog(angulo);
				logger.finishLine();
				*/
				motorIzq.rotate(Math.round(girarRuedaIzquierda / 90) * 570, true);
				motorDer.rotate(Math.round(girarRuedaIzquierda / 90) * (-570), false);
			}

			com.comunicar(Comunicacion.PATEAR);
			lectura = com.leer();
			while ((com.getComunicandose() == Comunicacion.GET_CONEXION)
					&& (lectura != Comunicacion.PATEAR)) {
				lectura = com.leer();
				// LCD.drawInt(lectura, 0, i);
			}

			// LCD.drawInt(lectura, 1, 0);
			//Sound.twoBeeps();
			//Sound.twoBeeps();
		}
		
		//Sound.beepSequenceUp();
		// me acomodo para seguir
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
		motorIzq.rotate(Math.round(girarRuedaIzquierda / 90) * 570, true);
		motorDer.rotate(Math.round(girarRuedaIzquierda / 90) * (-570), false);
	}

	@Override
	public void suppress() {
	}

}
