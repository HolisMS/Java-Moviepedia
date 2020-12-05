package satella.app.movies4.activity;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import satella.app.movies4.R;
import satella.app.movies4.adapter.MoviePagerAdapter;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        MoviePagerAdapter adapter = new MoviePagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MovieFragment(), getString(R.string.movie));
        adapter.addFragment(new TvShowFragment(), getString(R.string.tvshow));

        viewPager.setAdapter(adapter);

    }

}
