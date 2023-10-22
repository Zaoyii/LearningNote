package com.zcyi.learningnote.Pager.HomePager.NotePager;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zcyi.learningnote.Adapter.NoteAdapter;
import com.zcyi.learningnote.Dao.NoteDao;
import com.zcyi.learningnote.Entity.EntityNote;
import com.zcyi.learningnote.Entity.EntityNoteCard;
import com.zcyi.learningnote.InitDataBase.InitDataBase;
import com.zcyi.learningnote.R;
import com.zcyi.learningnote.Util.UtilMethod;
import com.zcyi.learningnote.databinding.FragmentNoteBinding;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment {
    FragmentNoteBinding binding;
    InitDataBase initDataBase;
    NoteDao noteDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(getLayoutInflater());
        initMethod();
        initList();
        binding.floatingActionButton.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), AddOrEditNoteActivity.class));
        });
        return binding.getRoot();
    }

    private void initList() {
        List<EntityNote> localNote = getLocalNote();
        if (localNote != null && localNote.size() > 0) {
            ArrayList<EntityNoteCard> list = noteToCard(localNote);
            binding.noNote.setVisibility(View.GONE);
            binding.noteAlert.setVisibility(View.VISIBLE);
            NoteAdapter noteAdapter = new NoteAdapter(list, getContext(), count -> {
                if (count == 0) {
                    binding.noNote.setVisibility(View.VISIBLE);
                }
                binding.noteAlert.setText(getString(R.string.note_alter, count + ""));
            });
            binding.noteList.setAdapter(noteAdapter);
            binding.noteList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            binding.noteAlert.setText(getString(R.string.note_alter, list.size() + ""));
        } else {
            binding.noNote.setVisibility(View.VISIBLE);
            binding.noteAlert.setVisibility(View.GONE);
        }
    }

    private ArrayList<EntityNoteCard> noteToCard(List<EntityNote> list) {
        ArrayList<EntityNoteCard> cards = new ArrayList<>();
        for (EntityNote note : list
        ) {
            EntityNoteCard entityNoteCard = new EntityNoteCard();
            entityNoteCard.setNoteCardId(note.getNoteId());
            entityNoteCard.setContent(note.getNoteContent());
            entityNoteCard.setTitle(note.getNoteTitle());
            entityNoteCard.setCreateTime(note.getCreateTime());
            entityNoteCard.setCover(note.getNoteImageUrl());
            entityNoteCard.setUsername(getString(R.string.item_username));
            entityNoteCard.setSlogan(getString(R.string.item_slogan));
            cards.add(entityNoteCard);
        }
        return cards;
    }

    private void initMethod() {
        initDataBase = UtilMethod.getInstance(getContext());
        noteDao = initDataBase.noteDao();
        System.out.println("fragment initMethod   is  running_________");
    }

    private List<EntityNote> getLocalNote() {
        List<EntityNote> allNote = noteDao.getAllNote();
        if (allNote.size() > 0) {
            return allNote;
        } else {
            return null;
        }

    }

    @Override
    public void onResume() {
        initList();
        super.onResume();
    }
}