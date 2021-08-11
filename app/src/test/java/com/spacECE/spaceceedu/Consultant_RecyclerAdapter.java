package com.spacECE.spaceceedu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.Consultants.Consultant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Consultant_RecyclerAdapter extends RecyclerView.Adapter<Consultant_RecyclerAdapter.MyViewHolder> implements Filterable {
    ArrayList<Consultant> consultants;
    ArrayList<Consultant> AllConsultants,preFilteredList;

    private RecyclerViewClickListener listener;

    public Consultant_RecyclerAdapter(ArrayList<Consultant> consultants, RecyclerViewClickListener listener) {
        this.consultants = consultants;
        this.listener = listener;
        AllConsultants = new ArrayList<>(consultants);
        preFilteredList = new ArrayList<>(consultants);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, rating, price, category;
        private ImageView profile;

        public MyViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.textView_Name);
            profile = view.findViewById(R.id.imageView_ProfilePic);
            category = view.findViewById(R.id.textView_Category);
            rating = view.findViewById(R.id.textView_Rating);
            price = view.findViewById(R.id.textView_Price);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {listener.onClick(view, getAdapterPosition()); }
    }

    @NonNull
    @Override
    public Consultant_RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultant_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = consultants.get(position).getName();
        String profilePic_src = consultants.get(position).getProfilePic_src();
        String rating = consultants.get(position).getRating();
        String categories = consultants.get(position).getCategories();
        int price = consultants.get(position).getPrice();

        holder.name.setText(name);
        holder.category.setText(categories);
        holder.price.setText(String.valueOf(price));
        holder.rating.setText(String.valueOf(rating));
        Picasso.get().load("https://careernuts.com/wp-content/uploads/2019/04/How-to-Become-a-Management-Consultant-in-India-1.jpg").into(holder.profile);
    }

    @Override
    public int getItemCount() {
        return consultants.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

    public void listUpdate(String categoryFilter, String ratingFilter) {
        preFilteredList = new ArrayList<>();

        if (categoryFilter != null || ratingFilter != null) {
            for (Consultant item : AllConsultants) {
                if (String.valueOf(item.getRating()).toLowerCase(Locale.ROOT).contains(ratingFilter) && item.getCategories().toString().toLowerCase(Locale.ROOT).contains(categoryFilter)) {
                    preFilteredList.add(item);
                }
            }
        }else{
            preFilteredList= new ArrayList<Consultant>(AllConsultants);
        }
        consultants.clear();
        consultants.addAll(preFilteredList);
        getFilter();

    }

    @Override
    public Filter getFilter(){
        return SearchFilter;
    }

    private Filter SearchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Consultant> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(preFilteredList);
            } else {
                String filteredPattern = charSequence.toString().toLowerCase(Locale.ROOT).trim();

                for (Consultant item : preFilteredList) {
                    if (item.getName().toLowerCase(Locale.ROOT).contains(filteredPattern) || item.getCategories().toString().toLowerCase(Locale.ROOT).contains(filteredPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            consultants.clear();
            consultants.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}