package com.example.plantparenthood;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Perenual extends com.example.plantparenthood.AbstractAPI
{
    private String APIUrl;
    private String APIPage;
    private String APIKey;
    private String APIParams;
    private JSONObject queryReceived;

    public Perenual()
    {
        APIUrl = "https://perenual.com/api/species-list?";
        APIPage = "page=";
        APIKey = "&key=" + "sk-d6w863e859a77e76936";
        APIParams = "&q=";
        queryReceived = null;
    }

    @Override
    public void queryAPI(RequestQueue queue, String queryParams, Integer page)
    {
        String localParams = APIParams + queryParams;
        String localPage = APIPage + page;

        String APIQuery = APIUrl + localPage + APIKey + localParams;
        System.out.println("User query is: " + APIQuery);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIQuery,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            queryReceived = new JSONObject(response);
                            com.example.plantparenthood.PlantCreator.addPlant(queryReceived, plantsearchref);
                        } catch (JSONException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        System.out.println("Error failed connecting to API");
                    }
                });
        queue.add(stringRequest);
    }

    @Override
    public void queryImageAPI(RequestQueue queue, Plant plant)
    {
        ImageRequest imageRequest = new ImageRequest(plant.plantImageURL, new Response.Listener<Bitmap>()
        {
            @Override
            public void onResponse(Bitmap response)
            {
                System.out.println("Found image for: " + plant.common_name);
                plant.default_image = response;
            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                System.out.println("Error finding image");
            }
        });
        queue.add(imageRequest);
    }
}
