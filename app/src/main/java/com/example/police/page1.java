package com.example.police;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class page1 extends AppCompatActivity {

    private AppCompatSpinner spinnerGender;

    private AppCompatSpinner villageSpinner;
    private AppCompatSpinner districtSpinner;

    public AppCompatSpinner compliantSpinner;

    private FirebaseFirestore db;
    private FirebaseDatabase database;

    private Button map, register;
    private TextInputEditText addressEditText;

    private TextView birth, complaintId;

    private TextView occurs;
    private int mYear, mMonth, mDay;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int LOCATION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page1);

        register = findViewById(R.id.register);
        complaintId = findViewById(R.id.complaintId);

        db = FirebaseFirestore.getInstance();

        register.setOnClickListener(v -> addDataToFirestoreDB());


        database = FirebaseDatabase.getInstance();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        map = findViewById(R.id.map);
        addressEditText = findViewById(R.id.editText);

        map.setOnClickListener(v -> getLastKnownLocation());

        compliantSpinner = findViewById(R.id.subject);

        villageSpinner = findViewById(R.id.villages);
        ArrayAdapter<CharSequence> compliantAdapter = ArrayAdapter.createFromResource(this,
                R.array.subject_array, android.R.layout.simple_spinner_item);
        compliantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        compliantSpinner.setAdapter(compliantAdapter);

        compliantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedSubject = parent.getItemAtPosition(position).toString();
                    Toast.makeText(page1.this, "Selected: " + selectedSubject, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button button = findViewById(R.id.camera);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }

            private void dispatchTakePictureIntent() {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Toast.makeText(page1.this, "Error creating file", Toast.LENGTH_SHORT).show();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(page1.this,
                                "com.example.police.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }

            private File createImageFile() throws IOException {
                // Create an image file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                // Save a file: path for use with ACTION_VIEW intents
                String currentPhotoPath = image.getAbsolutePath();
                return image;
            }
        });

        spinnerGender = findViewById(R.id.gender);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedGender = parent.getItemAtPosition(position).toString();
                    Toast.makeText(page1.this, "Selected: " + selectedGender, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        occurs = findViewById(R.id.occur);
        occurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(page1.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                occurs.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        birth = findViewById(R.id.birth);

        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(page1.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                birth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

//        Button takePhotoButton = findViewById(R.id.camera);
//
//        takePhotoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dispatchTakePictureIntent();
//            }
//
//            private void dispatchTakePictureIntent() {
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }
//            }
//        });

        districtSpinner = findViewById(R.id.districts);

        ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(this,
                R.array.district_array, android.R.layout.simple_spinner_item);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(districtAdapter);

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedGender = parent.getItemAtPosition(position).toString();
                    Toast.makeText(page1.this, "Selected: " + selectedGender, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String[] district = new String[]{"ariyalur", "chengalpattu", "chennai", "coimbatore",
                "cuddalore", "dharmapuri", "dindigul", "erode", "kanchipuram",
                "kanyakumari", "karur", "krishnagiri", "madurai", "nagapattinam", "namakkal",
                "nilgiris", "perambalur", "pudukkottai", "ramanathapuram", "ranipet", "salem",
                "sivaganga", "tenkasi", "thanjavur", "theni", "thoothukudi (tuticorin)", "tiruchirappalli",
                "tirunelveli", "tirupathur", "tiruppur", "tiruvannamalai", "tiruvarur",
                "vellore", "viluppuram", "virudhunagar"
        };

        // Setting up the array adapter for states
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, district);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(stateAdapter);

        // Set listener for state selection
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDistricts(district[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private void addDataToFirestoreDB() {
        db = FirebaseFirestore.getInstance();
        PoliceData newComplaint = new PoliceData(
                compliantSpinner.getSelectedItem().toString(),
                spinnerGender.getSelectedItem().toString(),
                districtSpinner.getSelectedItem().toString(),
                villageSpinner.getSelectedItem().toString(),
                occurs.getText().toString(),
                birth.getText().toString(),
                Objects.requireNonNull(addressEditText.getText()).toString(),
                "N/A"
        );

        db.collection("/complaints").add(newComplaint).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                complaintId.setText("Save your complaint ID for your future reference: "  + documentReference.getId());
                Toast.makeText(page1.this, documentReference.getId(), Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(page1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateDistricts(String district) {
        // Assuming each state has a predefined list of districts
        String[] villages;
        switch (district) {
            case "ariyalur":
                villages = new String[]{"Ameenabath", "Ariyalur (N)", "Ariyalur (S)", "Ayan Athur", "Chennivanam", "Govindapuram", "Iluppaiyur", "Kadaugur", "Kairlabath", "Kallankurichi", "Kavanoor", "Ottakoil", "Periyanagalur", "Pottaveli", "Rayampuram", "Thelur", "Valaja Nagaram", "Vilankudi", "Elakkurichi", "Alagiyamanavalam", "Chinnapattakadu", "Elakurichi", "Kamarasavalli", "Keelakolathur", "Kovilur", "Kuruvadi", "Sullankudi", "Thuthur", "Vilupanakurichi", "Keelapalur", "Ayansuthamalli", "Karaiyavetti", "Keelakavattankurichi", "Keelapalur",
                        "Kelaiyur", "Mallur", "Melapalur", "Palinganatham", "Parpanacherri", "Poondi", "Sannavur(N)", "Sannavur(S)", "Sathamnagalam", "Varanavasi"};
                setupvillageSpinner(villages);

            case "chengalpattu":
                villages = new String[]{"Chengalpattu", "Cheyyur",
                        "Madurantakam", "Tambaram", "Thiruporur", "Uthiramerur"
                };
                setupvillageSpinner(villages);
                break;
            case "chennai":
                villages = new String[]{"Alandur", "Ambattur", "Aminjikarai", "Ayanavaram",
                        "Egmore", "Guindy", "Madhavaram", "Madhuravoyal", "Mambalam", "Mylapore",
                        "Perambur", "Purasavakkam", "Sholinganallur", "Thiruvottriyur", "Tondiarpet",
                        "Velacherry"};
                setupvillageSpinner(villages);
                break;

            case "coimbatore":
                villages = new String[]{"Aanaimalai", "Annur", "Coimbatore(North)",
                        "Coimbatore(South)", "Kinathukadavu", "Madukarai", "Mettupalayam",
                        "Perur", "Pollachi", "Sulur", "Valparai"
                };
                setupvillageSpinner(villages);
                break;

            case "cuddalore":
                villages = new String[]{"Cuddalore", "Bhuvanagiri", "Chidambaram",
                        "Kattumannarkoil", "Kurinjipadi", "Panruti", "Srimushnam",
                        "Thittakudi", "Veppur", "Virudhachalam"
                };
                setupvillageSpinner(villages);
                break;

            case "dharmapuri":
                villages = new String[]{"Dharmapuri", "Harur", "Karimangalam",
                        "Nallampalli", "Palacode", "Pappireddipatti", "Pennagaram"
                };
                setupvillageSpinner(villages);
                break;

            case "dindigul":
                villages = new String[]{"Atthur", "Dindigul", "Dindigul (East)", "Dindigul (West)",
                        "Guziliyamparai", "Kodaikanal", "Natham", "Nilakottai", "Oddanchatram", "Palani",
                        "Vedasandur"
                };
                setupvillageSpinner(villages);
                break;

            case "erode":
                villages = new String[]{"Erode", "Anthiyur", "Bhavani", "Gobichettipalayam",
                        "Kodumudi", "Modakurichi", "Nambiyur", "Perundurai", "Sathiyamangalam", "Thalavadi"

                };
                setupvillageSpinner(villages);
                break;

            case "kallakurichi":
                villages = new String[]{"Kallakurichi", "Sankarapuram",
                        "Tirukkoyilur", "Ulundurpet", "Chinnasalem", "Kalvarayan Hills"

                };
                setupvillageSpinner(villages);
                break;


            case "kancheepuram":
                villages = new String[]{"Kancheepuram", "Kundrathur", "Sriperumbudur",
                        "Uthiramerur", "Walajabad"

                };
                setupvillageSpinner(villages);
                break;

            case "kanyakumari":
                villages = new String[]{"Agasteeswaram", "Kalkulam", "Killiyoor",
                        "Thiruvattar", "Thovalai", "Vilavancode"

                };
                setupvillageSpinner(villages);
                break;

            case "karur":
                villages = new String[]{"Aravakurichi", "Karur", "Kadavur", "Krishnarayapuram", "Kulithalai", "Manmangalam", "Pugalur"

                };
                setupvillageSpinner(villages);
                break;


            case "krishnagiri":
                villages = new String[]{"Anjetty", "Bargur", "Denkanikottai",
                        "Hosur", "Krishnagiri", "Pochampalli", "Shoolagiri", "Thally",
                        "Uthangarai", "Veppanapalli"

                };
                setupvillageSpinner(villages);
                break;

            case "madurai":
                villages = new String[]{"Madurai East", "Madurai West", "Melur",
                        "Peraiyur", "Thirumangalam", "Thiruparankundram", "Usilampatti", "Vadipatti"

                };
                setupvillageSpinner(villages);
                break;

            case "nagapattinam":
                villages = new String[]{"Kilvelur", "Nagapattinam",
                        "Thirukkuvalai", "Thirumarugal", "Tharangambadi", "Vedaranyam"

                };
                setupvillageSpinner(villages);
                break;


            case "namakkal":
                villages = new String[]{"Kolli Hills", "Kumarapalayam",
                        "Mohanoor", "Namakkal", "Paramathi Velur", "Rasipuram",
                        "Sendamangalam", "Tiruchengode"

                };
                setupvillageSpinner(villages);
                break;


            case "nilgiris":
                villages = new String[]{"Coonoor", "Gudalur", "Kotagiri", "Pandalur", "Udhagamandalam"

                };
                setupvillageSpinner(villages);
                break;

            case "perambalur":
                villages = new String[]{"Kunnam", "Perambalur", "Veppanthattai"

                };
                setupvillageSpinner(villages);
                break;

            case "pudukkotai":
                villages = new String[]{"Alangudi", "Aranthangi", "Avudaiyarkoil", "Gandarvakottai", "Iluppur", "Karambakudi", "Kulathur", "Manamelkudi", "Ponnamaravathi", "Pudukkottai", "Thirumayam"

                };
                setupvillageSpinner(villages);
                break;

            case "ramanathapuram":
                villages = new String[]{"Kamuthi", "Mudukulathur", "Paramakudi", "Ramanathapuram", "Rameswaram", "Tiruvadanai"

                };
                setupvillageSpinner(villages);
                break;

            case "ranipet":
                villages = new String[]{"Arakkonam", "Arcot", "Ranipet", "Sholingur", "Tirupattur", "Walajah"

                };
                setupvillageSpinner(villages);
                break;


            case "salem":
                villages = new String[]{"Attur", "Edappadi", "Gangavalli", "Mettur", "Omalur", "Salem", "Sankagiri", "Taramangalam", "Valapady", "Yercaud"

                };
                setupvillageSpinner(villages);
                break;


            case "sivaganga":
                villages = new String[]{"Devakottai", "Ilayangudi", "Karaikudi", "Manamadurai", "Sivaganga", "Tirupathur"

                };
                setupvillageSpinner(villages);
                break;


            case "tenkasi":
                villages = new String[]{"Alangulam", "Ambasamudram", "Kadayanallur", "Sankarankoil", "Shenkottai", "Tenkasi", "Vasudevanallur"

                };
                setupvillageSpinner(villages);
                break;

            case "thanjavur":
                villages = new String[]{"Budalur", "Kumbakonam", "Orathanadu", "Papanasam", "Pattukkottai", "Peravurani", "Thanjavur", "Thiruvaiyaru", "Tiruvonam"

                };
                setupvillageSpinner(villages);
                break;

            case "theni":
                villages = new String[]{"Andipatti", "Bodinayakanur", "Periyakulam", "Theni"

                };
                setupvillageSpinner(villages);
                break;


            case "thoothukudi(tuticorin)":
                villages = new String[]{"Ettayapuram", "Kovilpatti", "Ottapidaram", "Sathankulam", "Srivaikuntam", "Thoothukudi", "Tiruchendur", "Vilathikulam"

                };
                setupvillageSpinner(villages);
                break;


            case "tiruchirappalli":
                villages = new String[]{"Lalgudi", "Manachanallur", "Manapparai", "Musiri", "Srirangam", "Thiruverumbur", "Tiruchirappalli East", "Tiruchirappalli West", "Thuraiyur"

                };
                setupvillageSpinner(villages);
                break;

            case "tirunelveli":
                villages = new String[]{"Alangulam", "Ambasamudram", "Cheranmahadevi", "Kadayam", "Kalakkadu", "Nanguneri", "Palayamkottai", "Radhapuram", "Sankarankoil", "Shenkottai", "Sivagiri", "Tenkasi", "Tirunelveli", "Vegetable Market"

                };
                setupvillageSpinner(villages);
                break;


            case "tirupathur":
                villages = new String[]{"Jolarpet", "Natrampalli", "Tirupathur", "Vaniyambadi"

                };
                setupvillageSpinner(villages);
                break;


            case "tiruppur":
                villages = new String[]{"Avanashi", "Dharapuram", "Kangayam", "Palladam", "Tiruppur", "Udumalaipettai", "Vellakoil"

                };
                setupvillageSpinner(villages);
                break;

            case "tiruvallur":
                villages = new String[]{"Ambattur", "Gummidipoondi", "Ponneri", "Poonamallee", "Tiruttani", "Tiruvallur", "Uthukkottai"

                };
                setupvillageSpinner(villages);
                break;


            case "tiruvannamalai":
                villages = new String[]{"Arani", "Chengam", "Cheyyar", "Kalasapakkam", "Polur", "Tiruvannamalai", "Vandavasi"

                };
                setupvillageSpinner(villages);
                break;


            case "tiruvarur":
                villages = new String[]{"Kodavasal", "Mannargudi", "Nannilam", "Needamangalam", "Thiruthuraipoondi", "Thiruvarur", "Valangaiman"

                };
                setupvillageSpinner(villages);
                break;


            case "vellore":
                villages = new String[]{"Anaicut", "Arakonam", "Arcot", "Gudiyatham", "Katpadi", "Kilvaithinankuppam", "Sholinghur", "Tirupathur", "Vaniyambadi", "Vellore", "Walajapet"

                };
                setupvillageSpinner(villages);
                break;


            case "vilupuram":
                villages = new String[]{"Gingee", "Kallakurichi", "Sankarapuram", "Tindivanam", "Tirukkoyilur", "Ulundurpet", "Vanur", "Vikravandi", "Villupuram"

                };
                setupvillageSpinner(villages);
                break;


            case "virudhunagar":
                villages = new String[]{"Athoor", "Aruppukkottai", "Kariapatti", "Rajapalayam", "Sattur", "Sivakasi", "Srivilliputhur", "Tiruchuli", "Vembakottai", "Virudhunagar"

                };
                setupvillageSpinner(villages);
                break;


            default:
                villages = new String[]{};
                setupvillageSpinner(villages);
                break;
        }
    }

    public void setupvillageSpinner(String[] villages) {
        ArrayAdapter<String> villageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, villages);
        villageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        villageSpinner.setAdapter(villageAdapter);
    }



    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            String currentLocation = getAddressFromLocation(location);
                            addressEditText.setText(currentLocation);
                        } else {
                            Toast.makeText(page1.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    // Method to store data in Firebase Realtime Database
    private void storeDataInRealtimeDatabase(PoliceData policeData) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("police_data");
        ref.push().setValue(policeData);
    }

    // OnClick event handling
//    public void onClick(View v) {
//        // Retrieve input values
//        String subject = compliantSpinner.getSelectedItem() != null ? compliantSpinner.getSelectedItem().toString() : "";
//        String gender = spinnerGender.getSelectedItem() != null ? spinnerGender.getSelectedItem().toString() : "";
//        String district = districtSpinner.getSelectedItem() != null ? districtSpinner.getSelectedItem().toString() : "";
//        String village = villageSpinner.getSelectedItem() != null ? villageSpinner.getSelectedItem().toString() : "";
//        String incidentDate = occurs.getText().toString();
//        String dateOfBirth = birth.getText().toString();
//        String address = addressEditText.getText().toString();
//        String imageUri  /* initialize imageUri here */ = null;
//
//        // Check if all fields are filled
//        if (!subject.isEmpty() && !gender.isEmpty() && !district.isEmpty() && !village.isEmpty() && !incidentDate.isEmpty() && !dateOfBirth.isEmpty() && !address.isEmpty() && imageUri != null) {
//            // Create a new PoliceData object
//            PoliceData policeData = new PoliceData(subject, gender, district, village, new Date(incidentDate), new Date(dateOfBirth), address, imageUri);
//
//            // Store data in Firebase
//            storeDataInRealtimeDatabase(policeData);
//        } else {
//            // Show error message if any field is empty
//            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//        }
//    }


    private String getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
            assert addresses != null;
            if (!addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            return e.getMessage();
        }
        return "Error finding your address!";
    }
}
