package at.jku.tk.mms.voice.impl;

import java.io.*;

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

            AudioInputStream audioInputStream =new AudioInputStream(stream, this.audioFormat, nextToPlay.getPcmAudio().length / this.audioFormat.getFrameSize());

            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, this.audioFormat);
            final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);

            sourceLine.open();
            sourceLine.start();

            int readBytes;
            byte[] buf = new byte[1024];
            while (playing) {
                readBytes = audioInputStream.read(buf, 0, buf.length);
                if (readBytes == -1) break;
                sourceLine.write(buf, 0, buf.length);
            }

            sourceLine.flush();
            sourceLine.drain();
            sourceLine.stop();
            sourceLine.close();
        } catch (LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        }

        ui.updateUi();
    }

    private void threadRecording() {
        Recording r = new Recording();

        try {
            final DataLine.Info info = new DataLine.Info(TargetDataLine.class, this.audioFormat);
            final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
            final ByteArrayOutputStream baOut = new ByteArrayOutputStream();

            targetLine.open();
            targetLine.start();

            int readBytes;
            byte[] buf = new byte[64];
            while (recording) {
                readBytes = targetLine.read(buf, 0, buf.length);
                baOut.write(buf, 0, readBytes);
                if (readBytes == -1) break;
            }

            targetLine.flush();
            targetLine.drain();
            targetLine.stop();
            targetLine.close();

            r.setPcmAudio(baOut.toByteArray());

            setLastRecording(r);
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }

        ui.updateUi();
    }

}
