import lejos.nxt.LCD;
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
		LCD.clear();
		int i = 0;
		while(com.getComunicandose() == Comunicacion.GET_CONEXION){
			com.comunicar(Comunicacion.SENSAR);
			lectura = com.leer();
			while((com.getComunicandose() == Comunicacion.GET_CONEXION) && (lectura != AZUL) && (lectura != NARANJA)){
				lectura = com.leer();
				LCD.drawInt(lectura, 0, i);
			}
			
			if(lectura == NARANJA){
				// TODO me acomodo para tirar
				Sound.beep();
			}
			i++;
			com.comunicar(Comunicacion.PATEAR);
			lectura = com.leer();
			while((com.getComunicandose() == Comunicacion.GET_CONEXION) && (lectura != Comunicacion.PATEAR)){
				lectura = com.leer();
				LCD.drawInt(lectura, 0, i);
			} 
			i++;
			//LCD.drawInt(lectura, 1, 0);
			Sound.twoBeeps();
			Sound.twoBeeps();
			
		}
	}

	@Override
	public void suppress() {
	}

}
