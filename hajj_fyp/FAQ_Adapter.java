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

public class FAQ_Adapter extends RecyclerView.Adapter<FAQ_Adapter.FAQ_ViewHolder> {
    List<FAQ_Model> faq_modelList;

    public FAQ_Adapter(List<FAQ_Model> faq_modelList) {
        this.faq_modelList = faq_modelList;
    }

    @NonNull
    @Override
    public FAQ_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_row,parent,false);
        return new FAQ_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQ_ViewHolder holder, int position) {
        FAQ_Model faq_model = faq_modelList.get(position);
        holder.faqTitle.setText(faq_model.getTitle());
        holder.faqDesc.setText(faq_model.getDescription());

        boolean isExpandable = faq_modelList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return faq_modelList.size();
    }

    public class FAQ_ViewHolder extends RecyclerView.ViewHolder {
        TextView faqTitle, faqDesc;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;

        public FAQ_ViewHolder(@NonNull View itemView) {
            super(itemView);

            faqTitle = itemView.findViewById(R.id.faq_title);
            faqDesc = itemView.findViewById(R.id.faq_desc);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FAQ_Model faq_model = faq_modelList.get(getAdapterPosition());
                    faq_model.setExpandable(!faq_model.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
