package com.UCSDTripleC.XPloreUCSD;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.UCSDTripleC.XPloreUCSD.database.LandmarkDatabase;
import com.UCSDTripleC.XPloreUCSD.database.Landmark;
import com.UCSDTripleC.XPloreUCSD.utils.ClickTracker;
import com.UCSDTripleC.XPloreUCSD.utils.DownloadImageSaveTask;
import com.UCSDTripleC.XPloreUCSD.utils.Utils;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LandmarkDetailsOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandmarkDetailsOverviewFragment extends Fragment {

    private LandmarkDatabase landmarkDatabase;
    private Landmark currLandmark;

    // Audio playing related fields
    private MediaPlayer mediaPlayer;
    private ImageButton audioButton;
    private SeekBar audioSeek;
    private boolean isAudioPlaying = false;
    private TextView audioProgressText, audioTitle;
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

    // Related Links
    private RecyclerView relatedLinksRecycler;
    private RelatedLinksAdapter relatedLinksAdapter;

    // Related Places
    private RecyclerView relatedPlacesRecycler;
    private HorizontalRecyclerAdapter relatedPlacesAdapter;
    private final int MARGIN_RELATED_PLACES = 24;

    // Related Tours
    private RecyclerView relatedToursRecycler;
    private RelatedToursAdapter relatedToursAdapter;
    private LinearLayoutManager layoutManager;

    /**
     * The click tracker used in this fragment.
     */
    private ClickTracker clickTracker;

    /**
     * Constructor that initializes the click tracker.
     */
    public LandmarkDetailsOverviewFragment() {
        clickTracker = new ClickTracker();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currLandmark Landmark Object that represents the Landmark of interest.
     * @return A new instance of fragment LandmarkDetailsOverviewFragment.
     */
    public static LandmarkDetailsOverviewFragment newInstance(Landmark currLandmark, LandmarkDatabase database) {
        LandmarkDetailsOverviewFragment fragment = new LandmarkDetailsOverviewFragment();
        fragment.setLandmarkDatabase(database);
        fragment.setLocation(currLandmark);
        return fragment;
    }

    private void setLocation(Landmark currLandmark) {
        this.currLandmark = currLandmark;
    }

    private void setLandmarkDatabase(LandmarkDatabase landmarkDatabase) {
        this.landmarkDatabase = landmarkDatabase;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landmark_detail_overview, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        description = view.findViewById(R.id.overview_description);
        description.setText(currLandmark.getAbout());

        // Set up audio
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getContext(), PowerManager.PARTIAL_WAKE_LOCK);
        try {
            AssetFileDescriptor afd = getContext().getAssets().openFd(currLandmark.getAudio());
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioTitle = view.findViewById(R.id.overview_listen_title);
        audioTitle.setText("Introduction to " + currLandmark.getName());
        audioTitle.setSelected(true);

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
        amenitiesAdapter.setAmenities(currLandmark.getAmenities());
        DividerItemDecoration dividerItemDecorationAmenities = new DividerItemDecoration(getContext(),
            LinearLayoutManager.VERTICAL);
        dividerItemDecorationAmenities.setDrawable(getResources().getDrawable(R.drawable.horizontal_divider_12dp));
        amenitiesRecycler.addItemDecoration(dividerItemDecorationAmenities);
        amenitiesRecycler.setLayoutManager(new GridLayoutManager(getContext(), AMENITIES_NUM_COL));
        amenitiesRecycler.setAdapter(amenitiesAdapter);
        amenitiesRecycler.setNestedScrollingEnabled(false);

        boolean flag = false;
        for (boolean available: currLandmark.getAmenities().values()) {
            if (available) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            amenitiesRecycler.setVisibility(View.GONE);
            view
                .findViewById(R.id.landmark_details_overview_amenities_empty_text_view)
                .setVisibility(View.VISIBLE);
        }

        int top_divider_id = R.id.landmark_details_overview_amenities_divider;

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
        relatedVideosAdapter.setLinks(currLandmark.getVideos());
        relatedVideosRecycler.setAdapter(relatedVideosAdapter);

        // hide this section if empty
        if (currLandmark.getVideos().size() == 0) {
            view.findViewById(R.id.videos_title).setVisibility(View.GONE);
            relatedVideosRecycler.setVisibility(View.GONE);
            view.findViewById(R.id.landmark_details_overview_videos_divider).setVisibility(View.GONE);
            ConstraintLayout layout = view.findViewById(
                R.id.landmark_details_overview_constraint_layout
            );
            ConstraintSet constraint_set = new ConstraintSet();
            constraint_set.clone(layout);
            constraint_set.connect(
                R.id.more_info_title,
                ConstraintSet.TOP,
                top_divider_id,
                ConstraintSet.BOTTOM
            );
            constraint_set.applyTo(layout);
        } else {
            top_divider_id = R.id.landmark_details_overview_videos_divider;
        }

        // Set up related links
        relatedLinksRecycler = view.findViewById(R.id.related_links_recycler);
        relatedLinksRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        relatedLinksRecycler.setLayoutManager(layoutManager);
        relatedLinksAdapter = new RelatedLinksAdapter();
        relatedLinksAdapter.setLinks(currLandmark.getLinks());
        relatedLinksRecycler.setAdapter(relatedLinksAdapter);

        // hide this section if empty
        if (currLandmark.getLinks().size() == 0) {
            view.findViewById(R.id.more_info_title).setVisibility(View.GONE);
            relatedLinksRecycler.setVisibility(View.GONE);
            view.findViewById(R.id.landmark_details_overview_more_info_divider).setVisibility(View.GONE);
            ConstraintLayout layout = view.findViewById(
                R.id.landmark_details_overview_constraint_layout
            );
            ConstraintSet constraint_set = new ConstraintSet();
            constraint_set.clone(layout);
            constraint_set.connect(
                R.id.related_places_title,
                ConstraintSet.TOP,
                top_divider_id,
                ConstraintSet.BOTTOM
            );
            constraint_set.applyTo(layout);
        } else {
            top_divider_id = R.id.landmark_details_overview_more_info_divider;
        }

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
        relatedPlacesAdapter = new HorizontalRecyclerAdapter(clickTracker, MARGIN_RELATED_PLACES, 20, getContext());
        ArrayList<String> relatedPlace = currLandmark.getRelatedPlaces();
        String[] related_places = relatedPlace.toArray(new String[relatedPlace.size()]);
        String[] urls = Utils.nameToUrl(landmarkDatabase, related_places);
        relatedPlacesAdapter.setContent(related_places, urls);
        relatedPlacesRecycler.setAdapter(relatedPlacesAdapter);

        // hide this section if empty
        if (currLandmark.getRelatedPlaces().size() == 0) {
            view.findViewById(R.id.related_places_title).setVisibility(View.GONE);
            relatedPlacesRecycler.setVisibility(View.GONE);
            // since the last section is empty hide the hanging divider
            view.findViewById(top_divider_id).setVisibility(View.GONE);
        }
    }

    /**
     * Called on resume of this fragment and resets the {@code clickTracker}.
     */
    @Override
    public void onResume() {
        super.onResume();
        clickTracker.reset();
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
                add(R.drawable.ic_restaurant_24dp);
                add(R.drawable.ic_breakfast_24dp);
            }
        };
        private ArrayList<String> names = new ArrayList<String>() {
            {
                add("Restroom");
                add("Restaurant");
                add("Cafe");
            }
        };
        private String[] nameMap = {"restroom", "restaurant","cafe"};

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

        private ArrayList<String> links = new ArrayList<>();
        private HashMap<String, Bitmap> images = new HashMap<>();

        public RelatedVideosAdapter() {
        }

        public void setLinks(ArrayList<String> links) {
            this.links = links;
            notifyDataSetChanged();
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
            String link = links.get(position);
            String videoID = link.substring(link.lastIndexOf("v=") + 2);
            String thumbnailUrl = "https://img.youtube.com/vi/" + videoID + "/hqdefault.jpg";
            Log.d("Video Link", thumbnailUrl);
            if (images.containsKey(thumbnailUrl)) {
                holder.imageView.setImageBitmap(images.get(thumbnailUrl));
            }
            else {
                new DownloadImageSaveTask(holder.imageView, images).execute(thumbnailUrl);
            }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoID)));
                }
            });
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoID)));
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return links.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public ImageView imageView;
            public CardView cardView;
            public ImageButton imageButton;

            public MyViewHolder(CardView v) {
                super(v);
                imageView = v.findViewById(R.id.overview_video_image);
                imageButton = v.findViewById(R.id.overview_video_play_button);
                cardView = v;
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

    private class RelatedLinksAdapter extends RecyclerView.Adapter<RelatedLinksAdapter.MyViewHolder> {

        private ArrayList<String> Links = new ArrayList<>();

        public void setLinks(ArrayList<String> links) {
            Links = links;
            notifyDataSetChanged();
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RelatedLinksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
            // create a new view
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.overview_links_item, parent, false);

            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String link = Links.get(position);
            String[] split;
            if (!link.startsWith("http")) {
                split = link.split(":", 2);
            }
            else {
                split = new String[]{"Link", link};
            }
            holder.linkText.setText(split[0]);
            holder.linkText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browse = new Intent(Intent.ACTION_VIEW , Uri.parse(split[1].replace(" ","")));
                    try {
                        startActivity(browse);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return Links.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView linkText;

            public MyViewHolder(TextView v) {
                super(v);
                linkText = v;
            }
        }
    }

    private class RelatedToursAdapter extends RecyclerView.Adapter<RelatedToursAdapter.MyViewHolder> {

        private String[] nameSet = {"Geisel Library", "Price Center", "Fallen Star", "Jacobs School of Engineering"};
        private String[] timeSet = {"30min", "30min", "30min", "30min"};
        private String[] StopSet = {"5", "3", "4", "1"};
        private int[] pictures = {
            R.drawable.geisel_landmark,
            R.drawable.price_center_east,
            R.drawable.fallen_star,
            R.drawable.ucsd
        };

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
