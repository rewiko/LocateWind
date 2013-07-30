package LocateWind.Comtois;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class LastLocation extends Activity {
	public static Intent intent_servicea = null;
	private ListView maList;

	public void check_last() {

		this.maList = ((ListView) findViewById(R.id.listview));
		// Création de la ArrayList qui nous permettra de remplire la listView
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

		// On déclare la HashMap qui contiendra les informations pour un item
		HashMap<String, String> map;
		listItem.clear();

		try {
			for (Wind wind_recup : RecupService.getLocate_add()) {

				// Création d'une HashMap pour insérer les informations du
				// premier item de notre listView
				map = new HashMap<String, String>();
				// on insère un élément titre que l'on récupérera dans le
				// textView titre créé dans le fichier affichageitem.xml
				map.put("titre", "Force: " + wind_recup.getForce() + " à "
						+ wind_recup.getHeure() + " le " + wind_recup.getDate());
				// on insère un élément description que l'on récupérera dans le
				// textView description créé dans le fichier affichageitem.xml
				map.put("description",
						"Orientation: " + wind_recup.getOrientation());

				map.put("coord",
						wind_recup.getLatitude() + " "
								+ wind_recup.getLongitude());
				// enfin on ajoute cette hashMap dans la arrayList
				listItem.add(map);
			}

			// Création d'un SimpleAdapter qui se chargera de mettre les items
			// présent dans notre list (listItem) dans la vue affichageitem
			SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(),
					listItem, R.layout.list_locate, new String[] { "titre",
							"description" }, new int[] { R.id.titre,
							R.id.description });

			// On attribut à notre listView l'adapter que l'on vient de créer
			maList.setAdapter(mSchedule);

			// Enfin on met un écouteur d'évènement sur notre listView
			maList.setOnItemClickListener(new OnItemClickListener() {
				@SuppressWarnings("unchecked")
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					try {
						// on récupère la HashMap contenant les infos de notre
						// item (titre, description, img)
						HashMap<String, String> map = (HashMap<String, String>) maList
								.getItemAtPosition(position);
						String str[] = map.get("coord").split(" ");

						Locate_WindActivity.tabHost.setCurrentTab(0);
						new MapWind().Locatelast(Integer.parseInt(str[0]),
								Integer.parseInt(str[1]));
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			});

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.last_location);
		try {
			intent_servicea = new Intent(this, RecupService.class);
			check_last();
		} catch (Exception e) {

		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		// Création d'un MenuInflater qui va permettre d'instancier un Menu XML
		// en un objet Menu
		MenuInflater inflater = getMenuInflater();
		// Instanciation du menu XML spécifier en un objet Menu
		inflater.inflate(R.layout.menu_quit, menu);

		menu.getItem(0).getSubMenu();// .setHeaderIcon(R.drawable.option_white);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.quitter:
			// Pour fermer l'application il suffit de faire finish()
			try {
				// creation service qui appelee la classe RecupService qui va
				// recuperer les donnee dans la BDD
				stopService(MapWind.service);
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				// creation service qui appelee la classe RecupService qui va
				// recuperer les donnee dans la BDD
				stopService(intent_servicea);
			} catch (Exception e) {
				// TODO: handle exception
			}

			finish();
			return true;
		}
		return false;
	}

	protected void onResume() {
		super.onResume();
		try {
			stopService(intent_servicea);
		} catch (Exception e) {
		}
		try {
			startService(intent_servicea);
			check_last();
		} catch (Exception e) {
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
}