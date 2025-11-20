package br.com.collectionmanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.collectionmanager.data.CollectionManagerDbHelper;
import br.com.collectionmanager.model.Piece;

public class AddPieceActivity extends AppCompatActivity {
    
    private CollectionManagerDbHelper dbHelper;
    private EditText editTextName, editTextDescription, editTextEntryDate, editTextDeliveryDeadline, editTextObservations;
    private Spinner spinnerStatus;
    private Button buttonSave, buttonCancel;
    
    private long pieceId = -1; // For edit mode
    private long collectionId = -1; // The collection this piece belongs to
    private Calendar entryDateCalendar, deliveryDateCalendar;
    
    private String[] statusOptions = {
        "Pending",
        "In Production",
        "Completed"
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_piece);
        
        dbHelper = new CollectionManagerDbHelper(this);
        
        initializeViews();
        setupSpinner();
        setupDatePickers();
        loadPieceForEdit();
    }
    
    private void initializeViews() {
        editTextName = findViewById(R.id.edit_text_name);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextEntryDate = findViewById(R.id.edit_text_entry_date);
        editTextDeliveryDeadline = findViewById(R.id.edit_text_delivery_deadline);
        editTextObservations = findViewById(R.id.edit_text_observations);
        spinnerStatus = findViewById(R.id.spinner_status);
        buttonSave = findViewById(R.id.button_save);
        buttonCancel = findViewById(R.id.button_cancel);
        
        buttonSave.setOnClickListener(v -> savePiece());
        buttonCancel.setOnClickListener(v -> finish());
    }
    
    private void setupSpinner() {
        String[] statusOptionsTranslated = {
            getString(br.com.collectionmanager.R.string.pending),
            getString(br.com.collectionmanager.R.string.in_production),
            getString(br.com.collectionmanager.R.string.completed)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptionsTranslated);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
    }
    
    private void setupDatePickers() {
        entryDateCalendar = Calendar.getInstance();
        deliveryDateCalendar = Calendar.getInstance();
        
        // Set click listeners for date fields
        editTextEntryDate.setOnClickListener(v -> showDatePicker(entryDateCalendar, editTextEntryDate));
        editTextDeliveryDeadline.setOnClickListener(v -> showDatePicker(deliveryDateCalendar, editTextDeliveryDeadline));
        
        // Allow manual input as well
        editTextEntryDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePicker(entryDateCalendar, editTextEntryDate);
            }
        });
        
        editTextDeliveryDeadline.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePicker(deliveryDateCalendar, editTextDeliveryDeadline);
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
    
    private void loadPieceForEdit() {
        pieceId = getIntent().getLongExtra("piece_id", -1);

        if (pieceId != -1) {
            // This is edit mode
            Piece piece = dbHelper.getPiece(pieceId);
            if (piece != null) {
                editTextName.setText(piece.getName());
                editTextDescription.setText(piece.getDescription());
                editTextEntryDate.setText(piece.getEntryDate());
                editTextDeliveryDeadline.setText(piece.getDeliveryDeadline());
                editTextObservations.setText(piece.getObservations());

                // Set the spinner selection based on the status
                int spinnerPosition = 0;
                for (int i = 0; i < statusOptions.length; i++) {
                    if (statusOptions[i].equals(piece.getStatus())) {
                        spinnerPosition = i;
                        break;
                    }
                }
                spinnerStatus.setSelection(spinnerPosition);

                // Update calendars for the date pickers
                if (piece.getEntryDate() != null) {
                    try {
                        java.util.Date entryDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(piece.getEntryDate());
                        if (entryDate != null) {
                            entryDateCalendar.setTime(entryDate);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (piece.getDeliveryDeadline() != null) {
                    try {
                        java.util.Date deliveryDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(piece.getDeliveryDeadline());
                        if (deliveryDate != null) {
                            deliveryDateCalendar.setTime(deliveryDate);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            // This is add mode - we need the collection ID
            collectionId = getIntent().getLongExtra("collection_id", -1);
        }
    }
    
    private void savePiece() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String entryDate = editTextEntryDate.getText().toString().trim();
        String deliveryDeadline = editTextDeliveryDeadline.getText().toString().trim();
        String observations = editTextObservations.getText().toString().trim();

        // Map the selected translated status back to the original English value
        String selectedStatus = spinnerStatus.getSelectedItem().toString();
        String status = selectedStatus; // default to the selected value

        if (selectedStatus.equals(getString(R.string.pending))) {
            status = "Pending";
        } else if (selectedStatus.equals(getString(R.string.in_production))) {
            status = "In Production";
        } else if (selectedStatus.equals(getString(R.string.completed))) {
            status = "Completed";
        }

        if (name.isEmpty()) {
            editTextName.setError(getString(R.string.name) + " é obrigatório");
            editTextName.requestFocus();
            return;
        }

        if (entryDate.isEmpty()) {
            editTextEntryDate.setError(getString(R.string.entry_date) + " é obrigatória");
            editTextEntryDate.requestFocus();
            return;
        }

        if (deliveryDeadline.isEmpty()) {
            editTextDeliveryDeadline.setError(getString(R.string.delivery_deadline) + " é obrigatório");
            editTextDeliveryDeadline.requestFocus();
            return;
        }

        Piece piece;
        if (pieceId != -1) {
            // Update existing piece
            piece = new Piece(pieceId, name, description, entryDate, deliveryDeadline, observations, status, collectionId);
            long result = dbHelper.updatePiece(piece);
            if (result > 0) {
                Toast.makeText(this, "Peça atualizada com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao atualizar peça", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            // Create new piece
            piece = new Piece(name, description, entryDate, deliveryDeadline, observations, status, collectionId);
            long result = dbHelper.insertPiece(piece);
            if (result != -1) {
                Toast.makeText(this, "Peça salva com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao salvar peça", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        finish();
    }
}