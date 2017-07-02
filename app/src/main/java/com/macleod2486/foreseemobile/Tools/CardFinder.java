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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CardFinder
{
    Activity appActivity;

    Response.Listener<JSONArray> jsonListener;
    Response.Listener<String> cardRequestListener;
    Response.ErrorListener errorListener;

    ArrayList<String> cardList;
    ListView.OnItemClickListener cardClick;

    public CardFinder(final Activity activity, final ListView cardListView)
    {
        this.appActivity = activity;
        cardList = new ArrayList<String>();

        cardClick = new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String card = cardList.get(position).toString();
                String cardName = card.substring(0, card.indexOf("\n"));
                String cardSet = card.substring(card.indexOf(":")+2,card.length());
                Log.i("Cardfinder","Picked " + cardName);
                //Display popup to allow selection of set
                showCard(cardName, cardSet);
            }
        };

        cardRequestListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Document doc = new Document(response);
                Elements priceGuide = doc.select(".priceGuidePricePointData");
                Log.i("CardFinder",response);
                for(int index = 0; index < priceGuide.size(); index++)
                {
                    Log.i("CardFinder","Selected "+priceGuide.get(index).toString());
                }

            }
        };

        jsonListener = new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    Log.i("Cardfinder","CardName "+response.getJSONObject(0).getString("name"));

                    String cardName = "";
                    JSONObject card;
                    JSONArray editionArray;
                    String editions = "";

                    for(int index = 0; index < response.length(); index++)
                    {
                        card = response.getJSONObject(index);

                        cardName = card.getString("name");
                        Log.i("CardFinder","Card complete: "+card.toString());
                        if(card.has("editions"))
                        {
                            Log.i("CardFinder","Edition exists");
                            editionArray = card.getJSONArray("editions");
                            editions = "\nEditions: ";
                            for(int secondIndex = 0; secondIndex < editionArray.length(); secondIndex++)
                            {
                                if(editionArray.length() > 1)
                                {
                                    editions += editionArray.getJSONObject(secondIndex).getString("set") + ", ";
                                }
                                else
                                {
                                    editions += editionArray.getJSONObject(secondIndex).getString("set") + "  ";
                                }
                            }
                        }

                        editions = editions.substring(0, editions.length()-2);

                        cardList.add(index,cardName + " " + editions);
                    }

                    cardListView.setOnItemClickListener(cardClick);
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

        errorListener = new Response.ErrorListener()
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

        JsonArrayRequest request = new JsonArrayRequest(url, jsonListener, errorListener)
        {
            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("User-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
                return headers;
            }
        };;

        queue.add(request);
    }

    private void showCard(String cardName, String set)
    {
        String url = "http://shop.tcgplayer.com/magic/";
        set = set.replace("'","").replace(" ","-").toLowerCase();
        cardName = cardName.replace("'","").replace(" ","-").toLowerCase();
        url = url + set + "/" + cardName;

        RequestQueue queue = Volley.newRequestQueue(appActivity.getApplicationContext());

        StringRequest request = new StringRequest(url, cardRequestListener, errorListener);

        queue.add(request);
    }
}