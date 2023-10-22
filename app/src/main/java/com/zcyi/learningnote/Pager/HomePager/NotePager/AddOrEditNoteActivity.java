package com.zcyi.learningnote.Pager.HomePager.NotePager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.zcyi.learningnote.Dao.NoteDao;
import com.zcyi.learningnote.Entity.EntityNote;
import com.zcyi.learningnote.InitDataBase.InitDataBase;
import com.zcyi.learningnote.R;
import com.zcyi.learningnote.Util.UtilMethod;
import com.zcyi.learningnote.databinding.ActivityAddOrEditNoteBinding;

import java.util.Date;

public class AddOrEditNoteActivity extends AppCompatActivity {
    ActivityAddOrEditNoteBinding binding;
    InitDataBase initDataBase;
    NoteDao noteDao;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String createTime;
    boolean isAdd = true;
    EntityNote note;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddOrEditNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initMethod();
        Intent intent = getIntent();
        if (!(intent != null && intent.getBooleanExtra("isAdd", true))) {
            isAdd = false;
            long noteId = intent.getLongExtra("noteId", -1);
            note = noteDao.getNoteById(noteId);
            binding.noteTitle.setText(note.getNoteTitle().length() > 0 ? note.getNoteTitle() : "");
            binding.noteContent.setText(note.getNoteContent());
            if (note.getNoteImageUrl() != null && !note.getNoteImageUrl().isEmpty()) {
                imageUri = note.getNoteImageUrl();
                Glide.with(getApplicationContext()).load(note.getNoteImageUrl()).into(binding.noteImage);
            }
        }
        binding.closeButton.setOnClickListener(view -> finish());
        binding.saveButton.setOnClickListener(view -> saveNote());
        binding.noteContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.wordsCount.setText(getString(R.string.note_length, editable.toString().trim().length()));
            }
        });
        binding.checkImage.setOnClickListener(view -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
        binding.wordsCount.setText(getString(R.string.note_length, binding.noteContent.getText().toString().length()));
    }

    private void initMethod() {
        createTime = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        binding.createTime.setText(createTime);
        initDataBase = UtilMethod.getInstance(getApplicationContext());
        noteDao = initDataBase.noteDao();
        binding.wordsCount.setText(getString(R.string.note_length, 0));
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        imageUri = UtilMethod.getPath(getApplicationContext(), uri);
                        Glide.with(getApplicationContext()).load(uri).into(binding.noteImage);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
    }

    private void saveNote() {
        if (isAdd) {
            if (binding.noteContent.getText().toString().trim().isEmpty()) {
                UtilMethod.showToast(getApplicationContext(), "Content is empty~");
            } else {
                getCurrentNote();
                noteDao.insertNote(getCurrentNote());
                UtilMethod.showToast(getApplicationContext(), "Save note success!");
                finish();
            }

        } else {
            if (binding.noteContent.getText().toString().trim().isEmpty()) {
                UtilMethod.showToast(getApplicationContext(), "Content is empty~");
            } else {
                String content = binding.noteContent.getText().toString().trim();
                note.setNoteContent(content);
                note.setNoteTitle(binding.noteTitle.getText().toString().trim().length() > 0 ? binding.noteTitle.getText().toString().trim() : "");
                note.setNoteImageUrl(imageUri == null ? null : imageUri);
                noteDao.updateNote(note);
                UtilMethod.showToast(getApplicationContext(), "Save note success!");
                finish();
            }
        }
    }

    private EntityNote getCurrentNote() {
        String content = binding.noteContent.getText().toString().trim();
        String title = binding.noteTitle.getText().toString().trim();
        return new EntityNote(6, title, content, imageUri == null ? null : imageUri, createTime);

    }

}