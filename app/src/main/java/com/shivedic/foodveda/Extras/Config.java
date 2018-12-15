package com.shivedic.foodveda.Extras;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
/*
import com.abhiandroid.foodordering.Adapters.CartListAdapter;
import com.abhiandroid.foodordering.Fragments.ChoosePaymentMethod;
import com.abhiandroid.foodordering.Fragments.MyCartList;*/
import com.shivedic.foodveda.Activities.Login;
import com.shivedic.foodveda.Activities.MainActivity;
import com.shivedic.foodveda.Activities.SignUp;
import com.shivedic.foodveda.Activities.SplashScreen;
import com.shivedic.foodveda.Adapter.CartListAdapter;
import com.shivedic.foodveda.Fragments.ChoosePaymentMethod;
import com.shivedic.foodveda.Fragments.MyCartList;
import com.shivedic.foodveda.Fragments.MyProfile;
import com.shivedic.foodveda.Fragments.PincodeList;
import com.shivedic.foodveda.MVP.CartistResponse;
import com.shivedic.foodveda.MVP.SignUpResponse;
import com.shivedic.foodveda.MVP.UserProfileResponse;
import com.shivedic.foodveda.PaymentIntegrationMethods.OrderConfirmed;
import com.shivedic.foodveda.R;
import com.shivedic.foodveda.Retrofit.Api;
import com.shivedic.foodveda.Volley.Volley_Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.shivedic.foodveda.Activities.MainActivity.userId;

public class Config {
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    // id to handle the notification in the notification tray
    public static final String SHARED_PREF = "ah_firebase";
    static SweetAlertDialog pDialog = null;
    public static Context mContext;
    public static String paymentModeS = "";
    public static String tId = "";
    public static UserProfileResponse userProfileResponseData;
    public static List<String> cityPinsArray = new ArrayList<>();

    public static void moveTo(Context context, Class targetClass) {
        Intent intent = new Intent(context, targetClass);
        context.startActivity(intent);
    }

    public static boolean validateEmail(EditText editText, Context context) {
        String email = editText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            editText.setError(context.getString(R.string.err_msg_email));
            editText.requestFocus();
            return false;
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void showCustomAlertDialog(Context context, String title, String msg, int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);

        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public static void showCartCustomAlertDialog(final Context context, String title, String msg, int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);

        if (msg.length() > 0)
            alertDialog.setContentText(msg);
            alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
    }


