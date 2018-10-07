package com.cntn16.cong.contactonmaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    MapWrapperLayout mapWrapperLayout;
    View contentView;
    Button btnCall, btnBrowser;
    OnInterInfoWindowTouchListener lsCall, lsBrowser;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_wrapper);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapWrapperLayout.init(mMap, this);

        final ContactInfo[] contactInfo = new ContactInfo[]{
                new ContactInfo("Le Thanh Cong", 10.7637847, 106.6811816, "0909173336", "fb.com/cong.le.98lhp",BitmapDescriptorFactory.fromResource(R.mipmap.ic_contact_0_round)),
                new ContactInfo("Nguyen Quoc Vuong", 10.7615653,106.6820928, "0799965415", "fb.com/nqvuong98",BitmapDescriptorFactory.fromResource(R.mipmap.ic_contact_1_round)),
                new ContactInfo("Tran Ba Ngoc", 10.759890, 106.682785, "0989965515", "fb.com/ngocs2thaont",BitmapDescriptorFactory.fromResource(R.mipmap.ic_contact_2_round)),
                new ContactInfo("Le Cong Hung", 10.759858, 106.677689, "0375678453", "fb.com/profile.php?id=100007886733514",BitmapDescriptorFactory.fromResource(R.mipmap.ic_contact_3_round)),
                new ContactInfo("Tran Quoc Cuong",10.7637665,106.6853307, "0123456743", "fb.com/qcuong98",BitmapDescriptorFactory.fromResource(R.mipmap.ic_contact_4_round))
        };

        for (ContactInfo contactInfoI :contactInfo )
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(contactInfoI.latitude, contactInfoI.longitude))
                .title(contactInfoI.name)
                .icon(contactInfoI.avatar));


        // create an animation for map while moving to this location
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        // set some feature of map
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(contactInfo[1].latitude,contactInfo[1].longitude))      // Sets the center of the map to HCMUS
                .zoom(16)                   // Sets the zoom (1<= zoom <= 20)
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();
        // do animation to move to this location
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        contentView = LayoutInflater.from(this).inflate(R.layout.content, null);
        btnCall = (Button) contentView.findViewById(R.id.call);
        btnBrowser=(Button) contentView.findViewById(R.id.linkfb);
        lsCall = new OnInterInfoWindowTouchListener(btnCall) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                for (ContactInfo contactInfoI : contactInfo)
                if (marker.getPosition().latitude == contactInfoI.latitude
                        && marker.getPosition().longitude == contactInfoI.longitude) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+contactInfoI.phone));
                    startActivity(intent);

                }
            }
        };
        btnCall.setOnTouchListener(lsCall);


        lsBrowser = new OnInterInfoWindowTouchListener(btnBrowser) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                for (ContactInfo contactInfoI : contactInfo)
                if (marker.getPosition().latitude == contactInfoI.latitude
                        && marker.getPosition().longitude == contactInfoI.longitude) {
                    String url = "https://"+contactInfoI.link;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
            }
        };
        btnBrowser.setOnTouchListener(lsBrowser);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                lsCall.setMarker(marker);
                lsBrowser.setMarker(marker);
                TextView name = (TextView) contentView.findViewById(R.id.name);
                TextView location = (TextView) contentView.findViewById(R.id.location);
                name.setText(marker.getTitle());
                location.setText("Toa do: "+marker.getPosition().latitude +
                ", "+marker.getPosition().longitude);

                mapWrapperLayout.setMarkerWithInfoWindow(marker, contentView);
                return contentView;
            }
        });
    }
}
