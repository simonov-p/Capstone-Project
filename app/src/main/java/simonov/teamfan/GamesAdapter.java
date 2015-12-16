package simonov.teamfan;

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
    @Override
    public GamesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_games, parent, false);
        return new GamesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTeamA.setText("Izhorec");
        holder.nameTeamB.setText("GSW");
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

        }
    }
}
