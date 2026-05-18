package com.example.myapplication9777;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UploadActivity extends AppCompatActivity {

    ImageView uploadImage;
    Button saveButton;

    EditText workerId, workerName, department,
            salary, phoneNumber, shift;

    String imageURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadImage = findViewById(R.id.uploadImage);
        saveButton = findViewById(R.id.saveButton);

        workerId = findViewById(R.id.workerId);
        workerName = findViewById(R.id.workerName);
        department = findViewById(R.id.department);
        salary = findViewById(R.id.salary);
        phoneNumber = findViewById(R.id.phoneNumber);
        shift = findViewById(R.id.shift);

        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {

                                if (result.getResultCode() == Activity.RESULT_OK) {

                                    Intent data = result.getData();

                                    if (data != null) {
                                        uri = data.getData();
                                        uploadImage.setImageURI(uri);
                                    }

                                } else {
                                    Toast.makeText(
                                            UploadActivity.this,
                                            "No Image Selected!",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoPicker = new Intent(Intent.ACTION_PICK);

                photoPicker.setType("image/*");

                activityResultLauncher.launch(photoPicker);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    public void saveData() {

        StorageReference storageReference = FirebaseStorage
                .getInstance()
                .getReference()
                .child("Worker Images")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder =
                new AlertDialog.Builder(UploadActivity.this);

        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);

        AlertDialog dialog = builder.create();

        dialog.show();

        storageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask =
                                taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isComplete());

                        Uri urlImage = uriTask.getResult();

                        imageURL = urlImage.toString();

                        uploadData();

                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        dialog.dismiss();

                        Toast.makeText(
                                UploadActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    public void uploadData() {

        String id = workerId.getText().toString();
        String name = workerName.getText().toString();
        String dept = department.getText().toString();
        String sal = salary.getText().toString();
        String phone = phoneNumber.getText().toString();
        String workerShift = shift.getText().toString();

        DataClass dataClass = new DataClass(
                id,
                name,
                dept,
                sal,
                phone,
                workerShift,
                imageURL
        );

        String currentDate = DateFormat
                .getDateTimeInstance()
                .format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance()
                .getReference("Workers")
                .child(currentDate)
                .setValue(dataClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(
                                    UploadActivity.this,
                                    "Worker Saved Successfully!",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(
                                UploadActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}