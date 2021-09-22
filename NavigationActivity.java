package com.bignerdranch.android.tradesexplorer20;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.android.tradesexplorer20.Model.Area;
import com.bignerdranch.android.tradesexplorer20.Model.GameData;
import com.bignerdranch.android.tradesexplorer20.Model.Item;
import com.bignerdranch.android.tradesexplorer20.Model.Player;

import java.io.Console;

public class NavigationActivity extends AppCompatActivity {

    //Classfields
    private static final int REQUEST_CODE_PLAY = 0;
    private static final int REQUEST_CODE_WILD = 1;
    private static final int MAP_SIZE = 8;
    private static final String PLAYER = "au.edu.curtin.tradesexplorer.player";
    private static final String MAP = "au.edu.curtin.tradesexplorer.gamemap";

    private Button mMoveNorth;
    private Button mMoveSouth;
    private Button mMoveEast;
    private Button mMoveWest;
    private Button mOption;
    private Button mOverview;

    private Area mGameMap[][];
    private Player mPlayer;
    private TradePlayerLab mPlayerLab;
    private TradeItemLab mItemLab;
    private TradeAreaLab mAreaLab;
    private StatusBarFragment statusFrag;
    private AreaInfoFragment areaFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nagivation);

        //Load databases
        mPlayerLab = new TradePlayerLab();
        mItemLab = new TradeItemLab();
        mAreaLab = new TradeAreaLab();
        mPlayerLab.load(NavigationActivity.this);
        mItemLab.load(NavigationActivity.this);
        mAreaLab.load(NavigationActivity.this);

        //Assign views
        mMoveNorth = (Button) findViewById(R.id.moveNorth);
        mMoveSouth = (Button) findViewById(R.id.moveSouth);
        mMoveEast = (Button) findViewById(R.id.moveEast);
        mMoveWest = (Button) findViewById(R.id.moveWest);
        mOption = (Button) findViewById(R.id.buttonOption);
        mOverview = (Button) findViewById(R.id.buttonOverview);

        //Listen for button clicks
        mMoveNorth.setOnClickListener(navListen);
        mMoveSouth.setOnClickListener(navListen);
        mMoveEast.setOnClickListener(navListen);
        mMoveWest.setOnClickListener(navListen);

        //Create the status bar fragment
        FragmentManager fm = getSupportFragmentManager();
        statusFrag = (StatusBarFragment) fm.findFragmentById(R.id.status_bar_fragment_container);
        if (statusFrag == null) {
            statusFrag = new StatusBarFragment();
            fm.beginTransaction().add(R.id.status_bar_fragment_container, statusFrag, "StatusBar").commit();
        }

        //Create the area info fragment
        fm = getSupportFragmentManager();
        areaFrag = (AreaInfoFragment) fm.findFragmentById(R.id.area_info_fragment_container);
        if (areaFrag == null) {
            areaFrag = new AreaInfoFragment();
            fm.beginTransaction().add(R.id.area_info_fragment_container, areaFrag, "AreaInfo").commit();
        }

        //Listen to see if the user clicks on the option button
        mOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentArea = getCurrentArea();
                //Start new Activity either wilderness or town
                if (currentArea == "Wilderness") {
                    Intent i = WildernessActivity.newIntent(NavigationActivity.this, mPlayer);
                    startActivityForResult(i, REQUEST_CODE_PLAY);
                } else if (currentArea == "Town") {
                    Intent i = MarketActivity.newIntent(NavigationActivity.this, mPlayer);
                    startActivityForResult(i, REQUEST_CODE_PLAY);
                }
            }
        });

        //Check to see if the user clicks on the overview button
        mOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentArea = getCurrentArea();
                Intent i = OverviewActivity.newIntent(NavigationActivity.this, mPlayer, mGameMap);
                startActivityForResult(i, REQUEST_CODE_PLAY);
            }
        });

        //Check for saved state
        if (savedInstanceState != null) {
            mGameMap = (Area[][]) savedInstanceState.getSerializable(MAP);
            mPlayer = (Player) savedInstanceState.getSerializable(PLAYER);
        } else {
            mGameMap = new GameData(MAP_SIZE).getGrid();
            mPlayer = new Player(mGameMap[0][0]);
        }

        //Check to see if already a player in database
        if(mPlayerLab.size() == 0) {
            mPlayerLab.add(mPlayer);
        } else {
            //Else add player to the database
            mPlayer = mPlayerLab.get();
            mPlayer.setEquipment(mItemLab.getAll());
        }

        //Make sure to see if already area info in database
        if (mAreaLab.size() == 0) {
            for(int ii = 0; ii < MAP_SIZE; ii++) {
                for(int jj = 0; jj < MAP_SIZE; jj++) {
                    mAreaLab.add(mGameMap[ii][jj]);
                }
            }
        } else {
            //Else add it to the database
            mGameMap = mAreaLab.getGameMap(MAP_SIZE);
        }

        //Update things
        updateButtons();
        updateStats();
    }

    //Listen when direction button click
    View.OnClickListener navListen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.moveNorth:
                    movePlayerNorth();
                    break;
                case R.id.moveSouth:
                    movePlayerSouth();
                    break;
                case R.id.moveEast:
                    movePlayerEast();
                    break;
                case R.id.moveWest:
                    movePlayerWest();
                    break;
            }
            updatePlayer();
            updateButtons();
            updateStats();
        }
    };

    //Update the players position
    public void movePlayerNorth() {
        int currentCol = mPlayer.getColLocation();
        if (currentCol > 0) {
            mPlayer.setColLocation(currentCol - 1);
        }
    }

    public void movePlayerSouth() {
        int currentCol = mPlayer.getColLocation();
        if (currentCol < MAP_SIZE-1) {
            mPlayer.setColLocation(currentCol + 1);
        }
    }

    public void movePlayerEast() {
        int currentRow = mPlayer.getRowLocation();
        if (currentRow < MAP_SIZE-1) {
            mPlayer.setRowLocation(currentRow + 1);
        }
    }

    public void movePlayerWest() {
        int currentRow = mPlayer.getRowLocation();
        if (currentRow > 0) {
            mPlayer.setRowLocation(currentRow - 1);
        }
    }

    //Update the buttons to show that the player has reached the edge
    public void updateButtons() {
        int currentRow = mPlayer.getRowLocation();
        int currentCol = mPlayer.getColLocation();

        switch (currentRow) {
            case 0:
                mMoveWest.setEnabled(false);
                break;
            case MAP_SIZE-1:
                mMoveEast.setEnabled(false);
                break;
            default:
                mMoveWest.setEnabled(true);
                mMoveEast.setEnabled(true);
        }

        switch (currentCol) {
            case 0:
                mMoveNorth.setEnabled(false);
                break;
            case MAP_SIZE-1:
                mMoveSouth.setEnabled(false);
                break;
            default:
                mMoveNorth.setEnabled(true);
                mMoveSouth.setEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PLAYER, mPlayer);
        outState.putSerializable(MAP, mGameMap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_PLAY) {
            if (data == null) {
                return;
            }
            //If result good then update the player
            //And update the players stats/items
            mPlayer = mPlayerLab.get();
            updateStats();
            updateItemsLab();
        }
        updateStatusFragment();
    }

    //Update the stats while moving
    private void updateStats() {
        if (mPlayer.getHealth() == 0) {
            mMoveNorth.setEnabled(false);
            mMoveSouth.setEnabled(false);
            mMoveEast.setEnabled(false);
            mMoveWest.setEnabled(false);
            mOption.setEnabled(false);
        } else {
            updateStatusFragment();
            getExploredArea();
        }
    }

    //Update player health as they lose health while moving
    private void updatePlayer() {
        mPlayer.setHealth(Math.max(0.0, mPlayer.getHealth() - 5.0 - (mPlayer.getEquipmentMass() / 2.0)));
    }

    //Gets the current area type (Town, Wilderness)
    private String getCurrentArea() {
        int col = mPlayer.getColLocation();
        int row = mPlayer.getRowLocation();
        return mGameMap[row][col].getAreaType();
    }

    //Updates the database to show that area has been explored
    private void getExploredArea() {
        int col = mPlayer.getColLocation();
        int row = mPlayer.getRowLocation();
        mGameMap[row][col].setExplored(true);

        mAreaLab.update(mAreaLab.getGameMap(MAP_SIZE)[row][col]);
    }

    //Updates the fragments
    private void updateStatusFragment() {
        mPlayerLab.update(mPlayer);

        //Updates the status fragment
        FragmentManager fm = getSupportFragmentManager();
        statusFrag = (StatusBarFragment) fm.findFragmentById(R.id.status_bar_fragment_container);
        if (statusFrag != null) {
            statusFrag.updateStats();
        }

        //Updates the area info fragment
        FragmentManager fm2 = getSupportFragmentManager();
        areaFrag = (AreaInfoFragment) fm2.findFragmentById(R.id.area_info_fragment_container);
        if (areaFrag != null) {
            int col = mPlayer.getColLocation();
            int row = mPlayer.getRowLocation();
            areaFrag.updateArea(mAreaLab.getGameMap(MAP_SIZE)[row][col]);
        }
    }

    //Updates the items database, adds items to the database
    private void updateItemsLab() {
        for (Item i : mPlayer.getEquipment()) {
            if (mItemLab.itemCount(i.getId()) == 0) {
                mItemLab.add(i);
            }
        }
        mPlayer.setEquipment(mItemLab.getAll());
    }

    //Gets the current Area object
    public Area getArea() {
        int col = mPlayer.getColLocation();
        int row = mPlayer.getRowLocation();
        return mGameMap[row][col];
    }
}
