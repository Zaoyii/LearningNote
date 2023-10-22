package com.zcyi.learningnote.InitDataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.zcyi.learningnote.Dao.NoteDao;
import com.zcyi.learningnote.Entity.EntityNote;

@Database(entities = {EntityNote.class}, version = 1, exportSchema = false)
public abstract class InitDataBase extends RoomDatabase {
    public abstract NoteDao noteDao();

}
