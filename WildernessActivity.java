package com.bignerdranch.android.tradesexplorer20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.tradesexplorer20.Model.Equipment;
import com.bignerdranch.android.tradesexplorer20.Model.Food;
import com.bignerdranch.android.tradesexplorer20.Model.Item;
import com.bignerdranch.android.tradesexplorer20.Model.Player;

import java.util.LinkedList;
import java.util.List;

public class WildernessActivity extends AppCompatActivity {

    private static final String PLAYER = "au.edu.curtin.tradesexplorer.player";

    private RecyclerView mItemRecyclerView;
    private WildernessAdapter mAdapter;
    private Button mButtonLeave;
    private Player mPlayer;
    private List<Equipment> mPlayerEquipment;
    private List<Item> mWildernessItems;
    private TradePlayerLab mPlayerLab;
    private TradeItemLab mItemLab;
    private UserInvFragment userInvFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wilderness);

        mItemRecyclerView = (RecyclerView) findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        //Initializes and loads databases
        mPlayerLab = new TradePlayerLab();
        mItemLab = new TradeItemLab();
        mPlayerLab.load(WildernessActivity.this);
        mItemLab.load(WildernessActivity.this);

        mPlayer = mPlayerLab.get();
        createRandomEquipment();

        mButtonLeave = (Button) findViewById(R.id.buttonLeave);
        mButtonLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Creates the user inventory fragment
        FragmentManager fm = getSupportFragmentManager();
        userInvFrag = (UserInvFragment) fm.findFragmentById(R.id.user_inv_fragment_container);
        if (userInvFrag == null) {
            userInvFrag = new UserInvFragment();
            fm.beginTransaction().add(R.id.user_inv_fragment_container, userInvFrag, "UserInv").commit();
        }

        updateItem();
    }

    public void createRandomEquipment() {
        mWildernessItems = new LinkedList<>();
        mWildernessItems.add(new Food("Apple","Crunchy, Delicious and ready to eat and gain health! All 14 health is gained when eaten",20, 15));
        mWildernessItems.add(new Equipment("Arrows", "Useless alone but with a bow you'll be a force to be reconed with. 20g per arrow so very light!", 5, 5));
    }

    //When called it updates all the items in the market
    public void updateItem() {
        if (mWildernessItems.size() == 0) {
            mWildernessItems.add(null);
        } else {
            mWildernessItems.remove(null);
        }
        if (mAdapter == null) {
            mAdapter = new WildernessAdapter(mWildernessItems);
            mItemRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setItems(mWildernessItems);
            mAdapter.notifyDataSetChanged();
        }
    }

    //When called it updates the users inventory
    public void updateUserInv() {
        FragmentManager fm = getSupportFragmentManager();
        userInvFrag = (UserInvFragment) fm.findFragmentById(R.id.user_inv_fragment_container);
        if (userInvFrag != null) {
            userInvFrag.updateInv();
        }
    }

    public static Intent newIntent(Context packageContext, Player user) {
        Intent i = new Intent(packageContext, WildernessActivity.class);
        i.putExtra(PLAYER, user);
        return i;
    }

    public void setPlayerResult(Player player) {
        Intent data = new Intent();
        data.putExtra(PLAYER, player);
        setResult(RESULT_OK, data);
    }

    private class WildernessHolder extends RecyclerView.ViewHolder {

        //Classfields
        private Item mItem;
        private Button mButtonPickUp;
        private TextView mItemOutName;
        private TextView mItemOutDesc;

        public WildernessHolder(View itemView) {
            super(itemView);

            //Assigns View
            mButtonPickUp = (Button) itemView.findViewById(R.id.buttonPickUp);
            mItemOutName = (TextView) itemView.findViewById(R.id.textItemOutTitle);
            mItemOutDesc = (TextView) itemView.findViewById(R.id.textItemOutDesc);

            //If the user wants to buy something
            mButtonPickUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mWildernessItems.isEmpty()) {
                        Item i = mItem;
                        if (i instanceof Equipment) {
                            mPlayer.addEquipment((Equipment) i);
                            mItemLab.add(mItem);
                            Toast.makeText(WildernessActivity.this, "Item added to Inventory", Toast.LENGTH_LONG).show();
                            updateItem();
                            updateUserInv();
                        } else {
                            mPlayer.setHealth(mPlayer.getHealth() + ((Food) i).getHealth());
                            mPlayerLab.update(mPlayer);
                            Toast.makeText(WildernessActivity.this, ((Food) i).getHealth()+" health gained!", Toast.LENGTH_LONG).show();
                        }
                        mWildernessItems.remove(mItem);
                        updateItem();
                    }
                    setPlayerResult(mPlayer);
                }
            });
        }

        //Bind the views to values
        public void bindPlayer(Item item) {
            if (item == null) {
                mItemOutName.setText("Empty");
                mItemOutDesc.setText("There is no item to show");
            } else {
                mItem = item;
                mItemOutName.setText(item.getTitle());
                mItemOutDesc.setText(item.getDescription());
            }
        }
    }

    private class WildernessAdapter extends RecyclerView.Adapter<WildernessHolder> {
        private List<Item> mItems;

        public WildernessAdapter(List<Item> mItems) {
            this.mItems = mItems;
        }

        @Override
        public WildernessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.item_wild_display, parent, false);
            return new WildernessHolder(view);
        }

        @Override
        public void onBindViewHolder(WildernessHolder holder, int position) {
            holder.bindPlayer(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mWildernessItems.size();
        }

        public void setItems(List<Item> mItems) {
            this.mItems = mItems;
        }
    }

}
