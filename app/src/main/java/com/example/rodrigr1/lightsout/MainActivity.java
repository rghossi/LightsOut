package com.example.rodrigr1.lightsout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int NUM_BUTTONS = 7;
    private List<Button> buttons = new ArrayList<>();
    private int [] btn_values;
    private LightsOutGame lg;
    private Button newGameBtn;
    private TextView textView;
    private int[] buttonsI = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_values = new int[NUM_BUTTONS];
        lg = new LightsOutGame(NUM_BUTTONS);
        textView = (TextView) findViewById(R.id.textView);
        for(int i=0; i<NUM_BUTTONS ;i++) {
            buttons.add((Button) findViewById(buttonsI[i]));
            buttons.get(i).setOnClickListener(buttonClicked);
        }

        newGameBtn = (Button) findViewById(R.id.new_game_button);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getString(R.string.title));
                lg = new LightsOutGame(NUM_BUTTONS);
                updateButtons();
            }
        });
        updateButtons();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("btn_values", btn_values);
        outState.putInt("num_presses", lg.getNumPresses());
        outState.putString("text_view", textView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textView.setText(savedInstanceState.getString("text_view"));
        lg.setAllValues(savedInstanceState.getIntArray("btn_values"));
        lg.setNumPresses(savedInstanceState.getInt("num_presses"));
        updateButtons();
        disableButtonsIfWin();
    }

    private View.OnClickListener buttonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tmp = -1;
            for (int i=0;i<NUM_BUTTONS;i++){
                if (v.getId() == buttonsI[i]) {
                    tmp = i;
                    break;
                }
            }
            lg.pressedButtonAtIndex(tmp);
            String s = getResources().getQuantityString(R.plurals.message_format, lg.getNumPresses(), lg.getNumPresses());
            textView.setText(s);
            updateButtons();
            disableButtonsIfWin();
        }
    };

    private void disableButtonsIfWin(){
        if (lg.checkForWin()) {
            textView.setText(R.string.win_message);
            for (Button bt : buttons)
                bt.setEnabled(false);
        }
    }

    private void updateButtons(){
        for (int i=0;i<NUM_BUTTONS;i++){
            buttons.get(i).setEnabled(true);
            btn_values[i] = lg.getValueAtIndex(i);
            buttons.get(i).setText(String.valueOf(lg.getValueAtIndex(i)));
        }
    }
}
