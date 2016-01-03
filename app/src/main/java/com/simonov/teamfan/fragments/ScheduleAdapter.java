package com.simonov.teamfan.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.simonov.teamfan.R;
import com.simonov.teamfan.data.GamesContract;

/**
 * Created by petr on 03.01.2016.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private char[] mDataset;

    private Cursor mCursor;
    private TextView mEmptyView;
    final private Context mContext;

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
        public ViewHolder(View v) {
            super(v);
            mTextTeam = (TextView) v.findViewById(R.id.list_item_name_team_A);
            mTextOpponent = (TextView) v.findViewById(R.id.list_item_name_team_B);
            mTextDate = (TextView) v.findViewById(R.id.list_item__date);
            mTextScore = (TextView) v.findViewById(R.id.list_item_score);
            mTextNumberGame = (TextView) v.findViewById(R.id.list_item_number_of_game);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext,"click",Toast.LENGTH_SHORT).show();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ScheduleAdapter(Context context, TextView emptyView) {
//        mDataset = myDataset;
        mContext = context;
        mEmptyView = emptyView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
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
            String text1 = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME));
            String text2 = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME));
            String text3 = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_DATE));
            String text4 = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_SCORE))
                    + ":" + mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_SCORE));

            holder.mTextTeam.setText(text1);
            holder.mTextOpponent.setText(text2);
            holder.mTextDate.setText(text3);
            holder.mTextScore.setText(text4);

            final String text =  "GAME_ID:" + mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_GAME_ID)) +
                    "COLUMN_GAME_NBA_ID:" +  mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_GAME_NBA_ID))

                    ;

            holder.mTextScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
                }
            });
            Log.d("mytag","1:" + "n"  + String.valueOf(mDataset[position]) + " 2:"  + String.valueOf(position));
        } catch (NullPointerException e) {
            Log.e("mytag", "No cursor at position:" + String.valueOf(position) + "  " + e.getMessage());
        } catch (CursorIndexOutOfBoundsException c) {
            Log.e("mytag", "Cursor out of bounds:" + String.valueOf(position) + "  " + c.getMessage());
        }
    }

    private View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }
}

