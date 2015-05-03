package mobile.nmct.desopdracht;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.List;
import java.util.Locale;


public class OverzichtFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GEKOZEN_STAD = "gekozen_stad";

    private OnFragmentInteractionListener mListener;

    private String stad;
    private String omgeving;

    public static OverzichtFragment newInstance() {
        OverzichtFragment fragment = new OverzichtFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView txtStad;
    private Button btnStad;

    private GoogleMap mMap;

    public OverzichtFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stad = getArguments().getString(GEKOZEN_STAD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_overzicht, container, false);

        txtStad = (TextView) v.findViewById(R.id.txtStad);
        btnStad = (Button) v.findViewById(R.id.btnStad);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();



        btnStad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stad = txtStad.getText().toString();
                toonOmgeving(stad);
            }
        });
    }

    private void toonOmgeving(String stad) {

        if(!stad.equals("")) {
            omgevingenOphalen(stad);
        }
    }

    private void omgevingenOphalen(String stad) {

        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(stad);
            }
        }else{
            setUpMap(stad);
        }
    }

    private void setUpMap(String stad) {

        List<Address> addressList;
        double lat, lon;

        Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
        try {
            addressList = geo.getFromLocationName(stad, 1);
            if(addressList.size()>0) {
                lat = addressList.get(0).getLatitude();
                lon = addressList.get(0).getLongitude();

                String land = addressList.get(0).getCountryName();
                stad = stad.substring(0,1).toUpperCase() + stad.substring(1).toLowerCase();

                LatLng gekozenStad = new LatLng(lat, lon);

                mMap.addMarker(new MarkerOptions().position(gekozenStad).title(stad + " - " + land)).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gekozenStad, 10));

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        ToonKeuzeFragment();
                        return true;
                    }
                });
            }
        }catch(Exception exc) {
            Log.d("TEST", exc.getMessage());
        }
    }

    private void ToonKeuzeFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, new KeuzeFragment());
        ft.addToBackStack("KeuzeFragment");
        ft.commit();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
