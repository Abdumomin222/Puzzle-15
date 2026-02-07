package Puzzle.puzzle15;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    public static MediaPlayer mediaPlayer;

    private MediaPlayer shortplayer;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        pref = getSharedPreferences("pref",MODE_PRIVATE);
        setNewGame();
        setExit();
        setAbout();
        setSettings();
        setContinue();
        menuSound();
//        MusicManager.playMusic(this);



    }

    private void soundBar(){
        shortplayer = MediaPlayer.create(this,R.raw.sound_short);
        if (shortplayer != null) {
            shortplayer.start();
        }
    }

    private void setNewGame(){
        Button newGameButton = findViewById(R.id.new_game);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.edit().putBoolean("isContinue",false).apply();
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
                soundBar();
            }
        });
    }
    private void setContinue(){
        Button continueButton = findViewById(R.id.game_continue);
        if(pref.getString("data","").isEmpty()){
            continueButton.setVisibility(View.GONE);
        }else{
            continueButton.setVisibility(View.VISIBLE);
        }
        continueButton.setOnClickListener(v -> {
            pref.edit().putBoolean("isContinue",true).apply();
            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(intent);
            soundBar();
        });
    }
    private void setExit() {
        Button exitButton = findViewById(R.id.game_exit);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
//                mediaPlayer.stop();
                soundBar();
            }
        });
    }

    private void setAbout(){
        Button newButton = findViewById(R.id.game_about);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(intent);
                soundBar();
            }
        });
    }

    private void setSettings(){
        Button newButton = findViewById(R.id.game_setting);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity4.class);
                startActivity(intent);
                soundBar();
            }
        });

    }
    private void menuSound(){
        mediaPlayer = MediaPlayer.create(this,R.raw.sound_birds);
        mediaPlayer.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    //    public void menuSound(){
//        MusicManager.playMusic(this);
//    }

}