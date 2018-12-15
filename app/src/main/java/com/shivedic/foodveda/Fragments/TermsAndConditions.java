package com.shivedic.foodveda.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shivedic.foodveda.Activities.MainActivity;
import com.shivedic.foodveda.Activities.SplashScreen;
import com.shivedic.foodveda.Extras.Config;
import com.shivedic.foodveda.MVP.TermsResponse;
import com.shivedic.foodveda.R;
import com.shivedic.foodveda.Retrofit.Api;
import com.shivedic.foodveda.Volley.Volley_Request;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TermsAndConditions extends Fragment {

    View view;
    public static TextView faqText;
   // @BindView(R.id.faqText)
    //static TextView faqText;

    static SweetAlertDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        faqText = (TextView) getActivity().findViewById(R.id.faqText);
        view = inflater.inflate(R.layout.fragment_faq, container, false);
        ButterKnife.bind(this, view);
        getTerms();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.title.setText("Terms & Conditions");
        Config.getCartList(getActivity(), true);
    }

    public void getTerms() {
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        String req = "{\"res_id\":\"" + SplashScreen.resId + "\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(getActivity(), getResources().getString(R.string.mJSONURL_terms), "POST", "getTerms", req);

        /*
        Api.getClient().getTerms("res007", new Callback<TermsResponse>() {
            @Override
            public void success(TermsResponse termsResponse, Response response) {
                pDialog.dismiss();
                faqText.setText(Html.fromHtml(termsResponse.getTerms()));

            }

            @Override
            public void failure(RetrofitError error) {
                pDialog.dismiss();

            }
        });
        */
    }

    public static void getTCResponse(String response){
        try {
            JSONObject faqResponse = new JSONObject(response);
            pDialog.dismiss();
            faqText.setText(Html.fromHtml(faqResponse.getString("terms")));
        } catch(Exception ex){
            Log.d("myTag", "");
        }
    }
}
