package com.example.plantparenthood;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlantCreator
{
    public DataAccessObject database;

    PlantCreator(DataAccessObject newDatabase)
    {
        database = newDatabase;
    }

    public void addPlant(JSONObject nonparsedPlants, PlantSearcher makethisplantUI) throws JSONException
    {
        ArrayList<Plant> createdPlantObjects = new ArrayList<>();
        JSONArray plantsList = nonparsedPlants.getJSONArray("data");

        for (int i = 0; i < plantsList.length(); i++)
        {
            JSONObject currentPlant = plantsList.getJSONObject(i);
            int id = currentPlant.getInt("id");
            String common_name = currentPlant.getString("common_name");
            JSONArray scientific_name = currentPlant.getJSONArray("scientific_name");
            JSONArray other_name = currentPlant.getJSONArray("other_name");
            String cycle = currentPlant.getString("cycle");
            String watering = currentPlant.getString("watering");
            JSONArray sunlight = currentPlant.getJSONArray("sunlight");
            JSONObject default_image = currentPlant.getJSONObject("default_image");

            String[] plantScientificNames = new String[scientific_name.length()];
            for (int j = 0; j < scientific_name.length(); j++)
            {
                plantScientificNames[j] = scientific_name.getString(j);
            }

            /*String[] plantOtherNames = new String[other_name.length()];
            for (int k = 0; k < scientific_name.length(); k++)
            {
                plantOtherNames[k] = other_name.getString(k);
            }*/

            Plant newPlant = new Plant.PlantBuilder()
            .setId(id).setCommon_name(common_name)
            .setScientific_name(plantScientificNames[0])
            .setCycle(cycle)
            .setWatering(watering)
            .setPlantImageURL(default_image.getString("original_url"))
            .setDefault_image(BitmapFactory.decodeResource(makethisplantUI.getApplicationContext().getResources(), R.drawable.defaultimage))
            .buildPlant();

            System.out.println(default_image.getString("original_url"));

            createdPlantObjects.add(newPlant);
        }

        Integer currentPage = nonparsedPlants.getInt("current_page");
        Integer lastPage = nonparsedPlants.getInt("last_page");
        makethisplantUI.createPlantGrid(createdPlantObjects,currentPage,lastPage);
    }

    public void addCustomPlant()
    {

    }

    public void addPlantToDatabase(Plant plant)
    {
        database.addPlant(plant);
        List<Plant> newList = database.loadAllPlants();
        System.out.println("Plant list is now: " + newList.size());
    }
}
