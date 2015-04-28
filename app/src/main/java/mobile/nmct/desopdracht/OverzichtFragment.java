package mobile.nmct.desopdracht;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class OverzichtFragment extends ListFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GEKOZEN_STAD = "gekozen_stad";
    private static final String OMGEVING = "omgeving";

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
    private Button btnZoek;

    private ArrayList<String> listItems;
    private ListAdapter adapter;

    public OverzichtFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stad = getArguments().getString(GEKOZEN_STAD);
            omgeving = getArguments().getString(OMGEVING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_overzicht, container, false);

        txtStad = (TextView) v.findViewById(R.id.txtStad);
        btnStad = (Button) v.findViewById(R.id.btnStad);
        btnZoek = (Button) v.findViewById(R.id.btnZoek);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        btnStad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vulListOmgeving();
            }
        });
    }

    private void vulListOmgeving() {

        listItems = new ArrayList<String>();

        if(txtStad.getText().toString().isEmpty()) {
            Log.d("TEST", "Gelieve een stad in te vullen");
        }else {
            Log.d("TEST", "Vul listview op");

            //OMGEVINGEN VAN DE STAD OPHALEN VIA GOOGLE MAPS API
            omgevingenOphalen();

            //gegevens in adapter steken
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listItems) {
            };
            setListAdapter(adapter);
        }
    }

    private void omgevingenOphalen() {

        stad = txtStad.getText().toString();



        try
        {
            Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addressList = geo.getFromLocationName(stad, 1);
            if(addressList.size()>0) {
                Log.d("TEST", "lat: " + addressList.get(0).getLatitude());
                Log.d("TEST", "lon: " + addressList.get(0).getLongitude());

                //listItems.add(stad);
            }
        }catch (IOException exc){
            Log.d("TEST", exc.getMessage());
        }


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
