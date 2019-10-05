/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.newventuresoftware.waveformdemo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.newventuresoftware.waveform.WaveformView;
import com.newventuresoftware.waveformdemo.listener.AudioDataReceivedListener;
import com.newventuresoftware.waveformdemo.listener.PlaybackListener;
import com.newventuresoftware.waveformdemo.theard.PlaybackThread;
import com.newventuresoftware.waveformdemo.theard.RecordingThread;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO = 13;

    @BindView(R.id.waveformView)
    WaveformView mRealtimeWaveformView;
    @BindView(R.id.playbackWaveformView)
    WaveformView mPlaybackView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.playFab)
    FloatingActionButton playFab;

    private RecordingThread mRecordingThread;
    private PlaybackThread mPlaybackThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mRecordingThread = new RecordingThread(new AudioDataReceivedListener() {
            @Override
            public void onAudioDataReceived(byte[] data) {
                short[] buffer = convertArrayByteToArrayShort(data);
                mRealtimeWaveformView.setSamples(buffer);
            }
        });

        this.initializeRecordButton();
        this.initializePlayRecord();
    }

    private void initializeRecordButton() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mRecordingThread.recording()) {
                    startAudioRecordingSafe();
                } else {
                    mRecordingThread.stopRecording();
                    initializePlayRecord();
                    mPlaybackView.invalidate();
                }
            }
        });
    }

    private void initializePlayRecord() {
        short[] samples = null;

        try {
            samples = getAudioSample();
        } catch (IOException ex) {
            Log.e("**initializePlayRecord*", ex.getMessage());
        }
        mPlaybackThread = this.createPlaybackThread(samples, mPlaybackView, playFab);
        mPlaybackView.setChannels(1);
        mPlaybackView.setSampleRate(PlaybackThread.SAMPLE_RATE);
        mPlaybackView.setSamples(samples);

        playFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPlaybackThread.playing()) {
                    mPlaybackThread.startPlayback();
                    playFab.setImageResource(android.R.drawable.ic_media_pause);
                } else {
                    mPlaybackThread.stopPlayback();
                    playFab.setImageResource(android.R.drawable.ic_media_play);
                }
            }
        });
    }

    private PlaybackThread createPlaybackThread(short[] samples, final WaveformView mPlaybackView,
                                                final FloatingActionButton playFab) {

        return new PlaybackThread(samples, new PlaybackListener() {
            @Override
            public void onProgress(int progressInMiliseconds) {
                mPlaybackView.setMarkerPosition(progressInMiliseconds);
            }

            @Override
            public void onCompletion() {
                mPlaybackView.setMarkerPosition(mPlaybackView.getAudioLength());
                playFab.setImageResource(android.R.drawable.ic_media_play);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        mRecordingThread.stopRecording();
        mPlaybackThread.stopPlayback();
    }

    private short[] getAudioSample() throws IOException {
        byte[] data;

        if (mRecordingThread.getTrackRecord() == null) {
            InputStream is = getResources().openRawResource(R.raw.jinglebells);

            try {
                data = IOUtils.toByteArray(is);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } else {
            ByteArrayOutputStream buffer = mRecordingThread.getTrackRecord();

            data = buffer.toByteArray();
        }

        ShortBuffer sb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        short[] samples = new short[sb.limit()];
        sb.get(samples);
        return samples;
    }

    public short[] convertArrayByteToArrayShort(byte[] data) {
        ShortBuffer sb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        short[] samples = new short[sb.limit()];

        sb.get(samples);

        return samples;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startAudioRecordingSafe() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            mRecordingThread.startRecording();

        } else {
            requestMicrophonePermission();
        }
    }

    private void requestMicrophonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO)) {
            // Show dialog explaining why we need record audio
            Snackbar.make(mRealtimeWaveformView, "Microphone access is required in order to record audio",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mRecordingThread.stopRecording();
        }
    }
}











