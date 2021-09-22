package com.bignerdranch.android.tradesexplorer20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.tradesexplorer20.Model.Area;
import com.bignerdranch.android.tradesexplorer20.Model.Equipment;
import com.bignerdranch.android.tradesexplorer20.Model.Food;
import com.bignerdranch.android.tradesexplorer20.Model.Item;
import com.bignerdranch.android.tradesexplorer20.Model.Player;

import java.util.LinkedList;
import java.util.List;

public class MarketActivity extends AppCompatActivity {

    private static final String PLAYER = "au.edu.curtin.tradesexplorer.player";

    //Classfields
    private Button mButtonLeave;
    private List<Item> mItemBank;
    private Player mPlayer;

    private UserInvFragment userInvFrag;
    private RecyclerView mItemRecyclerView;
    private MarketAdapter mAdapter;

    private TradePlayerLab mPlayerLab;
    private TradeItemLab mItemLab;

    //Creates an intent, used by the parent class
    public static Intent newIntent(Context packageContext, Player user) {
        Intent i = new Intent(packageContext, MarketActivity.class);
        i.putExtra(PLAYER, user);
        return i;
    }

    //Creates the items used in the markets
    private void createItems() {
        mItemBank = new LinkedList<>();
        mItemBank.add(new Food("Apple","Crunchy, Delicious and ready to eat and gain health! All 14 health is gained when eaten",20, 15));
        mItemBank.add(new Equipment("Bow", "Shoot your enemies with this lightweight 500g bow, though Arrows not included!", 50, 10));
        mItemBank.add(new Food("Chicken noodle soup", "Amazing healing properties, up to 40 health gained. Might not be as good as your mums but its great", 50, 40));
        mItemBank.add(new Food("Chocolate", "Treat yourself, not a great healer but a great treat for you, only 5 health gained", 10, 5));
        mItemBank.add(new Equipment("Arrows", "Useless alone but with a bow you'll be a force to be reconed with. 20g per arrow so very light!", 5, 5));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        mItemRecyclerView = (RecyclerView) findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        //Initializes and loads databases
        mPlayerLab = new TradePlayerLab();
        mItemLab = new TradeItemLab();
        mPlayerLab.load(MarketActivity.this);
        mItemLab.load(MarketActivity.this);

        mButtonLeave = (Button) findViewById(R.id.buttonLeave);
        mPlayer = mPlayerLab.get();

        createItems();

        //Listens to see when back button has been pressed
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

    @Override
    public void onResume() {
        super.onResume();
    }

    //When called it updates all the items in the market
    public void updateItem() {
        if (mAdapter == null) {
            mAdapter = new MarketAdapter(mItemBank);
            mItemRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setItems(mItemBank);
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

    private class MarketHolder extends RecyclerView.ViewHolder {

        //Classfields
        private Item mItem;
        private Button mButtonOne;
        private Button mButtonTwo;
        private TextView mItemName;
        private TextView mItemDesc;
        private TextView mItemCost;
        private TextView mItemSell;

        public MarketHolder(View itemView) {
            super(itemView);

            //Assigns View
            mButtonOne = (Button) itemView.findViewById(R.id.buttonBuy);
            mButtonTwo = (Button) itemView.findViewById(R.id.buttonSell);
            mItemName = (TextView) itemView.findViewById(R.id.textItemTitle);
            mItemDesc = (TextView) itemView.findViewById(R.id.textItemDesc);
            mItemCost = (TextView) itemView.findViewById(R.id.textItemCost);
            mItemSell = (TextView) itemView.findViewById(R.id.textItemSell);

            //If the user wants to buy something
            mButtonOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Find cost and users cash, make sure have enough
                    double cost = mItem.getValue();
                    int cash = mPlayer.getCash();
                    if (cost <= cash) {
                        mPlayer.setCash((int) (cash - cost));
                        //Check to see if equipment or food
                        if (mItem instanceof Equipment) {
                            //Add equipment to database
                            mPlayer.addEquipment((Equipment) mItem);
                            mItemLab.add(mItem);
                            Toast.makeText(MarketActivity.this, "Item added to Inventory", Toast.LENGTH_LONG).show();
                            updateUserInv();
                        } else {
                            //Increase player health with the food
                            mPlayer.setHealth(mPlayer.getHealth() + ((Food) mItem).getHealth());
                            Toast.makeText(MarketActivity.this, ((Food) mItem).getHealth()+" health gained!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //Else they dont have enought money
                        Toast.makeText(MarketActivity.this, "Insufficent funds!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            //The user wants to sell a item
            mButtonTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Equipment> playerEquipment = mPlayer.getEquipment();
                    //Check to make sure equipment
                    if (mItem instanceof Equipment) {
                        Equipment currentEquipment = (Equipment) mItem;
                        if (playerEquipment.contains(currentEquipment)) {
                            //Gives the player back 75% of the items worth
                            double newPrice = currentEquipment.getValue() * 0.75;
                            mPlayer.setCash((int) (mPlayer.getCash() + newPrice));
                            mPlayer.removeEquipment(currentEquipment);
                            mItemLab.remove(mItem);
                            Toast.makeText(MarketActivity.this, "Item sold!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //They cant sell food
                        Toast.makeText(MarketActivity.this, "No Food to sell", Toast.LENGTH_LONG).show();
                    }
                    updateUserInv();
                }
            });
        }

        //Bind the views to values
        public void bindPlayer(Item item) {
            mItem = item;
            mItemName.setText(item.getTitle());
            mItemDesc.setText(item.getDescription());
            mItemCost.setText(String.format("Buy: $%s", String.valueOf(item.getValue())));
            mItemSell.setText(String.format("Sell: $%s", String.valueOf(item.getValue() * 0.75)));
        }
    }

    private class MarketAdapter extends RecyclerView.Adapter<MarketHolder> {
        private List<Item> mItems;

        public MarketAdapter(List<Item> mItems) {
            this.mItems = mItems;
        }

        @Override
        public MarketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.item_display, parent, false);
            return new MarketHolder(view);
        }

        @Override
        public void onBindViewHolder(MarketHolder holder, int position) {
            holder.bindPlayer(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mItemBank.size();
        }

        public void setItems(List<Item> mItems) {
            this.mItems = mItems;
        }
    }

}
