package br.com.livroandroid.carros.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.utils.PermissionUtils;
import livroandroid.lib.utils.AlertUtils;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String permissions[] = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        boolean ok = PermissionUtils.validate(this,0,permissions);

        if (ok){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int result : grantResults){
            if (result == PackageManager.PERMISSION_DENIED){
                AlertUtils.alert(getContext(), R.string.app_name, R.string.msg_alerta_permissao, R.string.ok, new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
                return;
            }
        }
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
