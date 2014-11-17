import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;

public class Comunicacion implements Runnable {
	private Thread t;
	private int comunicandose;
	private NXTCommConnector conector;
	private NXTConnection conn;
	private DataInputStream dis;
	private DataOutputStream dos;
	private int lectura;
	private Boolean comunicando;

	public final static int SENSAR = 2;
	public final static int PATEAR = 3;
	public final static int DISTANCIA = 4;

	public Comunicacion() {
		conector = RS485.getConnector();
		setComunicando(false);
	}

	public void start() {
		System.out.println("Starting Comunicacion");
		if (t == null) {
			t = new Thread(this, "Comunicacion");
			t.start();
		}
	}

	@Override
	public void run() {
		while (true) {
			// espero a querer comunicarme
			conn = null;
			LCD.clear();
			// LCD.drawString("quiero comunicar", 0, 1);
			conn = conector.connect("JL2", NXTConnection.PACKET);

			if (conn == null) {
				LCD.drawString("Error conexion", 0, 0);
			} else {
				dis = conn.openDataInputStream();
				dos = conn.openDataOutputStream();

				while (conn != null) { }

				try {
					dis.close();
					dos.close();
					conn.close();
				} catch (IOException e) {
					// e.printStackTrace();
					LCD.drawString("error close", 0, 1);
					break;
				}
			}

		}

	}

	public void comunicar(int aComunicar) {
		while (conn == null) {
		}
		try {
			dos.writeInt(aComunicar);
			dos.flush();
		} catch (IOException e) {
			conn.close();
			// e.printStackTrace();
			LCD.drawString("error escribir", 0, 1);
		}
	}

	public int leer() {
		lectura = 0;
		try {
			while (lectura == 0) {
				lectura = dis.readInt();
			}
		} catch (IOException e) {
			conn.close();
			// e.printStackTrace();
			LCD.drawString("error lectura", 0, 1);
		}
		return lectura;
	}

	/**
	 * @return the comunicandose
	 */
	public int getComunicandose() {
		return comunicandose;
	}

	/**
	 * @param comunicandose
	 *            the comunicandose to set
	 */
	public void setComunicandose(int comunicandose) {
		this.comunicandose = comunicandose;
	}

	/**
	 * @return the lectura
	 */
	public int getLectura() {
		return lectura;
	}

	/**
	 * @return the comunicando
	 */
	public Boolean getComunicando() {
		return comunicando;
	}

	/**
	 * @param comunicando
	 *            the comunicando to set
	 */
	public void setComunicando(Boolean comunicando) {
		this.comunicando = comunicando;
	}

}
