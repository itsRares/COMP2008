package com.bignerdranch.android.tradesexplorer20;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.tradesexplorer20.Model.Area;
import com.bignerdranch.android.tradesexplorer20.Model.Player;

public class CustomGridViewAdapter extends ArrayAdapter {

    //Classfields
    private Player player;
    private Area[][] data;
    private Context context;
    private TradeAreaLab mAreaLab;

    //Contructor
    public CustomGridViewAdapter(Context context, Area[][] data, Player player)
    {
        super(context, 0);
        this.context=context;
        this.data = data;
        this.player = player;

        //Gets Area database set
        mAreaLab = new TradeAreaLab();
        mAreaLab.load(getContext());
    }

    @Override
    public int getCount() {
        return 64;
    }

    @Override
    public View getView(int position, View grid_row, ViewGroup parent)
    {
        if (grid_row == null)
        {
            //Calc current column and row position
            int columnPosition = position % data[0].length;
            int rowPosition = (position - columnPosition) / data[0].length;
            //Finds the area object from them
            Area currentLocation = data[columnPosition][rowPosition];
            Area currentStarredLocation = mAreaLab.getGameMap(8)[columnPosition][rowPosition];

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            grid_row = inflater.inflate(R.layout.grid_row, parent, false);

            //Assigns views
            TextView textViewTitle = (TextView) grid_row.findViewById(R.id.textView);
            ImageView imageView = (ImageView) grid_row.findViewById(R.id.imageView);
            ImageView imageView2 = (ImageView) grid_row.findViewById(R.id.imageView2);
            ImageView playerLocation = (ImageView) grid_row.findViewById(R.id.playerLocation);
            ImageView starredLocation = (ImageView) grid_row.findViewById(R.id.starredLocation);

            //If the user has explored the area
            if (currentLocation.isExplored()) {
                //And if it is a town
                if (currentLocation.isTown()) {
                    //Show that it is a town via text and drawable
                    textViewTitle.setText(currentLocation.getAreaType());
                    imageView.setImageResource(R.drawable.ic_building4);
                } else {
                    //ELSE Show that it is the wilderness via text and drawable
                    textViewTitle.setText(currentLocation.getAreaType());
                    imageView.setImageResource(R.drawable.ic_grass3);
                }
            } else {
                //Else it hasnt been discovered yet, show the undiscovered drawable
                imageView2.setImageResource(R.drawable.custom_grid_border_undiscovered);
            }

            //Show the players location
            if (columnPosition == player.getRowLocation() && rowPosition == player.getColLocation()) {
                playerLocation.setImageResource(R.drawable.player_icon);
            }

            //Show the starred location
            if (currentStarredLocation.isStarred()) {
                starredLocation.setImageResource(R.drawable.starred_icon);
            }
        }

        return grid_row;
    }

}
