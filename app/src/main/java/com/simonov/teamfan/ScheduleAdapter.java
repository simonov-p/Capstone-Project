package com.simonov.teamfan;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simonov.teamfan.data.GamesContract;

/**
 * Created by petr on 03.01.2016.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private char[] mDataset;

    private Cursor mCursor;

    public void swapCursor(Cursor cursor) {
        Log.e("mytag", "swapCursor, count:" + cursor.getCount());
        mCursor = cursor;

        notifyDataSetChanged();

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextTeam;
        public TextView mTextOpponent;
        public TextView mTextDate;
        public TextView mTextScore;
        public ViewHolder(View v) {
            super(v);
            mTextTeam = (TextView) v.findViewById(R.id.list_item_name_team_A);
            mTextOpponent = (TextView) v.findViewById(R.id.list_item_name_team_B);
            mTextDate = (TextView) v.findViewById(R.id.list_item__date);
            mTextScore = (TextView) v.findViewById(R.id.list_item_score);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ScheduleAdapter(char[] myDataset) {
        mDataset = myDataset;
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
        try {
            mCursor.moveToPosition(position);
            String text1 = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_HOME));
            String text2 = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_AWAY));
            String text3 = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_DATE));
            String text4 = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_HOME_SCORE))
                    + ":" + mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_AWAY_SCORE));

            holder.mTextTeam.setText(text1);
            holder.mTextOpponent.setText(text2);
            holder.mTextDate.setText(text3);
            holder.mTextScore.setText(text4);
            Log.d("mytag","1:" + "n"  + String.valueOf(mDataset[position]) + " 2:"  + String.valueOf(position));
        } catch (NullPointerException e) {
            Log.e("mytag", "No cursor at position:" + String.valueOf(position) + "  " + e.getMessage());
        } catch (CursorIndexOutOfBoundsException c) {
            Log.e("mytag", "Cursor out of bounds:" + String.valueOf(position) + "  " + c.getMessage());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

