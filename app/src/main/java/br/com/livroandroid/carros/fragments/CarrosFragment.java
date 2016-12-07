package br.com.livroandroid.carros.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.parceler.Parcels;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.carros.CarrosApplication;
import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.activity.CarroActivity;
import br.com.livroandroid.carros.adapter.CarroAdapter;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.CarroDB;
import br.com.livroandroid.carros.domain.CarroService;
import livroandroid.lib.utils.AndroidUtils;
import livroandroid.lib.utils.IOUtils;
import livroandroid.lib.utils.SDCardUtils;

public class CarrosFragment extends BaseFragment {

    private int tipo;
    protected RecyclerView recyclerView;
    private List<Carro> carros;
    protected ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionMode actionMode;

    public static CarrosFragment newInstance(int tipo){
        Bundle args = new Bundle();
        args.putInt("tipo",tipo);
        CarrosFragment carrosFragment = new CarrosFragment();
        carrosFragment.setArguments(args);
        Log.i("CarroService","Tipo: "+tipo);
        return carrosFragment;
    }
    public CarrosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            this.tipo = getArguments().getInt("tipo");
        }

        CarrosApplication.getInstance().getBus().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_carros, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener());
        swipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3
        );

        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AndroidUtils.isNetworkAvailable(getContext())){
                    taskCarros(true);
                    Toast.makeText(getContext(),"Teste conexão",Toast.LENGTH_SHORT).show();
                }else {
                    swipeRefreshLayout.setRefreshing(false);
                    snack(recyclerView,R.string.msg_error_conexao_indisponivel);
                }
                taskCarros(true);
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskCarros(false);
    }

    private void taskCarros(Boolean pullToRefresh) {
        startTask("carros", new GetCarrosTask(pullToRefresh), pullToRefresh ? R.id.swipeToRefresh : R.id.progress);
    }

    private CarroAdapter.CarroOnClickListener onClickCarro() {
        return new CarroAdapter.CarroOnClickListener(){

            @Override
            public void onClickCarro(View view, int idx) {
                Carro c = carros.get(idx);
                if(actionMode == null) {
                        Intent intent = new Intent(getContext(), CarroActivity.class);
                        intent.putExtra("carro", Parcels.wrap(c));

                        startActivity(intent);

                }else {
                    c.selected = !c.selected;
                    updateActionModeTitle();
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onLongClickCarro(View itemView, int position) {
                if(actionMode != null) {
                    return;
                }

                actionMode = getAppCompatActivity().startSupportActionMode(getActionModeCallback());
                Carro c = carros.get(position);
                c.selected = true;
                recyclerView.getAdapter().notifyDataSetChanged();
                updateActionModeTitle();
            }
        };
    }

    private ActionMode.Callback getActionModeCallback() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.menu_frag_carro_context,menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                List<Carro> selectedCarros = getSelectedCarros();
                if (item.getItemId() == R.id.action_delete){
                    CarroDB db = new CarroDB(getContext());
                    try {
                        for (Carro c : selectedCarros){
                            db.delete(c);
                            carros.remove(c);
                        }
                    }finally {
                        db.close();
                    }
                    snack(recyclerView,"Carros excluídos com sucesso");
                }else if (item.getItemId() == R.id.action_share){
                    startTask("compartilhar",new CompartilharTask(selectedCarros));
                }
                mode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                for (Carro c : carros){
                    c.selected = false;
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        };
    }

    private void updateActionModeTitle() {
        if (actionMode != null){
            actionMode.setTitle("Selecione os carros.");
            actionMode.setSubtitle(null);
            List<Carro> selectedCarros = getSelectedCarros();
            if (selectedCarros.size() == 1){
                actionMode.setSubtitle("1 carro selecionado");
            }else if (selectedCarros.size()>1){
                actionMode.setSubtitle(selectedCarros.size()+" carros selecionados");
            }

        }
    }



    private List<Carro> getSelectedCarros() {
        List<Carro> list = new ArrayList<Carro>();
        for (Carro c : carros){
            if (c.selected){
                list.add(c);
            }
        }
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CarrosApplication.getInstance().getBus().unregister(this);
    }

    @Subscribe
    public void onBusAtualizarListaCarros(String refresh){
        taskCarros(false);
    }

    private class GetCarrosTask implements TaskListener<List<Carro>> {
        private boolean refresh;
        public GetCarrosTask(Boolean refresh) {
            this.refresh = refresh;
        }

        @Override
        public List<Carro> execute() throws Exception {
            return CarroService.getCarros(getContext(),tipo,refresh);
        }

        @Override
        public void updateView(List<Carro> carros) {
            if (carros != null){
                CarrosFragment.this.carros = carros;
                recyclerView.setAdapter(new CarroAdapter(getContext(),carros,onClickCarro()));
            }
        }

        @Override
        public void onError(Exception exception) {
            alert("Ocorreu algum erro ao buscar os dados");
        }

        @Override
        public void onCancelled(String cod) {

        }
    }

    private class CompartilharTask implements TaskListener{

        ArrayList<Uri> imageUris = new ArrayList<>();
        private final List<Carro>  selectedCarros;

        public CompartilharTask(List<Carro> selectedCarros){
            this.selectedCarros = selectedCarros;
        }

        @Override
        public Object execute() throws Exception {

            if (selectedCarros != null){
                for (Carro c : selectedCarros) {
                    String url = c.urlFoto;

                    String fileName = url.substring(url.lastIndexOf("/"));

                    File file = SDCardUtils.getPrivateFile(getContext(),"carros",fileName);
                    IOUtils.downloadToFile(url,file);

                    imageUris.add(Uri.fromFile(file));
                }
            }

            return null;
        }

        @Override
        public void updateView(Object response) {
            Intent shareIntent = new Intent();

            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,imageUris);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent,"Enviar Carros"));
        }

        @Override
        public void onError(Exception exception) {
            alert("Ocorreu um erro ao compartilhar.");
        }

        @Override
        public void onCancelled(String cod) {

        }
    }
}
