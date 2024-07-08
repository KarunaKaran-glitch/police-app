package com.example.police;

import android.widget.ArrayAdapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.police.R;

import java.util.Calendar;

public class page3 extends AppCompatActivity {

    private Spinner districtSpinner;
    private Spinner stationSpinner; // Changed the name to avoid conflicts
    private TextView selectedTime;
    private int hour, minute;
    private TextView date;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page3);

        selectedTime = findViewById(R.id.time);
        selectedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }

            private void showTimePickerDialog() {
                final Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(page3.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                selectedTime.setText("Selected Time: " + String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        date = findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(page3.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        districtSpinner = findViewById(R.id.districts);
        stationSpinner = findViewById(R.id.station); // Changed the name to avoid conflicts

        String[] district = new String[]{"ariyalur", "chengalpattu", "chennai", "coimbatore",
                "cuddalore", "dharmapuri", "dindigul", "erode",  "kanchipuram",
                "kanyakumari", "karur", "krishnagiri", "madurai", "nagapattinam", "namakkal",
                "nilgiris", "perambalur", "pudukkottai", "ramanathapuram", "ranipet", "salem",
                "sivaganga", "tenkasi", "thanjavur", "theni", "thoothukudi (tuticorin)", "tiruchirappalli",
                "tirunelveli", "tirupathur", "tiruppur", "tiruvannamalai", "tiruvarur",
                "vellore", "viluppuram", "virudhunagar"};
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, district);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(stateAdapter);

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

    public void updateDistricts(String district) {
        String[] stations; // Renamed to avoid conflicts
        switch (district.toLowerCase()) { // Convert district to lowercase for case-insensitive comparison
            case "ariyalur":
                stations = new String[]{"Ariyalur Police Station",
                        "Andimadam Police Station",
                        "Udayarpalayam Police Station",
                        "Sendurai Police Station"};
                break;
            case "chengalpattu":
                stations = new String[]{ "Alandur Police Station",
                        "Chromepet Police Station",
                        "Pallavaram Police Station",
                        "Tambaram Police Station"
                };
                break;
            case "chennai":
                stations = new String[]{ "Abiramapuram Police Station",
                        "Anna Nagar Police Station",
                        "Ayanavaram Police Station",
                        "Egmore Police Station",
                        "Flower Bazaar Police Station"};
                break;

            case "coimbatore":
                stations = new String[]{"Coimbatore City Police Station",
                        "Gandhipuram Police Station",
                        "Peelamedu Police Station",
                        "Saravanampatti Police Station",
                        "Ramanathapuram Police Station"
                };

                break;

            case "cuddalore":
                stations = new String[]{"Cuddalore Town Police Station",
                        "Chidambaram Police Station",
                        "Panruti Police Station",
                        "Virudhachalam Police Station",
                        "Neyveli Police Station"
                };

                break;

            case "dharmapuri":
                stations = new String[]{ "Dharmapuri Town Police Station",
                        "Palacode Police Station",
                        "Pennagaram Police Station",
                        "Hosur Police Station",
                        "Harur Police Station"
                };

                break;

            case "dindigul":
                stations = new String[]{"Dindigul Town Police Station",
                        "Oddanchatram Police Station",
                        "Palani Police Station",
                        "Natham Police Station",
                        "Nilakottai Police Station"
                };

                break;

            case "erode":
                stations = new String[]{ "Erode Town Police Station",
                        "Perundurai Police Station",
                        "Bhavani Police Station",
                        "Gobichettipalayam Police Station",
                        "Sathyamangalam Police Station"

                };

                break;

            case "kallakurichi":
                stations = new String[]{"Kallakurichi Town Police Station",
                        "Chinnasalem Police Station",
                        "Sankarapuram Police Station",
                        "Gangavalli Police Station",
                        "Ulundurpet Police Station"

                };

                break;


            case "kancheepuram":
                stations = new String[]{"Kancheepuram Town Police Station",
                        "Chengalpattu Police Station",
                        "Tambaram Police Station",
                        "Sriperumbudur Police Station",
                        "Walajabad Police Station"

                };

                break;

            case "kanyakumari":
                stations = new String[]{ "Kanyakumari Town Police Station",
                        "Nagercoil Town Police Station",
                        "Marthandam Police Station",
                        "Colachel Police Station",
                        "Padmanabhapuram Police Station"

                };

                break;

            case "karur":
                stations= new String[]{ "Karur Town Police Station",
                        "Kulithalai Police Station",
                        "Aravakurichi Police Station",
                        "Kadavur Police Station",
                        "Vangal Police Station"

                };

                break;


            case "krishnagiri":
                stations = new String[]{"Krishnagiri Town Police Station",
                        "Hosur Police Station",
                        "Denkanikottai Police Station",
                        "Uthangarai Police Station",
                        "Bargur Police Station"

                };

                break;

            case "madurai":
                stations= new String[]{ "Madurai City Police Station",
                        "Tallakulam Police Station",
                        "Anna Nagar Police Station",
                        "Thiruparankundram Police Station",
                        "Mettupalayam Police Station"

                };

                break;

            case "nagapattinam":
                stations = new String[]{ "Nagapattinam Town Police Station",
                        "Velankanni Police Station",
                        "Thirukkuvalai Police Station",
                        "Mayiladuthurai Police Station",
                        "Sirkazhi Police Station"

                };

                break;


            case "namakkal":
                stations = new String[]{"Namakkal Town Police Station",
                        "Rasipuram Police Station",
                        "Tiruchengode Police Station",
                        "Paramathi Police Station",
                        "Velur Police Station"

                };

                break;


            case "nilgiris":
                stations= new String[]{"Ooty Police Station",
                        "Coonoor Police Station",
                        "Kotagiri Police Station",
                        "Gudalur Police Station",
                        "Kundah Police Station"

                };

                break;

            case "perambalur":
                stations = new String[]{ "Perambalur Town Police Station",
                        "Veppanthattai Police Station",
                        "Alathur Police Station",
                        "Kunnam Police Station",
                        "Varahur Police Station"

                };

                break;

            case "pudukkotai":
                stations= new String[]{ "Pudukkottai Town Police Station",
                        "Aranthangi Police Station",
                        "Alangudi Police Station",
                        "Annavasal Police Station",
                        "Gandarvakottai Police Station"

                };

                break;

            case "ramanathapuram":
                stations = new String[]{ "Ramanathapuram Town Police Station",
                        "Paramakudi Police Station",
                        "Mudukulathur Police Station",
                        "Kamuthi Police Station",
                        "Rameswaram Police Station"

                };

                break;

            case "ranipet":
                stations = new String[]{    "Ranipet Town Police Station",
                        "Arcot Police Station",
                        "Arakkonam Police Station",
                        "Sholinghur Police Station",
                        "Walajapet Police Station"


                };

                break;


            case "salem":
                stations= new String[]{ "Salem Town Police Station",
                        "Attur Police Station",
                        "Mettur Police Station",
                        "Omalur Police Station",
                        "Yercaud Police Station"

                };

                break;


            case "sivaganga":
                stations = new String[]{    "Sivagangai Town Police Station",
                        "Manamadurai Police Station",
                        "Karaikudi Police Station",
                        "Thirupathur Police Station",
                        "Devakottai Police Station"


                };

                break;


            case "tenkasi":
                stations= new String[]{ "Tenkasi Town Police Station",
                        "Alangulam Police Station",
                        "Sankarankovil Police Station",
                        "Shenkottai Police Station",
                        "Ambasamudram Police Station"

                };

                break;

            case "thanjavur":
                stations = new String[]{ "Thanjavur Town Police Station",
                        "Kumbakonam Police Station",
                        "Pattukkottai Police Station",
                        "Thiruvaiyaru Police Station",
                        "Orathanadu Police Station"

                };

                break;

            case "theni":
                stations = new String[]{ "Theni Town Police Station",
                        "Periyakulam Police Station",
                        "Bodinayakanur Police Station",
                        "Andipatti Police Station",
                        "Cumbum Police Station"

                };

                break;


            case "thoothukudi(tuticorin)":
                stations = new String[]{"Thoothukudi Town Police Station",
                        "Kovilpatti Police Station",
                        "Ettayapuram Police Station",
                        "Sathankulam Police Station",
                        "Tiruchendur Police Station"

                };

                break;


            case "tiruchirapalli":
                stations = new String[]{ "Tiruchirappalli Town Police Station",
                        "Lalgudi Police Station",
                        "Srirangam Police Station",
                        "Thuraiyur Police Station",
                        "Manachanallur Police Station"

                };

                break;

            case "tirunelveli":
                stations = new String[]{"Tirunelveli Town Police Station",
                        "Palayamkottai Police Station",
                        "Ambasamudram Police Station",
                        "Sankarankovil Police Station",
                        "Tenkasi Police Station"

                };

                break;


            case "tirupathur":
                stations = new String[]{ "Thirupathur Town Police Station",
                        "Ambur Police Station",
                        "Vaniyambadi Police Station",
                        "Jolarpet Police Station",
                        "Natrampalli Police Station"

                };

                break;


            case "tiruppur":
                stations = new String[]{ "Tiruppur Town Police Station",
                        "Avanashi Police Station",
                        "Udumalpet Police Station",
                        "Dharapuram Police Station",
                        "Palladam Police Station"

                };

                break;

            case "tiruvallur":
                stations = new String[]{ "Thiruvallur Town Police Station",
                        "Tiruttani Police Station",
                        "Ponneri Police Station",
                        "Gummidipoondi Police Station",
                        "Thiruninravur Police Station"

                };

                break;


            case "tiruvannamalai":
                stations = new String[]{  "Thiruvannamalai Town Police Station",
                        "Chengam Police Station",
                        "Polur Police Station",
                        "Arani Police Station",
                        "Tiruvettipuram Police Station"

                };

                break;


            case "tiruvarur":
                stations = new String[]{ "Thiruvarur Town Police Station",
                        "Mannargudi Police Station",
                        "Nannilam Police Station",
                        "Needamangalam Police Station",
                        "Valangaiman Police Station"

                };
               
                break;



            case "vellore":
                stations= new String[]{ "Vellore Town Police Station",
                        "Katpadi Police Station",
                        "Gudiyatham Police Station",
                        "Arcot Police Station",
                        "Ambur Police Station"

                };

                break;


            case "vilupuram":
                stations = new String[]{"Villupuram Town Police Station",
                        "Tindivanam Police Station",
                        "Vikravandi Police Station",
                        "Ulundurpet Police Station",
                        "Gingee Police Station"

                };

                break;



            case "virudhunagar":
                stations = new String[]{ "Virudhunagar Town Police Station",
                        "Aruppukkottai Police Station",
                        "Sivakasi Police Station",
                        "Rajapalayam Police Station",
                        "Sattur Police Station"

                };
                break;

            default:
                stations = new String[0];
                break;
        }
        setupStationSpinner(stations);
    }

    public void setupStationSpinner(String[] stations) {
        ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stations);
        stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stationSpinner.setAdapter(stationAdapter);
    }
}
