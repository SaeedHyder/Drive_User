package com.ingic.driveuser.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.CancelReasonEnt;

import java.util.ArrayList;

/**
 * Created on 7/1/2017.
 */

public class ReasonCancelListViewAdapter extends BaseAdapter {
    String item;
    private Context context;
    private ArrayList<CancelReasonEnt> arrayList;
    private LayoutInflater inflater;
    private boolean isListView;
    private int selectedPosition = -1;


    public ReasonCancelListViewAdapter(Context context, ArrayList<CancelReasonEnt> arrayList, boolean isListView) {
        this.context = context;
        this.arrayList = arrayList;
        this.isListView = isListView;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(R.layout.row_item_reason_cancel, viewGroup, false);

            viewHolder.label = (TextView) view.findViewById(R.id.label);
            viewHolder.radioButton = (RadioButton) view.findViewById(R.id.radio_button);

            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();

        viewHolder.label.setText(arrayList.get(i).getTitle());
        if (selectedPosition == -1 && i == 0) {
            selectedPosition = 0;
            viewHolder.radioButton.setChecked(true);

        } else {
            viewHolder.radioButton.setChecked(i == selectedPosition);
        }
        //check the radio button if both position and selectedPosition matches

        //Set the position tag to both radio button and label
        viewHolder.radioButton.setTag(i);
        viewHolder.label.setTag(i);

        viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
            }
        });

        viewHolder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
            }


        });
        return view;
    }

    //On selecting any view set the current position to selectedPositon and notify adapter
    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
        notifyDataSetChanged();
    }

   /* @Override
    public void setData(int position, String Item, AnyTextView txtTo) {


      //  TrainingBookingCalenderItem Updateditem = (TrainingBookingCalenderItem) adapter.getItem(position);

       // txtTo.setText(item);
       //  Updateditem.setTxtTo(Item);

        //  Updateditem.setTxtFrom(Item);
       // adapter.add(Updateditem);
    }*/

    //Return the selectedPosition item
    public String getSelectedItem() {
        if (selectedPosition != -1) {
            // Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
            item = arrayList.get(selectedPosition).getId()+"";
            return item;
        }

        return "";
    }

    //Delete the selected position from the arrayList
    public void deleteSelectedPosition() {
        if (selectedPosition != -1) {
            arrayList.remove(selectedPosition);
            selectedPosition = -1;//after removing selectedPosition set it back to -1
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        private TextView label;
        private RadioButton radioButton;
    }
}
