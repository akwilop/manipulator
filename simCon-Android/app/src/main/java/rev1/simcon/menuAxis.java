package rev1.simcon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class menuAxis extends AppCompatActivity {

    private _simCon simCon = _simCon.getInstance();
    private ProgressBar[] prog = new ProgressBar[8];
    private Handler UIHandler = new Handler();
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_axis);
        prog[0] = (ProgressBar) findViewById(R.id.progressX);
        prog[1] = (ProgressBar) findViewById(R.id.progressY);
        prog[2] = (ProgressBar) findViewById(R.id.progressZ);
        prog[3] = (ProgressBar) findViewById(R.id.progressRX);
        prog[4] = (ProgressBar) findViewById(R.id.progressRY);
        prog[5] = (ProgressBar) findViewById(R.id.progressRZ);
        prog[6] = (ProgressBar) findViewById(R.id.progressSL);
        prog[7] = (ProgressBar) findViewById(R.id.progressDI);
        prog[0].setMax(1023);
        prog[1].setMax(1023);
        prog[2].setMax(1023);
        prog[3].setMax(1023);
        prog[4].setMax(1023);
        prog[5].setMax(1023);
        prog[6].setMax(1023);
        prog[7].setMax(1023);
        saveButton = (Button) findViewById(R.id.buttonSave);

        UIHandler.postDelayed(UIRefresh, 16);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    final Runnable UIRefresh = new Runnable() {
        public void run() {
            for(int i = 0; i < 8; i++) {
                prog[i].setProgress(simCon.logic[i]);
            }
            if(simCon.SAVE_FLAG) saveButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            else saveButton.getBackground().clearColorFilter();
            UIHandler.postDelayed(this, 16);
        }
    };

    public void buttonClick(View view) {
        Intent intent = new Intent(this, axis.class);
        switch(view.getId()) {
            case R.id.buttonX:
                intent.putExtra("name", getResources().getString(R.string.textX));
                intent.putExtra("axisID", 0);
                startActivity(intent);
                break;
            case R.id.buttonY:
                intent.putExtra("name", getResources().getString(R.string.textY));
                intent.putExtra("axisID", 1);
                startActivity(intent);
                break;
            case R.id.buttonZ:
                intent.putExtra("name", getResources().getString(R.string.textZ));
                intent.putExtra("axisID", 2);
                startActivity(intent);
                break;
            case R.id.buttonRX:
                intent.putExtra("name", getResources().getString(R.string.textRX));
                intent.putExtra("axisID", 3);
                startActivity(intent);
                break;
            case R.id.buttonRY:
                intent.putExtra("name", getResources().getString(R.string.textRY));
                intent.putExtra("axisID", 4);
                startActivity(intent);
                break;
            case R.id.buttonRZ:
                intent.putExtra("name", getResources().getString(R.string.textRZ));
                intent.putExtra("axisID", 5);
                startActivity(intent);
                break;
            case R.id.buttonSL:
                intent.putExtra("name", getResources().getString(R.string.textSL));
                intent.putExtra("axisID", 6);
                startActivity(intent);
                break;
            case R.id.buttonDI:
                intent.putExtra("name", getResources().getString(R.string.textDI));
                intent.putExtra("axisID", 7);
                startActivity(intent);
                break;
        }
    }

    public void buttonBclick(View view) {
        Intent intent = new Intent(this, buttonStat.class);
        startActivity(intent);
    }

    public void buttonSaveClick(View view) {
        simCon.sendReport(0, 7, 4);
    }

    public void buttonDefaultClick(View view) {
        simCon.sendReport(0, 7, 5);
    }
}
