package com.gocreative.tm.lukman;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText phoneV;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phoneV = findViewById(R.id.number);
        Button accept = findViewById(R.id.accept);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                        intent.putExtra("number", number);
                        startActivity(intent);

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = "+993" + phoneV.getText().toString();
                builder.setMessage(number + " belgisi dogrymy").setPositiveButton("Hawa", dialogClickListener)
                        .setNegativeButton("Ýok", dialogClickListener);

                if (number.length() < 12) {
                    Toast.makeText(RegisterActivity.this, "Telefon belgiňiz doly däl!", Toast.LENGTH_SHORT).show();
                }else{
                    builder.show();
                }

            }
        });
    }
}