package br.com.livroandroid.carros.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.domain.Carro;

/**
 * Created by thiago on 27/09/16.
 */

public class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.CarrosViewHolder> {
    protected static final String TAG = "livroandroid";
    private final List<Carro> carros;
    private final Context context;
    private CarroOnClickListener carroOnClickListener;

    public CarroAdapter(Context context, List<Carro> carros, CarroOnClickListener carroOnClickListener){
        this.context= context;
        this.carros = carros;
        this.carroOnClickListener = carroOnClickListener;
    }
    @Override
    public CarrosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_carro,parent,false);
        CarrosViewHolder holder = new CarrosViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CarrosViewHolder holder, final int position) {
        Carro c = carros.get(position);
        holder.tNome.setText(c.nome);
        holder.progress.setVisibility(View.VISIBLE);
        Picasso.with(context).load(c.urlFoto).fit().into(holder.img, new Callback() {
            @Override
            public void onSuccess() {
                holder.progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {
                holder.progress.setVisibility(View.GONE);
            }
        });
        if (carroOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    carroOnClickListener.onClickCarro(holder.itemView,position);
                }
            });
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                carroOnClickListener.onLongClickCarro(holder.itemView,position);
                return true;
            }
        });

        int corFundo = context.getResources().getColor(c.selected ? R.color.primary : R.color.white);
        holder.cardView.setCardBackgroundColor(corFundo);

        int corFonte = context.getResources().getColor(c.selected ? R.color.white : R.color.primary);
        holder.tNome.setTextColor(corFonte);
    }

    @Override
    public int getItemCount() {
        return carros != null ? carros.size() : 0;
    }

    public interface CarroOnClickListener{
        void onClickCarro(View view, int idx);
        void onLongClickCarro(View itemView, int position);
    }

    public static class CarrosViewHolder extends RecyclerView.ViewHolder{

        public TextView tNome;
        ImageView img;
        ProgressBar progress;
        CardView cardView;

        public CarrosViewHolder(View itemView) {
        super(itemView);
            tNome = (TextView) itemView.findViewById(R.id.text);
            img = (ImageView)itemView.findViewById(R.id.img);
            progress =(ProgressBar)itemView.findViewById(R.id.progressImg);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
    }
}
}