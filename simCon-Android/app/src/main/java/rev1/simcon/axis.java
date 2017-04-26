package rev1.simcon;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class axis extends AppCompatActivity {

    private _simCon simCon = _simCon.getInstance();
    private TextView name, textLogical, textAdc, textMin, textCent, textMax;
    private ProgressBar barLogical, barAdc;
    private CheckBox checkEn, checkInv, checkMod;
    private EditText editMin, editCent, editMax, editFilt;
    private Bundle parameter;
    private Handler UIHandler = new Handler();
    private int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_axis);

        name = (TextView) findViewById(R.id.name);
        textLogical = (TextView) findViewById(R.id.textLogical);
        textAdc = (TextView) findViewById(R.id.textAdc);
        textMin = (TextView) findViewById(R.id.textMin);
        textCent = (TextView) findViewById(R.id.textCent);
        textMax = (TextView) findViewById(R.id.textMax);
        barLogical = (ProgressBar) findViewById(R.id.barLogical);
        barAdc = (ProgressBar) findViewById(R.id.barAdc);
        barLogical.setMax(1023);
        barAdc.setMax(1023);
        checkEn = (CheckBox) findViewById(R.id.checkEn);
        checkInv = (CheckBox) findViewById(R.id.checkInv);
        checkMod = (CheckBox) findViewById(R.id.checkMod);
        editMin = (EditText) findViewById(R.id.editMin);
        editCent = (EditText) findViewById(R.id.editCent);
        editMax = (EditText) findViewById(R.id.editMax);
        editFilt = (EditText) findViewById(R.id.editFilt);

        parameter = getIntent().getExtras();
        name.setText(parameter.getString("name"));
        ID = parameter.getInt("axisID");
        simCon.sendReport(ID, 7, 3);

        UIHandler.postDelayed(UIRefresh, 16);

        editMin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        simCon.sendReport(ID, 0, Integer.parseInt(editMin.getText().toString()));
                    } catch (NumberFormatException e) {}
                    editMin.setText("");
                    return true;
                }
                return false;
            }
        });

        editCent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        simCon.sendReport(ID, 1, Integer.parseInt(editCent.getText().toString()));
                    } catch (NumberFormatException e) {}
                    editCent.setText("");
                    return true;
                }
                return false;
            }
        });

        editMax.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        simCon.sendReport(ID, 2, Integer.parseInt(editMax.getText().toString()));
                    } catch (NumberFormatException e) {}
                    editMax.setText("");
                    return true;
                }
                return false;
            }
        });

        editFilt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        simCon.sendReport(ID, 3, Integer.parseInt(editFilt.getText().toString()));
                    } catch (NumberFormatException e) {}
                    editFilt.setText("");
                    return true;
                }
                return false;
            }
        });

    }

    final Runnable UIRefresh = new Runnable() {
        public void run() {
            textLogical.setText(getResources().getString(R.string.logical) + " " + simCon.logic[ID]);
            barLogical.setProgress(simCon.logic[ID]);
            textAdc.setText(getResources().getString(R.string.adc) + " " + simCon.adc[ID]);
            barAdc.setProgress(simCon.adc[ID]);
            textMin.setText("" + simCon.min[ID]);
            textMax.setText("" + simCon.max[ID]);
            checkEn.setChecked(simCon.enable[ID]);
            checkInv.setChecked(simCon.invert[ID]);
            checkMod.setChecked(simCon.mod[ID]);
            editFilt.setHint("FILTER: " + simCon.filt[ID]);
            if(simCon.mod[ID]) {
                textCent.setVisibility(View.VISIBLE);
                textCent.setEnabled(true);
                editCent.setVisibility(View.VISIBLE);
                editCent.setEnabled(true);
                textCent.setText("" + simCon.cent[ID]);
            } else {
                textCent.setVisibility(View.INVISIBLE);
                textCent.setEnabled(false);
                editCent.setVisibility(View.INVISIBLE);
                editCent.setEnabled(false);
            }
            UIHandler.postDelayed(this, 16);
        }
    };

    public void sendEnable(View view) {
        simCon.sendReport(ID, 7, 0);
    }

    public void sendInvert(View view) {
        simCon.sendReport(ID, 7, 1);
    }

    public void sendMod(View view) {
        simCon.sendReport(ID, 7, 2);
    }
}
