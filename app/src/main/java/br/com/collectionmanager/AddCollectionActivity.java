package br.com.collectionmanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.collectionmanager.data.CollectionManagerDbHelper;
import br.com.collectionmanager.model.Collection;

public class AddCollectionActivity extends AppCompatActivity {
    
    private CollectionManagerDbHelper dbHelper;
    private EditText editTextCollectionName, editTextCollectionDescription, editTextStartDate, editTextReleaseDate;
    private Button buttonSaveCollection, buttonCancel;
    
    private long collectionId = -1; // For edit mode
    private Calendar startDateCalendar, releaseDateCalendar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);
        
        dbHelper = new CollectionManagerDbHelper(this);
        
        initializeViews();
        setupDatePickers();
        loadCollectionForEdit();
    }
    
    private void initializeViews() {
        editTextCollectionName = findViewById(R.id.edit_text_collection_name);
        editTextCollectionDescription = findViewById(R.id.edit_text_collection_description);
        editTextStartDate = findViewById(R.id.edit_text_start_date);
        editTextReleaseDate = findViewById(R.id.edit_text_release_date);
        buttonSaveCollection = findViewById(R.id.button_save_collection);
        buttonCancel = findViewById(R.id.button_cancel);

        buttonSaveCollection.setOnClickListener(v -> saveCollection());
        buttonCancel.setOnClickListener(v -> finish());
    }
    
    private void setupDatePickers() {
        startDateCalendar = Calendar.getInstance();
        releaseDateCalendar = Calendar.getInstance();
        
        // Set click listeners for date fields
        editTextStartDate.setOnClickListener(v -> showDatePicker(startDateCalendar, editTextStartDate));
        editTextReleaseDate.setOnClickListener(v -> showDatePicker(releaseDateCalendar, editTextReleaseDate));
        
        // Allow manual input as well
        editTextStartDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePicker(startDateCalendar, editTextStartDate);
            }
        });
        
        editTextReleaseDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePicker(releaseDateCalendar, editTextReleaseDate);
            }
        });
    }
    
    private void showDatePicker(Calendar calendar, EditText editText) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year1, month1, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year1);
                calendar.set(Calendar.MONTH, month1);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
                editText.setText(formattedDate);
            },
            year, month, day
        );
        
        datePickerDialog.show();
    }
    
    private void loadCollectionForEdit() {
        collectionId = getIntent().getLongExtra("collection_id", -1);

        if (collectionId != -1) {
            // This is edit mode
            Collection collection = dbHelper.getCollection(collectionId);
            if (collection != null) {
                editTextCollectionName.setText(collection.getName());
                editTextCollectionDescription.setText(collection.getDescription());
                editTextStartDate.setText(collection.getStartDate());
                editTextReleaseDate.setText(collection.getEstimatedRelease());

                // Update calendars for the date pickers
                if (collection.getStartDate() != null) {
                    try {
                        java.util.Date startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(collection.getStartDate());
                        if (startDate != null) {
                            startDateCalendar.setTime(startDate);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (collection.getEstimatedRelease() != null) {
                    try {
                        java.util.Date releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(collection.getEstimatedRelease());
                        if (releaseDate != null) {
                            releaseDateCalendar.setTime(releaseDate);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    private void saveCollection() {
        String name = editTextCollectionName.getText().toString().trim();
        String description = editTextCollectionDescription.getText().toString().trim();
        String startDate = editTextStartDate.getText().toString().trim();
        String releaseDate = editTextReleaseDate.getText().toString().trim();

        if (name.isEmpty()) {
            editTextCollectionName.setError(getString(R.string.name) + " é obrigatório");
            editTextCollectionName.requestFocus();
            return;
        }

        if (startDate.isEmpty()) {
            editTextStartDate.setError(getString(R.string.start_date) + " é obrigatória");
            editTextStartDate.requestFocus();
            return;
        }

        if (releaseDate.isEmpty()) {
            editTextReleaseDate.setError(getString(R.string.estimated_release) + " é obrigatória");
            editTextReleaseDate.requestFocus();
            return;
        }

        Collection collection;
        if (collectionId != -1) {
            // Update existing collection
            collection = new Collection(collectionId, name, description, startDate, releaseDate, null);
            long result = dbHelper.updateCollection(collection);
            if (result > 0) {
                Toast.makeText(this, "Coleção atualizada com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao atualizar coleção", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            // Create new collection
            collection = new Collection(name, description, startDate, releaseDate);
            long result = dbHelper.insertCollection(collection);
            if (result != -1) {
                Toast.makeText(this, "Coleção salva com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao salvar coleção", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        finish();
    }
}