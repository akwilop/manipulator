package rev1.simcon;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class menu extends AppCompatActivity {

    private _simCon simCon = _simCon.getInstance();
    public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private TextView status;
    private ProgressBar bar;
    private ImageView image;
    private Handler USBCheckHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        simCon.mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        simCon.mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbReceiver, filter);

        status = (TextView) findViewById(R.id.status);
        bar = (ProgressBar) findViewById(R.id.bar);
        image = (ImageView) findViewById(R.id.plug);

        USBCheckHandler.postDelayed(USBCheck, 1000);
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

    private void openMenuAxis() {
        Intent openMenuAxis = new Intent(this, menuAxis.class);
        openMenuAxis.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(openMenuAxis);
    }

    private void openMenu() {
        Intent openMenu = new Intent(this, menu.class);
        openMenu.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(openMenu);
    }

    private boolean simCon_detect(int VID, int PID) {

        HashMap<String, UsbDevice> deviceList = simCon.mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            if((device.getVendorId() == VID)&&(device.getProductId() == PID)) {
                simCon.device = device;
                return true;
            }
        }
        return false;
    }



    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){
                            simCon.USB_PERMISSION = _simCon.USB_PERMISSON_TRUE;

                            simCon.intf = device.getInterface(0);
                            simCon.inEndpoint = simCon.intf.getEndpoint(0);
                            simCon.outEndpoint = simCon.intf.getEndpoint(1);
                            simCon.connection = simCon.mUsbManager.openDevice(simCon.device);
                            simCon.connection.claimInterface(simCon.intf, true);

                            new Thread(new Runnable() {
                                public void run() {
                                    while(simCon.USB_PERMISSION == _simCon.USB_PERMISSON_TRUE) {
                                        simCon.connection.bulkTransfer(simCon.inEndpoint, simCon.inReport, simCon.inReport.length, 0);
                                        simCon.decodeReport();
                                    }
                                    return;
                                }
                            }).start();

                            openMenuAxis();
                        }
                    }
                    else {
                        simCon.USB_PERMISSION = _simCon.USB_PERMISSON_FALSE;
                    }
                }
            }
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    simCon.USB_PERMISSION = _simCon.USB_PERMISSON_FALSE;
                    simCon.connection.releaseInterface(simCon.intf);
                    simCon.connection.close();
                    openMenu();
                }
            }
        }
    };

    final Runnable USBCheck = new Runnable() {
        public void run() {
            if(simCon.USB_PERMISSION == _simCon.USB_PERMISSON_FALSE) {
                image.setVisibility(View.VISIBLE);
                bar.setVisibility(View.VISIBLE);
                status.setVisibility(View.VISIBLE);
                if(simCon_detect(1155, 22352)) {
                    simCon.USB_PERMISSION = _simCon.USB_PERMISSON_PENDING;
                    image.setVisibility(View.INVISIBLE);
                    bar.setVisibility(View.INVISIBLE);
                    status.setVisibility(View.INVISIBLE);
                    simCon.mUsbManager.requestPermission(simCon.device, simCon.mPermissionIntent);
                }
            }
            USBCheckHandler.postDelayed(this, 1000);
        }
    };

}
