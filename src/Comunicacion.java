import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

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
	public static int ACOMODAR = 2;
	public static int ACOMODADO = 3;
	public static int FIN_SENSADO = 4;

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
			while (comunicandose == SIN_COMUNICACION) {
			}

			conn = conector.connect("JL2", NXTConnection.PACKET);
			if (conn == null) {
				comunicandose = SIN_COMUNICACION;
			}
			dis = conn.openDataInputStream();
			dos = conn.openDataOutputStream();

			while (comunicandose != SIN_COMUNICACION) {
				lectura = 0;
				try {
					lectura = dis.readInt();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
			}
			
			comunicandose = SIN_COMUNICACION;
			try {
				dis.close();
				dos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}

			conn.close();
		}

	}

	public void comunicar(int aComunicar) {
		try {
			dos.writeInt(aComunicar);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
