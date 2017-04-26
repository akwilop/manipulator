package rev1.simcon;

import android.app.PendingIntent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;

import java.nio.ByteBuffer;

public class _simCon {
    private static _simCon ourInstance = new _simCon();

    public static _simCon getInstance() {
        return ourInstance;
    }

    private _simCon() {
    }

    public int USB_PERMISSION = USB_PERMISSON_FALSE;
    public boolean SAVE_FLAG = false;

    public PendingIntent mPermissionIntent;
    public UsbManager mUsbManager;
    public UsbDevice device;
    public UsbRequest mUsbRequest = new UsbRequest();

    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;
    public static final int RX = 3;
    public static final int RY = 4;
    public static final int RZ = 5;
    public static final int SL = 6;
    public static final int DI = 7;

    public static final int USB_PERMISSON_FALSE = 2;
    public static final int USB_PERMISSON_PENDING = 1;
    public static final int USB_PERMISSON_TRUE = 0;

    UsbInterface intf;
    UsbEndpoint inEndpoint;
    UsbEndpoint outEndpoint;
    UsbDeviceConnection connection;

    public byte[] inReport = new byte[22];

    public short[] logic = new short[8];
    public short[] adc = new short[8];
    public short[] min = new short[8];
    public short[] cent = new short[8];
    public short[] max = new short[8];
    public short[] filt = new short[8];
    public boolean[] mod = new boolean[8];
    public boolean[] enable = new boolean[8];
    public boolean[] invert = new boolean[8];
    public boolean[] button= new boolean[32];

    public short ID;

    public void decodeReport() {

        logic[_simCon.X]  = (short) (((inReport[1] & 0x03) << 8) | (inReport[0] & 0xFF));
        logic[_simCon.Y]  = (short) (((inReport[2] & 0x0F) << 6) | ((inReport[1] & 0xFC) >> 2));
        logic[_simCon.Z]  = (short) (((inReport[3] & 0x3F) << 4) | ((inReport[2] & 0xF0) >> 4));
        logic[_simCon.RX] = (short) (((inReport[4] & 0xFF) << 2) | ((inReport[3] & 0xC0) >> 6));
        logic[_simCon.RY] = (short) (((inReport[6] & 0x03) << 8) | (inReport[5] & 0xFF));
        logic[_simCon.RZ] = (short) (((inReport[7] & 0x0F) << 6) | ((inReport[6] & 0xFC) >> 2));
        logic[_simCon.SL] = (short) (((inReport[8] & 0x3F) << 4) | ((inReport[7] & 0xF0) >> 4));
        logic[_simCon.DI] = (short) (((inReport[9] & 0xFF) << 2) | ((inReport[8] & 0xC0) >> 6));

        for(short i = 0; i < 8; i++) {
            button[i]      = ((inReport[10] & (0x01 << i)) > 0);
            button[i + 8]  = ((inReport[11] & (0x01 << i)) > 0);
            button[i + 16] = ((inReport[12] & (0x01 << i)) > 0);
            button[i + 24] = ((inReport[13] & (0x01 << i)) > 0);
        }

        ID = (short) (inReport[15] & 0x07);
        enable[ID] = ((inReport[15] & (0x01 << 3)) > 0);
        invert[ID] = ((inReport[15] & (0x01 << 4)) > 0);
        mod[ID]    = ((inReport[15] & (0x01 << 5)) > 0);
        adc[ID]  = (short) (((inReport[17] & 0x03) << 8) | (inReport[16] & 0xFF));
        min[ID]  = (short) (((inReport[18] & 0x0F) << 6) | ((inReport[17] & 0xFC) >> 2));
        cent[ID]  = (short) (((inReport[19] & 0x3F) << 4) | ((inReport[18] & 0xF0) >> 4));
        max[ID] = (short) (((inReport[20] & 0xFF) << 2) | ((inReport[19] & 0xC0) >> 6));
        filt[ID] = (short) (inReport[21] & 0xFF);
    }

    public void sendReport(int ID, int CODE, int VAL) {
        if ((ID >= 0) && (ID < 8) && (CODE >= 0) && (CODE < 8)) {
            if(VAL < 0) VAL = 0;
            if(VAL > 1023) VAL = 1023;
            ByteBuffer outReport = ByteBuffer.allocate(2);
            byte[] message = new byte[2];
            message[0] = (byte) (((VAL & 0x300) >> 2) | ((CODE << 3) | ID));
            message[1] = (byte) (VAL & 0xFF);
            outReport.put(message);
            mUsbRequest.initialize(connection, outEndpoint);
            mUsbRequest.queue(outReport, outReport.capacity());
            if((CODE == 7)&&(VAL == 3)) {}
            else if ((CODE == 7)&&(VAL == 4)) SAVE_FLAG = false;
            else SAVE_FLAG = true;
        }
    }
}