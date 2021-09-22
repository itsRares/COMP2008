package com.bignerdranch.android.tradesexplorer20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bignerdranch.android.tradesexplorer20.Model.Area;

public class AreaInfoActivity extends AppCompatActivity {

    private static final String AREA = "au.edu.curtin.tradesexplorer.area";

    //CLASSFIELDS
    private AreaInfoFragment areaFrag;
    private Area mArea;
    private TradeAreaLab mAreaLab;
    private Button mButtonDone;

    //Creates a new intent, used by the parent class
    public static Intent newIntent(Context packageContext, Area area) {
        Intent i = new Intent(packageContext, AreaInfoActivity.class);
        i.putExtra(AREA, area);
        return i;
    }

    //Extracts the infomation from the intent
    public static Area getAreaInfo(Intent result) {
        return (Area)result.getSerializableExtra(AREA);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_info);

        //Assigns view
        mButtonDone = (Button) findViewById(R.id.buttonDone);

        mAreaLab = new TradeAreaLab();
        mAreaLab.load(AreaInfoActivity.this);
        mArea = mAreaLab.get(getAreaInfo(getIntent()).getId());

        //Creates the area info fragment
        FragmentManager fm = getSupportFragmentManager();
        areaFrag = (AreaInfoFragment) fm.findFragmentById(R.id.area_info_fragment_container);
        if (areaFrag == null) {
            areaFrag = new AreaInfoFragment();
            fm.beginTransaction().add(R.id.area_info_fragment_container, areaFrag, "AreaInfo").commit();
        }

        //Listens to see when the 'Leave' button is pressed
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updateStatusFragment();
    }

    //Updates the area info fragment to make sure it is up to date
    private void updateStatusFragment() {
        FragmentManager fm = getSupportFragmentManager();
        areaFrag = (AreaInfoFragment) fm.findFragmentById(R.id.area_info_fragment_container);
        if (areaFrag != null) {
            areaFrag.updateArea(mArea);
        }
    }

    public Area getArea() {
        return mArea;
    }
}
