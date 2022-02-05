package com.example.hajj_fyp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Dua_Adapter extends RecyclerView.Adapter<Dua_Adapter.Sunah_ViewHolder>{
    List<Dua_Model> dua_modelList;
    public Dua_Adapter(List<Dua_Model> dua_modelList) {
        this.dua_modelList = dua_modelList;
    }

    @NonNull
    @Override
    public Dua_Adapter.Sunah_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duarow,parent,false);
        return new Dua_Adapter.Sunah_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Dua_Adapter.Sunah_ViewHolder holder, int position) {
        Dua_Model dua_model = dua_modelList.get(position);
        holder.faqTitle.setText(dua_model.getTitle());
        holder.faqDescArabic.setText(dua_model.getArabicDescription());
        holder.faqDescEnglish.setText(dua_model.getEnglishDescription());

        boolean isExpandable = dua_modelList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

    }
    @Override
    public int getItemCount() {
        return dua_modelList.size();
    }

    public class Sunah_ViewHolder extends RecyclerView.ViewHolder {
        TextView faqTitle, faqDescArabic, faqDescEnglish;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;

        public Sunah_ViewHolder(@NonNull View itemView) {
            super(itemView);

            faqTitle = itemView.findViewById(R.id.faq_title);
            faqDescArabic = itemView.findViewById(R.id.faq_desc_arabic);
            faqDescEnglish = itemView.findViewById(R.id.faq_desc_english);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dua_Model dua_model = dua_modelList.get(getAdapterPosition());
                    dua_model.setExpandable(!dua_model.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
