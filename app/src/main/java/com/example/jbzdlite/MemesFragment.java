package com.example.jbzdlite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.jbzdlite.Util.println;

public class MemesFragment extends Fragment {

    public static final String DEFAULT_CATEGORY = "Główna";
    private static final String ARG_CATEGORY = "category";
    private String category;

    public static MemesFragment newInstance(String category) {
        MemesFragment fragment = new MemesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            category = getArguments().getString(ARG_CATEGORY, DEFAULT_CATEGORY);
        else
            category = DEFAULT_CATEGORY;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memes, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerAdapter adapter = new RecyclerAdapter(Categories.get().getCategory(category), container.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}