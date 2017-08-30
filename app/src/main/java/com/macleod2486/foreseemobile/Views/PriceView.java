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

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.macleod2486.foreseemobile.R;
import com.macleod2486.foreseemobile.Tools.PriceFinder;

public class PriceView extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.priceview, container, false);

        ListView cardList = (ListView) view.findViewById(R.id.cardData);
        ImageView image = (ImageView) view.findViewById(R.id.cardImage);

        PriceFinder finder = new PriceFinder(getActivity(), cardList, image);
        finder.getPriceOfCard("Lightning Bolt","Alpha");

        return view;
    }
}
