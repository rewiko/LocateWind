package LocateWind.Comtois;

import java.util.TimerTask;

public class RecupTimerTask extends TimerTask {
	private RecupService recup_data = null;

	public RecupTimerTask(RecupService paramRecupService) {
		this.recup_data = paramRecupService;
	}

	public void run() {
		this.recup_data.lance_thread_maj();
	}
}