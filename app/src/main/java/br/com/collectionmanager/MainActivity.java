package br.com.collectionmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import br.com.collectionmanager.adapter.CollectionAdapter;
import br.com.collectionmanager.data.CollectionManagerDbHelper;
import br.com.collectionmanager.model.Collection;

public class MainActivity extends AppCompatActivity {
    
    private CollectionManagerDbHelper dbHelper;
    private CollectionAdapter adapter;
    private RecyclerView recyclerView;
    private Button buttonAddCollection;
    private List<Collection> allCollections;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dbHelper = new CollectionManagerDbHelper(this);
        
        recyclerView = findViewById(R.id.recycler_view_collections);
        buttonAddCollection = findViewById(R.id.button_add_collection);
        
        setupRecyclerView();
        
        buttonAddCollection.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddCollectionActivity.class);
            startActivity(intent);
        });
        
        loadCollections();
    }
    
    private void setupRecyclerView() {
        adapter = new CollectionAdapter(this, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        adapter.setOnItemClickListener(collection -> {
            // Navigate to the collection details activity (pieces list for this collection)
            Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
            intent.putExtra("collection_id", collection.getId());
            intent.putExtra("collection_name", collection.getName());
            startActivity(intent);
        });
        
        adapter.setOnEditClickListener(collection -> {
            Intent intent = new Intent(MainActivity.this, AddCollectionActivity.class);
            intent.putExtra("collection_id", collection.getId());
            intent.putExtra("collection_name", collection.getName());
            intent.putExtra("collection_description", collection.getDescription());
            intent.putExtra("collection_start_date", collection.getStartDate());
            intent.putExtra("collection_release_date", collection.getEstimatedRelease());
            startActivity(intent);
        });
        
        adapter.setOnDeleteClickListener(collection -> showDeleteConfirmationDialog(collection));
    }
    
    private void loadCollections() {
        allCollections = dbHelper.getAllCollections();
        adapter.updateCollections(allCollections);
    }
    
    private void showDeleteConfirmationDialog(Collection collection) {
        new AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete) + " " + getString(R.string.collection_title).toLowerCase())
            .setMessage("Tem certeza que deseja excluir esta coleção? Todas as peças nesta coleção também serão excluídas.")
            .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                // Check if the collection has pieces associated with it
                List<br.com.collectionmanager.model.Piece> pieces = dbHelper.getPiecesByCollection(collection.getId());
                if (pieces.size() > 0) {
                    Toast.makeText(this, "Não é possível excluir coleção com peças. Por favor, exclua todas as peças primeiro.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Delete the collection
                int result = dbHelper.deleteCollection(collection.getId());
                if (result > 0) {
                    Toast.makeText(this, "Coleção excluída com sucesso", Toast.LENGTH_SHORT).show();
                    loadCollections(); // Refresh the list
                } else {
                    Toast.makeText(this, "Erro ao excluir coleção", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton(getString(R.string.no), null)
            .show();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadCollections(); // Refresh the list when returning to the activity
    }
}