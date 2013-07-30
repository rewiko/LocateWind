package LocateWind.Comtois;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class WindLocate extends Activity {
	private EditText ForceWind;
	private Spinner spinnerOrient;

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.windlocate);
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.locate_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		Button localButton = (Button) findViewById(R.id.button1);
		MapWind.getMapView().getOverlays();

		// au click du bouton OK on passe les données en parametre à postdata
		localButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				try {
					WindLocate.this.ForceWind = ((EditText) WindLocate.this
							.findViewById(R.id.editText1));
					WindLocate.this.spinnerOrient = ((Spinner) WindLocate.this
							.findViewById(R.id.spinner));

					String str = MapWind.myLocation.getMyLocation()
							.getLatitudeE6()
							+ " "
							+ MapWind.myLocation.getMyLocation()
									.getLongitudeE6()
							+ " "
							+ WindLocate.this.ForceWind.getText()
							+ " "
							+ WindLocate.this.spinnerOrient.getSelectedItem();
					if (!WindLocate.this.ForceWind.getText().toString()
							.equals(""))
						WindLocate.this.postData(str);
					else
						Toast.makeText(WindLocate.this.getBaseContext(),
								"PB champ force du vent vide!", 1).show();
				} catch (Exception localException) {
					Toast.makeText(WindLocate.this.getBaseContext(),
							"PB récupération des données GPS", 1).show();
				}
			}
		});
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
				stopService(MapWind.intent_service);
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				stopService(LastLocation.intent_servicea);
			} catch (Exception e) {
				// TODO: handle exception
			}
			finish();
			return true;
		}
		return false;
	}

	// envoie les données au serveur, qui les insere dans la base de donnée
	public void postData(String paramString) {

		String[] arrayOfString = paramString.split(" ");
		DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
		HttpPost localHttpPost = new HttpPost("http://"
				+ Locate_WindActivity.ip_serveur + "/android/insert_locate.php");
		try {
			ArrayList<BasicNameValuePair> localArrayList = new ArrayList<BasicNameValuePair>();
			localArrayList.add(new BasicNameValuePair("latitude",
					arrayOfString[0]));
			localArrayList.add(new BasicNameValuePair("longitude",
					arrayOfString[1]));
			localArrayList
					.add(new BasicNameValuePair("force", arrayOfString[2]));
			localArrayList.add(new BasicNameValuePair("orientation",
					arrayOfString[3]));
			localHttpPost.setEntity(new UrlEncodedFormEntity(localArrayList));
			Log.e("http réponse", localDefaultHttpClient.execute(localHttpPost)
					.toString());
			((TextView) findViewById(R.id.editText1)).setText("");
			Toast.makeText(getBaseContext(), "Enregistrement Effectué!", 1)
					.show();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "PB envoie des données!", 1)
					.show();
		}
		// on relance le service pour actualiser la map et la listview de
		// lastlocation
		try {
			stopService(MapWind.intent_service);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			startService(MapWind.intent_service);
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

}