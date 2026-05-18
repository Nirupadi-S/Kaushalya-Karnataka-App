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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateActivity extends AppCompatActivity {

    ImageView updateImage;

    Button updateButton;

    EditText updateWorkerId, updateWorkerName,
            updateDepartment, updateSalary,
            updatePhone, updateShift;

    String imageUrl, workerId, workerName,
            department, salary, phone,
            shift, key, oldImageUrl;

    Uri uri;

    DatabaseReference databaseReference;

    StorageReference storageReference;

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateImage = findViewById(R.id.updateImage);

        updateButton = findViewById(R.id.updateButton);

        updateWorkerId = findViewById(R.id.updateStdRegNo);

        updateWorkerName = findViewById(R.id.updateStdName);

        updateDepartment = findViewById(R.id.updateStdAge);

        updateSalary = findViewById(R.id.updateStdGender);

        updatePhone = findViewById(R.id.updateStdMobNo);

        updateShift = findViewById(R.id.updateStdParentNo);

        activityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {

                                if (result.getResultCode() == Activity.RESULT_OK) {

                                    Intent data = result.getData();

                                    uri = data.getData();

                                    updateImage.setImageURI(uri);

                                    storageReference =
                                            FirebaseStorage.getInstance()
                                                    .getReference()
                                                    .child("Worker Images")
                                                    .child(uri.getLastPathSegment());

                                } else {

                                    Toast.makeText(
                                            UpdateActivity.this,
                                            "No Image Selected!",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        });

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            updateWorkerName.setText(
                    bundle.getString("WorkerName")
            );

            updateWorkerId.setText(
                    bundle.getString("WorkerId")
            );

            updateDepartment.setText(
                    bundle.getString("Department")
            );

            updateSalary.setText(
                    bundle.getString("Salary")
            );

            updatePhone.setText(
                    bundle.getString("PhoneNumber")
            );

            updateShift.setText(
                    bundle.getString("Shift")
            );

            key = bundle.getString("Key");

            oldImageUrl = bundle.getString("Image");

            Glide.with(UpdateActivity.this)
                    .load(oldImageUrl)
                    .into(updateImage);
        }

        databaseReference =
                FirebaseDatabase.getInstance()
                        .getReference("Workers")
                        .child(key);

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoPicker =
                        new Intent(Intent.ACTION_PICK);

                photoPicker.setType("image/*");

                activityResultLauncher.launch(photoPicker);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveData();

                Intent intent =
                        new Intent(
                                UpdateActivity.this,
                                MainViewActivity.class
                        );

                startActivity(intent);
            }
        });
    }

    public void saveData() {

        if (uri != null) {

            storageReference =
                    FirebaseStorage.getInstance()
                            .getReference()
                            .child("Worker Images")
                            .child(uri.getLastPathSegment());

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(UpdateActivity.this);

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

                            imageUrl = urlImage.toString();

                            updateData();

                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            dialog.dismiss();
                        }
                    });

        } else {

            imageUrl = oldImageUrl;

            updateData();
        }
    }

    public void updateData() {

        workerName =
                updateWorkerName.getText().toString().trim();

        workerId =
                updateWorkerId.getText().toString().trim();

        department =
                updateDepartment.getText().toString().trim();

        salary =
                updateSalary.getText().toString().trim();

        phone =
                updatePhone.getText().toString().trim();

        shift =
                updateShift.getText().toString().trim();

        DataClass dataClass =
                new DataClass(
                        workerId,
                        workerName,
                        department,
                        salary,
                        phone,
                        shift,
                        imageUrl
                );

        databaseReference.setValue(dataClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(
                                    UpdateActivity.this,
                                    "Worker Updated!",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(
                                UpdateActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}