package br.com.livroandroid.carros.activity;

import android.os.Bundle;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.fragments.SiteLivroFragment;

public class SiteLivroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_livro);
        setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null){
            SiteLivroFragment fragment = new SiteLivroFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
        }
    }
}
