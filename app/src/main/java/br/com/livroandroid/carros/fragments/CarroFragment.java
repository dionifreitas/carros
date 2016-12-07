package br.com.livroandroid.carros.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import org.parceler.Parcels;

import br.com.livroandroid.carros.CarrosApplication;
import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.activity.CarroActivity;
import br.com.livroandroid.carros.activity.MapaActivity;
import br.com.livroandroid.carros.activity.VideoActivity;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.CarroDB;
import livroandroid.lib.utils.IntentUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarroFragment extends BaseFragment {

    private Carro carro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carro, container, false);
        carro = (Carro) Parcels.unwrap(getArguments().getParcelable("carro"));
        setHasOptionsMenu(true);
        view.findViewById(R.id.imgPlayVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVideo(carro.urlVideo,v);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTextString(R.id.tDesc, carro.desc);

        final ImageView imgView = (ImageView) getView().findViewById(R.id.img);
        Picasso.with(getContext()).load(carro.urlFoto).fit().into(imgView);

        setTextString(R.id.tLatLng,String.format("Lat/lng : %s %s",carro.latitude,carro.longitude));
        MapaFragment mapaFragment = new MapaFragment();
        mapaFragment.setArguments(getArguments());
        getChildFragmentManager().beginTransaction().replace(R.id.mapFragment,mapaFragment).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag_carro, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            EditarCarroDialog.show(getFragmentManager(), carro, new EditarCarroDialog.Callback() {
                @Override
                public void onCarroUpdated(Carro carro) {
                    toast("Carro ["+carro.nome+"] atualizado");
                    CarroDB db = new CarroDB(getContext());
                    db.save(carro);
                    CarroActivity a = (CarroActivity) getActivity();
                    a.setTitle(carro.nome);
                    CarrosApplication.getInstance().getBus().post("refresh");
                }
            });
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            DeletarCarroDialog.show(getFragmentManager(), new DeletarCarroDialog.Callback() {
                @Override
                public void onCLickYes() {
                    toast("Carro ["+carro.nome+"] deletado");
                    CarroDB db = new CarroDB(getActivity());
                    db.delete(carro);
                    CarrosApplication.getInstance().getBus().post("refresh");
                    getActivity().finish();
                }
            });
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            toast("Compartinhar");
            return true;
        } else if (item.getItemId() == R.id.action_maps) {

            Intent intent = new Intent(getContext(), MapaActivity.class);
            intent.putExtra("carro",Parcels.wrap(carro));
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_video) {
            final String url = carro.urlVideo;
            View menuItemView = getActivity().findViewById(item.getItemId());
            if (menuItemView != null && url != null){
                showVideo(url,menuItemView);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showVideo(final String url, View ancoraView) {
        if (url != null && ancoraView != null){
            PopupMenu popupMenu = new PopupMenu(getActionBar().getThemedContext(),ancoraView);
            popupMenu.inflate(R.menu.menu_popup_video);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_video_browser){
                        IntentUtils.openBrowser(getContext(),url);
                    }else if (item.getItemId() == R.id.action_video_player){
                        IntentUtils.showVideo(getContext(),url);
                    }else if (item.getItemId() == R.id.action_video_videoview){
                        Intent intent = new Intent(getContext(),VideoActivity.class);
                        intent.putExtra("carro", Parcels.wrap(carro));
                        startActivity(intent);
                    }
                    return true;
                }
            });
            popupMenu.show();
        }
    }
}
