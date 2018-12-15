package com.shivedic.foodveda.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.shivedic.foodveda.Activities.SplashScreen;
import com.shivedic.foodveda.Extras.Config;
import com.shivedic.foodveda.MVP.FAQResponse;
import com.shivedic.foodveda.Activities.MainActivity;
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


public class FAQ extends Fragment {

    View view;
    //@BindView(R.id.faqText)
    //static TextView faqText;
    public static TextView faqText;

    static SweetAlertDialog pDialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_faq, container, false);
        ButterKnife.bind(this, view);
        faqText = (TextView) view.findViewById(R.id.faqText);

        getFAQ();
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.title.setText("");
        Config.getCartList(getActivity(), true);

    }

    public void getFAQ() {
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        String req = "{\"res_id\":\"" + SplashScreen.resId + "\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(getActivity(), getResources().getString(R.string.mJSONURL_FAQ), "POST", "getFAQ", req);



        /*


        Api.getClient().getFAQ("res007",new Callback<FAQResponse>() {
            @Override
            public void success(FAQResponse faqResponse, Response response) {
                pDialog.dismiss();
                try {
                    MainActivity.title.setText(faqResponse.getTitle());
                    faqText.setText(Html.fromHtml(faqResponse.getDescription()));
                } catch (Exception e) {
                }

            }

            @Override
            public void failure(RetrofitError error) {
                pDialog.dismiss();

            }
        });
        */
    }

    public static void getFAQResponse(String response){
        try {
            JSONObject faqResponse = new JSONObject(response);
            pDialog.dismiss();
            MainActivity.title.setText(faqResponse.getString("title"));
            faqText.setText(Html.fromHtml(faqResponse.getString("description")));
        } catch(Exception ex){
            Log.d("myTag", "");
        }
    }
}
