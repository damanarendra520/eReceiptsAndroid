package com.narendradama.ereceipts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ReceiptsDetails extends Activity {

    private Toolbar mTopToolbar;

    public ImageView shareReceipt;

    public ImageView closeDetails;

    String receiptNumber = "";
    String date = "";
    String place = "";
    String amount = "";
    String saving = "";

    Map<String, Integer> map1;

    public ReceiptsDetails() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_details_view);

        Intent intent = getIntent();
        HashMap<String, String> hashMap = (HashMap<String, String>)intent.getSerializableExtra("ReceiptData");
        for (Map.Entry me : hashMap.entrySet()) {

            if (me.getKey().equals("receiptNumber"))
                receiptNumber =(String) me.getValue().toString();
            if (me.getKey().equals("date"))
                date =(String) me.getValue().toString();
            if (me.getKey().equals("place"))
                place =(String) me.getValue().toString();
            if (me.getKey().equals("amount"))
                amount =(String) me.getValue().toString();
            if (me.getKey().equals("savings"))
                saving =(String) me.getValue().toString();
        }


        TextView receiptNumberTextField = (TextView)findViewById(R.id.receiptNUmberTextField);
        receiptNumberTextField.setText(receiptNumber);

        TextView dateTextField = (TextView) findViewById(R.id.dateTextField);
        dateTextField.setText(date);

        TextView placeTextField = (TextView) findViewById(R.id.placeTextField);
        placeTextField.setText(place);

        TextView amountTextField = (TextView) findViewById(R.id.amountTextField);
        amountTextField.setText(amount);

        TextView savingsTextField = (TextView) findViewById(R.id.savingsTextField);
        savingsTextField.setText(saving);

        shareReceipt = (ImageView) findViewById(R.id.shareReceiptAction);
        shareReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Hi here are you're selected receipt details\n \nReceipt#:"+receiptNumber+"\n Date:"+date+"\n Place:"+place+" \n Amount:"+amount+"\n Savings:"+saving+" \n \nPlease find the attached bill eReceipt \n From Team eReceipt";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Bill From eReceipts");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                sharingIntent.putExtra(Intent.EXTRA_MIME_TYPES, "text/plain");
                startActivity(Intent.createChooser(sharingIntent, "Share Options"));
            }
        });

        closeDetails = (ImageView) findViewById(R.id.closeDetailsView);
        closeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void shareReceipt(View view){
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = "Hi \n Please find the attached bill eReceipt \n From Team eReceipt";
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Bill From eReceipts");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }

}
