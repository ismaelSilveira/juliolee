import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;

public class SensarYPatear implements Behavior {
	Comunicacion com;
	Boolean esperarColor;
	int lectura;
	
	public static int AZUL = 1;
	public static int NARANJA = 2;
	
	public SensarYPatear(Comunicacion com) {
		this.com = com;
	}

	@Override
	public boolean takeControl() {
		return com.getComunicandose() == Comunicacion.GET_CONEXION;
	}

	@Override
	public void action() {
		Sound.beep();
		esperarColor = true;
		
		while(esperarColor){
			com.comunicar(Comunicacion.SENSAR);
			lectura = 0;
			while(esperarColor && (lectura != 0)){
				lectura = com.getLectura();
			}
			
			if(lectura == NARANJA){
				// TODO me acomodo para tirar
				Sound.twoBeeps();
			}
			
			com.comunicar(Comunicacion.PATEAR);
			lectura = 0;
			while(esperarColor && (lectura != Comunicacion.PATEAR)){
				lectura = com.getLectura();
				Sound.twoBeeps();
				Sound.twoBeeps();
			} 
		}
	}

	@Override
	public void suppress() {
		esperarColor = false;
	}

}
