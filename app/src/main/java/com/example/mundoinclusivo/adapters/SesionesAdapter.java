package com.example.mundoinclusivo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mundoinclusivo.R;
import java.util.List;
import java.util.Map;

public class SesionesAdapter extends RecyclerView.Adapter<SesionesAdapter.ViewHolder> {

    private List<Map<String, String>> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String idSesion, String tema);
    }

    public SesionesAdapter(List<Map<String, String>> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sesion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> item = lista.get(position);
        holder.tvTema.setText(item.get("tema"));
        holder.tvFecha.setText("📅 " + item.get("fecha"));

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item.get("id"), item.get("tema")));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTema, tvFecha;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTema = itemView.findViewById(R.id.tvTemaSesion);
            tvFecha = itemView.findViewById(R.id.tvFechaSesion);
        }
    }
}