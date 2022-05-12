package at.jku.tk.mms.voice.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

import at.jku.tk.mms.voice.VoiceMemo;
import at.jku.tk.mms.voice.model.Recording;

public class VoiceMemoImpl implements Runnable {

    private boolean playing, recording;

    public AudioFormat audioFormat;

    private float fFrameRate = 44100.0F;

    private Recording lastRecording;

    private Recording nextToPlay;

    private VoiceMemo ui;

    public VoiceMemoImpl(VoiceMemo ui) {
        this.ui = ui;
        playing = false;
        recording = false;
        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, fFrameRate, 16, 2, 4, fFrameRate, false);
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isRecoding() {
        return recording;
    }

    public void startRecording() {
        recording = true;
        new Thread(this).start();
    }

    public void stopRecording() {
        recording = false;
    }

    public void startPlaying() {
        playing = true;
        new Thread(this).start();
    }

    public void stopPlaying() {
        playing = false;
    }

    public synchronized Recording getLastRecording() {
        try {
            wait();
            return lastRecording;
        } catch (InterruptedException e) {
        }
        return null;
    }

    private synchronized void setLastRecording(Recording r) {
        this.lastRecording = r;
        notify();
    }

    public void setNextToPlay(Recording r) {
        this.nextToPlay = r;
    }

    @Override
    public void run() {
        if (playing) {
            threadPlaying();
        }
        if (recording) {
            threadRecording();
        }

    }

    private void threadPlaying() {
        ByteArrayInputStream stream = new ByteArrayInputStream(nextToPlay.getPcmAudio());

        try {
            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, this.audioFormat);
            final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            line.open();
            line.start();

            int readBytes;
            byte[] buf = new byte[1024];
            while (playing) {
                readBytes = stream.read(buf, 0, buf.length);
                if (readBytes == -1) break;
                line.write(buf, 0, buf.length);
                System.out.println(readBytes);
            }

            line.flush();
            line.drain();
            line.stop();
            line.close();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }

        ui.updateUi();
    }

    private void threadRecording() {
        Recording r = new Recording();

        try {
            final DataLine.Info info = new DataLine.Info(TargetDataLine.class, this.audioFormat);
            final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            final ByteArrayOutputStream baOut = new ByteArrayOutputStream();

            line.open();
            line.start();

            int readBytes;
            byte[] buf = new byte[64];
            while (recording) {
                readBytes = line.read(buf, 0, buf.length);
                baOut.write(buf, 0, readBytes);
                if (readBytes == -1) break;
            }

            line.flush();
            line.drain();
            line.stop();
            line.close();

            r.setPcmAudio(buf);

            setLastRecording(r);
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }

        ui.updateUi();
    }

}
