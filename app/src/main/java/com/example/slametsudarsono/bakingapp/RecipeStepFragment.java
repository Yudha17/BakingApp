package com.example.slametsudarsono.bakingapp;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slametsudarsono.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

public class RecipeStepFragment extends Fragment implements Player.EventListener{

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String TAG = "PlayerActivity";

    private SimpleExoPlayer player;
    private PlayerView playerView;
//    private ComponentListener componentListener;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    TextView recipeInstruction;
    String stepDescription;
    String videoUrl;
    Step step;

    public RecipeStepFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.recipe_step_fragment, container, false);

        Bundle bundle = this.getArguments();
        step = bundle.getParcelable("Steps");
        stepDescription = step.getDescription();

        recipeInstruction = (TextView) rootView.findViewById(R.id.recipeInstructions);
        recipeInstruction.setText(stepDescription);

//        componentListener = new ComponentListener();
        playerView = (PlayerView) rootView.findViewById(R.id.video_view);

        initializePlayer();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if((Util.SDK_INT <= 23 || player == null)){
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Util.SDK_INT <= 23){
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(Util.SDK_INT > 23){
            releasePlayer();
        }
    }

    private void initializePlayer(){

        if(player == null){
            TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), new DefaultTrackSelector
                    (adaptiveTrackSelectionFactory), new DefaultLoadControl());
            player.addListener(this);
//            player.setVideoDebugListener(componentListener);
//            player.setAudioDebugListener(componentListener);
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);

            Bundle bundle = this.getArguments();
            videoUrl = step.getVideoURL();
            if(videoUrl.isEmpty()){
                Toast.makeText(getContext(), "No Video Link", Toast.LENGTH_LONG).show();
                onStop();
            } else {
                String userAgent = Util.getUserAgent(getContext(), "Recipe Instruction");
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl), new DefaultDataSourceFactory(getContext(),userAgent),
                        new DefaultExtractorsFactory(), null, null);

                player.prepare(mediaSource);
                player.setPlayWhenReady(true);
            }

         }
    }

    private void releasePlayer(){
        if(player != null){
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.removeListener(this);
//            player.setVideoListener(null);
//            player.setVideoDebugListener(null);
//            player.setAudioDebugListener(null);
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri){

        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    @SuppressLint("Inlined API")
    private void hideSystemUi(){
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        String stateString;
        switch(playbackState){
            case Player.STATE_IDLE:
                stateString = "ExoPlayer.STATE_IDLE         -";
                break;
            case Player.STATE_BUFFERING:
                stateString = "ExoPlayer.STATE_BUFFERING    -";
                break;
            case Player.STATE_READY:
                stateString = "ExoPlayer.STATE_READY        -";
                break;
            case Player.STATE_ENDED:
                stateString = "ExoPlayer.STATE_ENDED        -";
                break;
            default:
                stateString = "UNKNOWN STATE                -";
                break;
        }
        Log.d(TAG, "changed state to " + stateString + " playWhenReady: " + playWhenReady);

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

//    private class ComponentListener implements ExoPlayer.EventListener, VideoRendererEventListener, AudioRendererEventListener {
//
//        @Override
//        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
//
//        }
//
//        @Override
//        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
//
//        }
//
//        @Override
//        public void onLoadingChanged(boolean isLoading) {
//
//        }
//
//        @Override
//        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//            String stateString;
//            switch(playbackState){
//                case ExoPlayer.STATE_IDLE:
//                    stateString = "ExoPlayer.STATE_IDLE         -";
//                    break;
//                case ExoPlayer.STATE_BUFFERING:
//                    stateString = "ExoPlayer.STATE_BUFFERING    -";
//                    break;
//                case ExoPlayer.STATE_READY:
//                    stateString = "ExoPlayer.STATE_READY        -";
//                    break;
//                case ExoPlayer.STATE_ENDED:
//                    stateString = "ExoPlayer.STATE_ENDED        -";
//                    break;
//                default:
//                    stateString = "UNKNOWN STATE                -";
//                    break;
//            }
//            Log.d(TAG, "changed state to " + stateString + " playWhenReady: " + playWhenReady);
//        }
//
//        @Override
//        public void onRepeatModeChanged(int repeatMode) {
//
//        }
//
//        @Override
//        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
//
//        }
//
//        @Override
//        public void onPlayerError(ExoPlaybackException error) {
//
//        }
//
//        @Override
//        public void onPositionDiscontinuity(int reason) {
//
//        }
//
//
//        @Override
//        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
//
//        }
//
//        @Override
//        public void onSeekProcessed() {
//
//        }
//
//        @Override
//        public void onAudioEnabled(DecoderCounters counters) {
//
//        }
//
//        @Override
//        public void onAudioSessionId(int audioSessionId) {
//
//        }
//
//        @Override
//        public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
//
//        }
//
//        @Override
//        public void onAudioInputFormatChanged(Format format) {
//
//        }
//
//        @Override
//        public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
//
//        }
//
//
//
//        @Override
//        public void onAudioDisabled(DecoderCounters counters) {
//
//        }
//
//        @Override
//        public void onVideoEnabled(DecoderCounters counters) {
//
//        }
//
//        @Override
//        public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
//
//        }
//
//        @Override
//        public void onVideoInputFormatChanged(Format format) {
//
//        }
//
//        @Override
//        public void onDroppedFrames(int count, long elapsedMs) {
//
//        }
//
//        @Override
//        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
//
//        }
//
//        @Override
//        public void onRenderedFirstFrame(Surface surface) {
//
//        }
//
//        @Override
//        public void onVideoDisabled(DecoderCounters counters) {
//
//        }
//    }
}
