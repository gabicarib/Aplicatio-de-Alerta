package com.alerta.mobile_ufrpe.alerta.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.alerta.mobile_ufrpe.alerta.Entidades.Local;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MapsFragment extends SupportMapFragment implements GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, LocationListener {

    GoogleMap mMap;

    private LocationManager lm;
    private Location location;
    private double longitude = -34.927305;
    private double latitude = -8.027725;

    private FirebaseDatabase database;

    //Permissões
    private static final int REQUEST_PERMISSION = 1;

    private static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private GoogleApiClient mGoogleApiClient;
    private Marker marker;
    private GoogleMap map;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMapAsync(this);

        database = FirebaseDatabase.getInstance();

        getActivity().setTitle("Mapa");

        initMaps();

    }

    //Inicar Mapa
    public void initMaps() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions();

        } /*else {
            lm = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 60000, (LocationListener) this);
        }*/
    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.mMap = map;
        if (lm != null) {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

            }
        }

        //Markers adcionados pelo usuário
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Adicionando Ponto de Alagamento")
                        .setMessage("Tem certeza que deseja adicionar este ponto de alagamento?")
                        .setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("locais");
                                        Local local = new Local();

                                        mMap.addMarker(new MarkerOptions().position(latLng)
                                                .title(String.valueOf(latLng.latitude)
                                                        + "," + String.valueOf(latLng.longitude)));

                                        local.setNome("Local de Alagamento");
                                        local.setLatitude(latLng.latitude);
                                        local.setLongitude(latLng.longitude);

                                        Map<String, Object> childUpdates = new HashMap<>();
                                        childUpdates.put(myRef.push().getKey(), local.toMap());
                                        myRef.updateChildren(childUpdates);

                                        Toast.makeText(getContext()
                                                , "Ponto de Alagamento Adicionado "
                                                , Toast.LENGTH_LONG).show();
                                    }
                                })
                        .setNegativeButton("Não", null)
                        .show();

            }

        });

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 11));

        loadMarker();
    }


    @Override
    public void onLocationChanged(Location arg0) {

    }


    public void onProviderDisabled(String arg0) {

    }


    public void onProviderEnabled(String arg0) {

    }


    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Autorizado", Toast.LENGTH_SHORT).show();
                    initMaps();

                } else {
                    Toast.makeText(getContext(), "Permissão negada", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    public void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {


            Toast.makeText(getContext(), "Permissão negada", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_PERMISSION);
        }
    }


    public void loadMarker() {
        DatabaseReference locais = database.getReference("locais");

        locais.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                mMap.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshots) {
                    Local local = dataSnapshot1.getValue(Local.class);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(local.getLatitude(), local.getLongitude())).title(local.getNome()));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void onAddMarker() {
        loadMarker();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

}
