package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NotesAdapter extends BaseAdapter {
    // Khai báo biến toàn cục
    private Context context;
    private int layout;
    private List<NotesModel> noteList;

    // Constructor
    public NotesAdapter(Context context, int layout, List<NotesModel> noteList) {
        this.context = context;
        this.layout = layout;
        this.noteList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position); // Trả về đối tượng NotesModel tại vị trí position
    }

    @Override
    public long getItemId(int position) {
        return position; // Nếu NotesModel có ID thực tế, dùng: return noteList.get(position).getId();
    }

    // Tạo ViewHolder để tối ưu ListView
    private class ViewHolder {
        TextView textViewNote;
        ImageView imageViewEdit;
        ImageView imageViewDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder.textViewNote = convertView.findViewById(R.id.textViewNameNote);
            viewHolder.imageViewDelete = convertView.findViewById(R.id.imageViewDelete);
            viewHolder.imageViewEdit = convertView.findViewById(R.id.imageViewEdit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Lấy dữ liệu và hiển thị
        NotesModel notes = noteList.get(position);
        viewHolder.textViewNote.setText(notes.getNameNote());

        // Xử lý khi nhấn nút "Sửa"
        viewHolder.imageViewEdit.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).DialogCapNhat(notes.getIdNote(), notes.getNameNote());
            }
        });

        // Xử lý khi nhấn nút "Xóa"
        viewHolder.imageViewDelete.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).DialogXoa(notes.getIdNote());
            }
        });

        return convertView;
    }
}
