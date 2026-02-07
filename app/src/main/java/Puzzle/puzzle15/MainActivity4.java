package Puzzle.puzzle15;

import static Puzzle.puzzle15.R.layout.activity_main4;
import static Puzzle.puzzle15.R.raw.sound_short;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity4 extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayer2;
    private SharedPreferences pref;
    Button tagleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        setContentView(activity_main4);
        setSwitchMusic();
        pref.edit().putBoolean("switch",true).apply();
    }

//    private void sound(){
//        mediaPlayer = MediaPlayer.create(this, sound_short);
//        mediaPlayer.start();
//    }

    private void setSwitchMusic() {

        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setChecked(MusicManager.isPlaying());

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundMusic();
                toggleButton.setChecked(MusicManager.isPlaying());
            }
        });
    }

    private void backgroundMusic() {
        if (MusicManager.isPlaying()) {
            MusicManager.stopMusic();
            Toast.makeText(this, "Musiqa o‘chirildi", Toast.LENGTH_SHORT).show();
        } else {
            MusicManager.playMusic(this);
            Toast.makeText(this, "Musiqa yoqildi", Toast.LENGTH_SHORT).show();
        }
    }

}