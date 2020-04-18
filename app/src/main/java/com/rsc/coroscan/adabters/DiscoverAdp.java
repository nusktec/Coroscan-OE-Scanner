package com.rsc.coroscan.adabters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rsc.coroscan.R;
import com.rsc.coroscan.databinding.DiscoverListBinding;
import com.rsc.coroscan.models.CasesDiscover;

import java.util.List;

//class for discover cases adapter
public class DiscoverAdp extends RecyclerView.Adapter<DiscoverAdp.VH> {
    private CasemoreListener casemoreListener;
    List<CasesDiscover> discoverList;

    //initialize
    public DiscoverAdp(List<CasesDiscover> casesDiscovers, CasemoreListener casemoreListener) {
        this.casemoreListener = casemoreListener;
        this.discoverList = casesDiscovers;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DiscoverListBinding discoverListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.discover_list, parent, false);
        return new VH(discoverListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final CasesDiscover c = discoverList.get(position);
        holder.binding.stateName.setText("State: " + c.getState());
        holder.binding.stateCno.setText("Cases: " + c.getNcase());
        holder.binding.stateCdeath.setText("Death: " + c.getNdeath());
        holder.binding.stateCdischarge.setText("Discharged: " + c.getNdischarged());
        holder.binding.lytClk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casemoreListener.viewDetail(c);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (discoverList == null) ? 0 : this.discoverList.size();
    }

    //static VH class
    static class VH extends RecyclerView.ViewHolder {
        private DiscoverListBinding binding;

        VH(@NonNull DiscoverListBinding binding1) {
            super(binding1.getRoot());
            this.binding = binding1;
        }
    }

    //onclick listener
    public interface CasemoreListener {
        void viewDetail(CasesDiscover discover);
    }
}
