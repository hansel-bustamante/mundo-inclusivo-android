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

public class ActividadesAdapter extends RecyclerView.Adapter<ActividadesAdapter.ViewHolder> {

    private List<Map<String, String>> listaActividades;
    private OnItemClickListener listener;

    // Interfaz para manejar el clic en la actividad
    public interface OnItemClickListener {
        void onItemClick(Map<String, String> actividad);
    }

    public ActividadesAdapter(List<Map<String, String>> lista, OnItemClickListener listener) {
        this.listaActividades = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> actividad = listaActividades.get(position);
        holder.tvNombre.setText(actividad.get("nombre"));
        holder.tvFecha.setText("📅 " + actividad.get("fecha"));
        holder.tvLugar.setText("📍 " + actividad.get("lugar"));

        holder.itemView.setOnClickListener(v -> listener.onItemClick(actividad));
    }

    @Override
    public int getItemCount() {
        return listaActividades.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvFecha, tvLugar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreActividad);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvLugar = itemView.findViewById(R.id.tvLugar);
        }
    }
}