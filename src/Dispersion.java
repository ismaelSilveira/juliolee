import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Behavior;


public class Dispersion implements Behavior {

	private NXTRegulatedMotor motorIzq, motorDer;
	private UltrasonicSensor sonar_izq, sonar_der;
	private SensoresJulioLee2 sensores;
	private CompassHTSensor compass;
	private Boolean active;
	
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
		float dist_abajo = sonar_izq.getDistance() /* + sonar_der.getDistance()) / 2*/;
		
		return dist_abajo <= Constante.DISTANCIA_DISPERSION_MAX
				&& ((miro_hacia_otra_cancha && (dist_arriba/10 - dist_abajo - Constante.DISTANCIA_ZM) > Constante.DIFERNECIA_ARRIBA_Y_ABAJO) || (dist_arriba/10 - dist_abajo) > Constante.DIFERNECIA_ARRIBA_Y_ABAJO);
	}

	@Override
	public void action() {
		Sound.beep();
		active = true;
		motorDer.backward();
		motorIzq.backward();
		
		while(active && (sonar_izq.getDistance() <= Constante.DISTANCIA_DISPERSION_MAX) && (sensores.getBoton() == SensoresJulioLee2.BOTON_NO_APRETADO ))
			Thread.yield();
		
		motorIzq.stop(true);
		motorDer.stop();
	
		// si sali del while porque el boton de atras estaba apretado, espero sin hacer nada
		if(sensores.getBoton() == SensoresJulioLee2.BOTON_APRETADO){
			while(active && (sonar_izq.getDistance() <= Constante.DISTANCIA_DISPERSION_MAX))
				Thread.yield();
		}
		
	}

	@Override
	public void suppress() {
		active = false;
	}

}
