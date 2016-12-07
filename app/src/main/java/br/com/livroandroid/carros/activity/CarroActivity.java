package br.com.livroandroid.carros.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.fragments.CarroFragment;

public class CarroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro);

        setUpToolbar();

        Carro c = (Carro) Parcels.unwrap(getIntent().getParcelableExtra("carro"));

        getSupportActionBar().setTitle(c.nome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView appBarImg = (ImageView)findViewById(R.id.appBarImg);
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressImg);
        Picasso.with(getContext()).load(c.urlFoto).into(appBarImg, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {

            }
        });

        if (savedInstanceState == null){
            CarroFragment fragment = new CarroFragment();
            fragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.CarroFragment,fragment).commit();
        }
    }
}
