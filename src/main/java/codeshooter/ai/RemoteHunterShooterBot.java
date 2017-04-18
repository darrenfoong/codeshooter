package codeshooter.ai;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import codeshooter.model.ShooterState;

public class RemoteHunterShooterBot {
	public static void main(String[] args) {
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);

		ShooterBot shooterBot = new HunterShooterBot();

		try ( Socket socket = new Socket(hostname, port);
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ) {
			ShooterState shooterState;

			while ( (shooterState = (ShooterState) in.readObject()) != null ) {
				out.writeInt(shooterBot.getKey(shooterState));
				out.flush();
			}
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}
