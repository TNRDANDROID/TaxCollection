package com.nic.taxcollection.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nic.taxcollection.BuildConfig;
import com.nic.taxcollection.R;
import com.pos.device.printer.PrintCanvas;
import com.pos.device.printer.PrintTask;
import com.pos.device.printer.Printer;
import com.pos.device.printer.PrinterCallback;

import java.text.DecimalFormat;
import java.util.concurrent.CountDownLatch;

public class PaymentActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    RelativeLayout pay_btn,activity_main;
    EditText amount_et;
    ImageView back_img;



    CursorLoader cursorLoader;
    public static String param_status = "false";
    public static String sim_status = "false";
    String package_name = "";


    private Printer printer = null;
    private PrintTask printTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getSupportLoaderManager().initLoader(1, null, this);
        printer = com.pos.device.printer.Printer.getInstance();
        printTask = new PrintTask();
        printTask.setGray(130);

        activity_main = findViewById(R.id.activity_main);


        pay_btn = findViewById(R.id.pay_btn);
        amount_et = findViewById(R.id.amount_et);
        back_img = findViewById(R.id.back_icon);


        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!amount_et.getText().toString().equals("")){

                    visibleGone();
                    getBitmapFromView(activity_main);
                }
                else {
                    Toast.makeText(PaymentActivity.this,"Please Enter Amount",Toast.LENGTH_SHORT).show();
                }

                //onClickPayCard();
            }
        });
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

    public void onClickPayCard() {
        if (sim_status.equalsIgnoreCase("true")) {
            if (param_status.equalsIgnoreCase("true")) {
                String amt = amount_et.getText().toString();
                if (amt.isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();

                } else {
                    String txn_type = "SALE";
                    functionPaybyCard(amt, txn_type);
                }
            } else {
                Toast.makeText(PaymentActivity.this, "Please do terminal activation", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PaymentActivity.this, "Please configure Wifi or GPRS", Toast.LENGTH_SHORT).show();
        }
    }
    public String getTransactionId() {
        long number = (long) Math.floor(Math.random() * 900000L) + 100000L;
        String transaction_id = addTxnNo();
        return transaction_id;
    }

    public String addTxnNo() {
        int currenNo = 1;
        int nextNo = (currenNo + 1) % 1000000;
        return String.format("%06d", nextNo);
    }

    public String getConvertDoubleval(String amount) {
        Log.e("tag", "print amount" + amount);
        if (amount.length() == 0) {
            amount = "0";
        }
        DecimalFormat formatter = new DecimalFormat("#0.00");
        double d = Double.parseDouble(amount);
        amount = formatter.format(d);
        Log.e("tag", "print new amount" + amount);
        return amount;
    }

    // Sale Transaction api
    public void functionPaybyCard(String amt, String txn_type) {
        amt = getConvertDoubleval(amt);
        String transaction_id = getTransactionId();
        package_name = BuildConfig.APPLICATION_ID;
        String receipt = "YES";
        String CUSTOM_ACTION = "cn.desert.newpos.payui.master.YOUR_ACTION";
        Intent i = new Intent();
        i.setAction(CUSTOM_ACTION);
        i.putExtra("amount", amt);// Transaction Amount
        i.putExtra("result_mode", true);// Always true
        i.putExtra("action", "inApp");// InApp indication
        i.putExtra("txn_type", txn_type);// Mode of transaction
        i.putExtra("transaction_id", transaction_id); // Reference Id
        i.putExtra("package", package_name);
        i.putExtra("receipt", receipt);
        i.putExtra("receipt_print", "YES");// YES or NO
        startActivityForResult(i, 600);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (requestCode == 600) {
                    if (data.hasExtra("result_code")) {
                        boolean datas = data.getBooleanExtra("result_code", false);
                        if (data.getBooleanExtra("result_code", false)) {
                            String resp_data = data.getStringExtra("resp_data");

                        } else {
                            Toast.makeText(getApplicationContext(), data.getStringExtra("message"), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Empty result", Toast.LENGTH_LONG).show();
                    }
                } else if (requestCode == 601) {
                    if (data.hasExtra("result_code")) {
                        boolean datas = data.getBooleanExtra("result_code", false);
                        if (data.getBooleanExtra("result_code", false)) {
                            String resp_data = data.getStringExtra("resp_data");
                            String resp_dat = data.getStringExtra("resp_data");
                        } else {
                            Toast.makeText(getApplicationContext(), data.getStringExtra("message"), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), data.getStringExtra("message"), Toast.LENGTH_LONG).show();
                    }
                } else if (requestCode == 611) {
                    if (data.hasExtra("result_code")) {
                        boolean datas = data.getBooleanExtra("result_code", false);
                        if (data.getBooleanExtra("result_code", false)) {
                            String resp_data = data.getStringExtra("resp_data");
                            String resp = data.getStringExtra("resp_data");
                        } else {
                            Toast.makeText(getApplicationContext(), data.getStringExtra("message"), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), data.getStringExtra("message"), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Data null", Toast.LENGTH_LONG).show();
            }
        } else
            Toast.makeText(getApplicationContext(), "Result Failed", Toast.LENGTH_LONG).show();
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
    public void onPrintClick(String amount) {
        PrintCanvas canvas = new PrintCanvas();
        Paint paint = new Paint();


        setFontStyle(paint, 2, false);
        Bitmap Icon_smc = BitmapFactory.decodeResource(getResources(), R.drawable.tn_logo_new);

        canvas.drawBitmap(Icon_smc, paint);
        setFontStyle(paint, 1, true);
        canvas.drawText("Transaction Amount : " + amount, paint);
        canvas.drawText("Transaction Description : " + "Success", paint);
        canvas.drawText("Transaction Description : " + "Success", paint);
        canvas.drawLine(0,0,0,0,paint);


        printData(canvas);

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

    public void functionConfigure(String tid) {

        package_name = BuildConfig.APPLICATION_ID;
        String CUSTOM_ACTION = "com.example.menusample.YOUR_ACTION_CONFIG";
        Intent i = new Intent();
        i.setAction(CUSTOM_ACTION);
        i.putExtra("tid", tid);
        i.putExtra("action", "inApp");
        i.putExtra("package", package_name);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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

    private void visibleGone(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                onPrintClick("100");
            }
        }, 2000);
    }
}
