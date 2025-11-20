package br.com.collectionmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.collectionmanager.adapter.PieceAdapter;
import br.com.collectionmanager.data.CollectionManagerDbHelper;
import br.com.collectionmanager.model.Piece;

public class CollectionActivity extends AppCompatActivity {
    
    private CollectionManagerDbHelper dbHelper;
    private PieceAdapter adapter;
    private RecyclerView recyclerView;
    private Spinner spinnerStatusFilter;
    private Button buttonFilter, buttonAddPiece;
    private TextView textCollectionTitle;
    
    private long collectionId;
    private String collectionName;
    private List<Piece> allPieces;
    private String[] statusOptions = {
        "All",
        "Pending",
        "In Production",
        "Completed"
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        
        dbHelper = new CollectionManagerDbHelper(this);
        
        // Get collection info from intent
        collectionId = getIntent().getLongExtra("collection_id", -1);
        collectionName = getIntent().getStringExtra("collection_name");
        
        textCollectionTitle = findViewById(R.id.text_collection_title);
        recyclerView = findViewById(R.id.recycler_view_pieces);
        spinnerStatusFilter = findViewById(R.id.spinner_status_filter);
        buttonFilter = findViewById(R.id.button_filter);
        buttonAddPiece = findViewById(R.id.button_add_piece);
        
        textCollectionTitle.setText(collectionName);
        
        setupSpinner();
        setupRecyclerView();
        
        buttonAddPiece.setOnClickListener(v -> {
            Intent intent = new Intent(CollectionActivity.this, AddPieceActivity.class);
            intent.putExtra("collection_id", collectionId);
            startActivity(intent);
        });
        
        buttonFilter.setOnClickListener(v -> filterPieces());
        
        loadPieces();
    }
    
    private void setupSpinner() {
        String[] statusOptionsTranslated = {
            getString(R.string.all),
            getString(R.string.pending),
            getString(R.string.in_production),
            getString(R.string.completed)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptionsTranslated);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatusFilter.setAdapter(adapter);

        spinnerStatusFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update the filter when selection changes
                filterPieces();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    
    private void setupRecyclerView() {
        adapter = new PieceAdapter(this, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        adapter.setOnItemClickListener(piece -> {
            // Handle click on piece item if needed
            // For now, we just want to see the piece details
        });
        
        adapter.setOnEditClickListener(piece -> {
            Intent intent = new Intent(CollectionActivity.this, AddPieceActivity.class);
            intent.putExtra("piece_id", piece.getId());
            intent.putExtra("piece_name", piece.getName());
            intent.putExtra("piece_description", piece.getDescription());
            intent.putExtra("piece_entry_date", piece.getEntryDate());
            intent.putExtra("piece_delivery_deadline", piece.getDeliveryDeadline());
            intent.putExtra("piece_observations", piece.getObservations());
            intent.putExtra("piece_status", piece.getStatus());
            intent.putExtra("collection_id", collectionId);
            startActivity(intent);
        });
        
        adapter.setOnDeleteClickListener(piece -> showDeleteConfirmationDialog(piece));
    }
    
    private void loadPieces() {
        allPieces = dbHelper.getPiecesByCollection(collectionId);
        adapter.updatePieces(allPieces);
    }
    
    private void filterPieces() {
        String selectedStatus = spinnerStatusFilter.getSelectedItem().toString();

        // Map the selected translated status back to the original English value
        String statusFilter = selectedStatus; // default to the selected value

        if (selectedStatus.equals(getString(R.string.all))) {
            statusFilter = "All";
        } else if (selectedStatus.equals(getString(R.string.pending))) {
            statusFilter = "Pending";
        } else if (selectedStatus.equals(getString(R.string.in_production))) {
            statusFilter = "In Production";
        } else if (selectedStatus.equals(getString(R.string.completed))) {
            statusFilter = "Completed";
        }

        List<Piece> filteredPieces;
        if (statusFilter.equals("All")) {
            filteredPieces = allPieces;
        } else {
            filteredPieces = new ArrayList<>();
            for (Piece piece : allPieces) {
                if (piece.getStatus().equals(statusFilter)) {
                    filteredPieces.add(piece);
                }
            }
        }

        adapter.updatePieces(filteredPieces);
    }
    
    private void showDeleteConfirmationDialog(Piece piece) {
        new AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete) + " " + getString(R.string.piece_title).toLowerCase())
            .setMessage(getString(R.string.confirm_delete))
            .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                // Delete the piece
                int result = dbHelper.deletePiece(piece.getId());
                if (result > 0) {
                    Toast.makeText(this, "Peça excluída com sucesso", Toast.LENGTH_SHORT).show();
                    loadPieces(); // Refresh the list
                    filterPieces(); // Apply current filter
                } else {
                    Toast.makeText(this, "Erro ao excluir peça", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton(getString(R.string.no), null)
            .show();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadPieces(); // Refresh the list when returning to the activity
        filterPieces(); // Apply current filter
    }
}