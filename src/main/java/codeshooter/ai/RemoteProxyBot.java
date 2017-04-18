package codeshooter.ai;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import codeshooter.model.Shooter;
import codeshooter.model.ShooterState;

public class RemoteProxyBot extends ShooterBot {
	private static final long KEY_DURATION_IN_MS = 50;
	private static final long KEY_WAIT_IN_MS = 50;

	private int port;

	public RemoteProxyBot(int port) {
		this.port = port;
	}

	@Override
	public void start(Shooter shooter) {
		runnable = new Runnable() {
			@Override
			public void run() {
				try ( ServerSocket serverSocket = new ServerSocket(port);
						Socket clientSocket = serverSocket.accept();
						ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
						ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream()); ) {
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
				} catch ( IOException | InterruptedException e ) {
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
