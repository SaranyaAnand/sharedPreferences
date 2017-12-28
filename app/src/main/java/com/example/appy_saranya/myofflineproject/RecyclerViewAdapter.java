package com.example.appy_saranya.myofflineproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by appy-saranya on 12/28/2017.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<String> listNames;
    public RecyclerViewAdapter(ArrayList<String> list) {
        listNames=list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View layout;
        public ImageView ivPic;
        public TextView txtHeader;
        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            ivPic=v.findViewById(R.id.icon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {

        final String name = listNames.get(position);
        holder.txtHeader.setText(name);
        Bitmap bitmap=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myImages/"+name);
        holder.ivPic.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return listNames.size();
    }
}
