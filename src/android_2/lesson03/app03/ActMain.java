package android_2.lesson03.app03;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android_2.lesson03.app03.R;

/**
 * Any activity in this application.
 * TODO: put description of this activity.
 * */
public class ActMain extends Activity {
	
	/* Private fields for store links to UI components */
	private GoogleMap mvMap = null;
	
	/** 
	 * Private method that contain initialization code 
	 * for user interface (UI) components.
	 * */
	private void initUI() {		
		
		/* Get a handle to the Map Fragment and to 
		 * the Map object */				
		mvMap = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.frMap)).getMap();
		
		/* Enables the my-location layer in the map */
		mvMap.setMyLocationEnabled(true);
		
		/* Disable my-location button */
		mvMap.getUiSettings().setMyLocationButtonEnabled(false);
		
	}	

	/**
	 * Called when the activity is starting (for more details, 
	 * please see description into super class).
	 * */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		/* Invoke a parent method */
		super.onCreate(savedInstanceState);
		
		/* Load User Interface from resources */
		setContentView(R.layout.act_main);
	
		/* Initialize UI components (see initUI() method */
		this.initUI();
		
	}

	/** 
	 * Initialize the contents of the Activity's standard options menu. 
	 * You should place your menu items into menu (for more details, 
	 * please see description into super class). 
	 * */
	@Override	
	public boolean onCreateOptionsMenu(Menu menu) {		
		/* Inflate the menu; this adds items to the action bar if it is present */
		getMenuInflater().inflate(R.menu.act_main, menu);		
		return true;
	}	
			
	/** 
	 * This hook is called whenever an item in your options menu is 
	 * selected. The default implementation simply returns false to 
	 * have the normal processing happen (for more details, please 
	 * see description into super class). 
	 * */
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
				LatLng target = new LatLng(mvMap.getMyLocation().getLatitude(), 
						mvMap.getMyLocation().getLongitude());		
				
				/* Defines a camera move. An object of this type can be used 
				 * 	to modify a map's camera by calling */
				CameraUpdate camUpdate = CameraUpdateFactory.
						newLatLngZoom(target, 15F);
				
				/* Move camera to point with Animation */
				mvMap.animateCamera(camUpdate);
			}
			
			return true;			
		}	
		
		/* Menu "Add Point" */
		if (item.getItemId() == R.id.menu_map_point_new) {
			
			/* Create MarkerOptions object */
			MarkerOptions marker = new MarkerOptions();
			
			/* Set marker icon */
			marker.icon(null);
			
			/* Set a particular point in the marker image */
			marker.anchor(0.0f, 1.0f);
			
			/* Create a position for marker */
			LatLng target = new LatLng(mvMap.getMyLocation().getLatitude(), 
					mvMap.getMyLocation().getLongitude());		
			
			/* Set title for marker */
			marker.title("My Location");
			
			/* Set a position for marker */
			marker.position(target);					
			
			/* Add marker to a Map */
			mvMap.addMarker(marker);
			
		}			
		
		/* Invoke a parent method */
		return super.onOptionsItemSelected(item);
		
	}

}
