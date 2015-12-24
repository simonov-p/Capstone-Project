package simonov.teamfan;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by petr on 11-Dec-15.
 */
public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder>{
    private final Context mContext;
    private Cursor mCursor;
    private TextView mEmptyView;
    final private ItemChoiceManager mICM;
    final private GamesAdapterOnClickHandler mClickHandler;

    public void selectView(RecyclerView.ViewHolder vh) {
        if (vh instanceof GamesAdapter.ViewHolder) {
            GamesAdapter.ViewHolder vfh = (GamesAdapter.ViewHolder) vh;
            vfh.onClick(vfh.itemView);
        }
    }

    public static interface GamesAdapterOnClickHandler {
        void onClick(Long date, GamesAdapter.ViewHolder vh);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mICM.onRestoreInstanceState(savedInstanceState);
    }

    public GamesAdapter(Context context, GamesAdapterOnClickHandler dh,
                        View emptyView,
                        int choiceMode) {
        mContext = context;
        mClickHandler = dh;
        mEmptyView = (TextView) emptyView;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
    }

    public Cursor getCursor(){
        return mCursor;
    }

    public void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public GamesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_games, parent, false);
        view.setFocusable(true);
        return new GamesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.nameTeamA.setText(mCursor.getString(GamesFragment.COL_HOME));
        holder.nameTeamB.setText(mCursor.getString(GamesFragment.COL_AWAY));
        holder.score.setText(String.format("%s:%s",
                mCursor.getString(GamesFragment.COL_HOME_SCORE),
                mCursor.getString(GamesFragment.COL_AWAY_SCORE)));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.list_item_logo_team_A) ImageView logoTeamA;
        @Bind(R.id.list_item_logo_team_B) ImageView logoTeamB;
        @Bind(R.id.list_item_name_team_A) TextView nameTeamA;
        @Bind(R.id.list_item_name_team_B) TextView nameTeamB;
        @Bind(R.id.list_item_score) TextView score;
        @Bind(R.id.list_item__date) TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);

        }
    }

    public int getSelectedItemPosition() {
        return mICM.getSelectedItemPosition();
    }
}
