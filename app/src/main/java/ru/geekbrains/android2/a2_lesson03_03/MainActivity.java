package ru.geekbrains.android2.a2_lesson03_03;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

	/* Private fields for store links to UI components */
	private GoogleMap mvMap = null;
	SupportMapFragment mapFragment;

	public static final String APP_PREFERENCES = "mysettings";
	public static final String APP_PREFERENCES_LATITUDE = "myPointLatitude";
	public static final String APP_PREFERENCES_LONGITUDE = "myPointLongitude";
	public static final String APP_PREFERENCES_NAME = "mayPointName";

	private SharedPreferences mSettings = null;

	private Set<String> latitudesSet = new HashSet<String>();
	private Set<String> longitudesSet = new HashSet<String>();
	private Set<String> nameTargetsSet = new HashSet<String>();

	//Поля для алертДиалога
	EditText editTextName = null;
	EditText editTextLatitude = null;
	EditText editTextLongitude = null;

	/**
	 * Called when the activity is starting (for more details,
	 * please see description into super class).
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		/* Invoke a parent method */
		super.onCreate(savedInstanceState);

		/* Load User Interface from resources */
		setContentView(R.layout.activity_main);

		/* Initialize UI components (see initUI() method */
		this.initUI();

		//Инициализируем SharedPreferences
		mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
	}

	// Загрузка точек из SharedPreferences
	private void loadPoint() {
		if (mSettings.contains(APP_PREFERENCES_LATITUDE) &&
				mSettings.contains(APP_PREFERENCES_LONGITUDE) && mSettings.contains(APP_PREFERENCES_NAME)) {

			nameTargetsSet = mSettings.getStringSet(APP_PREFERENCES_NAME, new LinkedHashSet<String>());
			latitudesSet = mSettings.getStringSet(APP_PREFERENCES_LATITUDE, new LinkedHashSet<String>());
			longitudesSet = mSettings.getStringSet(APP_PREFERENCES_LONGITUDE, new LinkedHashSet<String>());

			String[] name = nameTargetsSet.toArray(new String[nameTargetsSet.size()]);
			String[] lat = latitudesSet.toArray(new String[latitudesSet.size()]);
			String[] lon = longitudesSet.toArray(new String[longitudesSet.size()]);

			for (int i = 0; i < lat.length && i < lon.length; i++) {

				LatLng target = new LatLng(Float.valueOf(lat[i]), Float.valueOf(lon[i]));
				String nameTarget = name[i];
				Log.d("onResume_latitude", String.valueOf(target.latitude));
				Log.d("onResume_longitude", String.valueOf(target.longitude));
				makeMarker(target, nameTarget);
			}
		}
	}

	/**
	 * Private method that contain initialization code
	 * for user interface (UI) components.
	 */
	private void initUI() {

		/* Get a handle to the Map Fragment and to
         * the Map object */
		mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.frMap);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap map) {

        /* Enables the my-location layer in the map */
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		map.setMyLocationEnabled(true);

		/* Disable my-location button */
		map.getUiSettings().setMyLocationButtonEnabled(true);
		mvMap = map;
		mvMap.setOnMapLongClickListener(this);

		//После готовности карты загружаем точки
		loadPoint();
	}


	/**
	 * Initialize the contents of the Activity's standard options menu.
	 * You should place your menu items into menu (for more details,
	 * please see description into super class).
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/* Inflate the menu; this adds items to the action bar if it is present */
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	/**
	 * This hook is called whenever an item in your options menu is
	 * selected. The default implementation simply returns false to
	 * have the normal processing happen (for more details, please
	 * see description into super class).
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		/* Menu "Map" */
		if (item.getItemId() == R.id.menu_map_mode_normal) {
			mvMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			return true;
		}

		/* Menu "Satellite" */
		if (item.getItemId() == R.id.menu_map_mode_satellite) {
			mvMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			return true;
		}

		/* Menu "Terrain" */
		if (item.getItemId() == R.id.menu_map_mode_terrain) {
			mvMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			return true;
		}

		/* Menu "Map Traffic" */
		if (item.getItemId() == R.id.menu_map_traffic) {
			mvMap.setTrafficEnabled(!mvMap.isTrafficEnabled());
			return true;
		}

		/* Menu "My Location" */
		if (item.getItemId() == R.id.menu_map_location) {

			/* If MyLocation service is enable */
			if (mvMap.isMyLocationEnabled()) {

				/* Get a pair of latitude and longitude coordinates (stored as
				 * 	degrees) from MyLocation (current location) */
				LatLng target = new LatLng(37.7749, -122.4194);
				//target = new LatLng(mvMap.getMyLocation().getLatitude(), mvMap.getMyLocation().getLongitude());

				/* Defines a camera move. An object of this type can be used
				 * 	to modify a map's camera by calling */
				CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(target, 15F);

				/* Move camera to point with Animation */
				mvMap.animateCamera(camUpdate);
			}

			return true;
		}

		/* Menu "Add Point" */
		if (item.getItemId() == R.id.menu_map_point_new) {

			createAlertDialog(mvMap.getCameraPosition().target);
		}
		return super.onOptionsItemSelected(item);
	}

	private void createAlertDialog(LatLng target){
		//Создание диалогового окна с возможностью ввода координат точки.
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogView = inflater.inflate(R.layout.dialog, null);

		//Cоздаем AlertDialog
		AlertDialog.Builder mDialogNBuilder = new AlertDialog.Builder(this);

		//Прикручиваем лейаут к алерту
		mDialogNBuilder.setView(dialogView);

		//Инициализация компонентов
		editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
		editTextLatitude = (EditText) dialogView.findViewById(R.id.editTextLatitude);
		editTextLongitude = (EditText) dialogView.findViewById(R.id.editTextLongitude);

		editTextLatitude.setText(String.valueOf(target.latitude));
		editTextLongitude.setText(String.valueOf(target.longitude));

		mDialogNBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						LatLng target = new LatLng(Float.valueOf(editTextLatitude.getText().toString()),
								Float.valueOf(editTextLongitude.getText().toString()));
						String nameTarget = editTextName.getText().toString();

						makeMarker(target, nameTarget);
						saveTargetsToSet(target, nameTarget);
					}
				});
		AlertDialog alertDialog = mDialogNBuilder.create();
		alertDialog.show();
	}

	//Создание и добавление маркера на карту
	private void makeMarker(LatLng target, String nameTarget) {
		MarkerOptions marker = new MarkerOptions();
		marker.icon(null);
		marker.anchor(0.0f, 0.0f);
		marker.title(nameTarget);
		marker.position(target);
		marker.draggable(false);
		mvMap.addMarker(marker);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//Сохраняем в SharedPreferences точки
		SharedPreferences.Editor editor = mSettings.edit();
		editor.putStringSet(APP_PREFERENCES_LATITUDE, latitudesSet);
		editor.putStringSet(APP_PREFERENCES_LONGITUDE, longitudesSet);
		editor.putStringSet(APP_PREFERENCES_NAME, nameTargetsSet);
		editor.apply();
	}

	//Сохранение точек в массивы
	private void saveTargetsToSet(LatLng target, String nameTarget) {
		Log.d("save_latitude", String.valueOf(target.latitude));
		Log.d("save_longitude", String.valueOf(target.longitude));
		Log.d("save_nameTarget", nameTarget);
		latitudesSet.add(String.valueOf(target.latitude));
		longitudesSet.add(String.valueOf(target.longitude));
		nameTargetsSet.add(nameTarget);
	}

	@Override
	public void onMapLongClick(LatLng latLng) {
		createAlertDialog(latLng);
	}
}
