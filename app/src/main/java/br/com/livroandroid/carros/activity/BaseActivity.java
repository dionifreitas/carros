package br.com.livroandroid.carros.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.activity.prefs.ConfiguracoesActivity;
import br.com.livroandroid.carros.activity.prefs.ConfiguracoesV11Activity;
import br.com.livroandroid.carros.fragments.CarrosFragment;
import br.com.livroandroid.carros.fragments.CarrosTabFragment;
import br.com.livroandroid.carros.fragments.SiteLivroFragment;
import livroandroid.lib.utils.AndroidUtils;
import livroandroid.lib.utils.NavDrawerUtil;

/**
 * Created by thiago on 22/09/16.
 */

public class BaseActivity extends livroandroid.lib.activity.BaseActivity{

    protected DrawerLayout drawerLayout;
    protected void setUpToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
    }

    protected void setUpNavDrawer(){
        final ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);

        if(drawerLayout != null && navigationView != null){
            NavDrawerUtil.setHeaderValues(navigationView,R.id.containerNavDrawerListViewHeader,R.drawable.nav_drawer_header,R.drawable.ic_logo_user,R.string.nav_drawer_username,R.string.nav_drawer_email);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                    onNavDrawerItemSelected(item);
                    return true;
                }
            });
        }
    }

    private void onNavDrawerItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_item_carros_todos:
                break;
            case R.id.nav_item_carros_classicos:
                Intent intent = new Intent(getContext(),CarrosActivity.class);
                intent.putExtra("tipo",R.string.classicos);
                startActivity(intent);
                break;
            case R.id.nav_item_carros_esportivos:
                intent = new Intent(getContext(),CarrosActivity.class);
                intent.putExtra("tipo",R.string.esportivos);
                startActivity(intent);
                break;
            case R.id.nav_item_carros_luxo:

                intent = new Intent(getContext(),CarrosActivity.class);
                intent.putExtra("tipo",R.string.luxo);
                startActivity(intent);
                break;
            case R.id.nav_item_site_livro:

                intent = new Intent(getContext(),SiteLivroActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_item_settings:
                if (AndroidUtils.isAndroid3Honeycomb()){
                    startActivity(new Intent(this, ConfiguracoesV11Activity.class));
                    Log.i("livroandroid","Honey");
                }else{
                    startActivity(new Intent(this, ConfiguracoesActivity.class));
                    Log.i("livroandroid","5.0");
                }
                break;
        }
    }

    protected void replaceFragment(Fragment fragment) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (drawerLayout != null){
                    openDrawer();
                    return true;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void openDrawer() {
        if(drawerLayout != null){
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    protected void closeDrawer(){
        if(drawerLayout != null){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

}
