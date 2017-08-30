/*
    ForeseeMobile
    Copyright (C) 2017  macleod2486

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see [http://www.gnu.org/licenses/].
 */

package com.macleod2486.foreseemobile.Tools;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class PriceFinder
{
    Activity appActivity;

    Response.Listener<JSONArray> imageListener;
    Response.Listener<JSONArray> jsonListener;
    Response.ErrorListener jsonErrorListener;

    ArrayList<String> cardList;

    String edition;

    public PriceFinder(final Activity activity, final ListView cardListView, final ImageView cardImage)
    {
        this.appActivity = activity;
        cardList = new ArrayList<String>();
        Log.i("PriceFinder", "Initalized");

        imageListener = new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    Log.i("PriceFinder","CardName "+response.getJSONObject(0).getString("name"));

                    JSONObject card;
                    JSONArray editionArray;
                    String imageUrl = "";

                    for(int index = 0; index < response.length(); index++)
                    {
                        card = response.getJSONObject(index);

                        Log.i("PriceFinder","Card complete: "+card.toString());
                        if(card.has("editions"))
                        {
                            Log.i("PriceFinder","Edition exists");
                            editionArray = card.getJSONArray("editions");
                            for(int secondIndex = 0; secondIndex < editionArray.length(); secondIndex++)
                            {
                                if(editionArray.getJSONObject(secondIndex).getString("set").equals(edition))
                                {
                                    imageUrl = editionArray.getJSONObject(secondIndex).getString("image_url");
                                }
                            }
                        }

                    }

                    //Set imageview from received image
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e("PriceFinder",e.toString());
                }

                Log.i("Volley", "Response ");
            }
        };

        jsonListener = new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                //Get current price data from Foresee api
            }
        };
    }

    public void getPriceOfCard(String cardName, String set)
    {
        Log.i("PriceFinder", "Initalized");

        edition = set;

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(appActivity);

        try
        {
            cardName = URLEncoder.encode(cardName, "UTF-8");
            set = URLEncoder.encode(set, "UTF-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String url = "https://api.deckbrew.com/mtg/cards?name="+cardName;
        RequestQueue queue = Volley.newRequestQueue(appActivity.getApplicationContext());

        JsonArrayRequest imageRequest = new JsonArrayRequest(url, jsonListener, jsonErrorListener);

        url = preference.getString("","")+"/api?source=MTGPrice&cardName="+cardName+"&set="+set;
        JsonArrayRequest request = new JsonArrayRequest(url, jsonListener, jsonErrorListener);

        queue.add(request);
        queue.add(imageRequest);
    }
}