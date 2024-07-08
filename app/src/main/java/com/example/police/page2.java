package com.example.police;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class page2 extends AppCompatActivity {

    private TextInputEditText complaintNumberET;
    private Button getDetailsBtn;
    private FirebaseFirestore db;
    private TextView complaintStatusTV, addressTV, dateOfBirthTV, genderTV, villageTV, subjectTV, incidentDateTV, districtTV, imageUriTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);
        db = FirebaseFirestore.getInstance();

        complaintNumberET = findViewById(R.id.complaintNumberET);
        getDetailsBtn = findViewById(R.id.getDetails);
        complaintStatusTV = findViewById(R.id.complaintStatusTV);
        addressTV = findViewById(R.id.addressTV);
        dateOfBirthTV = findViewById(R.id.dateOfBirthTV);
        genderTV = findViewById(R.id.genderTV);
        villageTV = findViewById(R.id.villageTV);
        subjectTV = findViewById(R.id.subjectTV);
        incidentDateTV = findViewById(R.id.incidentDateTV);
        districtTV = findViewById(R.id.districtTV);
        imageUriTV = findViewById(R.id.imageUriTV);

        getDetailsBtn.setOnClickListener(v -> fetchComplaintDetails());
    }

    private void fetchComplaintDetails() {
        String complaintNumber = Objects.requireNonNull(complaintNumberET.getText()).toString().trim();

        if (complaintNumber.isEmpty()) {
            Toast.makeText(this, "Enter a valid complaint number!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("complaints").document(complaintNumber)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(page2.this, "Error fetching details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (value != null && value.exists()) {
                            // Handle the document snapshot
                            Toast.makeText(page2.this, "Complaint details fetched!", Toast.LENGTH_SHORT).show();
                            PoliceData existingComplaint = value.toObject(PoliceData.class);
                            assert existingComplaint != null;
                            complaintStatusTV.setText(existingComplaint.toString());
                            addressTV.setText(existingComplaint.getAddress());
                            dateOfBirthTV.setText(existingComplaint.getDateOfBirth());
                            genderTV.setText(existingComplaint.getGender());
                            villageTV.setText(existingComplaint.getVillage());
                            subjectTV.setText(existingComplaint.getSubject());
                            incidentDateTV.setText(existingComplaint.getIncidentDate());
                            districtTV.setText(existingComplaint.getDistrict());
                            imageUriTV.setText(existingComplaint.getImageUri());
                        } else {
                            Toast.makeText(page2.this, "No such complaint found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
