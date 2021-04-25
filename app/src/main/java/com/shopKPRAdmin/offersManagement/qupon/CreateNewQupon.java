package com.shopKPRAdmin.offersManagement.qupon;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.shopKPRAdmin.network.RetrofitClient;
import com.shopKPRAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewQupon extends AppCompatActivity {

    private final String TAG = "Create Coupon";
    private final Calendar myCalendar = Calendar.getInstance();
    private Date StartDate, EndDate;

    private EditText quponDiscount;
    private EditText quponStartDate;
    private EditText quponEndDate;
    private SwitchMaterial quponEnabled;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qupon);

        ImageView backButton = findViewById(R.id.back_to_home);

        quponDiscount = findViewById(R.id.coupon_discount_amount);
        quponStartDate = findViewById(R.id.coupon_start_date);
        quponEndDate = findViewById(R.id.coupon_end_date);
        quponEnabled = findViewById(R.id.enable_coupon_switch);
        ImageView couponStartDatePicker = findViewById(R.id.start_date_picker);
        ImageView couponEndDatePicker = findViewById(R.id.end_date_picker);

        progressDialog = new ProgressDialog(this);

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(1);
        };

        final DatePickerDialog.OnDateSetListener date2 = (view, year, monthOfYear, dayOfMonth) -> {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(2);
        };


        couponStartDatePicker.setOnClickListener(v -> new DatePickerDialog(CreateNewQupon.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        couponEndDatePicker.setOnClickListener(v -> new DatePickerDialog(CreateNewQupon.this, date2, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        backButton.setOnClickListener(v -> onBackPressed());

        Button createQupon = findViewById(R.id.create_coupon_button);
        createQupon.setOnClickListener(v -> {
            validateCouponData();
        });

    }

    private void validateCouponData() {
        if(quponDiscount.getText().toString().isEmpty())
        {
            quponDiscount.setError("Coupon discount can not be empty");
            quponDiscount.requestFocus();
        }
        else if(StartDate == null)
        {
            Toast.makeText(this, "Coupon start date can not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(EndDate == null)
        {
            Toast.makeText(this, "Coupon End date can not be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            saveCouponToDatabase();
        }
    }

    private void saveCouponToDatabase() {

        progressDialog.setTitle("Coupon Creation");
        progressDialog.setMessage("Please wait while we are adding new coupon");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Qupon qupon = new Qupon();

        qupon.setDiscount(Double.parseDouble(quponDiscount.getText().toString()));
        qupon.setEnabled(quponEnabled.isChecked());
        qupon.setQuponEndDate(EndDate);
        qupon.setQuponStartDate(StartDate);

        RetrofitClient.getInstance().getApi()
                .addNewCoupon(qupon)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(CreateNewQupon.this, "Successfully created coupon", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(CreateNewQupon.this, "Can not add new coupon, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e(TAG, t.getMessage());
                        Toast.makeText(CreateNewQupon.this, "Can not add new coupon, please try again", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void updateLabel(int state) {

        String myFormat = "dd/MM/yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        SimpleDateFormat format2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        if(state == 1)
        {
            quponStartDate.setText(sdf.format(myCalendar.getTime()));

            try {
                StartDate = format2.parse(myCalendar.getTime().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        else if(state == 2)
        {
            quponEndDate.setText(sdf.format(myCalendar.getTime()));

            try {
                EndDate = format2.parse(myCalendar.getTime().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


}
