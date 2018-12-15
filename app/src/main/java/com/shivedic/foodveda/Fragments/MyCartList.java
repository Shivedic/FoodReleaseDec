package com.shivedic.foodveda.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import com.shivedic.foodveda.Activities.AccountVerification;
import com.shivedic.foodveda.Adapter.CartListAdapter;
import com.shivedic.foodveda.Extras.Config;
import com.shivedic.foodveda.Activities.Login;
import com.shivedic.foodveda.MVP.CartProducts;
import com.shivedic.foodveda.MVP.CartistResponse;
import com.shivedic.foodveda.Activities.MainActivity;
import com.shivedic.foodveda.MVP.Extra;
import com.shivedic.foodveda.MVP.Product;
import com.shivedic.foodveda.MVP.Variants;
import com.shivedic.foodveda.R;
import com.shivedic.foodveda.Retrofit.Api;
import com.shivedic.foodveda.Activities.SignUp;
import com.shivedic.foodveda.Activities.SplashScreen;
import com.shivedic.foodveda.Volley.Volley_Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.shivedic.foodveda.Fragments.FavoriteList.verify;

public class MyCartList extends Fragment {

    View view;
public static RecyclerView category;
public static Button proceed,conttinue;
public static LinearLayout cartlayyout,emaill,llogin;

