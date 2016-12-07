package br.com.livroandroid.carros.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.domain.Carro;

/**
 * Created by thiago on 30/11/16.
 */

public class EditarCarroDialog extends DialogFragment {

    private Callback callback;
    private Carro carro;
    private TextView tNome;

    public interface Callback{
        void onCarroUpdated(Carro carro);
    }

    public static void show(FragmentManager fm, Carro carro, Callback callback){
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("editar_carro");
        if(prev != null){
            ft.remove(prev);
        }
        ft.addToBackStack("null");
        EditarCarroDialog frag = new EditarCarroDialog();
        frag.callback = callback;
        Bundle args = new Bundle();

        args.putParcelable("carro", Parcels.wrap(carro));
        frag.setArguments(args);
        frag.show(ft,"editar_carro");

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null){
            return;
        }

        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width,height);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_editar_carro,container,false);
        view.findViewById(R.id.btAtualizar).setOnClickListener(onClickAtualizar());
        tNome = (TextView)view.findViewById(R.id.tNome);
        this.carro = Parcels.unwrap(getArguments().getParcelable("carro"));
        if (carro != null){
            tNome.setText(carro.nome);
        }
        return view;
    }

    private View.OnClickListener onClickAtualizar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String novoNome = tNome.getText().toString();
                if (novoNome == null || novoNome.trim().length() == 0){
                    tNome.setError("Informe o nome");
                    return;
                }
                Context context = getView().getContext();

                carro.nome = novoNome;
                if(callback != null){
                    callback.onCarroUpdated(carro);
                }
                dismiss();
            }
        };
    }
}
