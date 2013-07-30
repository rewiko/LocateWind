package LocateWind.Comtois;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

public class MapWind extends MapActivity implements LocationListener {
	public static Intent intent_service;
	private static MapView mapView = null;
	public static MapController mc = null;
	public static MyLocationOverlay myLocation = null;
	private double lat = 0.0;
	public static LocationManager lm = null;
	private double lng = 0.0;
	private Locate_Recep locate_recep;

	public static Intent service;

	// est appeler via le menu android bouton me permet de revenir a sa position
	// sinon à chaque changement de position
	// on était automatiquement centrer sur ses coordonnées via l'interface
	// location listener ce qui n'est pas forcement ergonomique si l'utilisateur
	// visite la map
	private void LocateMe() {
		try {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,
					this);
			mc.animateTo(myLocation.getMyLocation());
			mc.setCenter(myLocation.getMyLocation());
			return;
		} catch (Exception e) {

		}
		LocationManager objgps = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// si le GPS est desactivé on recupère les coordonnées grâce à la
		// triangulisation
		// Check GPS configuration
		if (!objgps.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			try {
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
						10000, 0, this);
				mc.animateTo(myLocation.getMyLocation());
				mc.setCenter(myLocation.getMyLocation());
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

	}

	public static MapView getMapView() {
		return mapView;
	}

