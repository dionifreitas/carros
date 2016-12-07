package br.com.livroandroid.carros.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import org.parceler.Parcels;
import java.util.List;
import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.adapter.CarroAdapter;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.CarroDB;

public class CarrosIntentActivity extends BaseActivity {

    private List<Carro> carros;
    private String TAG = "livroandroid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carros_intent);

        Intent intent = getIntent();
        Uri uri = intent.getData();

        Log.d(TAG,"Action: "+ intent.getAction());
        Log.d(TAG,"Scheme: "+uri.getScheme());
        Log.d(TAG,"Host: "+uri.getHost());
        Log.d(TAG,"Path: "+uri.getPath());
        Log.d(TAG,"PathSegments: "+uri.getPathSegments());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        setUpToolbar();

        CarroDB db = new CarroDB(this);

        try{
            if ("/carros".equals(uri.getPath())){
                this.carros = db.findAll();
                recyclerView.setAdapter(new CarroAdapter(this,carros,onClickCarro()));
            }else {
                List<String> paths = uri.getPathSegments();
                String nome = paths.get(1);
                Carro carro = db.findByNome(nome);
                if (carro != null){
                    Intent carroIntent = new Intent(this,CarroActivity.class);
                    carroIntent.putExtra("carro", Parcels.wrap(carro));
                    startActivity(carroIntent);
                    finish();
                }
            }
        }finally {
            db.close();
        }

    }

    private CarroAdapter.CarroOnClickListener onClickCarro() {
        return new CarroAdapter.CarroOnClickListener() {
            @Override
            public void onClickCarro(View view, int idx) {
                Carro c = carros.get(idx);
                Intent intent = new Intent();
                intent.putExtra("nome",c.nome);
                intent.putExtra("url_foto",c.urlFoto);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }

            @Override
            public void onLongClickCarro(View itemView, int position) {

            }
        };
    }
}
