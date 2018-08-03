package com.example.tps_new_0005.autosearch;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private static final String LOG_TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private TextView mNameTextView;
    private TextView mAddressTextView;
    private TextView mIdTextView;
    private TextView mPhoneTextView;
    private TextView mWebTextView;
    private TextView mAttTextView;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private AutocompleteFilter filter;
    DisplayAddress da;
    /*private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));*/
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(12.910676, 77.640339),
            new LatLng(13.070989, 77.566707));
    /*private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(20.593684, 78.962880), new LatLng(20.593684, 78.962880));*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);
        mNameTextView = (TextView) findViewById(R.id.name);
        mAddressTextView = (TextView) findViewById(R.id.address);
        mIdTextView = (TextView) findViewById(R.id.place_id);
        mPhoneTextView = (TextView) findViewById(R.id.phone);
        mWebTextView = (TextView) findViewById(R.id.web);
        mAttTextView = (TextView) findViewById(R.id.att);

        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        filter=new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE).build();
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW,null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        //da=new DisplayAddress(this);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);

        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            /*mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
                mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }*/

            LatLng latlng=place.getLatLng();
            double lat=latlng.latitude;
            double lng=latlng.longitude;
            Locale locale=place.getLocale();
            Geocoder geocoder = new Geocoder(getApplicationContext(), locale);
            try {
                List<android.location.Address> addresses = geocoder.getFromLocation(lat,lng,1);

                if(addresses != null) {
                    android.location.Address returnedAddress = addresses.get(0);
                    /*StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                    for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                        //Toast.makeText(getApplicationContext(), "Address is: " + returnedAddress.getAddressLine(i), Toast.LENGTH_LONG).show();
                    }*/
                    String streetno=returnedAddress.getSubThoroughfare();
                    String street=returnedAddress.getThoroughfare();
                   String sublocality1="",sublocality2="",sublocality3="";
                    String addressline=returnedAddress.getAddressLine(1);
                    int addlength=returnedAddress.getMaxAddressLineIndex();
                    if(addlength==2)
                        addressline=returnedAddress.getAddressLine(0);
                    if(addlength==1 || addlength==0)
                        addressline="";
                    StringBuilder newaddress=new StringBuilder(addressline);
                        if (newaddress.toString().contains(",")) {
                            sublocality1 = newaddress.substring(1 + newaddress.lastIndexOf(","));
                            int index = newaddress.lastIndexOf(",");
                            int len = newaddress.substring(newaddress.lastIndexOf(",")).length();
                            newaddress = newaddress.delete(index, index + len);
                            //Toast.makeText(getApplicationContext(), country, Toast.LENGTH_LONG).show();
                            if (newaddress.toString().contains(",")) {
                                sublocality2 = newaddress.substring(1 + newaddress.lastIndexOf(",")).trim();
                                index = newaddress.lastIndexOf(",");
                                len = newaddress.substring(newaddress.lastIndexOf(",")).length();
                                newaddress = newaddress.delete(index, index + len);
                                if (newaddress.toString().contains(",")) {
                                    sublocality3 = newaddress.substring(1 + newaddress.lastIndexOf(",")).trim();
                                    index = newaddress.lastIndexOf(",");
                                    len = newaddress.substring(newaddress.lastIndexOf(",")).length();
                                    newaddress = newaddress.delete(index, index + len);
                                } else
                                    sublocality3 = newaddress.toString();
                            } else
                                sublocality2 = newaddress.toString();
                        } else
                            sublocality1 = newaddress.toString();
                    String city=returnedAddress.getLocality();
                    String state=returnedAddress.getAdminArea();
                    String pincode=returnedAddress.getPostalCode();
                    String country=returnedAddress.getCountryName();

                    Toast.makeText(getApplicationContext(),"Address Length= "+addlength+" "+addressline,Toast.LENGTH_LONG).show();
                    Bundle bundle=new Bundle();
                    bundle.putString("streetno",streetno);
                    bundle.putString("street",street);
                    bundle.putString("sublocality1",sublocality1);
                    bundle.putString("sublocality2",sublocality2);
                    bundle.putString("sublocality3",sublocality3);
                    bundle.putString("city",city);
                    bundle.putString("state",state);
                    bundle.putString("pincode",pincode);
                    bundle.putString("country",country);
                    Intent intent=new Intent(getApplicationContext(),DisplayAddress.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Can't find the address",Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Cannot get address",Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }
}
