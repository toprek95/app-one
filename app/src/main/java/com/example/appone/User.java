package com.example.appone;

import android.net.Uri;

public class User {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String gender;
	private String country;
	private Uri profileImageUri;

	public User(String firstName, String lastName, String email, String password, String gender, String country, Uri profileImageUrl) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.country = country;
		this.profileImageUri = profileImageUrl;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Uri getProfileImageUri() {
		return profileImageUri;
	}

	public void setProfileImageUri(Uri profileImageUri) {
		this.profileImageUri = profileImageUri;
	}

	@Override
	public String toString() {
		return "First name: " + firstName + "\n" +
				"Last name: " + lastName + "\n" +
				"Email: " + email + "\n" +
				"Password: " + password + "\n" +
				"Gender: " + gender + "\n" +
				"Country: " + country + "\n" +
				"Profile image: " + profileImageUri.getPath();
	}
}
