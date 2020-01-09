package com.example.projetpam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.net.Proxy.Type.HTTP;

public class Contact extends AppCompatActivity {

    static final int PICK_CONTACT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Button btnChooseContact = (Button) findViewById(R.id.btnChooseContact);

    }

    public void onClick(View v) {
        if(v.getId() == R.id.btnChooseContact) {
            Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();
                String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                Cursor cursor = getContentResolver().query(contactUri, projection,null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                    String number = cursor.getString(numberIndex);
                    String name = cursor.getString(nameIndex);
                    EditText edNumber = (EditText) findViewById(R.id.edNumero);
                    edNumber.setText(number);
                }

            }
        }
    }

    public void composeMmsMessage(View view) {
        EditText edNumero = (EditText) findViewById(R.id.edNumero);
        String number = edNumero.getText().toString();

        SmsManager.getDefault().sendTextMessage(number, null, "Acceptez vous le rdv ?", null, null);
        Toast successMessage = Toast.makeText(getApplicationContext(),getResources().getString(R.string.success_message) + number, Toast.LENGTH_LONG);
        successMessage.setGravity(Gravity.BOTTOM,0,0);
        successMessage.show();
    }
}
