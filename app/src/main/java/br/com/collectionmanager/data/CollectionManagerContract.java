package br.com.collectionmanager.data;

import android.provider.BaseColumns;

public class CollectionManagerContract {
    
    // Prevent instantiation
    private CollectionManagerContract() {}
    
    // Inner class for collections table
    public static class CollectionEntry implements BaseColumns {
        public static final String TABLE_NAME = "collections";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_CREATED_AT = "created_at";
    }
    
    // Inner class for pieces table
    public static class PieceEntry implements BaseColumns {
        public static final String TABLE_NAME = "pieces";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_ENTRY_DATE = "entry_date";
        public static final String COLUMN_DELIVERY_DEADLINE = "delivery_deadline";
        public static final String COLUMN_OBSERVATIONS = "observations";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_COLLECTION_ID = "collection_id";
    }
}