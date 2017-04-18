package codeshooter.ai;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import codeshooter.model.Shooter;
import codeshooter.model.ShooterState;

public class RemoteProxyBot extends ShooterBot {
	private int port;

	private static Logger LOGGER = Logger.getLogger(RemoteProxyBot.class.getName());

	public RemoteProxyBot(int port) {
		LOGGER.info("Creating RemoteProxyBot listening at port " + port);

		this.port = port;
	}

	@Override
	public void start(Shooter shooter) {
		runnable = new Runnable() {
			@Override
			public void run() {
				LOGGER.info("Waiting for connection at port " + port);

				try ( ServerSocket serverSocket = new ServerSocket(port);
						Socket clientSocket = serverSocket.accept();
						ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
						ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream()); ) {
					LOGGER.info("Connected at port " + port);

					while ( true ) {
						ShooterState state = new ShooterState(shooter.getHealth(),
																shooter.getSensor().getReadings(),
																shooter.getSensor().getTypes());

						out.writeObject(state);
						out.flush();

						int key = in.readInt();

						// TODO: deal with invalid keys

						shooter.processPressKeyCode(key);

						Thread.sleep(KEY_DURATION_IN_MS);

						shooter.processReleaseKeyCode(key);

						Thread.sleep(KEY_WAIT_IN_MS);
					}
				} catch ( InterruptedException e ) {
					LOGGER.severe("Thread interrupted");
				} catch ( IOException e ) {
					LOGGER.severe("IOException");
					e.printStackTrace();
				}
			}
		};

		new Thread(runnable).start();
	}

	@Override
	public int getKey(ShooterState shooterState) {
		return 0;
	}
}
