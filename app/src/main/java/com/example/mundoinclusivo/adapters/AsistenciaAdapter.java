package com.example.mundoinclusivo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mundoinclusivo.R;
import java.util.List;
import java.util.Map;

public class AsistenciaAdapter extends RecyclerView.Adapter<AsistenciaAdapter.ViewHolder> {

    private List<Map<String, String>> lista;
    private OnCheckChangeListener listener;

    public interface OnCheckChangeListener {
        void onCheckChanged(String idPersona, boolean isChecked);
    }

    public AsistenciaAdapter(List<Map<String, String>> lista, OnCheckChangeListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asistencia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> p = lista.get(position);

        holder.tvNombre.setText(p.get("nombre_completo"));
        holder.tvCI.setText(p.get("ci"));

        // Evitar que el listener se dispare al reciclar la vista (scrolling)
        holder.cbAsistencia.setOnCheckedChangeListener(null);

        // Estado del check (1 = true, 0 = false)
        boolean asistio = "1".equals(p.get("asistio"));
        holder.cbAsistencia.setChecked(asistio);

        // Configurar nuevo listener
        holder.cbAsistencia.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Actualizamos el modelo local para que el scroll no lo borre
            p.put("asistio", isChecked ? "1" : "0");
            // Avisamos a la actividad
            listener.onCheckChanged(p.get("id_persona"), isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCI;
        CheckBox cbAsistencia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreAsistencia);
            tvCI = itemView.findViewById(R.id.tvCIAsistencia);
            cbAsistencia = itemView.findViewById(R.id.cbAsistencia);
        }
    }
}