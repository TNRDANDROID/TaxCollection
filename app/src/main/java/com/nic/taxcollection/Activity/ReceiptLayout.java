package com.nic.taxcollection.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nic.taxcollection.R;
import com.pos.device.printer.PrintCanvas;
import com.pos.device.printer.PrintTask;
import com.pos.device.printer.Printer;
import com.pos.device.printer.PrinterCallback;

import java.util.concurrent.CountDownLatch;

public class ReceiptLayout extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private Printer printer = null;
    private PrintTask printTask = null;
    CursorLoader cursorLoader;
    public static String param_status = "false";
    public static String sim_status = "false";
    String package_name = "";
    LinearLayout content_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_layout);

        getSupportLoaderManager().initLoader(1, null, this);
        printer = com.pos.device.printer.Printer.getInstance();
        printTask = new PrintTask();
        printTask.setGray(130);

        content_layout = findViewById(R.id.content_layout);

        onPrintClick();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        cursorLoader = new CursorLoader(this, Uri.parse("content://com.example.menusample.provider.InAppProvider/cte"), null, null, null, null);
        return cursorLoader;
    }

    @SuppressLint("Range")
    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        try {
            cursor.moveToFirst();
            StringBuilder res = new StringBuilder();
            while (!cursor.isAfterLast()) {
                sim_status = cursor.getString(cursor.getColumnIndex("sim_status"));
                param_status = cursor.getString(cursor.getColumnIndex("param_status"));
                cursor.moveToNext();
            }
        } catch (NullPointerException ne) {

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {

    }

    public void onPrintClick() {
        PrintCanvas canvas = new PrintCanvas();
        Paint paint = new Paint();


        setFontStyle(paint, 2, false);
        Bitmap Icon_smc = getBitmapFromView(content_layout);

        canvas.drawBitmap(Icon_smc, paint);
        /*setFontStyle(paint, 1, true);
        canvas.drawText("Transaction Amount : " + amount, paint);
        canvas.drawText("Transaction Description : " + "Success", paint);
        canvas.drawText("Transaction Description : " + "Success", paint);
        canvas.drawLine(0,0,0,0,paint);*/


        printData(canvas);

    }
    private void setFontStyle(Paint paint, int size, boolean isBold) {
        if (isBold) {
            Typeface MONOSPACE_BOLD = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD);
            paint.setTypeface(MONOSPACE_BOLD);

        } else {
            paint.setTypeface(Typeface.MONOSPACE);

        }
        switch (size) {
            case 0:
                break;
            case 1:
                paint.setTextSize(16F);
                break;
            case 2:
                paint.setTextSize(22F);
                break;
            case 3:
                paint.setTextSize(30F);
                break;
            case 4:
                paint.setTextSize(14F);
                break;
            case 5:
                paint.setTextSize(18F);
                break;
            case 6:
                paint.setTextSize(19F);
                break;
            default:
                break;
        }
    }
    public static byte[] draw2PxPoint(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int square = height * width;

        int[] pixels = new int[square];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        byte[] data = new byte[square >> 3];

        int B = 0, b = 0;
        byte[] bits = {(byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};
        for (int i = 0; i < square; i++) {
            if (pixels[i] < -7829368) {//- 0x888888
                data[B] |= bits[b];
            }

            if (b == 7) {
                b = 0;
                B++;
            } else {
                b++;
            }
        }
        return data;
    }

    private int printData(PrintCanvas pCanvas) {
        final CountDownLatch latch = new CountDownLatch(1);
        int ret = printer.getStatus();

        if (ret == Printer.PRINTER_OK) {
            byte[] result = draw2PxPoint(pCanvas.getBitmap());

            printTask.setPrintBuffer(result);
            printer.startPrint(printTask, new PrinterCallback() {
                @Override
                public void onResult(int i, PrintTask printTask) {

                    Log.v("ResultControl", "onResult 3 : " + i);
                    if (i == -3) {
                        showDialoguePaper();
                    } else {

                    }
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Please insert paper roll", Toast.LENGTH_LONG).show();

        }
        return ret;
    }


    public void showDialoguePaper() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Dialog dialog = new Dialog(getApplicationContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_customerdialog);
                    TextView text = (TextView) dialog.findViewById(R.id.textDialog);

                    text.setText("Please insert paper roll");
                    // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
                    dialog.show();
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    TextView declineButton = (TextView) dialog.findViewById(R.id.declineButton);
                    TextView textview_no = (TextView) dialog.findViewById(R.id.textview_no);
                    textview_no.setVisibility(View.GONE);
                    declineButton.setText("OK");
                    textview_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                        }
                    });
                    declineButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });
                }


            });
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
}
