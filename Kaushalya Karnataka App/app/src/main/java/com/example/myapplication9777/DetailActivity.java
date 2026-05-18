package com.example.myapplication9777;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    TextView detailName, detailWorkerId,
            detailDepartment, detailSalary,
            detailPhone, detailShift;

    ImageView detailImage;

    FloatingActionButton deleteBtn, editBtn;

    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailName = findViewById(R.id.detailName);
        detailWorkerId = findViewById(R.id.detialRegNo);
        detailDepartment = findViewById(R.id.detialAge);
        detailSalary = findViewById(R.id.detialGender);
        detailPhone = findViewById(R.id.detialContact);
        detailShift = findViewById(R.id.detialParentNo);

        detailImage = findViewById(R.id.detailImage);

        deleteBtn = findViewById(R.id.deleteBtn);
        editBtn = findViewById(R.id.editBtn);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            detailName.setText(bundle.getString("WorkerName"));

            detailWorkerId.setText(bundle.getString("WorkerId"));

            detailDepartment.setText(bundle.getString("Department"));

            detailSalary.setText(bundle.getString("Salary"));

            detailPhone.setText(bundle.getString("PhoneNumber"));

            detailShift.setText(bundle.getString("Shift"));

            key = bundle.getString("Key");

            imageUrl = bundle.getString("Image");

            Glide.with(this)
                    .load(bundle.getString("Image"))
                    .into(detailImage);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference reference =
                        FirebaseDatabase.getInstance()
                                .getReference("Workers");

                FirebaseStorage storage =
                        FirebaseStorage.getInstance();

                StorageReference storageReference =
                        storage.getReferenceFromUrl(imageUrl);

                storageReference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                reference.child(key).removeValue();

                                Toast.makeText(
                                        DetailActivity.this,
                                        "Worker Deleted!",
                                        Toast.LENGTH_SHORT
                                ).show();

                                startActivity(
                                        new Intent(
                                                getApplicationContext(),
                                                MainViewActivity.class
                                        )
                                );

                                finish();
                            }
                        });
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =
                        new Intent(DetailActivity.this,
                                UpdateActivity.class)

                                .putExtra(
                                        "WorkerName",
                                        detailName.getText().toString()
                                )

                                .putExtra(
                                        "WorkerId",
                                        detailWorkerId.getText().toString()
                                )

                                .putExtra(
                                        "Department",
                                        detailDepartment.getText().toString()
                                )

                                .putExtra(
                                        "Salary",
                                        detailSalary.getText().toString()
                                )

                                .putExtra(
                                        "PhoneNumber",
                                        detailPhone.getText().toString()
                                )

                                .putExtra(
                                        "Shift",
                                        detailShift.getText().toString()
                                )

                                .putExtra(
                                        "Image",
                                        imageUrl
                                )

                                .putExtra(
                                        "Key",
                                        key
                                );

                startActivity(intent);
            }
        });
    }
}