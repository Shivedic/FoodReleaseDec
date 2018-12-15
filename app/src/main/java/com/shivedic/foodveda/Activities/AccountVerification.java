package com.shivedic.foodveda.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shivedic.foodveda.Extras.Config;
import com.shivedic.foodveda.MVP.SignUpResponse;
import com.shivedic.foodveda.R;
import com.shivedic.foodveda.Retrofit.Api;
import com.shivedic.foodveda.Volley.Volley_Request;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AccountVerification extends AppCompatActivity {

    @BindView(R.id.resendEmail)
    Button resendEmail;
    @BindView(R.id.email)
    EditText email;
    static SweetAlertDialog pDialog = null;
static Context mContext;
static Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = AccountVerification.this;
        mActivity = this;
        setContentView(R.layout.activity_account_verification);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.resendEmail, R.id.signUp,R.id.login, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resendEmail:
                if (Config.validateEmail(email,AccountVerification.this))
                resendEmail();
                break;
            case R.id.signUp:
                Config.moveTo(AccountVerification.this, SignUp.class);
                break;
            case R.id.login:
                Config.moveTo(AccountVerification.this, Login.class);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void resendEmail() {
        pDialog = new SweetAlertDialog(AccountVerification.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        String req = "{\"res_id\":\""+ SplashScreen.resId +"\",\"email\":\"" + email.getText().toString().trim() + "\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(AccountVerification.this, getResources().getString(R.string.mJSONURL_resentmail), "POST", "resentemail", req);

        // sending gcm token to server

        /*
        Api.getClient().resentEmail("res007", email.getText().toString().trim(),
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        pDialog.dismiss();
                        Log.d("resendEmailResponse", signUpResponse.getSuccess() + "");
                        Toast.makeText(AccountVerification.this, signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {
                            finish();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });
                */
    }

    public static void resentEmailResponse(String response){
        try{
            JSONObject jObj = new JSONObject(response);
            pDialog.dismiss();
            Log.d("resendEmailResponse", jObj.getString("success") + "");
            Toast.makeText(mContext, jObj.getString("message"), Toast.LENGTH_SHORT).show();
            if (jObj.getString("success").equalsIgnoreCase("true")) {
                mActivity.finish();
            }
        }catch(Exception e){
            Log.d("mytag", "error in AccountVerification : ", e);
        }
    }
}
