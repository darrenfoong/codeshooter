package codeshooter.ui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Topbar extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final int HEIGHT = 20;
	private static final long UPDATE_INTERVAL_IN_MS = 1000;

	private int secondsElapsed = 0;
	private JLabel timeLabel = new JLabel();

	private Thread topbarThread = new Thread() {
		@Override
		public void run() {
			while ( true ) {
				update();

				try {
					Thread.sleep(UPDATE_INTERVAL_IN_MS);
				} catch ( InterruptedException e ) {
					e.printStackTrace();
				}
			}
		}
	};

	public Topbar() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setPreferredSize(new Dimension(0, HEIGHT));

		add(timeLabel);
		timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	public void update() {
		secondsElapsed++;
		timeLabel.setText(Integer.toString(secondsElapsed));
	}

	public void start() {
		topbarThread.start();
	}
}
