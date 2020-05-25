package com.alerta.mobile_ufrpe.alerta.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alerta.mobile_ufrpe.alerta.Domain.MessageEB;
import com.alerta.mobile_ufrpe.alerta.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.greenrobot.event.EventBus;

public class ReceberCoordenadas extends AppCompatActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    TextView titulo;
    Button btnReceberCoordenadas;
    TextView receberCoordenadas;

    double latitude;
    double longitude;

    public static final int PERMISSAO = 1;
    Location location = null;

//

    public static final String LOCATION = "location";
    public static final String TYPE = "type";
    public static final String ADDRESS = "address";

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private EditText etAddress;
    private TextView tvAddressLocation;
    private Button btNameToCoord;
    private Button btCoordToName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receber_coordenadas);

        titulo = (TextView) findViewById(R.id.titulo);
        btnReceberCoordenadas = (Button) findViewById(R.id.btnReceberCoordenadas);
        receberCoordenadas = (TextView) findViewById(R.id.receberCoordenadas);

        //

        EventBus.getDefault().register(this);

        etAddress = (EditText) findViewById(R.id.et_address);
        tvAddressLocation = (TextView) findViewById(R.id.tv_address_location);
        btNameToCoord = (Button) findViewById(R.id.bt_name_to_coord);
        btCoordToName = (Button) findViewById(R.id.bt_coord_to_name);

        callConnection();

        //

        btnReceberCoordenadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lerCoordenadas();
            }
        });
    }


    private void lerCoordenadas() {

        //Verificar permissao
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSAO);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (gps) {
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.i("Coordenadas", "Lat: " + latitude + " | Lon: " + longitude);
                    receberCoordenadas.setText("Lat: " + latitude + " | Lon: " + longitude);

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
                    mapFragment.getMapAsync(this);
                }
            } else Log.i("Gps", "Gps Desativado");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSAO:
                if (permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    lerCoordenadas();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.i("novaCoordenada", "Lat: " + latitude + " Lon: " + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }




    ///





    private synchronized void callConnection() {
        Log.i("LOG", "AddressLocationActivity.callConnection()");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    public void callIntentService(int type, String address) {
        Intent it = new Intent(this, LocationIntentService.class);
        it.putExtra(TYPE, type);
        it.putExtra(ADDRESS, address);
        it.putExtra(LOCATION, mLastLocation);
        startService(it);
    }


    // LISTERNERS
    @Override

    public void onConnected(Bundle bundle) {

        Log.i("LOG", "AddressLocationActivity.onConnected(" + bundle + ")");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location l = LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if(l != null){
            mLastLocation = l;
            btNameToCoord.setEnabled(true);
            //btCoordToName.setEnabled(true);
        }

    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG", "AddressLocationActivity.onConnectionSuspended(" + i + ")");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LOG", "AddressLocationActivity.onConnectionFailed(" + connectionResult + ")");
    }

    public void getLocationListener(View view){
        int type;
        String address = null;

        if(view.getId() == R.id.bt_name_to_coord){
            type = 1;
            address = etAddress.getText().toString();
        }
        else{
            type = 2;
        }

        callIntentService(type, address);
    }

    public void onEvent(final MessageEB m){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Log.i("LOG", m.getResultMessage());
                tvAddressLocation.setText("Data: "+m.getResultMessage());
            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        LocationIntentService l = new LocationIntentService();

        LatLng coordenadas = new LatLng(l.a.getLatitude(),l.a.getLongitude());

        MarkerOptions marcador = new MarkerOptions();
        marcador.position(coordenadas).title("Meu Local").snippet("Atualizado");

        googleMap.addMarker(marcador);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(coordenadas,15);
        googleMap.animateCamera(update);


    }



}
