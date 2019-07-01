package com.narendradama.ereceipts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.narendradama.ereceipts.Database.DBHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AddBills extends Activity {
    int from_Where_I_Am_Coming = 0;
    private DBHelper mydb ;

    TextView receiptNumber ;
    TextView date;
    TextView amount;
    TextView place;
    TextView savings;
    int id_To_Update = 0;

    ImageView cancelButton;

    ImageView saveBillButton;

    Button pickReceipt;

    ImageView pickedImage;

    private EditText receiptNumberEditText;
    private EditText dateEditText;
    private EditText amontEditText;
    private EditText placeEditText;
    private EditText savingsEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bills_add);

        mydb = new DBHelper(AddBills.this);
        receiptNumberEditText = (EditText) findViewById(R.id.receiptNumberTextView);
        dateEditText = (EditText) findViewById(R.id.editDate);
        amontEditText = (EditText) findViewById(R.id.editAmount);
        placeEditText = (EditText) findViewById(R.id.editPlace);
        savingsEditText = (EditText) findViewById(R.id.editSavings);

        //pickedImage = (ImageView) findViewById(R.id.pickedReceiptView);
        saveBillButton = (ImageView) findViewById(R.id.save_bill);

        saveBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(receiptNumberEditText.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(),"Something went wrong, Please fill all the details", Toast.LENGTH_LONG).show();
                }else{
                    try{
                        Boolean isitTrue = mydb.insertReceipt(receiptNumberEditText.getText().toString(), dateEditText.getText().toString(), amontEditText.getText().toString(), placeEditText.getText().toString(), savingsEditText.getText().toString());
                        if (isitTrue) {
                            finish();
                        }
                    }catch(Exception e){
                        e.getStackTrace();
                    }
                }



            }
        });
        cancelButton = (ImageView) findViewById(R.id.cancel_add_bill);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void selectImage(){
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddBills.this);
        builder.setTitle("Add Receipt!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    pickedImage.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                pickedImage.setImageURI(selectedImage);
            }
        }
    }

    private byte[] imageViewToByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
