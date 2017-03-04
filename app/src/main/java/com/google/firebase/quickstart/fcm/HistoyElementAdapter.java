package com.google.firebase.quickstart.fcm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 2017-03-04.
 */

public class HistoyElementAdapter extends RecyclerView.Adapter<HistoyElementAdapter.EndpointEventViewHolder> {
    List<HistoyElement> HistoyElementList;
    private Context context;

    public class EndpointEventViewHolder extends RecyclerView.ViewHolder {
        public TextView ReceivedText;
        public TextView SendedText;
        public TextView Datetime;

        public EndpointEventViewHolder(View view) {
            super(view);
            ReceivedText = (TextView) view.findViewById(R.id.received_text);
            SendedText = (TextView) view.findViewById(R.id.sended_text);
            Datetime = (TextView) view.findViewById(R.id.datetime);
        }
    }

    public HistoyElementAdapter(List<HistoyElement> eeList, Context ctx) {
        HistoyElementList = eeList;
        context = ctx;
    }

    @Override
    public EndpointEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_element, parent, false);

        return new EndpointEventViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(EndpointEventViewHolder holder, int position) {
        HistoyElement histoyElement = HistoyElementList.get(position);
        holder.Datetime.setText(histoyElement.Datetime);
        holder.ReceivedText.setText(histoyElement.Received);
        holder.SendedText.setText(histoyElement.Sended);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked " , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return HistoyElementList.size();
    }


    public static class HistoyElement {

        public HistoyElement(int _id, String _received, String _sended, String _datetime) {
            Received = _received;
            Sended = _sended;
            Datetime = _datetime;
            id = _id;
        }

        int id;
        int getId(){
            return id;
        }
        String Received;
        String Sended;
        String Datetime;

    }
}