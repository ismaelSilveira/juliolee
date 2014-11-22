import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;
import lejos.robotics.subsumption.Behavior;
import lejos.util.LogColumn;
import lejos.util.NXTDataLogger;


public class Dispersion implements Behavior {

	private NXTRegulatedMotor motorIzq, motorDer;
	private UltrasonicSensor sonar_izq, sonar_der;
	private SensoresJulioLee2 sensores;
	private CompassHTSensor compass;
	private Boolean active;
	private int dist_abajo = 0;
	
	public Dispersion(NXTRegulatedMotor izq, NXTRegulatedMotor der, UltrasonicSensor s_izq, UltrasonicSensor s_der,
			SensoresJulioLee2 sensores, CompassHTSensor c) {
		this.sensores = sensores;
		this.sonar_izq = s_izq;
		this.sonar_der = s_der;
		this.compass = c;
		this.motorIzq = izq;
		this.motorDer = der;
		active = false;
	
	}

	@Override
	public boolean takeControl() {
		float orientacion = compass.getDegreesCartesian();
		boolean miro_hacia_otra_cancha = orientacion < 45 || orientacion > 315;
		int dist_arriba = sensores.getDistancia();
		int dist_der = sonar_der.getDistance();
		int dist_izq = sonar_izq.getDistance();
		dist_abajo = Math.min(dist_izq, dist_der);
		
		boolean take = (dist_abajo < Constante.DISTANCIA_DISPERSION_MAX)
				&& (
						(miro_hacia_otra_cancha && (dist_arriba / 10 - dist_abajo - Constante.DISTANCIA_ARRIBA_ZM / 10) > Constante.DIFERNECIA_ARRIBA_Y_ABAJO) 
						|| ((!miro_hacia_otra_cancha) && (dist_arriba / 10 - dist_abajo) > Constante.DIFERNECIA_ARRIBA_Y_ABAJO)
					);
		
		return take;
	}

	@Override
	public void action() {
		Sound.twoBeeps();
		active = true;
		
		motorDer.backward();
		motorIzq.backward();
	
		dist_abajo = Math.min(sonar_izq.getDistance(), sonar_der.getDistance());
		
		while(active && (sensores.getBoton() == SensoresJulioLee2.BOTON_NO_APRETADO ) && (dist_abajo <= Constante.DISTANCIA_DISPERSION_MAX))
			dist_abajo = Math.min(sonar_izq.getDistance(), sonar_der.getDistance());
		
		motorIzq.stop(true);
		motorDer.stop();
	
		// si sali del while porque el boton de atras estaba apretado, espero sin hacer nada
		if(sensores.getBoton() == SensoresJulioLee2.BOTON_APRETADO){
			while(active && (dist_abajo <= Constante.DISTANCIA_DISPERSION_MAX))
				dist_abajo = Math.min(sonar_izq.getDistance(), sonar_der.getDistance());
		}
		
	}

	@Override
	public void suppress() {
		active = false;
	}
}
