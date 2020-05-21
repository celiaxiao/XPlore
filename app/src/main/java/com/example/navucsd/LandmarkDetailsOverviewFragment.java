package com.example.navucsd;

import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.PowerManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LandmarkDetailsOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandmarkDetailsOverviewFragment extends Fragment {
    private static final String ARG_PARAM1 = "placeName";
    private static final String DEFAULT_LOCATION = "Geisel Library";

    private String placeName;
    private SearchBarDB database;
    private Location currLocation;

    // Audio playing related fields
    private MediaPlayer mediaPlayer;
    private ImageButton audioButton;
    private SeekBar audioSeek;
    private boolean isAudioPlaying = false;
    private TextView audioProgressText;
    private Handler seekBarUpdateHandler;
    private Runnable seekBarRunnable;
    private final int UPDATE_INTERVAL = 50;

    // Description
    private TextView description;

    // Amenities
    private RecyclerView amenitiesRecycler;
    private AmenitiesAdapter amenitiesAdapter;
    private final int AMENITIES_NUM_COL = 2;

    // Related Videos
    private RecyclerView relatedVideosRecycler;
    private RelatedVideosAdapter relatedVideosAdapter;

    // Related Stories
    private RecyclerView relatedStoriesRecycler;
    private RelatedStoriesAdapter relatedStoriesAdapter;
    private MaterialButton moreStoriesBtn;
    private boolean isExpanded = false;

    // Related Places
    private RecyclerView relatedPlacesRecycler;
    private HorizontalRecyclerAdapter relatedPlacesAdapter;
    private final int MARGIN_RELATED_PLACES = 24;

    // Related Tours
    private RecyclerView relatedToursRecycler;
    private RelatedToursAdapter relatedToursAdapter;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton navButton;

    public LandmarkDetailsOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param placeName Parameter 1.
     * @return A new instance of fragment LandmarkDetailsOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LandmarkDetailsOverviewFragment newInstance(String placeName) {
        LandmarkDetailsOverviewFragment fragment = new LandmarkDetailsOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, placeName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new SearchBarDB(getContext(), "one by one");
        if (getArguments() != null) {
            placeName = getArguments().getString(ARG_PARAM1);
            currLocation = database.getByName(placeName);
        }
        else {
            currLocation = database.getByName(DEFAULT_LOCATION);
        }
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.troll_song);
        mediaPlayer.setWakeMode(getContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landmark_detail_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        description = view.findViewById(R.id.overview_description);
        description.setText(currLocation.getAbout());

        // Set up Seekbar
        int duration = mediaPlayer.getDuration();
        audioProgressText = view.findViewById(R.id.overview_listen_time);
        audioProgressText.setText(formatTime(duration));
        audioSeek = view.findViewById(R.id.overview_listen_seekbar);
        audioSeek.setMax(duration);
        audioSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isAudioPlaying) {
                    startAudio();
                }
            }
        });

        // Set up audio control button
        audioButton = view.findViewById(R.id.overview_listen_play_button);
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAudioPlaying) {
                    pauseAudio();
                }
                else {
                    startAudio();
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                pauseAudio();
            }
        });

        // Set up amenities table
        amenitiesRecycler = view.findViewById(R.id.amenities_content_recycler);
        amenitiesAdapter = new AmenitiesAdapter();
        amenitiesAdapter.setAmenities(currLocation.getAmenities());
        DividerItemDecoration dividerItemDecorationAmenities = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecorationAmenities.setDrawable(getResources().getDrawable(R.drawable.horizontal_divider_12dp));
        amenitiesRecycler.addItemDecoration(dividerItemDecorationAmenities);
        amenitiesRecycler.setLayoutManager(new GridLayoutManager(getContext(), AMENITIES_NUM_COL));
        amenitiesRecycler.setAdapter(amenitiesAdapter);
        amenitiesRecycler.setNestedScrollingEnabled(false);

        navButton = view.findViewById(R.id.overview_nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // Set up related videos
        relatedVideosRecycler = view.findViewById(R.id.related_videos_recycler);
        relatedVideosRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecorationRelatedVideos = new DividerItemDecoration(relatedVideosRecycler.getContext(),
                LinearLayoutManager.VERTICAL) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                // hide the divider for the last child
                if (position == state.getItemCount() - 1) {
                    outRect.set(0, 0, 0, 0);
                } else {
                    super.getItemOffsets(outRect, view, parent, state);
                }
            }
        };
        dividerItemDecorationRelatedVideos.setDrawable(getResources().getDrawable(R.drawable.horizontal_divider_16dp));
        relatedVideosRecycler.addItemDecoration(dividerItemDecorationRelatedVideos);
        relatedVideosRecycler.setLayoutManager(layoutManager);
        relatedVideosAdapter = new RelatedVideosAdapter();
        relatedVideosRecycler.setAdapter(relatedVideosAdapter);

        // Set up related stories
        relatedStoriesRecycler = view.findViewById(R.id.related_stories_recycler);
        relatedStoriesRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecorationRelatedStories = new DividerItemDecoration(relatedStoriesRecycler.getContext(),
                LinearLayoutManager.VERTICAL) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                // hide the divider for the last child
                if (position == state.getItemCount() - 1) {
                    outRect.set(0, 0, 0, 0);
                } else {
                    super.getItemOffsets(outRect, view, parent, state);
                }
            }
        };
        dividerItemDecorationRelatedStories.setDrawable(getResources().getDrawable(R.drawable.horizontal_divider_16dp));
        relatedStoriesRecycler.addItemDecoration(dividerItemDecorationRelatedStories);
        relatedStoriesRecycler.setLayoutManager(layoutManager);
        relatedStoriesAdapter = new RelatedStoriesAdapter();
        relatedStoriesRecycler.setAdapter(relatedStoriesAdapter);

        moreStoriesBtn = view.findViewById(R.id.story_view_more);
        moreStoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpanded) {
                    moreStoriesBtn.setText("Collapse");
                    relatedStoriesAdapter.setExpand(true);
                }
                else {
                    moreStoriesBtn.setText("View More(13)");
                    relatedStoriesAdapter.setExpand(false);
                }
                isExpanded = !isExpanded;
            }
        });

        // Set up the related places
        relatedPlacesRecycler = view.findViewById(R.id.related_places_recycler);
        relatedPlacesRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        DividerItemDecoration dividerItemDecorationRelatedPlaces = new DividerItemDecoration(getContext(),
                LinearLayoutManager.HORIZONTAL) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                int px_margin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN_RELATED_PLACES, getResources().getDisplayMetrics()));
                // hide the divider for the last child
                if (position == state.getItemCount() - 1) {
                    outRect.set(0, 0, px_margin, 0);
                } else {
                    super.getItemOffsets(outRect, view, parent, state);
                }
            }
        };
        dividerItemDecorationRelatedPlaces.setDrawable(getResources().getDrawable(R.drawable.vertical_divider_20dp));
        relatedPlacesRecycler.addItemDecoration(dividerItemDecorationRelatedPlaces);
        relatedPlacesRecycler.setLayoutManager(layoutManager);
        relatedPlacesAdapter = new HorizontalRecyclerAdapter(MARGIN_RELATED_PLACES, 20);
        relatedPlacesRecycler.setAdapter(relatedPlacesAdapter);

        // Set up related tours
        relatedToursRecycler = view.findViewById(R.id.related_tours_recycler);
        relatedToursRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecorationRelatedTours = new DividerItemDecoration(relatedToursRecycler.getContext(),
                LinearLayoutManager.VERTICAL) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                // hide the divider for the last child
                if (position == state.getItemCount() - 1) {
                    outRect.set(0, 0, 0, 0);
                } else {
                    super.getItemOffsets(outRect, view, parent, state);
                }
            }
        };
        dividerItemDecorationRelatedTours.setDrawable(getResources().getDrawable(R.drawable.horizontal_divider_16dp));
        relatedToursRecycler.addItemDecoration(dividerItemDecorationRelatedTours);
        relatedToursRecycler.setLayoutManager(layoutManager);
        relatedToursAdapter = new RelatedToursAdapter();
        relatedToursRecycler.setAdapter(relatedToursAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.release();
        }
        if (seekBarUpdateHandler != null) {
            if (seekBarRunnable != null) {
                seekBarUpdateHandler.removeCallbacks(seekBarRunnable);
            }
            seekBarUpdateHandler = null;
        }
    }

    private void startAudio() {
        isAudioPlaying = true;
        audioButton.setImageResource(R.drawable.ic_pause_24dp);
        mediaPlayer.start();
        if (seekBarUpdateHandler == null) {
            seekBarUpdateHandler = new Handler();
            seekBarRunnable = new Runnable() {
                @Override
                public void run() {
                    int currTime = mediaPlayer.getCurrentPosition();
                    audioProgressText.setText(formatTime(currTime));
                    audioSeek.setProgress(currTime);
                    seekBarUpdateHandler.postDelayed(this, UPDATE_INTERVAL);
                }
            };
            seekBarUpdateHandler.post(seekBarRunnable);
        }
    }

    private void pauseAudio() {
        isAudioPlaying = false;
        audioButton.setImageResource(R.drawable.ic_play_arrow_24dp);
        mediaPlayer.pause();
    }

    private String formatTime(int currTime) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(currTime),
                TimeUnit.MILLISECONDS.toSeconds(currTime) % TimeUnit.MINUTES.toSeconds(1));
