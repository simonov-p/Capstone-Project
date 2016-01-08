package com.simonov.teamfan.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.simonov.teamfan.R;
import com.simonov.teamfan.data.GamesContract;
import com.simonov.teamfan.objects.Event;
import com.simonov.teamfan.utils.Utilities;

/**
 * Created by petr on 03.01.2016.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private boolean mIsSmallIcons;
    private Cursor mCursor;
    private TextView mEmptyView;
    final private Context mContext;
    private ScheduleAdapterOnClickHandler mClickHandler;
    final private ItemChoiceManager mICM;

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;

        notifyDataSetChanged();
        Log.d("mytag", "onLoadFinished-notifyDataSetChanged");

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mAwayTeamScore;
        public TextView mHomeTeamScore;
        public TextView mAwayTeamName;
        public TextView mHomeTeamName;
        public TextView mTextDate;
        public ImageView mAwayTeamLogo;
        public ImageView mHomeTeamLogo;
        public CardView mCard;
        public ViewHolder(View v) {
            super(v);
            mAwayTeamName = (TextView) v.findViewById(R.id.list_item_name_away_team);
            mHomeTeamName = (TextView) v.findViewById(R.id.list_item_name_home_team);
            mAwayTeamScore = (TextView) v.findViewById(R.id.list_item_score_away_team);
            mHomeTeamScore = (TextView) v.findViewById(R.id.list_item_score_home_team);
            mTextDate = (TextView) v.findViewById(R.id.list_item__date);
            mAwayTeamLogo = (ImageView) v.findViewById(R.id.list_item_logo_away_team);
            mHomeTeamLogo = (ImageView) v.findViewById(R.id.list_item_logo_home_team);
            mCard = (CardView) v.findViewById(R.id.card_view);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(new Event(mCursor), this);
            mICM.onClick(this);
        }
    }

    public ScheduleAdapter(Context context, ScheduleAdapterOnClickHandler handler, TextView emptyView,
                           int choiceMode, boolean smallIcons) {
        mContext = context;
        mEmptyView = emptyView;
        mClickHandler = handler;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
        mIsSmallIcons = smallIcons;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_games, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mCursor) {
            Log.e("mytag", "onBindViewHolder Cursor is null, position " + position);
            return;
        }
        try {
            mCursor.moveToPosition(position);

            String teamName = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME));
            String opponentName = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME));
            String dateText = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_DATE));

            String event = mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_GAME_NBA_ID));

            if (Utilities.isHomeGame(event, teamName)){
                fillAwayTeam(opponentName,
                        mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_SCORE)),
                        holder,
                        mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_WON)),
                        mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_LOST))
                );
                fillHomeTeam(teamName,
                        mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_SCORE)),
                        holder,
                        mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_WON)),
                        mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_LOST))
                );
            } else {
                fillAwayTeam(teamName,
                        mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_SCORE)),
                        holder,
                        mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_WON)),
                        mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_LOST))
                );
                fillHomeTeam(opponentName,
                        mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_SCORE)),
                        holder,
                        mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_WON)),
                        mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_LOST))
                );
            }

            holder.mTextDate.setText(Utilities.convertDate(dateText) + " game:" +
                    String.format("%s/%d", String.valueOf(position + 1), mCursor.getCount()));

            mICM.onBindViewHolder(holder, position);

            if (mIsSmallIcons){
                holder.mHomeTeamName.setVisibility(View.GONE);
                holder.mAwayTeamName.setVisibility(View.GONE);
                holder.mHomeTeamLogo.getLayoutParams().height = Utilities.getDensSize(mContext.getResources().getDimensionPixelSize(R.dimen.logo_small_size), mContext);
                holder.mAwayTeamLogo.getLayoutParams().height = Utilities.getDensSize(mContext.getResources().getDimensionPixelSize(R.dimen.logo_small_size), mContext);
                holder.mTextDate.setText(Utilities.convertDateToDate(dateText));
                if (holder.mAwayTeamScore.getText().toString().contains("-")){
                    holder.mHomeTeamScore.setVisibility(View.GONE);
                    holder.mAwayTeamScore.setVisibility(View.GONE);
                } else {
                    holder.mHomeTeamScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, Utilities.getDensSize(mContext.getResources().getDimensionPixelSize(R.dimen.middle_text_size), mContext));
                    holder.mAwayTeamScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, Utilities.getDensSize(mContext.getResources().getDimensionPixelSize(R.dimen.middle_text_size), mContext));
                }
            }

        } catch (NullPointerException e) {
            Log.e("mytag", "No cursor at position:" + String.valueOf(position) + "  " + e.getMessage());
        } catch (CursorIndexOutOfBoundsException c) {
            Log.e("mytag", "Cursor out of bounds:" + String.valueOf(position) + "  " + c.getMessage());
//        } catch (IllegalStateException e){
//            Log.e("mytag", "Illegal exception:" + String.valueOf(position) + "  " + e.getMessage());

        }
    }

    private void fillAwayTeam(String teamFullName, String score, ViewHolder holder, String gamesWon, String gamesLost){
        Glide.with(mContext)
                .load(Utilities.getTeamLogo(mContext, teamFullName))
                .error(R.mipmap.ic_launcher)
                .crossFade()
                .into(holder.mAwayTeamLogo);
        holder.mAwayTeamName.setText(teamFullName);
        if (score.equals(GamesContract.GamesEntry.NO_SCORE)){
            holder.mAwayTeamScore.setText(String.format("%s-%s", gamesWon, gamesLost));
            holder.mAwayTeamScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, Utilities.getDensSize(mContext.getResources().getDimensionPixelSize(R.dimen.middle_text_size), mContext));

            holder.mCard.setContentDescription(String.format("NBA event %s versus %s . Game start at %s",
                    mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME)),
                    mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME)),
                    mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_DATE))
                    ));
        } else {
            holder.mAwayTeamScore.setText(score);
            holder.mAwayTeamScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, Utilities.getDensSize(mContext.getResources().getDimensionPixelSize(R.dimen.big_text_size), mContext));

            holder.mCard.setContentDescription(String.format("NBA event %s versus %s . Game score %d %s %d %s",
                    mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME)),
                    mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME)),
                    mCursor.getInt(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_SCORE)),
                    mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME)),
                    mCursor.getInt(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_SCORE)),
                    mCursor.getString(mCursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME))
                    ));
        }
    }
    private void fillHomeTeam(String teamFullName, String score, ViewHolder holder, String gamesWon, String gamesLost){
        Glide.with(mContext)
                .load(Utilities.getTeamLogo(mContext, teamFullName))
                .error(R.mipmap.ic_launcher)
                .crossFade()
                .into(holder.mHomeTeamLogo);
        holder.mHomeTeamName.setText(teamFullName);
        if (score.equals(GamesContract.GamesEntry.NO_SCORE)){
            holder.mHomeTeamScore.setText(String.format("%s-%s",gamesWon,gamesLost));
            holder.mHomeTeamScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, Utilities.getDensSize(mContext.getResources().getDimensionPixelSize(R.dimen.middle_text_size), mContext));
        } else {
            holder.mHomeTeamScore.setText(score);
            holder.mHomeTeamScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, Utilities.getDensSize(mContext.getResources().getDimensionPixelSize(R.dimen.big_text_size), mContext));
        }
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }
    public interface ScheduleAdapterOnClickHandler {
        void onClick(Event gameEvent, ViewHolder vh);
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

