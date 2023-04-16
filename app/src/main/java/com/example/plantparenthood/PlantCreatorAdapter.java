package com.example.plantparenthood;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;

public class PlantCreatorAdapter extends RecyclerView.Adapter {
    private ArrayList<Plant> plantsList;
    private Context openActivity;

    public PlantCreatorAdapter(ArrayList<Plant> newPlantsList, Context newContext) {
        plantsList = newPlantsList;
        openActivity = newContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.plantsquare, parent, false);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(currentView) {
        };
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Plant thisPlant = plantsList.get(position);
        TextView plantCommonName = (TextView) holder.itemView.findViewById(R.id.plantCommonName);
        ImageView plantImage = (ImageView) holder.itemView.findViewById(R.id.plantImage);
        TextView plantOtherNames = (TextView) holder.itemView.findViewById(R.id.plantScientificNames);

        plantCommonName.setText(thisPlant.common_name);
        plantImage.setImageBitmap(thisPlant.default_image);
        plantOtherNames.setText(thisPlant.scientific_name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupPopup(view, thisPlant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plantsList.size();
    }

    private void setupPopup(View view, Plant thisPlant) {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.activity_plant_popup, null);

        PopupWindow newPopupWindow = new PopupWindow(newPopup, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        newPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView plantCommonName = newPopup.findViewById(R.id.plantCommonName);
        plantCommonName.setText(thisPlant.common_name);

        TextView plantScientificName = newPopup.findViewById(R.id.plantScientificName);
        plantScientificName.setText(thisPlant.scientific_name);

        ImageView plantImage = newPopup.findViewById(R.id.plantImage);
        plantImage.setImageBitmap(thisPlant.default_image);

        Button closeButton = newPopup.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPopupWindow.dismiss();
            }
        });

        Button addPlant = newPopup.findViewById(R.id.addPlant);
        addPlant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        PlantCreator.addPlantToDatabase(thisPlant);
                        // System.out.println(thisPlant.common_name + " Added to save data");
                    }
                });
                Toast.makeText(view.getContext(), "Plant successfully added", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
