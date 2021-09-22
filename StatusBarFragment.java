package com.bignerdranch.android.tradesexplorer20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.android.tradesexplorer20.Model.Player;

public class StatusBarFragment extends Fragment{

    //Classfields
    private TradePlayerLab mPlayerLab;
    private TradeItemLab mItemLab;
    private RecyclerView mStatusBarRecyclerView;
    private TradeStatusAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mPlayerLab = new TradePlayerLab();
        mItemLab = new TradeItemLab();

        mPlayerLab.load(getActivity());
        mItemLab.load(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_bar, container, false);
        mStatusBarRecyclerView = view.findViewById(R.id.status_bar_recycler_view);
        mStatusBarRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateStats();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //Updates the status bar info
    public void updateStats() {
        Player mPlayer = mPlayerLab.get();
        mPlayer.setEquipment(mItemLab.getAll());
        if (mAdapter == null) {
            mAdapter = new TradeStatusAdapter(mPlayer);
            mStatusBarRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setPlayer(mPlayer);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class TradeStatusHolder extends RecyclerView.ViewHolder {

        //Classfields
        private Player mPlayer;
        private TextView mCashAmountText;
        private TextView mHealthAmountText;
        private TextView mMassAmountText;
        private Button mRestart;

        public TradeStatusHolder(View itemView) {
            super(itemView);

            //Assigns view
            mCashAmountText = itemView.findViewById(R.id.amountCash);
            mHealthAmountText = itemView.findViewById(R.id.amountHealth);
            mMassAmountText = itemView.findViewById(R.id.amountMass);
            mRestart = (Button) itemView.findViewById(R.id.buttonRestart);

            //Listens to see when the restart button has been pressed
            mRestart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPlayer.setHealth(100);
                    mPlayer.setCash(100);
                    mPlayer.setColLocation(0);
                    mPlayer.setRowLocation(0);
                    mPlayerLab.update(mPlayer);
                    mItemLab.removeAll();
                    //Restarts intent
                    Intent intent = getActivity().getIntent();
                    getActivity().overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    startActivity(intent);
                }
            });
        }

        //Binds view to values
        public void bindPlayer(Player player) {
            mPlayer = player;
            mCashAmountText.setText("$" + String.valueOf(mPlayer.getCash()));
            mHealthAmountText.setText(String.valueOf(mPlayer.getHealth()));
            mMassAmountText.setText(String.valueOf(mPlayer.getEquipmentMass()) + "g");
        }
    }

    private class TradeStatusAdapter extends RecyclerView.Adapter<TradeStatusHolder> {
        private Player mPlayer;

        public TradeStatusAdapter(Player player) {
            mPlayer = player;
        }

        @Override
        public TradeStatusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.status_bar, parent, false);
            return new TradeStatusHolder(view);
        }

        @Override
        public void onBindViewHolder(TradeStatusHolder holder, int position) {
            holder.bindPlayer(mPlayer);
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public void setPlayer(Player player) {
            mPlayer = player;
        }
    }

}
