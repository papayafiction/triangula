

package de.sopamo.triangula.android.musicProcessing;

import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDeviceBase;


public abstract class BaseOutputAudioDevice extends AudioDeviceBase {
    private Logger log = Logger.getLogger(BaseOutputAudioDevice.class.getName());

    protected int position = 0;
    protected int freq;
    protected int channels;
    protected int samplesPerMillisecond;
    protected boolean init = false;
    protected SampleProcessor processor;

    public BaseOutputAudioDevice(SampleProcessor processor) {
        this.processor = processor;
    }

    @Override
    protected void openImpl() throws JavaLayerException {
        super.openImpl();
    }

    @Override
    protected void writeImpl(short[] samples, int offs, int len) throws JavaLayerException {
        if(!init)
        {/*
            log.log(Level.INFO, "number of channels: " + getDecoder().getOutputChannels());
            log.log(Level.INFO, "number of samples: " + getDecoder().getOutputFrequency());*/
            freq = getDecoder().getOutputFrequency();
            channels = getDecoder().getOutputChannels();
            samplesPerMillisecond = (freq * channels)/1000;
           /* log.log(Level.INFO, "samples/ms: " + samplesPerMillisecond);
            log.log(Level.INFO, "buffer length: " + len);*/

            if(processor != null)
                processor.init(freq, channels);
            init = true;
        }
        position += len/samplesPerMillisecond;

        outputImpl(samples, offs, len);
    }

    protected abstract void outputImpl(short[] samples, int offs, int len) throws JavaLayerException;

    /**
     * Retrieves the current playback position in milliseconds.
    */
    public int getPosition() {
        log.log(Level.FINE, "position: " + position + "ms");
        return position;
    }

    public SampleProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(SampleProcessor processor) {
        this.processor = processor;
    }
}
