package br.com.livroandroid.carros.activity;

import android.app.backup.BackupManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.adapter.TabsAdapter;
import br.com.livroandroid.carros.fragments.AboutDialog;
import br.com.livroandroid.carros.fragments.CarrosFragment;
import br.com.livroandroid.carros.fragments.CarrosTabFragment;
import livroandroid.lib.utils.Prefs;

public class MainActivity extends BaseActivity {

    private BackupManager backupManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        setUpNavDrawer();
        setupViewPagerTabs();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snack(v,"Exemplo de FAB Button");
            }
        });
        backupManager = new BackupManager(getContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_about:
                AboutDialog.showAbout(getSupportFragmentManager());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setupViewPagerTabs(){

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new TabsAdapter(getContext(),getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        int cor = ContextCompat.getColor(getContext(),R.color.white);
        tabLayout.setTabTextColors(cor,cor);
        int tabIdx = Prefs.getInteger(getContext(),"tabIdx");
        viewPager.setCurrentItem(tabIdx);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Prefs.setInteger(getContext(),"tabIdx",viewPager.getCurrentItem());
                backupManager.dataChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
