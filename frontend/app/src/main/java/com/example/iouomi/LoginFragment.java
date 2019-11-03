package com.example.iouomi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    //private final String URL = "https://jsonplaceholder.typicode.com/todos/1";
    private final String URL = "http://iouomi.centralus.cloudapp.azure.com:5000/login/";
    private CoordinatorLayout frame;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private Button signupButton;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_login, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        phoneEditText = view.findViewById(R.id.phone_edittext);
        passwordEditText = view.findViewById(R.id.password_edittext);
        loginButton = view.findViewById(R.id.login_button);
        signupButton = view.findViewById(R.id.signup_button);
        frame = getActivity().findViewById(R.id.fragment_container);
        progressBar = view.findViewById(R.id.login_progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneEditText.getText().toString().equals("")
                        || passwordEditText.getText().toString().equals("")) {
                    Snackbar.make(frame, "Both fields are required!",
                            Snackbar.LENGTH_SHORT).show();
                } else {

                    String user = phoneEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    phoneEditText.setText("");
                    passwordEditText.setText("");
                    Snackbar.make(frame, "Logging in...",
                            Snackbar.LENGTH_SHORT).show();
                    //pass in a JSON Object consisting of
                    //phone number and password.

                    JSONObject jsonRequest = new JSONObject();
                    try {
                        jsonRequest.put("phone_number", user);
                        jsonRequest.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("LoginFragment", jsonRequest.toString());
                    volleyJsonObjectRequest(URL, jsonRequest);
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StartActivity) getActivity()).replaceFragment(new SignupFragment());
            }
        });
    }

    public void volleyJsonObjectRequest(String url, JSONObject jsonRequest){

        String  REQUEST_TAG = "com.androidtutorialpoint.volleyJsonObjectRequest";
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, url, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "there was an error", Toast.LENGTH_SHORT).show();
                Log.d("LoginFragment", error.toString());
            }
        });

        jsonObjectReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

}
