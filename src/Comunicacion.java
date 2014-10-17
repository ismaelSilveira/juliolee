import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import lejos.nxt.LCD;
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

	public static int SIN_COMUNICACION = 0;
	public static int GET_CONEXION = 1;
	public static int SENSAR = 2;
	public static int PATEAR = 3;

	public Comunicacion() {
		conector = RS485.getConnector();
		comunicandose = SIN_COMUNICACION;
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
			while (comunicandose == SIN_COMUNICACION) {
			}
			LCD.clear();
			// LCD.drawString("quiero comunicar", 0, 1);
			conn = conector.connect("JL2", NXTConnection.PACKET);
			if (conn == null) {
				comunicandose = SIN_COMUNICACION;
			}

			dis = conn.openDataInputStream();
			dos = conn.openDataOutputStream();

			while (comunicandose == GET_CONEXION) {
			}

			try {
				dis.close();
				dos.close();
				conn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// LCD.drawString("error close", 0, 1);
				comunicandose = SIN_COMUNICACION;
				break;
			}

		}

	}

	public void comunicar(int aComunicar) {
		while ((conn == null) && (comunicandose != SIN_COMUNICACION)) {
		}
		if (comunicandose == GET_CONEXION) {
			try {
				dos.writeInt(aComunicar);
				dos.flush();
			} catch (IOException e) {
				// e.printStackTrace();
				LCD.drawString("error escribir", 0, 1);
				comunicandose = SIN_COMUNICACION;
			}
		}
	}

	public int leer() {
		lectura = 0;
		try {
			lectura = dis.readInt();
		} catch (IOException e) {
			// e.printStackTrace();
			LCD.drawString("error lectura", 0, 1);
			comunicandose = SIN_COMUNICACION;
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

}
