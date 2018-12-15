package com.shivedic.foodveda.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.shivedic.foodveda.Activities.AccountVerification;
import com.shivedic.foodveda.Extras.Common;
import com.shivedic.foodveda.Extras.Config;
import com.shivedic.foodveda.Activities.Login;
import com.shivedic.foodveda.MVP.SignUpResponse;
import com.shivedic.foodveda.MVP.UserProfileResponse;
import com.shivedic.foodveda.Activities.MainActivity;
import com.shivedic.foodveda.R;
import com.shivedic.foodveda.Retrofit.Api;
import com.shivedic.foodveda.Activities.SignUp;
import com.shivedic.foodveda.Activities.SplashScreen;
import com.shivedic.foodveda.Volley.Volley_Request;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.shivedic.foodveda.Activities.MainActivity.userId;

public class MyProfile extends Fragment {
public static Button submit,logout;
public static LinearLayout profile,loginn,verifyemail;
public static EditText fullNameEdit, mobEditText,cityEditText,areaEditText,buildingEditText,pincodeEditText, stateEditText, landmarkEditText;
    View view;
   // @BindViews({R.id.fullNameEdt, R.id.mobEditText, R.id.cityEditText, R.id.areaEditText, R.id.buildingEditText, R.id.pincodeEditText, R.id.stateEditText, R.id.landmarkEditText,})
   // List<EditText> editTexts;
   public static UserProfileResponse userProfileResponseData;
   // @BindView(R.id.submitBtn)
    //Button submitBtn;
    //@BindViews({R.id.male, R.id.female})
    //List<CircleImageView> circleImageViews;
    static CircleImageView maleView, femaleView;
    static String gender = "";
   // @BindView(R.id.profileLayout)
    //static LinearLayout profileLayout;
    //@BindView(R.id.loginLayout)
    //LinearLayout loginLayout;
    //@BindView(R.id.logout)
    //Button logout;
public static Context context;
    static SweetAlertDialog pDialog = null;
  //  @BindView(R.id.verifyEmailLayout)
    //static LinearLayout verifyEmailLayout;
static SweetAlertDialog ps1, ps2 = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this, view);
        loginn = (LinearLayout) view.findViewById(R.id.profileLayout);
        profile = (LinearLayout) view.findViewById(R.id.loginLayout);
        verifyemail = (LinearLayout) view.findViewById(R.id.verifyEmailLayout);


        logout = (Button) view.findViewById(R.id.logout);
        submit = (Button) view.findViewById(R.id.submitBtn);

        fullNameEdit = (EditText) view.findViewById(R.id.fullNameEdt);
                mobEditText = (EditText) view.findViewById(R.id.mobEditText);
                cityEditText = (EditText) view.findViewById(R.id.cityEditText);
                        areaEditText = (EditText) view.findViewById(R.id.areaEditText);
        buildingEditText = (EditText) view.findViewById(R.id.buildingEditText);
                pincodeEditText = (EditText) view.findViewById(R.id.pincodeEditText);
        stateEditText = (EditText) view.findViewById(R.id.stateEditText);
                landmarkEditText = (EditText) view.findViewById(R.id.landmarkEditText);

                maleView = (CircleImageView) view.findViewById(R.id.male);
        femaleView = (CircleImageView) view.findViewById(R.id.male);
                // Inflate the layout for this fragment
        MainActivity.title.setText("My Profile");
        if (!userId.equalsIgnoreCase("")) {
            getUserProfileData();
        } else {
            profile.setVisibility(View.INVISIBLE);
            loginn.setVisibility(View.VISIBLE);
        }
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
            }
        });
        return view;
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private static void setUserProfileData() {
        fullNameEdit.setText(userProfileResponseData.getName());
        mobEditText.setText(userProfileResponseData.getMobile());
        cityEditText.setText(userProfileResponseData.getCity());
        areaEditText.setText(userProfileResponseData.getLocality());
        buildingEditText.setText(userProfileResponseData.getFlat());
        pincodeEditText.setText(userProfileResponseData.getPincode());
        stateEditText.setText(userProfileResponseData.getState());
        landmarkEditText.setText(userProfileResponseData.getLandmark());
        try {
            if (userProfileResponseData.getGender().equalsIgnoreCase("Female")) {
                maleView.setImageResource(R.drawable.male_unselect);
                femaleView.setImageResource(R.drawable.female_select);
                gender = "female";
            } else if (userProfileResponseData.getGender().equalsIgnoreCase("male")) {

                maleView.setImageResource(R.drawable.male_select);
                femaleView.setImageResource(R.drawable.female_unselect);
                gender = "male";
            }
        } catch (Exception e) {

        }
    }

    @OnClick({R.id.male, R.id.female, R.id.submitBtn, R.id.logout, R.id.loginNow, R.id.txtSignUp, R.id.verfiyNow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.male:
                maleView.setImageResource(R.drawable.male_select);
                femaleView.setImageResource(R.drawable.female_unselect);
                gender = "male";
                break;
            case R.id.female:
                maleView.setImageResource(R.drawable.male_unselect);
                femaleView.setImageResource(R.drawable.female_select);
                gender = "female";
                break;
            case R.id.submitBtn:
                if (gender.equalsIgnoreCase("")) {
                    Config.showCustomAlertDialog(getActivity(), "Please choose your gender to update your profile", "",
                            SweetAlertDialog.ERROR_TYPE);
                } else if (validate(fullNameEdit)
                        && validate(mobEditText)
                        && validate(cityEditText)
                        && validate(areaEditText)
                        && validate(buildingEditText)
                        && validatePinCode(pincodeEditText)
                        && validate(stateEditText)) {
                    updateProfile();
                }
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.loginNow:
                Config.moveTo(getActivity(), Login.class);
                break;
            case R.id.txtSignUp:
                Config.moveTo(getActivity(), SignUp.class);
                break;

            case R.id.verfiyNow:
                Config.moveTo(getActivity(), AccountVerification.class);
                break;
        }
    }

    private void logout() {

        final SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText("Are you sure you want to logout?");
        alertDialog.setCancelText("Cancel");
        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismissWithAnimation();
            }
        });
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
        btn.setText("Logout");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.saveUserData(getActivity(), "email", "");
                Common.saveUserData(getActivity(), "userId", "");
                Config.moveTo(getActivity(), Login.class);
                getActivity().finishAffinity();

            }
        });
    }

    private boolean validate(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            return true;
        }
        editText.setError("Please Fill This");
        editText.requestFocus();
        return false;
    }

    private boolean validatePinCode(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            if (SplashScreen.restaurantDetailResponseData.getDeliverycity().contains(editText.getText().toString().trim()))
                return true;
            else {
                Config.showPincodeCustomAlertDialog(getActivity(),
                        "Not Available",
                        "We currently don't deliver in your area.",
                        SweetAlertDialog.WARNING_TYPE);
                editText.setError("Not available");
                editText.requestFocus();
                return false;
            }
        }
        editText.setError("Please Fill This");
        editText.requestFocus();
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Config.getCartList(getActivity(), true);
    }


    public void getUserProfileData() {
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        String req = "{\"res_id\":\"" + SplashScreen.resId + "\",\"user_id\":\""+ MainActivity.userId +"\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(getActivity(), getResources().getString(R.string.mJSONURL_userprofile), "POST", "userprofileprofile", req);

        /*
        Api.getClient().getUserProfile("res007",
                MainActivity.userId, new Callback<UserProfileResponse>() {
                    @Override
                    public void success(UserProfileResponse userProfileResponse, Response response) {
                        pDialog.dismiss();
                        try {
                            userProfileResponseData = userProfileResponse;
                            if (userProfileResponse.getSuccess().equalsIgnoreCase("false")) {
                                profileLayout.setVisibility(View.INVISIBLE);
                                verifyEmailLayout.setVisibility(View.VISIBLE);
                            } else
                                setUserProfileData();
                        } catch (Exception e) {
                            Log.d("profileResponse", "NULL");
                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                    }
                });
                */
    }

    public void updateProfile() {
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        String req = "{\"res_id\":\"" + SplashScreen.resId + "\",\"user_id\":\""+ MainActivity.userId  +"\",\"name\":\""+ fullNameEdit.getText().toString().trim() +"\",\"city\":\""+cityEditText.getText().toString().trim()+"\",\"state\":\""+ stateEditText.getText().toString().trim()+"\",\"pincode\":\""+ pincodeEditText.getText().toString().trim() +"\",\"local\":\""+ areaEditText.getText().toString().trim() +"\",\"flat\":\"" + buildingEditText.getText().toString().trim() +"\",\"gender\":\""+gender+"\",\"phone\":\""+mobEditText.getText().toString().trim()+"\",\"landmark\":\""+ landmarkEditText.getText().toString().trim()+"\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(getActivity(), getResources().getString(R.string.mJSONURL_updateprofile), "POST", "updateProfile", req);

        /*
        Api.getClient().updateProfile("res007",
                userId,
                editTexts.get(0).getText().toString().trim(),
                editTexts.get(2).getText().toString().trim(),
                editTexts.get(6).getText().toString().trim(),
                editTexts.get(5).getText().toString().trim(),
                editTexts.get(3).getText().toString().trim(),
                editTexts.get(4).getText().toString().trim(),
                gender,
                editTexts.get(1).getText().toString().trim(),
                editTexts.get(7).getText().toString().trim(),
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        pDialog.dismiss();
                        if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {
                            Config.showCustomAlertDialog(getActivity(),
                                    "Profile Status",
                                    "Profile updated",
                                    SweetAlertDialog.SUCCESS_TYPE);
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                    }
                });
                */
    }

    public static void getUpdateProfileResponse(String response){
        try {
            pDialog.dismiss();
            JSONObject resp = new JSONObject(response);
            if(resp.getString("success").equals("true")){
                Config.showCustomAlertDialog(context,
                        "Profile Status",
                        "Profile updated",
                        SweetAlertDialog.SUCCESS_TYPE);
            } else {
                Toast.makeText(context, "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex ){
            Log.d("myTag", "error : " , ex);
        }
    }

    public static void getUserProfileResponse(String response){
        try {
            pDialog.dismiss();
            JSONObject resp = new JSONObject(response);
                userProfileResponseData = new UserProfileResponse(resp.getString("name"),resp.getString("gender"),resp.getString("mobile"),resp.getString("city"),resp.getString("locality"),resp.getString("flat"),resp.getString("pincode"),resp.getString("state"),resp.getString("landmark"),resp.getString("success"));
                if (resp.getString("success").equalsIgnoreCase("false")) {
                    profile.setVisibility(View.INVISIBLE);
                    verifyemail.setVisibility(View.VISIBLE);
                } else
                    setUserProfileData();

            } catch (Exception ex ){
            Log.d("myTag", "error : " , ex);
        }


    }
}
