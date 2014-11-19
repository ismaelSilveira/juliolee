import lejos.nxt.LCD;
import lejos.nxt.Sound;

public class SensoresJulioLee2 implements Runnable {
	public final static int BOTON_APRETADO = 1;
	public final static int BOTON_NO_APRETADO = 2;
	
	private Thread t;
	volatile private int distancia;
	private int boton;
	private Comunicacion com;

	public SensoresJulioLee2(Comunicacion com) {
		this.com = com;
		this.distancia = 0;
		this.boton = 0;
	}

	public void start() {
		if (t == null) {
			t = new Thread(this, "SensoresJulioLee2");
			t.start();
		}
	}

	@Override
	public void run() {
		LCD.clear();
		LCD.drawString("SENSORES", 0, 0);

		while (true) {
			if (!com.getComunicandoSP()) {
				Sound.beepSequenceUp();
				com.setComunicandoS(true);
				com.comunicar(Comunicacion.DISTANCIA);
				this.distancia = com.leer();
				com.setComunicandoS(false);
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (!com.getComunicandoSP()) {
				Sound.beepSequenceUp();
				com.setComunicandoS(true);
				com.comunicar(Comunicacion.BOTON);
				this.boton = com.leer();
				com.setComunicandoS(false);
			}
/*
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			
		}
	}

	public int getDistancia() {
		return this.distancia;
	}

	public int getBoton() {
		return this.boton;
	}
}
