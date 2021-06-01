package com.example.appone;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;


public class MainActivity extends AppCompatActivity {

	private static final int PICK_PHOTO_FOR_AVATAR = 1;
	private static final String DEFAULT_LOCAL = "Bosnia & Herzegovina";
	private ImageView userImage;
	private Button rotateImageButton, registerButton, pickImageButton;
	private Bitmap bitmap;
	private CheckBox termsConditionsCheckBox;
	private TextInputLayout passwordLayout, confirmPasswordLayout, emailLayout, firstNameLayout, lastNameLayout;
	private TextInputEditText password, confirmPassword, email, firstName, lastName;
	private RadioGroup genderRadioGroup;
	private Spinner countries;

	private RelativeLayout parentRelativeLayout;

	private String gender = "-1";
	private String selectedCountry = DEFAULT_LOCAL;
	ArrayList<User> users = new ArrayList<>();
	private Uri profileImageUri = Uri.EMPTY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initFields();

		setDefaultImage();

		final ArrayAdapter<String> adapter = new ArrayAdapter<>(
				this,
				android.R.layout.simple_spinner_dropdown_item,
				getListOfCountries().toArray(new String[0])
		);

		countries.setAdapter(adapter);
		countries.setSelection(adapter.getPosition(DEFAULT_LOCAL));

		countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selectedCountry = adapter.getItem(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		pickImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pickImage();
			}
		});

		rotateImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rotateImage();
			}
		});

		genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.male_radio_button:
						gender = "Male";
						break;
					case R.id.female_radio_button:
						gender = "Female";
						break;
					case R.id.other_radio_button:
						gender = "Other";
						break;
					default:
						gender = "-1";
						break;
				}
			}
		});

		confirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId== EditorInfo.IME_ACTION_DONE){
					confirmPassword.clearFocus();
					confirmPasswordLayout.clearFocus();
				}
				return false;
			}
		});

		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (firstName.getText().toString().isEmpty()) {
					firstNameLayout.setError("First name can't be empty!");
				} else if (lastName.getText().toString().isEmpty()) {
					lastNameLayout.setError("Last name can't be empty!");
				} else if (!isValidEmail(email.getText().toString().trim())) {
					emailLayout.setError("Invalid email address!");
				} else if (password.getText().toString().length() < 6) {
					passwordLayout.setError("Must be longer than 6 characters!");
				} else if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
					confirmPasswordLayout.setError("Confirm password doesn't match!");
				} else if (gender.equals("-1")) {
					Toast.makeText(MainActivity.this, "Please select gender.", Toast.LENGTH_SHORT).show();
				} else if (!termsConditionsCheckBox.isChecked()) {
					termsConditionsCheckBox.setError("You must agree to Terms and Conditions!");
					Toast.makeText(MainActivity.this, "You must agree to Terms and Conditions!", Toast.LENGTH_SHORT).show();
				} else {

					users.add(new User(
							firstName.getText().toString(),
							lastName.getText().toString(),
							email.getText().toString(),
							password.getText().toString(),
							gender,
							selectedCountry,
							profileImageUri
					));

					showSnackbar(users.size()-1);

				}
			}
		});

		removeWarnings();
	}

	private SortedSet<String> getListOfCountries() {
		final SortedSet<String> countriesList = new TreeSet<>();
		for (Locale locale : Locale.getAvailableLocales()) {
			if (!TextUtils.isEmpty(locale.getDisplayCountry())) {
				countriesList.add(locale.getDisplayCountry());
			}
		}
		return countriesList;
	}

	private void setDefaultImage() {

		Glide.with(MainActivity.this)
				.asBitmap()
				.load(R.mipmap.ic_launcher)
				.apply(RequestOptions.circleCropTransform())
				.into(MainActivity.this.userImage);
	}

	private void initFields() {

		parentRelativeLayout = findViewById(R.id.parent_relative_layout);

		userImage = findViewById(R.id.user_image);

		pickImageButton = findViewById(R.id.pick_image_button);

		rotateImageButton = findViewById(R.id.rotate_image_button);
		rotateImageButton.setVisibility(View.GONE);

		termsConditionsCheckBox = findViewById(R.id.terms_conditions_checkbox);

		registerButton = findViewById(R.id.register_button);

		passwordLayout = findViewById(R.id.password_layout);

		confirmPasswordLayout = findViewById(R.id.confirm_password_layout);

		emailLayout = findViewById(R.id.email_layout);

		firstNameLayout = findViewById(R.id.first_name_layout);

		lastNameLayout = findViewById(R.id.last_name_layout);

		genderRadioGroup = findViewById(R.id.gender_radio_group);

		confirmPassword = findViewById(R.id.confirm_password_edit_text);

		password = findViewById(R.id.password_edit_text);

		email = findViewById(R.id.email_edit_text);

		firstName = findViewById(R.id.first_name_edit_text);

		lastName = findViewById(R.id.last_name_edit_text);

		countries = findViewById(R.id.countries_spinner);
	}

	private void showSnackbar(int position) {
		Snackbar snackbar = Snackbar.make(this.parentRelativeLayout, "User created: \n" + users.get(position).toString(), Snackbar.LENGTH_INDEFINITE)
				.setAction("OK", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						emptyEntries();
					}
				})
				.setAnchorView(R.id.register_button);

		View snackbarView = snackbar.getView();
		TextView snackTextView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

		snackTextView.setMaxLines(8);
		snackbar.show();

	}

	private void emptyEntries() {
		lastName.setText(null);
		email.setText(null);
		password.setText(null);
		confirmPassword.setText(null);
		firstName.setText(null);
		genderRadioGroup.clearCheck();
		termsConditionsCheckBox.setChecked(false);

		Glide.with(MainActivity.this)
				.asBitmap()
				.load(R.mipmap.ic_launcher)
				.apply(RequestOptions.circleCropTransform())
				.into(MainActivity.this.userImage);
		profileImageUri = Uri.EMPTY;
		gender = "-1";
		selectedCountry = DEFAULT_LOCAL;
		bitmap = BitmapFactory.decodeResource(this.getResources(),
				R.mipmap.ic_launcher);
		rotateImageButton.setVisibility(View.GONE);

	}

	public static boolean isValidEmail(CharSequence target) {
		return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
	}

	private void removeWarnings() {

		termsConditionsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					termsConditionsCheckBox.setError(null);
				}
			}
		});

		password.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				passwordLayout.setError(null);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});


		confirmPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				confirmPasswordLayout.setError(null);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		email.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				emailLayout.setError(null);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		firstName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				firstNameLayout.setError(null);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		lastName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				lastNameLayout.setError(null);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	public void rotateImage() {

		Matrix matrix = new Matrix();
		matrix.postRotate(90);

		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

		Glide.with(MainActivity.this)
				.asBitmap()
				.load(bitmap)
				.apply(RequestOptions.circleCropTransform())
				.into(MainActivity.this.userImage);
	}

	public void pickImage() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == MainActivity.RESULT_OK) {
			if (data == null) {
				Toast.makeText(MainActivity.this, "Image not selected", Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				InputStream inputStream = MainActivity.this.getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
				if (inputStream == null) {
					Toast.makeText(MainActivity.this, "Error selecting image", Toast.LENGTH_SHORT).show();
					return;
				}
				bitmap = BitmapFactory.decodeStream(inputStream);

				profileImageUri = data.getData();

				Glide.with(MainActivity.this)
						.asBitmap()
						.load(bitmap)
						.apply(RequestOptions.circleCropTransform())
						.into(MainActivity.this.userImage);

				MainActivity.this.rotateImageButton.setVisibility(View.VISIBLE);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		rotateImageButton.setVisibility(View.GONE);
	}
}
