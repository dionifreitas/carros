package br.com.livroandroid.carros.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.domain.Carro;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends BaseFragment implements OnMapReadyCallback {

    private GoogleMap map;
    private Carro carro;

    public MapaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        setHasOptionsMenu(true);
        this.carro = Parcels.unwrap(getArguments().getParcelable("carro"));

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        if (carro != null) {
            map.setMyLocationEnabled(true);
            LatLng location = new LatLng(Double.parseDouble(carro.latitude), Double.parseDouble(carro.longitude));
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 13);

            map.moveCamera(update);

            map.addMarker(new MarkerOptions().title(carro.nome).snippet(carro.desc).position(location));

            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag_mapa, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (map != null && carro != null) {
            if (item.getItemId() == R.id.action_location_carro) {
                LatLng location = new LatLng(Double.parseDouble(carro.latitude), Double.parseDouble(carro.longitude));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
            } else if (item.getItemId() == R.id.action_location_directions) {
                toast("Mostrar rota/direções até a fábrica.");
            } else if (item.getItemId() == R.id.action_zoom_in) {
                toast("zoom +");
                map.animateCamera(CameraUpdateFactory.zoomIn());
            } else if (item.getItemId() == R.id.action_zoom_out) {
                toast("zoom -");
                map.animateCamera(CameraUpdateFactory.zoomOut());
            } else if (item.getItemId() == R.id.action_mapa_normal) {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            } else if (item.getItemId() == R.id.action_mapa_satelite) {
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else if (item.getItemId() == R.id.action_mapa_terreno) {
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            } else if (item.getItemId() == R.id.action_mapa_hibrido) {
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
