package Puzzle.puzzle15;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button[][] buttons;
    private GameCoordinate emptyButtonCoordinate;
    private ArrayList<Integer> numbers;
    private int score;
    private TextView scoreTextView;
    private Chronometer chronometer;
    private static MediaPlayer clickSoundPlayer;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("pref", MODE_PRIVATE);

        loadViews();
        loadData();
        loadDataView();
        bagraundMusic();

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt("score", 0);
            long time = savedInstanceState.getLong("time", 0);
            chronometer.setBase(SystemClock.elapsedRealtime() + time);
            chronometer.start();
            scoreTextView.setText(String.valueOf(score));
        } else {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            score = 0;
            scoreTextView.setText("0");

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("score", score);
        outState.putLong("time", chronometer.getBase() - SystemClock.elapsedRealtime());
    }

    private void loadViews() {
        numbers = new ArrayList<>(16);
        buttons = new Button[4][4];
        emptyButtonCoordinate = new GameCoordinate(3, 3);
        scoreTextView = findViewById(R.id.step);
        chronometer = findViewById(R.id.chronometr);

        ViewGroup gamePanel = findViewById(R.id.game_panel);
        for (int i = 0; i < gamePanel.getChildCount(); i++) {
            Button button = (Button) gamePanel.getChildAt(i);
            int row = i / 4;
            int col = i % 4;
            button.setTag(new GameCoordinate(row, col));
            button.setOnClickListener(this::onGameButtonClick);
            buttons[row][col] = button;
        }

        findViewById(R.id.game_restar).setOnClickListener(v -> {
            playClickSound();
            gameRestart();
        });

        findViewById(R.id.game_finish).setOnClickListener(v -> {
            playClickSound();
            finish();
        });
    }

    private void loadData() {
        if (pref.getBoolean("isContinue", false)) {
            String data = pref.getString("data", "");
            if (!data.isEmpty()) {
                for (String s : data.split("\\*")) {
                    numbers.add(Integer.parseInt(s));
                }
                return;
            }
        }
//        generateData();
    }

//    private void generateData() {
//        numbers.clear();
//        for (int i = 0; i < 16; i++) numbers.add(i);
//        do {
////            Collections.shuffle(numbers);
//        } while (!check());
//        chronometer.setBase(SystemClock.elapsedRealtime());
//        chronometer.start();
//    }

    private void loadDataView() {
        for (int i = 0; i < numbers.size(); i++) {
            int row = i / 4;
            int col = i % 4;
            int val = numbers.get(i);

            if (val == 0) {
                emptyButtonCoordinate = new GameCoordinate(row, col);
                buttons[row][col].setText("");
                buttons[row][col].setBackgroundResource(R.color.game_button_empty_background);
            } else {
                buttons[row][col].setText(String.valueOf(val));
                buttons[row][col].setBackgroundResource(R.color.game_button_background);
            }
        }
        score = 0;
        scoreTextView.setText("0");
    }

    private void onGameButtonClick(View v) {
        GameCoordinate current = (GameCoordinate) v.getTag();
        int rowDiff = Math.abs(emptyButtonCoordinate.getRow() - current.getRow());
        int colDiff = Math.abs(emptyButtonCoordinate.getCol() - current.getCol());

        if (rowDiff + colDiff == 1) {
            Button clickedButton = (Button) v;
            Button emptyButton = buttons[emptyButtonCoordinate.getRow()][emptyButtonCoordinate.getCol()];

            playClickSound();

            emptyButton.setText(clickedButton.getText());
            emptyButton.setBackgroundResource(R.color.game_button_background);

            clickedButton.setText("");
            clickedButton.setBackgroundResource(R.color.game_button_empty_background);

            emptyButtonCoordinate = current;
            score++;
            scoreTextView.setText(String.valueOf(score));

            if (isWin()) {
                chronometer.stop();
                Toast.makeText(this, "Siz yutdingiz!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isWin() {
        if (emptyButtonCoordinate.getRow() != 3 || emptyButtonCoordinate.getCol() != 3) return false;
        for (int i = 0; i < 15; i++) {
            int row = i / 4;
            int col = i % 4;
            String expected = String.valueOf(i + 1);
            if (!expected.equals(buttons[row][col].getText().toString())) return false;
        }
        return true;
    }

    private void gameRestart() {
        clearView();
//        generateData();
        loadDataView();
    }

    private void clearView() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        score = 0;
        scoreTextView.setText("0");
    }

    private boolean check() {
        if (numbers.indexOf(0) != 15) return false;

        List<Integer> flat = new ArrayList<>(numbers);
        flat.remove(Integer.valueOf(0));

        int inversions = 0;
        for (int i = 0; i < flat.size(); i++) {
            for (int j = i + 1; j < flat.size(); j++) {
                if (flat.get(i) > flat.get(j)) inversions++;
            }
        }

        int emptyRowFromBottom = 4 - (numbers.indexOf(0) / 4);
        return (emptyRowFromBottom % 2 == 0) == (inversions % 2 != 0);
    }

    private void playClickSound() {
        if (clickSoundPlayer != null) clickSoundPlayer.release();
        clickSoundPlayer = MediaPlayer.create(this, R.raw.sound_short);
        clickSoundPlayer.setOnCompletionListener(MediaPlayer::release);
        clickSoundPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int row = i / 4;
            int col = i % 4;
            String text = buttons[row][col].getText().toString();
            sb.append(text.isEmpty() ? "0" : text).append("*");
        }
        sb.deleteCharAt(sb.length() - 1);
        pref.edit().putBoolean("isContinue", true).putString("data", sb.toString()).apply();
        Log.d("TTT", "Saved Data: " + sb.toString());
    }

    private void bagraundMusic(){
        MusicManager.playMusic(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicManager.stopMusic();
    }
}