    public static void showLoginCustomAlertDialog(final Context context, String title, String msg, int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);
        alertDialog.setCancelText("Login");
        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Config.moveTo(context, Login.class);
            }
        });
        alertDialog.setConfirmText("Signup");
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Config.moveTo(context, SignUp.class);
            }
        });
        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        btn1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public static void showPincodeCustomAlertDialog(final Context context, String title, String msg, int type) {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);

        alertDialog.setConfirmText("Places We Deliver");
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismissWithAnimation();
                ((MainActivity) context).loadFragment(new PincodeList(), true);

            }
        });
        if (msg.length() > 0) {
            alertDialog.setContentText(msg);
        }
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        btn1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

    }

    public static void showPincodeCustomAlertDialog1(final Context context, String title, String msg, int type) {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);

        alertDialog.setConfirmText("Change Pincode");
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismissWithAnimation();
                ((MainActivity) context).loadFragment(new MyProfile(), true);

            }
        });
        if (msg.length() > 0) {
            alertDialog.setContentText(msg);
        }
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        btn1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

    }

    public static void getCartList(final Context context, final boolean b) {
        if (b)
            MainActivity.progressBar.setVisibility(View.VISIBLE);
        MainActivity.cartCount.setVisibility(View.GONE);
        String req = "{\"res_id\":\"res007\",\"user_id\":\"" + MainActivity.userId + "\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(context, context.getString(R.string.mJSONURL_viewcart), "POST", "getCartList", req);
        /*
        Api.getClient().getCartList("res007", MainActivity.userId, new Callback<CartistResponse>() {
            @Override
            public void success(CartistResponse cartistResponse, Response response) {
                MainActivity.progressBar.setVisibility(View.GONE);
                try {
                    if (cartistResponse.getProducts().size() <= 0) {
                        MainActivity.cartCount.setVisibility(View.GONE);
                    } else {
                        MainActivity.cartCount.setText(cartistResponse.getProducts().size() + "");
                        if (!b) {
                            Log.d("equals", "equals");
                            MainActivity.cartCount.setVisibility(View.GONE);

                        } else {
                            MainActivity.cartCount.setVisibility(View.VISIBLE);

                        }
                    }
                } catch (Exception e) {
                    MainActivity.cartCount.setVisibility(View.GONE);

                }

            }

            @Override
            public void failure(RetrofitError error) {
                MainActivity.progressBar.setVisibility(View.GONE);
            }
        });
        */
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void addOrder(final Context context, String transactionId, String paymentMode) {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        paymentModeS = paymentMode;
        tId = transactionId;
        mContext = context;
        String req = "{\"res_id\":\"" + SplashScreen.resId + "\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(mContext, mContext.getResources().getString(R.string.mJSONURL_restaurantdetail), "POST", "getRestaurantDetailOrder", req);

/*
        Api.getClient().addOrder("res007", MainActivity.userId,
                MyCartList.cartistResponseData.getCartid(),
                ChoosePaymentMethod.address,
                ChoosePaymentMethod.mobileNo,
                transactionId,
                "succeeded",
                CartListAdapter.totalAmountPayable,
                paymentMode,
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        pDialog.dismiss();
                        Intent intent = new Intent(context, OrderConfirmed.class);
                        context.startActivity(intent);
                        ((Activity) context).finishAffinity();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                         pDialog.dismiss();
                        ((Activity) context).finish();
                    }
                });
                */
    }

    public static void resDetailResponse(String response){
        try {
            pDialog.dismissWithAnimation();
            JSONObject jObj = new JSONObject(response);
            String openStatus = jObj.getString("open");
            JSONArray deliveryPins = jObj.getJSONArray("deliverycity");

            for(int i=0; i<deliveryPins.length(); i++){
                cityPinsArray.add(deliveryPins.get(i).toString());
            }
            if(openStatus.equals("open")){
                pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.colorPrimary));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();
                String req = "{\"res_id\":\"" + SplashScreen.resId + "\",\"user_id\":\"" + MainActivity.userId + "\"}";
                Volley_Request postRequest = new Volley_Request();
                postRequest.createRequest(mContext, mContext.getResources().getString(R.string.mJSONURL_userprofile), "POST", "userprofileconfig", req);
            }
            else {
                pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.colorPrimary));
                pDialog.setTitleText("Sorry, Restaurant Closed");
                pDialog.setContentText("Store closed at the moment try later");
                pDialog.setCancelable(false);
                pDialog.show();
                }
        }catch(JSONException ex ){
            Log.d("myTag", "ex : " , ex);
        }
    }

    public static void getUserProfileResponseConfig(String response){
        try {
            pDialog.dismiss();
            JSONObject resp = new JSONObject(response);
            userProfileResponseData = new UserProfileResponse(resp.getString("name"),resp.getString("gender"),resp.getString("mobile"),resp.getString("city"),resp.getString("locality"),resp.getString("flat"),resp.getString("pincode"),resp.getString("state"),resp.getString("landmark"),resp.getString("success"));
            Boolean deliveryAvail = false;

            for(int i=0; i<cityPinsArray.size(); i++){
                Log.d("myTag", "conparing user,s : " + userProfileResponseData.getPincode() + " to " + cityPinsArray.get(i));
                if(userProfileResponseData.getPincode().equals(cityPinsArray.get(i))){
                    deliveryAvail = true;
                    break;
                }
            }

            if (resp.getString("success").equalsIgnoreCase("true")) {
                if(deliveryAvail) {
                    pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.colorPrimary));
                    pDialog.setTitleText("Loading");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    String req = "{\"res_id\":\"" + SplashScreen.resId + "\",\"user_id\":\"" + MainActivity.userId + "\",\"cart_id\":\"" + MyCartList.cartistResponseData.getCartid() + "\",\"address\":\"" + ChoosePaymentMethod.address.substring(0, ChoosePaymentMethod.address.length() - 10).trim() + "\",\"phone\":\"" + ChoosePaymentMethod.mobileNo + "\",\"paymentref\":\"" + tId + "\",\"paystatus\":\"" + "succeeded" + "\",\"total\":\"" + CartListAdapter.totalAmountPayable + "\",\"paymentmode\":\"" + paymentModeS + "\"}";
                    Volley_Request postRequest = new Volley_Request();
                    postRequest.createRequest(mContext, mContext.getResources().getString(R.string.mJSONURL_addorders), "POST", "addOrder", req);
                }
                else {
                    pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                    pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.colorPrimary));
                    pDialog.setTitleText("Sorry, No Order Placed");
                    pDialog.setContentText("Store does not deliver to your pincode");
                    pDialog.setCancelable(false);
                    pDialog.show();
                }
            }
        } catch (Exception ex ){
            Log.d("myTag", "error : " , ex);
        }
    }

        public static void addOrederResponse(String response){
        try {
            pDialog.dismissWithAnimation();
            Log.d("myTag", "recieved response : " + response);
            Log.d("myTag", "last index of " + response.substring(response.lastIndexOf(">") + 1, response.length()));
            String responseString = response.substring(response.lastIndexOf(">")+ 1, response.length());
            JSONObject jObj = new JSONObject(responseString);
            if(jObj.getString("success").equals("true")) {
              //  String no="8958043695";
                //String msg="new Order";
                Intent intent = new Intent(mContext, OrderConfirmed.class);
                //PendingIntent pi= PendingIntent.getActivity(mContext, 0, intent,0);
                //SmsManager sms=SmsManager.getDefault();
                //sms.sendTextMessage(no, null, msg, pi,null);
               // Intent intent = new Intent(mContext, OrderConfirmed.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finishAffinity();
            }
        } catch (Exception e ) {
            Log.d("myTag", "error in add order : " , e);
        }
    }
}