	// est appélée quand on clique sur un élément de la list last location de
	// l'onglet 3
	public void Locatelast(int paramInt1, int paramInt2) {
		try {
			GeoPoint localGeoPoint = new GeoPoint(paramInt1, paramInt2);
			mc.animateTo(localGeoPoint);
			mc.setCenter(localGeoPoint);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public Intent getIntent_service() {
		return intent_service;
	}

	public MyLocationOverlay getMyLocation() {
		return myLocation;
	}

	// initialise la map
	public void initLocate() {
		myLocation = new MyLocationOverlay(getApplicationContext(), mapView);
		myLocation.runOnFirstFix(new Runnable() {
			public void run() {
				MapWind.mc.animateTo(MapWind.myLocation.getMyLocation());
				MapWind.mc.setCenter(MapWind.myLocation.getMyLocation());
				MapWind.mc.setZoom(17);
			}
		});
		mapView.getOverlays().add(myLocation);
		myLocation.enableCompass();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	// est appelé pour peupler la map d'overlay, il récupere la liste créer par
	// le service
	public void locate_maj() {
		try {
			Drawable drawable = this.getResources().getDrawable(
					R.drawable.marker);
			WindItem itemizedoverlay = new WindItem(drawable, this);

			for (Wind wind_recup : RecupService.getLocate_add()) {
				GeoPoint geoPoint = new GeoPoint(wind_recup.getLatitude(),
						wind_recup.getLongitude());
				OverlayItem overlayitem = new OverlayItem(geoPoint, "Force: "
						+ wind_recup.getForce() + " à " + wind_recup.getHeure()
						+ " le " + wind_recup.getDate(), "Orientation: "
						+ wind_recup.getOrientation());
				itemizedoverlay.addOverlayItem(overlayitem);
			}
			// ajout des points a la carte
			List<Overlay> mapOverlays = MapWind.getMapView().getOverlays();
			mapOverlays.add(itemizedoverlay);

			Toast.makeText(MapWind.this, "Ajout des points à la map!",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(MapWind.this, "PB Ajout des points à la map!",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		this.lm = ((LocationManager) getSystemService("location"));
		try {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,
					this);
			mc = mapView.getController();
			mc.setZoom(14);
		} catch (Exception e) {

		}
		LocationManager objgps = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Check GPS configuration
		if (!objgps.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			try {
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
						10000, 0, this);
				mc = mapView.getController();
				mc.setZoom(14);
			} catch (Exception e) {

			}
		}

		try {
			// creation service qui appelee la classe RecupService qui va
			// recuperer les donnee dans la BDD
			service = new Intent(this, RecupService.class);
			stopService(service);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			// creation service qui appelee la classe RecupService qui va
			// recuperer les donnee dans la BDD
			startService(service);
		} catch (Exception e) {
			// TODO: handle exception
		}
		initLocate();

	}

	public boolean onCreateOptionsMenu(Menu menu) {

		// Création d'un MenuInflater qui va permettre d'instancier un Menu XML
		// en un objet Menu
		MenuInflater inflater = getMenuInflater();
		// Instanciation du menu XML spécifier en un objet Menu
		inflater.inflate(R.layout.menu, menu);

		// Il n'est pas possible de modifier l'icône d'entête du sous-menu via
		// le fichier XML on le fait donc en JAVA
		menu.getItem(0).getSubMenu();// .setHeaderIcon(R.drawable.option_white);

		return true;
	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// lat = location.getLatitude();
		// lng = location.getLongitude();
		//
		// GeoPoint p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		// mc.animateTo(myLocation.getMyLocation());
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		// permet de revenir sur sa position
		case R.id.position_me:
			try {
				LocateMe();
			} catch (Exception e) {
				// TODO: handle exception

			}

			return true;
			// permet de mettre à jour la map manuellement, sachant que le
			// service tourne en tache de fond et actualise la map regulierement
		case R.id.update_locate:
			try {
				// creation service qui appelee la classe RecupService qui va
				// recuperer les donnee dans la BDD
				stopService(service);
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				// creation service qui appelee la classe RecupService qui va
				// recuperer les donnee dans la BDD
				startService(service);
			} catch (Exception e) {
				// TODO: handle exception
			}

			return true;

		case R.id.quitter:
			// Pour fermer l'application il suffit de faire finish()
			try {
				// creation service qui appelee la classe RecupService qui va
				// recuperer les donnee dans la BDD
				stopService(service);
			} catch (Exception e) {
				// TODO: handle exception
			}

			finish();
			return true;
		}
		return false;
	}

	protected void onPause() {
		super.onPause();
		try {
			this.lm.removeUpdates(this);
			unregisterReceiver(this.locate_recep);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void onProviderDisabled(String paramString) {
		// Lorsque la source (GSP ou réseau GSM) est désactivé
		Log.i("Tuto géolocalisation", "La source a été désactivé");
	}

	public void onProviderEnabled(String paramString) {
	}

	@Override
	public void onResume() {
		super.onResume();

		try {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,
					this);
			myLocation.enableCompass();
			myLocation.enableMyLocation();
			this.locate_recep = new Locate_Recep(this);
			IntentFilter localIntentFilter = new IntentFilter("locate_list");
			registerReceiver(this.locate_recep, localIntentFilter);
		} catch (Exception e) {
		}
		LocationManager objgps = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Check GPS configuration
		if (!objgps.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			try {
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
						10000, 0, this);
				myLocation.enableCompass();
				myLocation.enableMyLocation();
				this.locate_recep = new Locate_Recep(this);
				IntentFilter localIntentFilter = new IntentFilter("locate_list");
				registerReceiver(this.locate_recep, localIntentFilter);
			} catch (Exception e) {
			}
		}

		try {
			// creation service qui appelee la classe RecupService qui va
			// recuperer les donnee dans la BDD
			service = new Intent(this, RecupService.class);
			stopService(service);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			// creation service qui appelee la classe RecupService qui va
			// recuperer les donnee dans la BDD
			startService(service);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void onStop() {
		super.onStop();
		try {
			stopService(MapWind.intent_service);

		} catch (Exception e) {
		}
		try {
			stopService(LastLocation.intent_servicea);

		} catch (Exception e) {
		}
	}

	public void onDestroy() {
		super.onDestroy();
		try {
			stopService(MapWind.intent_service);

		} catch (Exception e) {
		}
		try {
			stopService(LastLocation.intent_servicea);

		} catch (Exception e) {
		}
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	// ancienne methode pour enregistrer et lire dans un fichier les données des
	// vents solution inutile en utilisant un serveur

	// public void ReadSettings(Context context) {
	// Drawable drawable = this.getResources().getDrawable(
	// R.drawable.ic_launcher);
	// WindItem itemizedoverlay = new WindItem(drawable, this);
	// try {
	// // Création du flux bufférisé sur un FileReader, immédiatement suivi
	// // par un
	// // try/finally, ce qui permet de ne fermer le flux QUE s'il le
	// // reader
	// // est correctement instancié (évite les NullPointerException)
	// BufferedReader buff = new BufferedReader(new FileReader(
	// context.getFilesDir() + "/locate_Wind"));
	//
	// try {
	// String line;
	// // Lecture du fichier ligne par ligne. Cette boucle se termine
	// // quand la méthode retourne la valeur null.
	// while ((line = buff.readLine()) != null) {
	// // System.out.println(line);
	// // faites ici votre traitement
	// // affiche le contenu de mon fichier dans un popup
	// // surgissant
	// String str[] = line.split(" ");
	// GeoPoint geoPoint = new GeoPoint(Integer.parseInt(str[0]),
	// Integer.parseInt(str[1]));
	// OverlayItem overlayitem = new OverlayItem(geoPoint,
	// "Force: " + str[2] + " à " + str[4] + " le "
	// + str[5], "Orientation: " + str[3]);
	// itemizedoverlay.addOverlayItem(overlayitem);
	// Toast.makeText(context, " " + line, Toast.LENGTH_SHORT)
	// .show();
	// }
	// } finally {
	// // dans tous les cas, on ferme nos flux
	// buff.close();
	// // ajout des points a la carte
	// List<Overlay> mapOverlays = mapView.getOverlays();
	// mapOverlays.add(itemizedoverlay);
	// }
	//
	// } catch (Exception e) {
	// Toast.makeText(context, "Settings not read", Toast.LENGTH_SHORT)
	// .show();
	// }
	/*
	 * finally { try { isr.close(); fIn.close(); } catch (IOException e) {
	 * Toast.makeText(context, "Settings not read",Toast.LENGTH_SHORT).show(); }
	 * }
	 */

	// }
}