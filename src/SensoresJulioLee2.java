import lejos.nxt.Sound;

public class SensoresJulioLee2 implements Runnable {
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

		while (true) {
			if (!com.getComunicando()) {
				com.comunicar(Comunicacion.DISTANCIA);
				this.distancia = com.leer();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!com.getComunicando()) {
				com.comunicar(Comunicacion.BOTON);
				this.boton = com.leer();
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int getDistancia() {
		return this.distancia;
	}

	public int getBoton() {
		return this.boton;
	}
}
