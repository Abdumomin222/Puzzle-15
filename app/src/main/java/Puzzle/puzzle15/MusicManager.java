package Puzzle.puzzle15;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;

public class MusicManager {
    private static MediaPlayer mediaPlayer;

    public static void playMusic(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.sound_birds);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public static boolean isPlaying(){
        mediaPlayer.isPlaying();
        return true;
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