   // @BindView(R.id.categoryRecyclerView)
    //static RecyclerView productsRecyclerView;
    public static List<CartProducts> productsData = new ArrayList<>();
    public static CartistResponse cartistResponseData;
    //@BindView(R.id.proceedToPayment)
    //static Button proceedToPayment;
    public static Context context;
   // @BindView(R.id.emptyCartLayout)
    //static LinearLayout emptyCartLayout;
    //@BindView(R.id.loginLayout)
    //LinearLayout loginLayout;
    //@BindView(R.id.continueShopping)
    //Button continueShopping;
public static Context mContext;
public static Activity activity;
    //@BindView(R.id.verifyEmailLayout)
    //static LinearLayout verifyEmailLayout;
    static SweetAlertDialog pDialog = null;
    // Initialise it from onAttach()
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart_list, container, false);
        ButterKnife.bind(this, view);
        activity = (Activity) view.getContext();
        category = (RecyclerView) view.findViewById(R.id.categoryRecyclerView);
        proceed = (Button) view.findViewById(R.id.proceedToPayment);
        conttinue = (Button) view.findViewById(R.id.continueShopping);
        cartlayyout = (LinearLayout) view.findViewById(R.id.emptyCartLayout);
        emaill = (LinearLayout) view.findViewById(R.id.verifyEmailLayout);
        llogin = (LinearLayout) view.findViewById(R.id.loginLayout);

        context = getActivity();
        MainActivity.title.setText("My Cart");
        try {
            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((int) Double.parseDouble(CartListAdapter.totalAmountPayable) >= Integer.parseInt(SplashScreen.restaurantDetailResponseData.getMinorder()))
                        ((MainActivity) getActivity()).loadFragment(new ChoosePaymentMethod(), true);
                    else
                        Config.showCustomAlertDialog(getActivity(),
                                "",
                                "Minimum order value must be atleast " + SplashScreen.restaurantDetailResponseData.getMinorder(),
                                SweetAlertDialog.WARNING_TYPE);

                }
            });
        }catch (Exception e) {
            Log.d("myTag", "error in mycartlist : " , e);
        }

        return view;
    }

    @OnClick({R.id.continueShopping, R.id.loginNow, R.id.txtSignUp, R.id.verfiyNow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueShopping:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.cart.setVisibility(View.VISIBLE);
    }

    public void getCartList() {
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        String req = "{\"res_id\":\"" + SplashScreen.resId + "\",\"user_id\":\""+ MainActivity.userId + "\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(getActivity(), getResources().getString(R.string.mJSONURL_viewcart), "POST", "viewCart", req);

        /*
        Api.getClient().getCartList("res007", MainActivity.userId, new Callback<CartistResponse>() {
            @Override
            public void success(CartistResponse cartistResponse, Response response) {

                cartistResponseData = cartistResponse;
                pDialog.dismiss();
                productsData = new ArrayList<>();
                productsData = cartistResponse.getProducts();
                if (cartistResponse.getSuccess().equalsIgnoreCase("false")) {
                    verifyEmailLayout.setVisibility(View.VISIBLE);
                    proceedToPayment.setVisibility(View.GONE);
                } else {
                    try {
                        Log.d("cartId", cartistResponse.getCartid());
                        cartistResponse.getProducts().size();
                        proceedToPayment.setVisibility(View.VISIBLE);
                        setProductsData();
                    } catch (Exception e) {
                        proceedToPayment.setVisibility(View.GONE);
                        emptyCartLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("errorInCartList", error.toString());

                pDialog.dismiss();

            }
        });
        */
    }

    public static void viewCartResponse(String response){
        try {
            pDialog.dismissWithAnimation();

            JSONObject cartObj = new JSONObject(response);
            if (cartObj.getString("success").equalsIgnoreCase("true")) {
                if (cartObj.getString("message").equalsIgnoreCase("Empty cart")) {
                    Config.showCartCustomAlertDialog(context,
                            "Empty cart",
                            "Your cart is empty",
                            SweetAlertDialog.NORMAL_TYPE);
                }
                else {
                JSONArray PArr = cartObj.getJSONArray("products");
                productsData = new ArrayList<>();
                for (int i = 0; i < PArr.length(); i++) {
                    JSONObject jObj = PArr.getJSONObject(i);
                    List<String> imageList = new ArrayList<>();
                /*
                JSONArray images = jObj.getJSONArray("images");
                for (int j = 0; j < images.length(); j++) {
                    imageList.add(images.get(i).toString());
                }
*/
                    //JSONArray variantL = jObj.getJSONArray("variants");
                    List<Variants> variantList = new ArrayList<>();
                    //for (int k = 0; k < variantL.length(); k++) {
                    JSONObject variant = jObj.getJSONObject("variants");
                    variantList.add(new Variants(variant.getString("varientid"), variant.getString("variantname"), variant.getString("varprice"), variant.getString("varquantity")));
                    // }

                    JSONArray extraL = jObj.getJSONArray("extra");
                    List<Extra> extraList = new ArrayList<>();
                    Log.d("myTag", "extralist len : " + extraL.length());
                    for (int k = 0; k < extraL.length(); k++) {
                        JSONObject extra = extraL.getJSONObject(k);
                        Log.d("myTag", "extra string : " + extra.getString("extraid") + extra.getString("extraname") + extra.getString("extraprice"));
                        extraList.add(new Extra(extra.getString("extraid"), extra.getString("extraname"), extra.getString("extraprice"),extra.getString("extraquantity") ));
                    }
                    CartProducts p = new CartProducts(jObj.getString("productId"), "", jObj.getString("plimit"), "", jObj.getString("productName"), "", "", "", jObj.getString("primaryimage"), "", "", "", "", variantList.get(0), imageList, extraList);
                    productsData.add(p);
                }

                cartistResponseData = new CartistResponse(cartObj.getString("cartid"), cartObj.getString("userid"), cartObj.getString("useremail"), cartObj.getString("tax"), cartObj.getString("delivery"), productsData);
                Log.d("myTag", "cartlistresponse products : " + cartistResponseData.getProducts().size());
                if (cartObj.getString("success").equalsIgnoreCase("false")) {
                    verify.setVisibility(View.VISIBLE);
                    proceed.setVisibility(View.GONE);
                } else {
                    try {
//                        Log.d("myTag", "extra prod : " + productsData.get(1).getExtra().get(0).getExtraname());
                        cartistResponseData.getProducts().size();
                        proceed.setVisibility(View.VISIBLE);
                        CartListAdapter wishListAdapter;
                        GridLayoutManager gridLayoutManager;
                        gridLayoutManager = new GridLayoutManager(context, 1);
                        category.setLayoutManager(gridLayoutManager);
                        wishListAdapter = new CartListAdapter(context, productsData);
                        category.setAdapter(wishListAdapter);

                        setProductsData();
                    } catch (Exception e) {
                        Log.d("myTag", "error in mycarlist : ", e);
                    }
                }
                }
            }
        } catch(Exception ex){
            Log.d("myTag", "error : " , ex);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.cart.setVisibility(View.GONE);
        MainActivity.cartCount.setVisibility(View.GONE);
        Config.getCartList(getActivity(), false);
        if (!MainActivity.userId.equalsIgnoreCase("")) {
            getCartList();
        } else {
            proceed.setVisibility(View.GONE);
            llogin.setVisibility(View.VISIBLE);
        }
    }

    private static void setProductsData() {
        CartListAdapter wishListAdapter;
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(context, 1);
        category.setLayoutManager(gridLayoutManager);
        wishListAdapter = new CartListAdapter(context, productsData);
        category.setAdapter(wishListAdapter);
    }
}
