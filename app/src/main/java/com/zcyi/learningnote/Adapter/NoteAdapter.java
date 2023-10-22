package com.zcyi.learningnote.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.zcyi.learningnote.Dao.NoteDao;
import com.zcyi.learningnote.Entity.EntityNoteCard;
import com.zcyi.learningnote.InitDataBase.InitDataBase;
import com.zcyi.learningnote.Pager.HomePager.NotePager.AddOrEditNoteActivity;
import com.zcyi.learningnote.R;
import com.zcyi.learningnote.Util.UtilMethod;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    ArrayList<EntityNoteCard> list;
    Context context;
    int deletePosition;
    InitDataBase initDataBase;
    NoteDao noteDao;
    CountListen countListen;

    public NoteAdapter(ArrayList<EntityNoteCard> list, Context context, CountListen countListen) {
        this.list = list;
        this.context = context;
        this.countListen = countListen;
        initDataBase = UtilMethod.getInstance(context);
        noteDao = initDataBase.noteDao();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        if (position == holder.getLayoutPosition()) {
            holder.username.setText(list.get(position).getUsername());
            holder.slogan.setText(list.get(position).getSlogan());
            holder.createTime.setText(list.get(position).getCreateTime());
            if (list.get(position) != null && !list.get(position).getTitle().isEmpty()) {
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText(list.get(position).getTitle());
                holder.content.setTextColor(Color.GRAY);
                holder.content.setTextSize(16);
            } else {
                holder.title.setVisibility(View.GONE);
                holder.content.setTextColor(Color.BLACK);
                holder.content.setTextSize(24);
            }
            holder.content.setText(list.get(position).getContent());
            holder.userAvatar.setShapeAppearanceModel(ShapeAppearanceModel.builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build());
            if (list.get(position).getCover() == null) {
                holder.cover.setVisibility(View.GONE);
            } else {
                holder.cover.setVisibility(View.VISIBLE);
                Glide.with(context).load(list.get(position).getCover()).into(holder.cover);
            }
            Glide.with(context).load("https://pic4.zhimg.com/v2-8bf31da17408b3f012f4ec06c2a3eacf_r.jpg").into(holder.userAvatar);
            holder.delete.setOnClickListener(view -> {
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Are you sure you want delete this note?")
                        .setPositiveButton("yeah", (dialogInterface, i) -> {
                            deletePosition = holder.getLayoutPosition();
                            deleteNote();
                        }).setNegativeButton("no", null).show();
            });
            holder.itemCard.setOnClickListener(view -> {
                Intent intent = new Intent(context, AddOrEditNoteActivity.class);
                intent.putExtra("isAdd", false);
                intent.putExtra("noteId", list.get(position).getNoteCardId());
                context.startActivity(intent);
            });
        }
    }

    private void deleteNote() {
        noteDao.deleteNoteById(list.get(deletePosition).getNoteCardId());
        notifyItemRemoved(deletePosition);
        list.remove(deletePosition);
        countListen.countListen(list.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView itemCard;
        ShapeableImageView userAvatar;
        TextView username;
        TextView slogan;
        TextView createTime;
        ImageView cover;
        TextView title;
        TextView content;
        Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCard = itemView.findViewById(R.id.item_card);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            username = itemView.findViewById(R.id.username);
            slogan = itemView.findViewById(R.id.slogan);
            createTime = itemView.findViewById(R.id.create_time);
            cover = itemView.findViewById(R.id.cover);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            delete = itemView.findViewById(R.id.delete);
        }

    }

    public interface CountListen {
        void countListen(int count);
    }

}
