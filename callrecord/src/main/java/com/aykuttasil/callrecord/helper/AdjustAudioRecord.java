package com.aykuttasil.callrecord.helper;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;

/**
 * Created by vaclavstrnad on 24/05/2018.
 */

public class AdjustAudioRecord extends AudioRecord {

    private float gain = 1.0f;

    /**
     * Class constructor.
     * Though some invalid parameters will result in an {@link IllegalArgumentException} exception,
     * other errors do not.  Thus you should call {@link #getState()} immediately after construction
     * to confirm that the object is usable.
     *
     * @param audioSource       the recording source.
     *                          See {@link MediaRecorder.AudioSource} for the recording source definitions.
     * @param sampleRateInHz    the sample rate expressed in Hertz. 44100Hz is currently the only
     *                          rate that is guaranteed to work on all devices, but other rates such as 22050,
     *                          16000, and 11025 may work on some devices.
     *                          {@link AudioFormat#SAMPLE_RATE_UNSPECIFIED} means to use a route-dependent value
     *                          which is usually the sample rate of the source.
     *                          {@link #getSampleRate()} can be used to retrieve the actual sample rate chosen.
     * @param channelConfig     describes the configuration of the audio channels.
     *                          See {@link AudioFormat#CHANNEL_IN_MONO} and
     *                          {@link AudioFormat#CHANNEL_IN_STEREO}.  {@link AudioFormat#CHANNEL_IN_MONO} is guaranteed
     *                          to work on all devices.
     * @param audioFormat       the format in which the audio data is to be returned.
     *                          See {@link AudioFormat#ENCODING_PCM_8BIT}, {@link AudioFormat#ENCODING_PCM_16BIT},
     *                          and {@link AudioFormat#ENCODING_PCM_FLOAT}.
     * @throws IllegalArgumentException
     */
    public AdjustAudioRecord(int audioSource, int sampleRateInHz, int channelConfig, int audioFormat) throws IllegalArgumentException {
        super(audioSource,
                sampleRateInHz,
                channelConfig,
                audioFormat,
                AudioRecord.getMinBufferSize(sampleRateInHz,
                        channelConfig,
                        audioFormat));
    }

    @Override
    public int read(@NonNull short[] audioData, int offsetInShorts, int sizeInShorts) {
        float gain = getGain(); // taken from the UI control, perhaps in range from 0.0 to 2.0
        int numRead = super.read(audioData, offsetInShorts, sizeInShorts);
        if (numRead > 0) {
            for (int i = 0; i < numRead; ++i) {
                audioData[i] = (short) Math.min((int) (audioData[i] * gain), (int) Short.MAX_VALUE);
            }
        }
        return numRead;
    }

    public float getGain() {
        return gain;
    }

    public AdjustAudioRecord setGain(float gain) {
        this.gain = gain;
        return this;
    }
}
