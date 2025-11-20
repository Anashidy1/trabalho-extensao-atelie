package br.com.collectionmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.collectionmanager.R;
import br.com.collectionmanager.model.Collection;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {
    
    private List<Collection> collections;
    private Context context;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;
    private OnItemClickListener itemClickListener;
    
    public interface OnEditClickListener {
        void onEditClick(Collection collection);
    }
    
    public interface OnDeleteClickListener {
        void onDeleteClick(Collection collection);
    }
    
    public interface OnItemClickListener {
        void onItemClick(Collection collection);
    }
    
    public CollectionAdapter(Context context, List<Collection> collections) {
        this.context = context;
        this.collections = collections;
    }
    
    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }
    
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
    
    public void updateCollections(List<Collection> newCollections) {
        this.collections = newCollections;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_collection, parent, false);
        return new CollectionViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        Collection collection = collections.get(position);
        
        holder.textCollectionName.setText(collection.getName());
        holder.textCollectionDescription.setText(collection.getDescription() != null ? collection.getDescription() : "");
        holder.textStartDate.setText(collection.getStartDate() != null ? collection.getStartDate() : "");
        holder.textReleaseDate.setText(collection.getEstimatedRelease() != null ? collection.getEstimatedRelease() : "");
        
        holder.buttonEdit.setOnClickListener(v -> {
            if (editClickListener != null) {
                editClickListener.onEditClick(collection);
            }
        });
        
        holder.buttonDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(collection);
            }
        });
        
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(collection);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return collections.size();
    }
    
    public static class CollectionViewHolder extends RecyclerView.ViewHolder {
        TextView textCollectionName, textCollectionDescription, textStartDate, textReleaseDate;
        Button buttonEdit, buttonDelete;
        
        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            textCollectionName = itemView.findViewById(R.id.text_collection_name);
            textCollectionDescription = itemView.findViewById(R.id.text_collection_description);
            textStartDate = itemView.findViewById(R.id.text_start_date);
            textReleaseDate = itemView.findViewById(R.id.text_release_date);
            buttonEdit = itemView.findViewById(R.id.button_edit_collection);
            buttonDelete = itemView.findViewById(R.id.button_delete_collection);
        }
    }
}