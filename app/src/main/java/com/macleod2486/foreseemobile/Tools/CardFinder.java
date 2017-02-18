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
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.ArrayList;

public class CardFinder
{
    Activity appActivity;

    Response.Listener<JSONArray> jsonListener;
    Response.ErrorListener jsonErrorListener;

    ArrayList<String> cardList;

    public CardFinder(final Activity activity, final ListView cardListView)
    {
        this.appActivity = activity;
        cardList = new ArrayList<String>();

        jsonListener = new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    Log.i("Cardfinder","CardName "+response.getJSONObject(0).getString("name"));

                    for(int index = 0; index < response.length(); index++)
                    {
                        cardList.add(index,response.getJSONObject(index).getString("name"));
                    }

                    cardListView.setAdapter(new ArrayAdapter(appActivity,android.R.layout.simple_list_item_1, cardList));

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e("Cardfinder",e.toString());
                }

                Log.i("Volley", "Response ");
            }
        };

        jsonErrorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        };
    }

    public void getCardInfo(String cardName)
    {
        try
        {
            cardName = URLEncoder.encode(cardName, "UTF-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String url = "https://api.deckbrew.com/mtg/cards?name="+cardName;
        RequestQueue queue = Volley.newRequestQueue(appActivity.getApplicationContext());

        JsonArrayRequest request = new JsonArrayRequest(url, jsonListener, jsonErrorListener);

        queue.add(request);
    }
}