package fr.edenyorke.tesou;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements LocationListener, LocationSource{
	
	 private GoogleMap map;
	 
	 private OnLocationChangedListener mListener;
	    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        
        if(locationManager != null)
        {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
             
            if(gpsIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
            }
            else if(networkIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
            }
            else
            {
                //Show an error dialog that GPS is disabled...
            }
        }
        else
        {
            //Show some generic error dialog because something must have gone wrong with location manager.
        }
     
    setUpMapIfNeeded();
    }


    @Override
    public void onPause()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(this);
        }
        super.onPause();
    }
     
    @Override
    public void onResume()
    {
        super.onResume();
         
        setUpMapIfNeeded();
         
        if(locationManager != null)
        {
        	map.setMyLocationEnabled(true);
        }
    }
    
	
	
	 private void setUpMapIfNeeded() {
	        // Do a null check to confirm that we have not already instantiated the map.
	        if (map == null)
	        {
	            // Try to obtain the map from the SupportMapFragment.
	        	map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	            // Check if we were successful in obtaining the map.
	            
	            if (map != null)
	            {
	                setUpMap();
	            }
	 
	            //This is how you register the LocationSource
	            map.setLocationSource(this);
	            
	        }
	    }
	
	


	 /**
	     * This is where we can add markers or lines, add listeners or move the camera.
	     * <p>
	     * This should only be called once and when we are sure that {@link #mMap} is not null.
	     */
	    private void setUpMap()
	    {
	        map.setMyLocationEnabled(true);
	    }
	     
	    @Override
	    public void activate(OnLocationChangedListener listener)
	    {
	        mListener = listener;
	    }
	     
	    @Override
	    public void deactivate()
	    {
	        mListener = null;
	    }
	 
	    @Override
	    public void onLocationChanged(Location location)
	    {
	    	if( mListener != null )
	        {
	            mListener.onLocationChanged( location );
	     
	            LatLngBounds bounds = this.map.getProjection().getVisibleRegion().latLngBounds;
	     
	            if(!bounds.contains(new LatLng(location.getLatitude(), location.getLongitude())))
	            {
	            	changeLocationOnMap(location);  
	            }
	        }
	    }


		private void changeLocationOnMap(Location location) {
			// Getting latitude of the current location
			double latitude = location.getLatitude();
			// Getting longitude of the current location
			double longitude = location.getLongitude();
			// Creating a LatLng object for the current location
			LatLng myPosition = new LatLng(latitude, longitude);
			map.addMarker(new MarkerOptions().position(myPosition).title("Start"));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition,16));
			
		}
	 
	    @Override
	    public void onProviderDisabled(String provider)
	    {
	        // TODO Auto-generated method stub
	        Toast.makeText(this, "provider disabled", Toast.LENGTH_SHORT).show();
	    }
	 
	    @Override
	    public void onProviderEnabled(String provider)
	    {
	        // TODO Auto-generated method stub
	        Toast.makeText(this, "provider enabled", Toast.LENGTH_SHORT).show();
	    }
	 
	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras)
	    {
	        // TODO Auto-generated method stub
	        Toast.makeText(this, "status changed", Toast.LENGTH_SHORT).show();
	    }
    
}
