package br.com.collectionmanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.collectionmanager.R;
import br.com.collectionmanager.model.Piece;

public class PieceAdapter extends RecyclerView.Adapter<PieceAdapter.PieceViewHolder> {
    
    private List<Piece> pieces;
    private Context context;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;
    private OnItemClickListener itemClickListener;
    
    public interface OnEditClickListener {
        void onEditClick(Piece piece);
    }
    
    public interface OnDeleteClickListener {
        void onDeleteClick(Piece piece);
    }
    
    public interface OnItemClickListener {
        void onItemClick(Piece piece);
    }
    
    public PieceAdapter(Context context, List<Piece> pieces) {
        this.context = context;
        this.pieces = pieces;
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
    
    public void updatePieces(List<Piece> newPieces) {
        this.pieces = newPieces;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public PieceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_piece, parent, false);
        return new PieceViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PieceViewHolder holder, int position) {
        Piece piece = pieces.get(position);
        
        holder.textPieceName.setText(piece.getName());
        holder.textPieceDescription.setText(piece.getDescription() != null ? piece.getDescription() : "");
        holder.textEntryDate.setText(piece.getEntryDate() != null ? piece.getEntryDate() : "");
        holder.textDeliveryDeadline.setText(piece.getDeliveryDeadline() != null ? piece.getDeliveryDeadline() : "");
        holder.textObservations.setText(piece.getObservations() != null ? piece.getObservations() : "");
        // Set status color based on status
        int statusColor = Color.GRAY;
        String statusToDisplay = piece.getStatus() != null ? piece.getStatus() : "";

        if (piece.getStatus() != null) {
            if (piece.getStatus().equals("Pending")) {
                statusColor = context.getResources().getColor(R.color.pending_color);
                statusToDisplay = context.getString(R.string.pending);
            } else if (piece.getStatus().equals("In Production")) {
                statusColor = context.getResources().getColor(R.color.production_color);
                statusToDisplay = context.getString(R.string.in_production);
            } else if (piece.getStatus().equals("Completed")) {
                statusColor = context.getResources().getColor(R.color.completed_color);
                statusToDisplay = context.getString(R.string.completed);
            }
        }
        holder.textStatus.setText(statusToDisplay);
        holder.textStatus.setBackgroundColor(statusColor);
        
        holder.buttonEdit.setOnClickListener(v -> {
            if (editClickListener != null) {
                editClickListener.onEditClick(piece);
            }
        });
        
        holder.buttonDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(piece);
            }
        });
        
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(piece);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return pieces.size();
    }
    
    public static class PieceViewHolder extends RecyclerView.ViewHolder {
        TextView textPieceName, textPieceDescription, textEntryDate, textDeliveryDeadline, textObservations, textStatus;
        Button buttonEdit, buttonDelete;
        
        public PieceViewHolder(@NonNull View itemView) {
            super(itemView);
            textPieceName = itemView.findViewById(R.id.text_piece_name);
            textPieceDescription = itemView.findViewById(R.id.text_piece_description);
            textEntryDate = itemView.findViewById(R.id.text_entry_date);
            textDeliveryDeadline = itemView.findViewById(R.id.text_delivery_deadline);
            textObservations = itemView.findViewById(R.id.text_observations);
            textStatus = itemView.findViewById(R.id.text_status);
            buttonEdit = itemView.findViewById(R.id.button_edit_piece);
            buttonDelete = itemView.findViewById(R.id.button_delete_piece);
        }
    }
}