package com.example.mundoinclusivo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mundoinclusivo.R; // Asegúrate de que importe tu R
import java.util.List;
import java.util.Map;

public class ParticipantesAdapter extends RecyclerView.Adapter<ParticipantesAdapter.ViewHolder> {

    private List<Map<String, String>> lista;

    public ParticipantesAdapter(List<Map<String, String>> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usamos un layout simple de Android para no crear otro XML, o puedes reusar item_asistencia
        // Aquí usaré 'android.R.layout.simple_list_item_2' que ya viene en el sistema
        // para mostrar Título (Nombre) y Subtítulo (CI).
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> participante = lista.get(position);

        holder.text1.setText(participante.get("nombre_completo"));
        holder.text2.setText("CI: " + participante.get("ci"));

        // Estilo visual básico
        holder.text1.setTextSize(16);
        holder.text2.setTextColor(0xFF666666); // Gris
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Estos IDs son estándar de Android
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}