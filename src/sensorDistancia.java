public class sensorDistancia implements Runnable {
	private Thread t;
	private int distancia;
	private Comunicacion com;
	
	public sensorDistancia(Comunicacion com) {
		this.com = com;
		this.distancia = 0;
	}

	public void start() {
		if (t == null) {
			t = new Thread(this, "Comunicacion");
			t.start();
		}
	}

	@Override
	public void run() {
		
		while(true){
			if(!com.getComunicando()){
				com.comunicar(Comunicacion.DISTANCIA);
				this.distancia = com.leer();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int getDistancia(){
		return this.distancia;
	}
}
