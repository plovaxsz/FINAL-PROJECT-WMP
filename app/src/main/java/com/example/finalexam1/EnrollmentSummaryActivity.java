package com.example.finalexam1;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class EnrollmentSummaryActivity extends AppCompatActivity {

    private TextView subjectsList, totalCredits;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_summary);

        subjectsList = findViewById(R.id.subjectsList);
        totalCredits = findViewById(R.id.totalCredits);

        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Retrieve the subjects and total credits from Firestore
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("enrollments").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String selectedSubjects = document.getString("selectedSubjects");
                                int total = document.getLong("totalCredits").intValue();

                                // Set the text views to show the subjects and total credits
                                subjectsList.setText(selectedSubjects);
                                totalCredits.setText("Total Credits: " + total);
                            } else {
                                subjectsList.setText("No subjects selected");
                                totalCredits.setText("Total Credits: 0");
                            }
                        } else {
                            Toast.makeText(EnrollmentSummaryActivity.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
