public class sensorDistancia implements Runnable {
	private Thread t;
	private int distancia;
	private int boton;
	private Comunicacion com;
	
	public sensorDistancia(Comunicacion com) {
		this.com = com;
		this.distancia = 0;
		this.boton = 0;
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
			if(!com.getComunicando()){
				com.comunicar(Comunicacion.BOTON);
				this.boton = com.leer();
			}
		}
	}

	public int getDistancia(){
		return this.distancia;
	}
	
	public int getBoton(){
		return this.boton;
	}
}
