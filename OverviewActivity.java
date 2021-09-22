package com.bignerdranch.android.tradesexplorer20;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.bignerdranch.android.tradesexplorer20.Model.Area;
import com.bignerdranch.android.tradesexplorer20.Model.Player;

public class OverviewActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_AREA = 0;
    private static final String PLAYER = "au.edu.curtin.tradesexplorer.player";
    private static final String AREA = "au.edu.curtin.tradesexplorer.area";

    //Classfields
    private StatusBarFragment statusFrag;
    private Area mGameData[][];
    private Button mButtonLeave;

    //Creates and intent, used by the parent activity
    public static Intent newIntent(Context packageContext, Player user, Area gameData[][]) {
        Intent i = new Intent(packageContext, OverviewActivity.class);
        i.putExtra(PLAYER, user);
        i.putExtra(AREA, gameData);
        return i;
    }

    //Extracts the player info from the intent
    public static Player getPlayerInfo(Intent result) {
        return (Player)result.getSerializableExtra(PLAYER);
    }

    //Extracts the Area gird from the intent
    public static Area[][] getAreaInfo(Intent result) {
        return (Area[][])result.getSerializableExtra(AREA);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        mButtonLeave = (Button) findViewById(R.id.buttonLeave);

        //Assigns View
        GridView grid = (GridView)findViewById(R.id.grid);
        mGameData = getAreaInfo(getIntent());
        Player mPlayer = getPlayerInfo(getIntent());

        // Create the Custom Adapter Object
        CustomGridViewAdapter gridViewAdapter = new CustomGridViewAdapter(this, mGameData, mPlayer);
        // Set the Adapter to GridView
        grid.setAdapter(gridViewAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Gets the users position
                int columnPosition = position % mGameData[0].length;
                int rowPosition = (position - columnPosition) / mGameData[0].length;

                //Finds the area from the position
                Area currentLocation = mGameData[columnPosition][rowPosition];
                //If area is explored
                if (currentLocation.isExplored()) {
                    //Can start Activity info intent
                    Intent i = AreaInfoActivity.newIntent(OverviewActivity.this, currentLocation);
                    startActivityForResult(i, REQUEST_CODE_AREA);
                } else {
                    //Else cannot
                    Toast.makeText(OverviewActivity.this, "Area not discovered!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Creates the status bar fragment
        FragmentManager fm = getSupportFragmentManager();
        statusFrag = (StatusBarFragment) fm.findFragmentById(R.id.status_bar_fragment_container);
        if (statusFrag == null) {
            statusFrag = new StatusBarFragment();
            fm.beginTransaction().add(R.id.status_bar_fragment_container, statusFrag, "StatusBar").commit();
        }

        //Listens to see when the leave button is pressed
        mButtonLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Passes success result back
                //Not that I need it but the area in the navigation activity was not updating
                //And i didnt know another way but this
                Intent data = new Intent();
                data.putExtra(PLAYER, "Nothing else worked but this ugly mess :(");
                setResult(RESULT_OK, data);
                onBackPressed();
            }
        });

    }
}
