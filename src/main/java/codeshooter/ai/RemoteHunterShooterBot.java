package codeshooter.ai;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import codeshooter.model.ShooterState;

public class RemoteHunterShooterBot {
	private static Logger LOGGER = Logger.getLogger(RemoteHunterShooterBot.class.getName());

	public static void main(String[] args) {
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);

		ShooterBot shooterBot = new HunterShooterBot();

		LOGGER.info("Connecting to " + hostname + " at port " + port);

		try ( Socket socket = new Socket(hostname, port);
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ) {
			LOGGER.info("Connected to " + hostname + " at port " + port);

			ShooterState shooterState;

			while ( (shooterState = (ShooterState) in.readObject()) != null ) {
				out.writeInt(shooterBot.getKey(shooterState));
				out.flush();
			}
		} catch ( ClassNotFoundException e ) {
			LOGGER.severe("ShooterState class not found");
		} catch ( IOException e ) {
			LOGGER.severe("IOException");
			e.printStackTrace();
		}
	}
}
