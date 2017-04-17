package codeshooter.ai;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import codeshooter.model.ShooterState;

public class RemoteRandomShooterBot {
	private static int[] outputKeys = { KeyEvent.VK_LEFT,
											KeyEvent.VK_RIGHT,
											KeyEvent.VK_UP,
											KeyEvent.VK_DOWN,
											KeyEvent.VK_SPACE
										};

	public static void main(String[] args) {
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);

		try ( Socket socket = new Socket(hostname, port);
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ) {
			ShooterState state;

			while ( (state = (ShooterState) in.readObject()) != null ) {
				if ( state.getHealth() > 0 ) {
					int randomIndex = (new Random()).nextInt(outputKeys.length);
					int key = outputKeys[randomIndex];

					out.writeInt(key);
					out.flush();
				} else {
					break;
				}
			}
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}
