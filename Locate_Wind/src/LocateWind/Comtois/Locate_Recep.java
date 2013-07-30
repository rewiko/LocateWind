package LocateWind.Comtois;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Locate_Recep extends BroadcastReceiver {
	MapWind map = null;
	LastLocation test = null;

	public Locate_Recep(MapWind paramMapWind) {
		this.map = paramMapWind;
	}

	public void onReceive(Context paramContext, Intent paramIntent) {
		this.map.locate_maj();
	}
}