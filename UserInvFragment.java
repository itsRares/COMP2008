package com.bignerdranch.android.tradesexplorer20;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.tradesexplorer20.Model.Area;
import com.bignerdranch.android.tradesexplorer20.Model.Equipment;
import com.bignerdranch.android.tradesexplorer20.Model.Food;
import com.bignerdranch.android.tradesexplorer20.Model.Item;
import com.bignerdranch.android.tradesexplorer20.Model.Player;

import java.util.LinkedList;
import java.util.List;

public class UserInvFragment extends Fragment {

    private TradePlayerLab mPlayerLab;
    private TradeItemLab mItemLab;

    private RecyclerView mUserInvRecyclerView;
    private UserInvAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlayerLab = new TradePlayerLab();
        mItemLab = new TradeItemLab();

        mPlayerLab.load(getActivity());
        mItemLab.load(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_inv, container, false);
        mUserInvRecyclerView = view.findViewById(R.id.user_inv_recycler_view);
        mUserInvRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        updateInv();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateInv() {
        Player mPlayer = mPlayerLab.get();
        mPlayer.setEquipment(mItemLab.getAll());
        List<Equipment> equipment = mPlayer.getEquipment();
        if (mItemLab.size() == 0) {
            equipment = new LinkedList<>();
            equipment.add(null);
        }

        if (mAdapter == null) {
            mAdapter = new UserInvAdapter(equipment);
            mUserInvRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setItems(equipment);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class UserInvHolder extends RecyclerView.ViewHolder {

        private Equipment mEquipment;
        private Button mButtonOne;
        private Button mButtonTwo;
        private TextView mItemName;
        private TextView mItemDesc;

        public UserInvHolder(View itemView) {
            super(itemView);

            mButtonOne = (Button) itemView.findViewById(R.id.buttonBuy);
            mButtonTwo = (Button) itemView.findViewById(R.id.buttonSell);
            mItemName = (TextView) itemView.findViewById(R.id.textItemTitle);
            mItemDesc = (TextView) itemView.findViewById(R.id.textItemDesc);

            mButtonTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEquipment != null) {
                        switch (mEquipment.getTitle()) {
                            case "Improbabilty Drive":
                                //something
                                break;
                            case "Ben Kenobi":
                                //Something
                                break;
                            case "Portable Smell-O-Scope":
                                //Something
                                break;
                            default:
                                //something
                        }
                    }
                }
            });

            mButtonOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEquipment != null) {
                        mItemLab.remove(mEquipment);
                        Toast.makeText(getActivity(), "Item has been dropped", Toast.LENGTH_LONG).show();
                        updateInv();
                    }
                }
            });
        }

        public void bindItem(Equipment equipment) {
            mEquipment = equipment;
            if (mItemLab.size() > 0) {
                mItemName.setText(equipment.getTitle());
                mItemDesc.setText(equipment.getDescription());
            } else {
                mItemName.setText("Empty");
                mItemDesc.setText("There is no item to show");
            }

        }
    }

    private class UserInvAdapter extends RecyclerView.Adapter<UserInvHolder> {
        private List<Equipment> mEquipment;

        public UserInvAdapter(List<Equipment> equipment) {
            mEquipment = equipment;
        }

        @Override
        public UserInvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_inv_display, parent, false);
            return new UserInvHolder(view);
        }

        @Override
        public void onBindViewHolder(UserInvHolder holder, int position) {
            holder.bindItem(mEquipment.get(position));
        }

        @Override
        public int getItemCount() {
            return mEquipment.size();
        }

        public void setItems(List<Equipment> equipment) {
            mEquipment = equipment;
        }
    }
}
