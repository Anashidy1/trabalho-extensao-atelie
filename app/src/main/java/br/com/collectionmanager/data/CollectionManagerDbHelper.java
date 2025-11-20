package br.com.collectionmanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.collectionmanager.model.Collection;
import br.com.collectionmanager.model.Piece;

public class CollectionManagerDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "CollectionManagerDbHelper";
    
    // Database information
    private static final String DATABASE_NAME = "CollectionManager.db";
    private static final int DATABASE_VERSION = 1;
    
    // SQL statements to create tables
    private static final String SQL_CREATE_COLLECTIONS_TABLE = 
        "CREATE TABLE " + CollectionManagerContract.CollectionEntry.TABLE_NAME + " (" +
        CollectionManagerContract.CollectionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        CollectionManagerContract.CollectionEntry.COLUMN_NAME + " TEXT NOT NULL, " +
        CollectionManagerContract.CollectionEntry.COLUMN_DESCRIPTION + " TEXT, " +
        CollectionManagerContract.CollectionEntry.COLUMN_START_DATE + " TEXT, " +
        CollectionManagerContract.CollectionEntry.COLUMN_RELEASE_DATE + " TEXT, " +
        CollectionManagerContract.CollectionEntry.COLUMN_CREATED_AT + " TEXT NOT NULL" +
        ");";
    
    private static final String SQL_CREATE_PIECES_TABLE =
        "CREATE TABLE " + CollectionManagerContract.PieceEntry.TABLE_NAME + " (" +
        CollectionManagerContract.PieceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        CollectionManagerContract.PieceEntry.COLUMN_NAME + " TEXT NOT NULL, " +
        CollectionManagerContract.PieceEntry.COLUMN_DESCRIPTION + " TEXT, " +
        CollectionManagerContract.PieceEntry.COLUMN_ENTRY_DATE + " TEXT, " +
        CollectionManagerContract.PieceEntry.COLUMN_DELIVERY_DEADLINE + " TEXT, " +
        CollectionManagerContract.PieceEntry.COLUMN_OBSERVATIONS + " TEXT, " +
        CollectionManagerContract.PieceEntry.COLUMN_STATUS + " TEXT NOT NULL, " +
        CollectionManagerContract.PieceEntry.COLUMN_COLLECTION_ID + " INTEGER NOT NULL, " +
        "FOREIGN KEY(" + CollectionManagerContract.PieceEntry.COLUMN_COLLECTION_ID + ") " +
        "REFERENCES " + CollectionManagerContract.CollectionEntry.TABLE_NAME + "(" +
        CollectionManagerContract.CollectionEntry._ID + ") ON DELETE CASCADE" +
        ");";
    
    public CollectionManagerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating database tables");
        db.execSQL(SQL_CREATE_COLLECTIONS_TABLE);
        db.execSQL(SQL_CREATE_PIECES_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now, we'll simply drop and recreate the tables
        db.execSQL("DROP TABLE IF EXISTS " + CollectionManagerContract.PieceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CollectionManagerContract.CollectionEntry.TABLE_NAME);
        onCreate(db);
    }
    
    // Collections operations
    public long insertCollection(Collection collection) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CollectionManagerContract.CollectionEntry.COLUMN_NAME, collection.getName());
        values.put(CollectionManagerContract.CollectionEntry.COLUMN_DESCRIPTION, collection.getDescription());
        values.put(CollectionManagerContract.CollectionEntry.COLUMN_START_DATE, collection.getStartDate());
        values.put(CollectionManagerContract.CollectionEntry.COLUMN_RELEASE_DATE, collection.getEstimatedRelease());
        values.put(CollectionManagerContract.CollectionEntry.COLUMN_CREATED_AT, collection.getCreatedAt());
        
        long id = db.insert(CollectionManagerContract.CollectionEntry.TABLE_NAME, null, values);
        db.close();
        Log.d(TAG, "Inserted collection with ID: " + id);
        return id;
    }
    
    public List<Collection> getAllCollections() {
        List<Collection> collections = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] projection = {
            CollectionManagerContract.CollectionEntry._ID,
            CollectionManagerContract.CollectionEntry.COLUMN_NAME,
            CollectionManagerContract.CollectionEntry.COLUMN_DESCRIPTION,
            CollectionManagerContract.CollectionEntry.COLUMN_START_DATE,
            CollectionManagerContract.CollectionEntry.COLUMN_RELEASE_DATE,
            CollectionManagerContract.CollectionEntry.COLUMN_CREATED_AT
        };
        
        Cursor cursor = db.query(
            CollectionManagerContract.CollectionEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            CollectionManagerContract.CollectionEntry.COLUMN_CREATED_AT + " DESC" // Sort by creation date, newest first
        );
        
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(CollectionManagerContract.CollectionEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.CollectionEntry.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.CollectionEntry.COLUMN_DESCRIPTION));
            String startDate = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.CollectionEntry.COLUMN_START_DATE));
            String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.CollectionEntry.COLUMN_RELEASE_DATE));
            String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.CollectionEntry.COLUMN_CREATED_AT));
            
            Collection collection = new Collection(id, name, description, startDate, releaseDate, createdAt);
            collections.add(collection);
        }
        cursor.close();
        db.close();
        
        return collections;
    }
    
    public Collection getCollection(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] projection = {
            CollectionManagerContract.CollectionEntry._ID,
            CollectionManagerContract.CollectionEntry.COLUMN_NAME,
            CollectionManagerContract.CollectionEntry.COLUMN_DESCRIPTION,
            CollectionManagerContract.CollectionEntry.COLUMN_START_DATE,
            CollectionManagerContract.CollectionEntry.COLUMN_RELEASE_DATE,
            CollectionManagerContract.CollectionEntry.COLUMN_CREATED_AT
        };
        
        String selection = CollectionManagerContract.CollectionEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        
        Cursor cursor = db.query(
            CollectionManagerContract.CollectionEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        );
        
        Collection collection = null;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.CollectionEntry.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.CollectionEntry.COLUMN_DESCRIPTION));
            String startDate = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.CollectionEntry.COLUMN_START_DATE));
            String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.CollectionEntry.COLUMN_RELEASE_DATE));
            String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.CollectionEntry.COLUMN_CREATED_AT));
            
            collection = new Collection(id, name, description, startDate, releaseDate, createdAt);
        }
        cursor.close();
        db.close();
        
        return collection;
    }
    
    public int updateCollection(Collection collection) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CollectionManagerContract.CollectionEntry.COLUMN_NAME, collection.getName());
        values.put(CollectionManagerContract.CollectionEntry.COLUMN_DESCRIPTION, collection.getDescription());
        values.put(CollectionManagerContract.CollectionEntry.COLUMN_START_DATE, collection.getStartDate());
        values.put(CollectionManagerContract.CollectionEntry.COLUMN_RELEASE_DATE, collection.getEstimatedRelease());
        
        String selection = CollectionManagerContract.CollectionEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(collection.getId()) };
        
        int count = db.update(
            CollectionManagerContract.CollectionEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        );
        db.close();
        Log.d(TAG, "Updated collection with ID: " + collection.getId() + ", rows affected: " + count);
        return count;
    }
    
    public int deleteCollection(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // First, delete all pieces associated with this collection due to foreign key constraint
        // The ON DELETE CASCADE in the schema definition will handle this automatically
        
        String selection = CollectionManagerContract.CollectionEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        
        int count = db.delete(
            CollectionManagerContract.CollectionEntry.TABLE_NAME,
            selection,
            selectionArgs
        );
        db.close();
        Log.d(TAG, "Deleted collection with ID: " + id + ", rows affected: " + count);
        return count;
    }
    
    // Pieces operations
    public long insertPiece(Piece piece) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CollectionManagerContract.PieceEntry.COLUMN_NAME, piece.getName());
        values.put(CollectionManagerContract.PieceEntry.COLUMN_DESCRIPTION, piece.getDescription());
        values.put(CollectionManagerContract.PieceEntry.COLUMN_ENTRY_DATE, piece.getEntryDate());
        values.put(CollectionManagerContract.PieceEntry.COLUMN_DELIVERY_DEADLINE, piece.getDeliveryDeadline());
        values.put(CollectionManagerContract.PieceEntry.COLUMN_OBSERVATIONS, piece.getObservations());
        values.put(CollectionManagerContract.PieceEntry.COLUMN_STATUS, piece.getStatus());
        values.put(CollectionManagerContract.PieceEntry.COLUMN_COLLECTION_ID, piece.getCollectionId());
        
        long id = db.insert(CollectionManagerContract.PieceEntry.TABLE_NAME, null, values);
        db.close();
        Log.d(TAG, "Inserted piece with ID: " + id);
        return id;
    }
    
    public List<Piece> getAllPieces() {
        List<Piece> pieces = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] projection = {
            CollectionManagerContract.PieceEntry._ID,
            CollectionManagerContract.PieceEntry.COLUMN_NAME,
            CollectionManagerContract.PieceEntry.COLUMN_DESCRIPTION,
            CollectionManagerContract.PieceEntry.COLUMN_ENTRY_DATE,
            CollectionManagerContract.PieceEntry.COLUMN_DELIVERY_DEADLINE,
            CollectionManagerContract.PieceEntry.COLUMN_OBSERVATIONS,
            CollectionManagerContract.PieceEntry.COLUMN_STATUS,
            CollectionManagerContract.PieceEntry.COLUMN_COLLECTION_ID
        };
        
        Cursor cursor = db.query(
            CollectionManagerContract.PieceEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            CollectionManagerContract.PieceEntry.COLUMN_ENTRY_DATE + " DESC" // Sort by entry date
        );
        
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_DESCRIPTION));
            String entryDate = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_ENTRY_DATE));
            String deliveryDeadline = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_DELIVERY_DEADLINE));
            String observations = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_OBSERVATIONS));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_STATUS));
            long collectionId = cursor.getLong(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_COLLECTION_ID));
            
            Piece piece = new Piece(id, name, description, entryDate, deliveryDeadline, observations, status, collectionId);
            pieces.add(piece);
        }
        cursor.close();
        db.close();
        
        return pieces;
    }
    
    public List<Piece> getPiecesByCollection(long collectionId) {
        List<Piece> pieces = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] projection = {
            CollectionManagerContract.PieceEntry._ID,
            CollectionManagerContract.PieceEntry.COLUMN_NAME,
            CollectionManagerContract.PieceEntry.COLUMN_DESCRIPTION,
            CollectionManagerContract.PieceEntry.COLUMN_ENTRY_DATE,
            CollectionManagerContract.PieceEntry.COLUMN_DELIVERY_DEADLINE,
            CollectionManagerContract.PieceEntry.COLUMN_OBSERVATIONS,
            CollectionManagerContract.PieceEntry.COLUMN_STATUS,
            CollectionManagerContract.PieceEntry.COLUMN_COLLECTION_ID
        };
        
        String selection = CollectionManagerContract.PieceEntry.COLUMN_COLLECTION_ID + " = ?";
        String[] selectionArgs = { String.valueOf(collectionId) };
        
        Cursor cursor = db.query(
            CollectionManagerContract.PieceEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            CollectionManagerContract.PieceEntry.COLUMN_ENTRY_DATE + " DESC"
        );
        
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_DESCRIPTION));
            String entryDate = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_ENTRY_DATE));
            String deliveryDeadline = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_DELIVERY_DEADLINE));
            String observations = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_OBSERVATIONS));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_STATUS));
            
            Piece piece = new Piece(id, name, description, entryDate, deliveryDeadline, observations, status, collectionId);
            pieces.add(piece);
        }
        cursor.close();
        db.close();
        
        return pieces;
    }
    
    public List<Piece> getPiecesByStatus(String status) {
        List<Piece> pieces = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] projection = {
            CollectionManagerContract.PieceEntry._ID,
            CollectionManagerContract.PieceEntry.COLUMN_NAME,
            CollectionManagerContract.PieceEntry.COLUMN_DESCRIPTION,
            CollectionManagerContract.PieceEntry.COLUMN_ENTRY_DATE,
            CollectionManagerContract.PieceEntry.COLUMN_DELIVERY_DEADLINE,
            CollectionManagerContract.PieceEntry.COLUMN_OBSERVATIONS,
            CollectionManagerContract.PieceEntry.COLUMN_STATUS,
            CollectionManagerContract.PieceEntry.COLUMN_COLLECTION_ID
        };
        
        String selection = CollectionManagerContract.PieceEntry.COLUMN_STATUS + " = ?";
        String[] selectionArgs = { status };
        
        Cursor cursor = db.query(
            CollectionManagerContract.PieceEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        );
        
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_DESCRIPTION));
            String entryDate = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_ENTRY_DATE));
            String deliveryDeadline = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_DELIVERY_DEADLINE));
            String observations = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_OBSERVATIONS));
            long collectionId = cursor.getLong(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_COLLECTION_ID));
            
            Piece piece = new Piece(id, name, description, entryDate, deliveryDeadline, observations, status, collectionId);
            pieces.add(piece);
        }
        cursor.close();
        db.close();
        
        return pieces;
    }
    
    public Piece getPiece(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] projection = {
            CollectionManagerContract.PieceEntry._ID,
            CollectionManagerContract.PieceEntry.COLUMN_NAME,
            CollectionManagerContract.PieceEntry.COLUMN_DESCRIPTION,
            CollectionManagerContract.PieceEntry.COLUMN_ENTRY_DATE,
            CollectionManagerContract.PieceEntry.COLUMN_DELIVERY_DEADLINE,
            CollectionManagerContract.PieceEntry.COLUMN_OBSERVATIONS,
            CollectionManagerContract.PieceEntry.COLUMN_STATUS,
            CollectionManagerContract.PieceEntry.COLUMN_COLLECTION_ID
        };
        
        String selection = CollectionManagerContract.PieceEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        
        Cursor cursor = db.query(
            CollectionManagerContract.PieceEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        );
        
        Piece piece = null;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_DESCRIPTION));
            String entryDate = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_ENTRY_DATE));
            String deliveryDeadline = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_DELIVERY_DEADLINE));
            String observations = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_OBSERVATIONS));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_STATUS));
            long collectionId = cursor.getLong(cursor.getColumnIndexOrThrow(CollectionManagerContract.PieceEntry.COLUMN_COLLECTION_ID));
            
            piece = new Piece(id, name, description, entryDate, deliveryDeadline, observations, status, collectionId);
        }
        cursor.close();
        db.close();
        
        return piece;
    }
    
    public int updatePiece(Piece piece) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CollectionManagerContract.PieceEntry.COLUMN_NAME, piece.getName());
        values.put(CollectionManagerContract.PieceEntry.COLUMN_DESCRIPTION, piece.getDescription());
        values.put(CollectionManagerContract.PieceEntry.COLUMN_ENTRY_DATE, piece.getEntryDate());
        values.put(CollectionManagerContract.PieceEntry.COLUMN_DELIVERY_DEADLINE, piece.getDeliveryDeadline());
        values.put(CollectionManagerContract.PieceEntry.COLUMN_OBSERVATIONS, piece.getObservations());
        values.put(CollectionManagerContract.PieceEntry.COLUMN_STATUS, piece.getStatus());
        values.put(CollectionManagerContract.PieceEntry.COLUMN_COLLECTION_ID, piece.getCollectionId());
        
        String selection = CollectionManagerContract.PieceEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(piece.getId()) };
        
        int count = db.update(
            CollectionManagerContract.PieceEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        );
        db.close();
        Log.d(TAG, "Updated piece with ID: " + piece.getId() + ", rows affected: " + count);
        return count;
    }
    
    public int deletePiece(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        String selection = CollectionManagerContract.PieceEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        
        int count = db.delete(
            CollectionManagerContract.PieceEntry.TABLE_NAME,
            selection,
            selectionArgs
        );
        db.close();
        Log.d(TAG, "Deleted piece with ID: " + id + ", rows affected: " + count);
        return count;
    }
}