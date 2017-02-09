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

package com.macleod2486.foreseemobile.Activities;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.macleod2486.foreseemobile.R;
import com.macleod2486.foreseemobile.Views.Main;

public class MainActivity extends AppCompatActivity
{
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;

    private Fragment main;

    private int index;

    @Override
    public void onCreate(Bundle instance)
    {
        super.onCreate(instance);

        setContentView(R.layout.activity_main);

        main = new Main();

        //Configures the drawer
        drawer = (DrawerLayout)findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.mipmap.ic_drawer, R.string.drawer_open, R.string.drawer_close)
        {
            public void onDrawerClosed(View view)
            {
                getSupportActionBar().setTitle(R.string.drawer_close);
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View view)
            {
                getSupportActionBar().setTitle(R.string.drawer_open);
                super.onDrawerOpened(view);
            }
        };
        drawer.setDrawerListener(drawerToggle);
        drawer.setDrawerLockMode(drawer.LOCK_MODE_UNLOCKED);

        //Sets up the listview within the drawer
        String [] menuList = getResources().getStringArray(R.array.menu);
        ListView list = (ListView)findViewById(R.id.menu);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                Log.i("MainActivity","Position "+position);
                if(position == 0)
                {
                    index = 0;
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, main, "main").commit();
                }

                drawer.closeDrawers();
            }
        });

        //Make the actionbar clickable to bring out the drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Displays the first fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.main, main, "main").commit();
    }
}
