import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;

public class SensarYPatear implements Behavior {
	Comunicacion com;

	public SensarYPatear(Comunicacion com) {
		this.com = com;
	}

	@Override
	public boolean takeControl() {
		return ( (com.getComunicandose() == Comunicacion.GET_CONEXION) && (com.getLectura() == Comunicacion.ACOMODAR) );
	}

	@Override
	public void action() {
		Sound.beep();
		// TODO me acomodo
		com.comunicar(Comunicacion.ACOMODADO);
	}

	@Override
	public void suppress() {
	}

}