package com.example.gproqmsnhanxe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class KHListViewAdapter extends BaseAdapter {
    public ArrayList<KhachHang> KhachHangs;
    private Context context;
    public KHListViewAdapter(Context _context, ArrayList<KhachHang> _khachhangs) {
        this.context = _context;
        this.KhachHangs = _khachhangs;
    }

    @Override
    public int getCount() {
        return KhachHangs.size();
    }

    @Override
    public Object getItem(int position) {
        return KhachHangs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        final KHListViewHolder listViewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           // row = layoutInflater.inflate(R.layout.activity_k_h_list_view, parent, false);
            row = layoutInflater.inflate(R.layout.activity_custom_list_view, parent, false);
            listViewHolder = new KHListViewHolder();
           listViewHolder.lbten = row.findViewById(R.id.lbten);
           listViewHolder.lbbso = row.findViewById(R.id.lbbso);
          listViewHolder.lbsolan = row.findViewById(R.id.lbsolan);
            listViewHolder.lbma = row.findViewById(R.id.lbma);
            row.setTag(listViewHolder);
        } else {
            row = convertView;
            listViewHolder = (KHListViewHolder) row.getTag();
        }
        final KhachHang obj = (KhachHang) getItem(position);
      listViewHolder.lbma.setText(obj.getCode() + " - " + obj.getPhone());
        listViewHolder.lbbso.setText(obj.getLicensePlate());
       listViewHolder.lbten.setText(obj.getName());
         listViewHolder.lbsolan.setText( (obj.getTimes()+""));
        return row;
    }
}
