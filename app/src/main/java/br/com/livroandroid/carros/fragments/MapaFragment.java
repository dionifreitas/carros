package br.com.livroandroid.carros.fragments;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.parceler.Parcels;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.CarroService;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends BaseFragment implements OnMapReadyCallback {

    private GoogleMap map;
    private Carro carro;
    private static List<LatLng> list;
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
        list = new ArrayList<>();
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
                if(list == null){
                    Toast.makeText(getContext(),"Obtendo localização",Toast.LENGTH_SHORT).show();
                }else {
                    String destino = map.getMyLocation().getLatitude() + "," + map.getMyLocation().getLongitude();
                    String origem = String.valueOf(carro.latitude) + "," + String.valueOf(carro.longitude);
                    Task tarefa = new Task(map);
                    tarefa.execute(destino, origem);
                    Log.d("livroandroid","Tamanho fora: "+list.size());
                }


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

    private class Task extends AsyncTask<String,Integer,List<LatLng>>{

        private GoogleMap map;

        public Task(GoogleMap map){
            this.map = map;
        }

        @Override
        protected List<LatLng> doInBackground(String... params) {
            try {
                return CarroService.getLocalizacoes(params[0],params[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<LatLng> list) {
            Log.d("livroandroid","Tamanho: "+list.size());
            MapaFragment.list = list;
            desenhaRotas();
            super.onPostExecute(list);
        }

        private void desenhaRotas(){
            PolylineOptions po = new PolylineOptions();

            for(int i = 0; i < list.size();i++){
                po.add(list.get(i));
            }

            po.color(Color.BLUE);
            LatLng location = new LatLng(map.getMyLocation().getLatitude(),map.getMyLocation().getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location,4);
            map.animateCamera(update);
            map.addPolyline(po);
        }

    }
}
