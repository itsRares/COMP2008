package com.bignerdranch.android.tradesexplorer20;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bignerdranch.android.tradesexplorer20.Model.Area;

public class AreaInfoFragment extends Fragment {

    //Classfields
    private RecyclerView mAreaInfoRecyclerView;
    private TradeAreaAdapter mAdapter;
    private TradeAreaLab mAreaLab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_area_info, container, false);
        mAreaInfoRecyclerView = view.findViewById(R.id.area_info_recycler_view);
        mAreaInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Gets Area database set
        mAreaLab = new TradeAreaLab();
        mAreaLab.load(getActivity());

        //Checks to see where this fragment is getting used
        if (getActivity() instanceof AreaInfoActivity) {
            //Firstly it can be used in the Area info activity
            AreaInfoActivity activity = (AreaInfoActivity) getActivity();
            updateArea(activity.getArea());
        } else if (getActivity() instanceof NavigationActivity) {
            //Or it can be used in the Navigation file
            NavigationActivity activity = (NavigationActivity) getActivity();
            updateArea(activity.getArea());
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //Updates the area info/Creates the adapters/Notifies the adapters
    public void updateArea(Area area) {
        if (mAdapter == null) {
            mAdapter = new TradeAreaAdapter(area);
            mAreaInfoRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setArea(area);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class TradeAreaHolder extends RecyclerView.ViewHolder {

        private Area mArea;
        private TextView mRegionText;
        private EditText mEditText;
        private CheckBox mStarred;

        public TradeAreaHolder(final View itemView) {
            super(itemView);

            //Assigns view
            mRegionText = itemView.findViewById(R.id.textRegion);
            mEditText = itemView.findViewById(R.id.editText);
            mStarred = itemView.findViewById(R.id.checkBox);

            //Listens to see when the user is typing
            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //Nothin
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //Nothin
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Updates the area desc in database
                    mArea.setDescription(s.toString());
                    mAreaLab.update(mArea);
                }
            });

            mStarred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Updates the Starred in the database
                    mArea.setStarred(mStarred.isChecked());
                    mAreaLab.update(mArea);
                }
            });
        }

        //Binds view to values
        public void bindArea(Area area) {
            mArea = area;
            mRegionText.setText(mArea.getAreaType());
            mEditText.setText(mArea.getDescription());
            mStarred.setChecked(mArea.isStarred());

        }
    }

    private class TradeAreaAdapter extends RecyclerView.Adapter<AreaInfoFragment.TradeAreaHolder> {
        private Area mArea;

        public TradeAreaAdapter(Area area) {
            mArea = area;
        }

        @Override
        public TradeAreaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.area_info, parent, false);
            return new TradeAreaHolder(view);
        }

        @Override
        public void onBindViewHolder(TradeAreaHolder holder, int position) {
            holder.bindArea(mArea);
        }

        @Override
        //There can only be 1 Area info view at a time
        public int getItemCount() {
            return 1;
        }

        public void setArea(Area area) {
            mArea = area;
        }
    }

}
