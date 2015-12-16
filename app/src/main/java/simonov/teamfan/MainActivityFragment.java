package simonov.teamfan;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    @Bind(R.id.games_recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.empty_text_view) TextView mEmptyTextView;
    private GamesAdapter mGamesAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        mGamesAdapter = new GamesAdapter();

        mRecyclerView.setAdapter(mGamesAdapter);

        return rootView;
    }
}
