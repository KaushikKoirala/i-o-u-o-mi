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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupFragment extends Fragment {

    //private final String URL = "https://jsonplaceholder.typicode.com/todos/1";
    private final String URL = "http://iouomi.centralus.cloudapp.azure.com:5000/hello";
    private CoordinatorLayout frame;
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private Button confirmButton;
    private ProgressBar signupProgress;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_signup, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        nameEditText = view.findViewById(R.id.name_edittext);
        phoneEditText = view.findViewById(R.id.phone_edittext);
        passwordEditText = view.findViewById(R.id.password_edittext);
        confirmEditText = view.findViewById(R.id.confirm_edittext);
        confirmButton = view.findViewById(R.id.confirm_button);
        signupProgress = view.findViewById(R.id.signup_progress);
        frame = getActivity().findViewById(R.id.fragment_container);


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = nameEditText.getText().toString();
                String phoneStr = phoneEditText.getText().toString();
                String passwordStr = passwordEditText.getText().toString();
                String confirmStr = confirmEditText.getText().toString();
                if (nameStr.equals("") || phoneStr.equals("") ||
                        passwordStr.equals("") || confirmStr.equals("")) {
                    Snackbar.make(frame, "All fields are required!",
                            Snackbar.LENGTH_SHORT).show();
                } else if (!passwordStr.equals(confirmStr)) {
                    Snackbar.make(frame, "Passwords do not match!",
                            Snackbar.LENGTH_SHORT).show();
                } else {
                    nameEditText.setText("");
                    phoneEditText.setText("");
                    passwordEditText.setText("");
                    confirmEditText.setText("");
                    Snackbar.make(frame, "Signing up....",
                            Snackbar.LENGTH_SHORT).show();
                    JSONObject jsonRequest = new JSONObject();
                    try {
                        jsonRequest.put("phone_number", phoneStr);
                        jsonRequest.put("password", passwordStr);
                        jsonRequest.put("name", nameStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    volleyJsonObjectRequest(URL, jsonRequest);

                }
            }
        });
    }

    public void volleyJsonObjectRequest(String url, JSONObject jsonRequest){

        String  REQUEST_TAG = "com.androidtutorialpoint.volleyJsonObjectRequest";
        signupProgress.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, url, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        signupProgress.setVisibility(View.INVISIBLE);
                        Snackbar.make(frame, "Account created. Please login",
                                Snackbar.LENGTH_SHORT).show();
                        signupProgress.setVisibility(View.INVISIBLE);
                        getActivity().onBackPressed();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                signupProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "there was an error", Toast.LENGTH_SHORT).show();
                Log.d("SigninFragment", error.toString());
            }
        });

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }


}
