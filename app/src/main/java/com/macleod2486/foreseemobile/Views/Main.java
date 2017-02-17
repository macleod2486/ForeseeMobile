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

package com.macleod2486.foreseemobile.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.macleod2486.foreseemobile.R;
import com.macleod2486.foreseemobile.Tools.CardFinder;

public class Main extends Fragment
{
    private Button searchButton;
    private TextView searchText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.main, container, false);

        searchButton = (Button)view.findViewById(R.id.searchButton);
        searchText = (TextView)view.findViewById(R.id.search);

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                searchForCard(searchText.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle instance)
    {
        super.onCreate(instance);
    }

    private void searchForCard(String text)
    {
        //Do a search for the card.
        Log.i("Main","Entered: "+text);

        CardFinder finder = new CardFinder(getContext());
        finder.getCardInfo(text);

    }
}
