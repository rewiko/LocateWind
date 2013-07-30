package LocateWind.Comtois;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecupService extends Service {
	private static List<Wind> locate_add = null;
	private Timer chrono = null;
	private RecupTimerTask tmt;
	private InputStream is = null;

	public void onStart(Intent intent, int startId) {

		chrono.scheduleAtFixedRate(tmt, 0, (long) 300000000);
	}

	@Override
	public void onCreate() {
		// creation de la liste de point
		locate_add = new ArrayList<Wind>();

		// lancement chrono et du timer task
		chrono = new Timer("chrono_maj_locate");
		tmt = new RecupTimerTask(this);

	}

	public static List<Wind> getLocate_add() {
		return locate_add;
	}

	// éxécution du thread
	private Runnable recupRunnable = new Runnable() {
		public void run() {
			try {
				// il semblerait que depuis les versions 2.2 ou 2.3 les requetes
				// http doivent se faire dans un thread differents et le fait de
				// preparer loopper a resolut le probleme
				// looper introduit donc une boucle
				// On met alors en place un Looper qu’on appelle parfois
				// également “boucle évènementielle”. La classe Looper permet de
				// préparer un Thread à la lecture répétitive d’actions.
				Looper.prepare();
				locate_maj();
				Looper.loop();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};

	// creation du thread
	public void lance_thread_maj() {
		new Thread(null, this.recupRunnable).start();
	}

	// permet de recuperer les données de la base de donnée: coté serveur
	// encoder en JSON, on va donc decoder le JSON pour le récuperer (JSON a une
	// syntaxe plus light que XML, mais les 2 correspondent )
	public void locate_maj() {

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://"
					+ Locate_WindActivity.ip_serveur
					+ "/android/check_locate.php");
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			Log.i("log_tag", "OK requete http connection");
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
		}

		Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
		WindItem itemizedoverlay = new WindItem(drawable, this);
		String result = null;

		StringBuilder sb = null;

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line = "0";

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();

		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// paring data
		int latitude;
		int longitude;
		double force;
		String orientation;
		String date;
		// parsing JSON
		try {
			JSONArray jArray = new JSONArray(result);
			JSONObject json_data = null;

			for (int i = 0; i < jArray.length(); i++) {
				json_data = jArray.getJSONObject(i);
				latitude = json_data.getInt("latitude");
				longitude = json_data.getInt("longitude");
				force = json_data.getDouble("force");
				orientation = json_data.getString("orientation");
				date = json_data.getString("date");

				String str[] = date.split(" ");

				locate_add.add(new Wind(latitude, longitude, force,
						orientation, str[1], str[0]));

			}

			// creation service pour envoyer a l'OS android
			Intent intent = new Intent("locate_list");
			this.sendBroadcast(intent);

		} catch (JSONException e1) {
			Toast.makeText(getBaseContext(), "No Locate Found",
					Toast.LENGTH_LONG).show();
		} catch (ParseException e1) {
			Toast.makeText(getBaseContext(), "PB parsing JSON!",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getBaseContext(), "PB recup donnee!",
					Toast.LENGTH_LONG).show();
		}

	}

	public IBinder onBind(Intent paramIntent) {
		return null;
	}

}