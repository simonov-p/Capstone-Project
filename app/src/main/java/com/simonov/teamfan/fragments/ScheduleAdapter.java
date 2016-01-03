package com.simonov.teamfan.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.simonov.teamfan.R;
import com.simonov.teamfan.data.GamesContract;
import com.simonov.teamfan.utils.Utilities;

/**
 * Created by petr on 03.01.2016.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private char[] mDataset;

    private Cursor mCursor;
    private TextView mEmptyView;
    final private Context mContext;
    private ScheduleAdapterOnClickHandler mClickHandler;
    final private ItemChoiceManager mICM;

    public void swapCursor(Cursor cursor) {
//        Log.e("mytag", "swapCursor, count:" + cursor.getCount());
        mCursor = cursor;

        notifyDataSetChanged();

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextTeam;
        public TextView mTextOpponent;
        public TextView mTextDate;
        public TextView mTextScore;
        public TextView mTextNumberGame;
        public ImageView mImageTeam;
        public ImageView mImageOpponent;
        public ViewHolder(View v) {
            super(v);
            mTextTeam = (TextView) v.findViewById(R.id.list_item_name_team_A);
            mTextOpponent = (TextView) v.findViewById(R.id.list_item_name_team_B);
            mTextDate = (TextView) v.findViewById(R.id.list_item__date);
            mTextScore = (TextView) v.findViewById(R.id.list_item_score);
            mTextNumberGame = (TextView) v.findViewById(R.id.list_item_number_of_game);
            mImageTeam = (ImageView) v.findViewById(R.id.list_item_logo_team_A);
            mImageOpponent = (ImageView) v.findViewById(R.id.list_item_logo_team_B);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(mCursor.getLong(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_GAME_ID)), this);
            mICM.onClick(this);
        }
    }

    public ScheduleAdapter(Context context, ScheduleAdapterOnClickHandler handler, TextView emptyView, int choiceMode) {
        mContext = context;
        mEmptyView = emptyView;
        mClickHandler = handler;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_games, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (null == mCursor) {
            Log.e("mytag", "onBindViewHolder Cursor is null, position " + position);
            return;
        }
        holder.mTextNumberGame.setText(String.format("%s/%d", String.valueOf(position + 1), mCursor.getCount()));

        try {
            mCursor.moveToPosition(position);

            String teamName = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME));
            String opponentName = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME));
            String dateText = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_DATE));
            String gameScore = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_SCORE))
                    + ":" + mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_SCORE));

            Glide.with(mContext)
                    .load(Utilities.getTeamLogo(mContext, teamName))
                    .error(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(holder.mImageTeam);

            Glide.with(mContext)
                    .load(Utilities.getTeamLogo(mContext, opponentName))
                    .error(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(holder.mImageOpponent);

            holder.mTextTeam.setText(teamName);
            holder.mTextOpponent.setText(opponentName);
            holder.mTextDate.setText(dateText);
            holder.mTextScore.setText(gameScore);

            final String text =  "GAME_ID:" + mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_GAME_ID)) +
                    "COLUMN_GAME_NBA_ID:" +  mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_GAME_NBA_ID));

            Log.d("mytag","1:" + "n"  + String.valueOf(mDataset[position]) + " 2:"  + String.valueOf(position));
            mICM.onBindViewHolder(holder, position);

        } catch (NullPointerException e) {
            Log.e("mytag", "No cursor at position:" + String.valueOf(position) + "  " + e.getMessage());
        } catch (CursorIndexOutOfBoundsException c) {
            Log.e("mytag", "Cursor out of bounds:" + String.valueOf(position) + "  " + c.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }
    public static interface ScheduleAdapterOnClickHandler {
        void onClick(Long game, ViewHolder vh);
    }
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mICM.onRestoreInstanceState(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        mICM.onSaveInstanceState(outState);
    }

    public int getSelectedItemPosition() {
        return mICM.getSelectedItemPosition();
    }
}

