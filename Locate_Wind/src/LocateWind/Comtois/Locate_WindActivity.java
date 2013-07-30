package LocateWind.Comtois;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class Locate_WindActivity extends TabActivity implements
		GestureDetector.OnGestureListener {
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	public static String ip_serveur = "178.170.101.101";
	public static TabHost tabHost;
	private GestureDetector gestureScanner;

	private void addTab(String paramString, Intent paramIntent) {
		// ajout de tab avec une vue personnalisé tab_indicator qui permet de
		// gagnée de la place sur l'ecran en enlevant les icones superflus
		TabHost.TabSpec spec = tabHost.newTabSpec(paramString);

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(paramString);

		spec.setIndicator(tabIndicator);
		spec.setContent(paramIntent);
		tabHost.addTab(spec);

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getResources();

		tabHost = getTabHost();
		// ajout des tab au tabhost
		addTab("Map", new Intent().setClass(this, MapWind.class));
		addTab("Locate", new Intent().setClass(this, WindLocate.class));
		addTab("Last", new Intent().setClass(this, LastLocation.class));

		// tab 0 par defaut affiché
		tabHost.setCurrentTab(0);
		this.gestureScanner = new GestureDetector(this);
	}

	public void onResume() {
		super.onResume();
		// On instancie notre layout en tant que View

		Context context = getApplicationContext();

		LocationManager objgps = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// permet d'afficher une boite de dialogue si le GPS n'est pas activé et
		// renvoie directement dans parmètres android au niveau du GPS pour
		// l'activer
		// Check GPS configuration
		if (!objgps.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// Création de l'AlertDialog
			AlertDialog.Builder adb = new AlertDialog.Builder(this);

			// On donne un titre à l'AlertDialog
			adb.setTitle("le GPS n'est pas activé, voulez vous l'activer?");

			// On modifie l'icône de l'AlertDialog pour le fun ;)
			adb.setIcon(android.R.drawable.ic_dialog_alert);

			// On affecte un bouton "OK" à notre AlertDialog et on lui affecte
			// un évènement
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					Intent intentRedirectionGPSSettings = new Intent(
							android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					intentRedirectionGPSSettings
							.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					startActivityForResult(intentRedirectionGPSSettings, 0);

				}
			});

			// On crée un bouton "Annuler" à notre AlertDialog et on lui affecte
			// un évènement
			adb.setNegativeButton("Annuler",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			adb.show();
		}
	}

	public boolean onDown(MotionEvent paramMotionEvent) {
		return true;
	}

	// permet de changer d'onglet en slidant avec le doigt ( le problème avec
	// cette application est que la map et la listview prenne tout l'ecran du
	// coup il faut bien commencer au bord de'écran et effectuer le geste)
	// sinon marche très bien sans commencer forcement au bord de écran au
	// niveau du 2 eme onglet, et sinon on peut toujours appuyer sur l'onglet
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
			return false;
		if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
				&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			tabHost.setCurrentTab(1 + tabHost.getCurrentTab());
			return true;

		} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
				&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			tabHost.setCurrentTab(-1 + tabHost.getCurrentTab());
			return true;
		}
		return false;

	}

	public void onLongPress(MotionEvent paramMotionEvent) {
	}

	public boolean onScroll(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
		return true;
	}

	public void onShowPress(MotionEvent paramMotionEvent) {
	}

	public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
		return true;
	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		return this.gestureScanner.onTouchEvent(paramMotionEvent);
	}
}