//        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(currTime),
//                TimeUnit.MILLISECONDS.toMinutes(currTime) % TimeUnit.HOURS.toMinutes(1),
//                TimeUnit.MILLISECONDS.toSeconds(currTime) % TimeUnit.MINUTES.toSeconds(1));
    }

    private class AmenitiesAdapter extends RecyclerView.Adapter<AmenitiesAdapter.MyViewHolder> {

        private ArrayList<Integer> icons = new ArrayList<Integer>() {
            {
                add(R.drawable.ic_wc_24dp);
                add(R.drawable.ic_bus_24dp);
                add(R.drawable.ic_local_parking_24dp);
                add(R.drawable.ic_restaurant_24dp);
                add(R.drawable.ic_breakfast_24dp);
            }
        };
        private ArrayList<String> names = new ArrayList<String>() {
            {
                add("Restroom");
                add("Bus Stop");
                add("Parking");
                add("Restaurant");
                add("Cafe");
            }
        };
        private String[] nameMap = {"restroom","busstop","parking","restaurant","cafe"};

        public void setAmenities(HashMap<String, Boolean> amenities) {
            for (int i = nameMap.length - 1; i >= 0; i--) {
                if (!amenities.get(nameMap[i])) {
                    icons.remove(i);
                    names.remove(i);
                }
            }
            notifyDataSetChanged();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            private LinearLayout linearLayout;
            private TextView textView;
            private ImageView icon;
            public MyViewHolder(LinearLayout linearLayout) {
                super(linearLayout);
                this.linearLayout = linearLayout;
                textView = linearLayout.findViewById(R.id.amenities_name);
                icon = linearLayout.findViewById(R.id.amenities_icon);
            }
        }

        @Override
        public AmenitiesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.overview_amenities_item, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.icon.setImageResource(icons.get(position));
            holder.textView.setText(names.get(position));
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return icons.size();
        }
    }

    private class RelatedVideosAdapter extends RecyclerView.Adapter<RelatedVideosAdapter.MyViewHolder> {

        private String[] titleSet = {"Geisel Library: An Intro to Brutalism", "Wait...What’s On Geisel Library Roof!?"};
//        private String[] videoFiles = {};

        public RelatedVideosAdapter() {
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RelatedVideosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
            // create a new view
            CardView v = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.overview_videos_item, parent, false);

            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.textViewVideoTitle.setText(titleSet[position]);
            holder.textViewTime.setText("3:03");
//            holder.videoView.setVideoPath(videoFiles[position]);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return titleSet.length;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView textViewVideoTitle, textViewTime;
            public VideoView videoView;

            public MyViewHolder(CardView v) {
                super(v);
                textViewVideoTitle = v.findViewById(R.id.overview_video_name);
                textViewTime = v.findViewById(R.id.overview_video_time);
                videoView = v.findViewById(R.id.overview_video_view);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(v.getContext(), LandmarkDetailsActivity.class);
//                        v.getContext().startActivity(intent);
                    }
                });
            }
        }
    }

    private class RelatedStoriesAdapter extends RecyclerView.Adapter<RelatedStoriesAdapter.MyViewHolder> {

        private String[] storySet = {"\"Throughout my four years of college, one of things I loved to do is digging up all kinds of the animemangas at the shelves at the 4th floor...",
                "\"My favorite study area on campus is Geisel’s 8th floor! It was going through rennovation and reopened on Oct 1st, 2018, my first quarter as a..."};
        private String[] dateSet = {"Jan 10, 2020", "Dec 21, 2019"};
        private boolean isExpended = false;
        private final int NUM_COLLAPSED = 1;

        public RelatedStoriesAdapter() {
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RelatedStoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
            // create a new view
            CardView v = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.overview_stories_item, parent, false);

            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.textViewStory.setText(storySet[position]);
            holder.textViewDate.setText(dateSet[position]);
//            holder.videoView.setVideoPath(videoFiles[position]);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            if (!isExpended) {
                return NUM_COLLAPSED;
            }
            return storySet.length;
        }

        public void setExpand(boolean isExpanded) {
            this.isExpended = isExpanded;
            notifyDataSetChanged();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView textViewStory, textViewDate, textViewRead;

            public MyViewHolder(CardView v) {
                super(v);
                textViewStory = v.findViewById(R.id.story_content);
                textViewDate = v.findViewById(R.id.story_date);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(v.getContext(), LandmarkDetailsActivity.class);
//                        v.getContext().startActivity(intent);
                    }
                });
            }
        }
    }


    private class RelatedToursAdapter extends RecyclerView.Adapter<RelatedToursAdapter.MyViewHolder> {

        private String[] nameSet = {"Geisel Library", "Price Center", "Fallen Star", "Jacobs School of Engineering"};
        private String[] timeSet = {"30min", "30min", "30min", "30min"};
        private String[] StopSet = {"5", "3", "4", "1"};
        private int[] pictures = {R.drawable.geisel_landmark, R.drawable.price_center_east, R.drawable.fallen_star, R.drawable.ucsd};

        public RelatedToursAdapter() {
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RelatedToursAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
            // create a new view
            CardView v = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.overview_tours_item, parent, false);

            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.textViewPlaceName.setText(nameSet[position]);
            holder.textViewTime.setText(timeSet[position]);
            holder.textViewStops.setText(StopSet[position] + " Stops");
            holder.imageViewTourPhoto.setImageResource(pictures[position]);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return nameSet.length;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView textViewPlaceName, textViewTime, textViewStops;
            public ImageView imageViewTourPhoto;

            public MyViewHolder(CardView v) {
                super(v);
                textViewPlaceName = v.findViewById(R.id.overview_tours_name);
                textViewTime = v.findViewById(R.id.overview_tours_time);
                textViewStops = v.findViewById(R.id.overview_tours_stops);
                imageViewTourPhoto = v.findViewById(R.id.overview_tours_image);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(v.getContext(), LandmarkDetailsActivity.class);
//                        v.getContext().startActivity(intent);
                    }
                });
            }
        }
    }
}
