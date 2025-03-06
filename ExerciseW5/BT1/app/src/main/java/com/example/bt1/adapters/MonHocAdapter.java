package com.example.bt1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bt1.R;
import com.example.bt1.model.MonHoc;

import java.util.List;

public class MonHocAdapter extends BaseAdapter {
    //khai báo
    private Context context;
    private int layout;
    private List<MonHoc> monHocList;

    public MonHocAdapter(Context context, int layout, List<MonHoc> monHocList) {
        this.context = context;
        this.layout = layout;
        this.monHocList = monHocList;
    }

    @Override
    public int getCount() {
        return monHocList.size();
    }

    @Override
    public Object getItem(int i) {
        return monHocList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // initialize viewholder
        ViewHolder viewHolder;
        if (view == null) {       // tao View cho layout
            //lấy view
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //gọi view chứa layout
            view = inflater.inflate(layout, null);

            //ánh xạ view
            viewHolder = new ViewHolder();
            viewHolder.tvTenMH = (TextView) view.findViewById(R.id.txtTenMH);
            viewHolder.tvDesc = (TextView) view.findViewById(R.id.txtDesc);
            viewHolder.imgMonHoc = (ImageView) view.findViewById(R.id.imgMonHoc);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        //gán giá trị
        MonHoc monHoc = monHocList.get(i);
        viewHolder.tvTenMH.setText(monHoc.getName());
        viewHolder.tvDesc.setText(monHoc.getDecs());
        viewHolder.imgMonHoc.setImageResource(monHoc.getPic());

        //trả về view
        return view;
    }
    private class ViewHolder{
        TextView tvTenMH,tvDesc;
        ImageView imgMonHoc;

    }
}

