package rev1.simcon;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;

import java.util.Timer;
import java.util.TimerTask;

public class buttonStat extends AppCompatActivity {

    private _simCon simCon = _simCon.getInstance();
    private RadioButton[] button = new RadioButton[32];
    private Handler UIHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_stat);

        button[0] = (RadioButton) findViewById(R.id.radioButton1);
        button[1] = (RadioButton) findViewById(R.id.radioButton2);
        button[2] = (RadioButton) findViewById(R.id.radioButton3);
        button[3] = (RadioButton) findViewById(R.id.radioButton4);
        button[4] = (RadioButton) findViewById(R.id.radioButton5);
        button[5] = (RadioButton) findViewById(R.id.radioButton6);
        button[6] = (RadioButton) findViewById(R.id.radioButton7);
        button[7] = (RadioButton) findViewById(R.id.radioButton8);
        button[8] = (RadioButton) findViewById(R.id.radioButton9);
        button[9] = (RadioButton) findViewById(R.id.radioButton10);
        button[10] = (RadioButton) findViewById(R.id.radioButton11);
        button[11] = (RadioButton) findViewById(R.id.radioButton12);
        button[12] = (RadioButton) findViewById(R.id.radioButton13);
        button[13] = (RadioButton) findViewById(R.id.radioButton14);
        button[14] = (RadioButton) findViewById(R.id.radioButton15);
        button[15] = (RadioButton) findViewById(R.id.radioButton16);
        button[16] = (RadioButton) findViewById(R.id.radioButton17);
        button[17] = (RadioButton) findViewById(R.id.radioButton18);
        button[18] = (RadioButton) findViewById(R.id.radioButton19);
        button[19] = (RadioButton) findViewById(R.id.radioButton20);
        button[20] = (RadioButton) findViewById(R.id.radioButton21);
        button[21] = (RadioButton) findViewById(R.id.radioButton22);
        button[22] = (RadioButton) findViewById(R.id.radioButton23);
        button[23] = (RadioButton) findViewById(R.id.radioButton24);
        button[24] = (RadioButton) findViewById(R.id.radioButton25);
        button[25] = (RadioButton) findViewById(R.id.radioButton26);
        button[26] = (RadioButton) findViewById(R.id.radioButton27);
        button[27] = (RadioButton) findViewById(R.id.radioButton28);
        button[28] = (RadioButton) findViewById(R.id.radioButton29);
        button[29] = (RadioButton) findViewById(R.id.radioButton30);
        button[30] = (RadioButton) findViewById(R.id.radioButton31);
        button[31] = (RadioButton) findViewById(R.id.radioButton32);

        UIHandler.postDelayed(UIRefresh, 16);
    }

    final Runnable UIRefresh = new Runnable() {
        public void run() {
            for(int i = 0; i < 32; i++) {
                if(simCon.button[i]) button[i].setChecked(true);
                else button[i].setChecked(false);
            }
            UIHandler.postDelayed(this, 16);
        }
    };

}
