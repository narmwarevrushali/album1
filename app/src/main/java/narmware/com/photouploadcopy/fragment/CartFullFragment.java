package narmware.com.photouploadcopy.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.activity.CouponActivity;
import narmware.com.photouploadcopy.activity.SelectFrameActivity;
import narmware.com.photouploadcopy.activity.WebViewActivity;
import narmware.com.photouploadcopy.adapter.CartFriendsAdapter;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Address;
import narmware.com.photouploadcopy.models.CartResponse;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.models.Image;
import narmware.com.photouploadcopy.support.DatabaseAccess;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFullFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFullFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFullFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static TextView mTxtAlbumPrice,mTxtItemDesc,mTxtAlbumSize,mTxtDisc,mTxtTotal,mTxtShipping;
    ImageView mItemImg;
    Button mBtnSendCart;
    String mAddress;
    String mMobile;
    public static DatabaseAccess databaseAccess;
    //FloatingActionButton mFabAdd,mFabDelete;
    Button mBtnAddFrnd;
   public static Button mBtnCoupon;
    RelativeLayout mRelativeBtnCoupon;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private OnFragmentInteractionListener mListener;
JSONParser mJsonParser;
    List<Friends> mCategoryItems;
    List<Friends> temp;
    LinearLayout mLinearEmpty;
    protected RecyclerView mCategoryRecyclerView;
    protected CartFriendsAdapter mCategoryAdapter;
    protected Dialog mNoConnectionDialog;
    //protected Dialog mEmptyProfDialog;
    //protected Dialog mEmptyMobileDialog;

    String totalPics,totalFrnds;
    int AlbumPrice,FramePrice;
    String mCouponName;
    int sendToMePos=0;
    public static int totalCount,totalAmount,finalTotal,minTransactionAmt,total,discountAmount,shippingCharges;

    public static Context mContext;

    String mState;
    String mCity;
    String mDist;
    String mPin;

    public CartFullFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFullFragment newInstance(String param1, String param2) {
        CartFullFragment fragment = new CartFullFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if(enter) {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, 400);
        }else {
            return MoveAnimation.create(MoveAnimation.RIGHT, enter, 400);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart_full, container, false);
        mTxtTotal=view.findViewById(R.id.txt_total);

        init(view);
        mListener.onFragmentInteraction("Cart");
        SharedPreferencesHelper.setSelectedFragment("CartFullFragment",getContext());

        return view;
    }

    private void init(View view) {

        fragmentManager=getFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }

        mContext=getContext();
        mTxtTotal=view.findViewById(R.id.txt_total);
        mTxtDisc=view.findViewById(R.id.txt_disc);
        mTxtShipping=view.findViewById(R.id.txt_shipping);

        if(SharedPreferencesHelper.getAlbumPrice(mContext)!=null) {
            AlbumPrice = Integer.parseInt(SharedPreferencesHelper.getAlbumPrice(mContext));
        }

        if(SharedPreferencesHelper.getFramePrice(mContext)!=null) {
             FramePrice = Integer.parseInt(SharedPreferencesHelper.getFramePrice(mContext));
        }

       // totalAmount= Integer.parseInt(SharedPreferencesHelper.getAlbumPrice(getContext()));
        totalAmount=AlbumPrice+FramePrice;
        mCouponName=SharedPreferencesHelper.getCouponName(getContext());

        mBtnCoupon=view.findViewById(R.id.btn_coupon);
        mBtnCoupon.setOnClickListener(this);

        mRelativeBtnCoupon=view.findViewById(R.id.rltv_btn_coupon);
        mRelativeBtnCoupon.setOnClickListener(this);
        if(mCouponName!=null)
        {
            mBtnCoupon.setText(mCouponName);
        }

        mBtnAddFrnd=view.findViewById(R.id.btn_add_cart_frnd);
        mBtnAddFrnd.setOnClickListener(this);

        mBtnSendCart=view.findViewById(R.id.send_cart);
        mBtnSendCart.setOnClickListener(this);

        mTxtAlbumPrice=view.findViewById(R.id.total_album_price);

        mTxtAlbumSize=view.findViewById(R.id.item_size);
        mTxtAlbumSize.setText(SharedPreferencesHelper.getAlbumSize(getContext()));

        mItemImg=view.findViewById(R.id.item_img);
        mItemImg.setOnClickListener(this);
        Picasso.with(getContext())
                .load(SharedPreferencesHelper.getFramePath(getContext()))
                .fit()
                .into(mItemImg);

        mJsonParser=new JSONParser();

        mLinearEmpty=view.findViewById(R.id.frnds_empty);

        databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();

        temp =databaseAccess.getCartSelectedFriends();
        totalFrnds= String.valueOf(temp.size());
        if(temp.size()!=0) {
            mLinearEmpty.setVisibility(View.INVISIBLE);
            setFrndAdapter(view);
        }


       List<Image> images=databaseAccess.getAllDetails();
        mTxtItemDesc=view.findViewById(R.id.item_desc);
        totalPics= String.valueOf(images.size());
        mTxtItemDesc.setText(totalPics);


    }

    public void setFrndAdapter(View v){
        mCategoryItems = new ArrayList<>();

        if(temp.size()!=0) {
            mLinearEmpty.setVisibility(View.INVISIBLE);
            mCategoryRecyclerView = v.findViewById(R.id.recycler);
            mCategoryAdapter = new CartFriendsAdapter(getContext(), mCategoryItems);
           // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            mCategoryRecyclerView.setLayoutManager(mLayoutManager);
            mCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mCategoryRecyclerView.setAdapter(mCategoryAdapter);
            mCategoryRecyclerView.setNestedScrollingEnabled(false);
            mCategoryRecyclerView.setFocusable(false);


            for (int c = 0; c < temp.size(); c++) {

                if(temp.get(c).getFr_name().equals(Constants.SEND_TO_ME))
                {
                    sendToMePos=c;
                }
                else {
                    mCategoryItems.add(temp.get(c));
                }
                Log.e("Cart Friends data", "Name  " + temp.get(c).getF_id() + "   " + temp.get(c).getFr_qty() + "   " + temp.get(c).getCart_flag());
            }
            Log.e("Sendtome","Name  "+temp.get(sendToMePos).getFr_name());
            mCategoryItems.add(temp.get(sendToMePos));

            mCategoryAdapter.notifyDataSetChanged();
        }
        else {
            mLinearEmpty.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setPrice();
    }

    public static void setPrice(){
        totalCount=0;
        databaseAccess = DatabaseAccess.getInstance(mContext);
        databaseAccess.open();
        List<Friends> temp =databaseAccess.getCartSelectedFriends();
        mTxtShipping.setText(String.valueOf(SharedPreferencesHelper.getShippingCharges(mContext)));
        shippingCharges=SharedPreferencesHelper.getShippingCharges(mContext);

        for(int c=0;c < temp.size();c++)
        {
            try {

                    totalCount = totalCount + Integer.parseInt(temp.get(c).getFr_qty());
                    Log.e("Friends data","Quantity  " + totalCount);
                Log.e("Single Friend data", "  "+temp.get(c).getFr_name()+"  Quantity  " + temp.get(c).getFr_qty());


            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        discountAmount=0;
        total=totalCount * totalAmount;
       // mTxtTotal.setText(String.valueOf(total));
        if(total==0)
        {
            mTxtTotal.setText(String.valueOf(totalAmount));
        }
        else{
            mTxtTotal.setText(String.valueOf(total));
        }
        SharedPreferencesHelper.setTotalPrice(total,mContext);

        if(SharedPreferencesHelper.getCouponMinPrice(mContext)!=null) {
             minTransactionAmt= Integer.parseInt(SharedPreferencesHelper.getCouponMinPrice(mContext));

            if(minTransactionAmt <= total)
            {
                if(SharedPreferencesHelper.getCouponPrice(mContext)!=null)
                {
                    discountAmount = Integer.parseInt(SharedPreferencesHelper.getCouponPrice(mContext));
                    mTxtDisc.setText(String.valueOf(discountAmount));
                    finalTotal=(total-discountAmount)+shippingCharges;
                }
            }
            else{
                if(total==0)
                {
                    Toast.makeText(mContext, "Please select atleast one receiver", Toast.LENGTH_SHORT).show();
                }
                else {

                    new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Coupon is not valid below Rs."+SharedPreferencesHelper.getCouponMinPrice(mContext))
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .show();

                    //Toast.makeText(mContext, "Coupon is not applicable", Toast.LENGTH_SHORT).show();
                }

                SharedPreferencesHelper.setCouponMinPrice(null, mContext);
                SharedPreferencesHelper.setCouponPrice(null, mContext);
                SharedPreferencesHelper.setCouponName(null, mContext);
                finalTotal = (totalCount * totalAmount)+shippingCharges;

                mTxtDisc.setText("0");
                mBtnCoupon.setText("Get Coupon");
            }
        }

        else {
            finalTotal = (totalCount * totalAmount)+shippingCharges;
        }
        //finalTotal=totalCount * totalAmount;
        if(finalTotal!=0) {
            mTxtAlbumPrice.setText(String.valueOf(finalTotal));
        }else {
            mTxtAlbumPrice.setText(String.valueOf(totalAmount));
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.send_cart:

                if(SharedPreferencesHelper.getFramePrice(mContext)==null)
                {
                    Toast.makeText(mContext, "Please select your frame", Toast.LENGTH_SHORT).show();
                }
                if(totalCount==0)
                {
                    Toast.makeText(mContext, "Please select atleast one Receiver", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(totalPics)==0)
                {
                    Toast.makeText(mContext, "No order generated", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
                    databaseAccess.open();
                    ArrayList<Address> userProfile = databaseAccess.getSingleUser();

                    mAddress=userProfile.get(0).getAddress();
                    mState=userProfile.get(0).getState();
                    mCity=userProfile.get(0).getCity();
                    mDist=userProfile.get(0).getDist();
                    mPin=userProfile.get(0).getPin();
                    mMobile=userProfile.get(0).getMobile();

                    new SendCartData().execute();

                    //showAddressDialog();
                    //int flag=0;

                  /*  if (mAddress==null || mAddress.equals("")) {
                        flag=1;
                    }
                    if(mMobile==null || mMobile.equals(""))
                    {
                        flag=2;
                    }
                    if(flag==1)
                    {
                        showAddressDialog();
                    }
                    if(flag==2)
                    {
                        showMobileDialog();
                    }
                    if(flag==0) {
                        new SendCartData().execute();
                    }*/
                }
                //Toast.makeText(mContext,totalCount+"", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_add_cart_frnd:
                fragmentManager=getFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack("null");
                fragmentTransaction.replace(R.id.fragment_container,new AddFrndsForCartFragment());
                fragmentTransaction.commit();
                break;

            case R.id.item_img:

                if( SharedPreferencesHelper.getAlbumId(mContext)==null)
                {
                    Toast.makeText(mContext, "Please select photos first", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intentFrame = new Intent(getContext(), SelectFrameActivity.class);
                    startActivity(intentFrame);
                }
               // getActivity().finish();
                break;

            case R.id.rltv_btn_coupon:
                Intent intent=new Intent(getContext(), CouponActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_coupon:
                Intent intentCoupon=new Intent(getContext(), CouponActivity.class);
                startActivity(intentCoupon);
                break;

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String title);
    }

    class SendCartData extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(mContext,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(getContext());
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("Preparing cart...");
            mProgress.setCancelable(false);
            mProgress.show();

        }
        

        @Override
        protected String doInBackground(String... strings) {
            String json = null;
            try
            {
                Gson gson = new Gson();

                List<Friends> friend=new ArrayList<>();
                List<Friends> getFrndsData=databaseAccess.getCartSelectedFriends();
                for(int i=0;i<getFrndsData.size();i++)
                {
                    Friends friends=new Friends(0,null,null,null,null,null,null,null,null,null,null,null,null,0);

                    if(getFrndsData.get(i).getFr_name().equals(Constants.SEND_TO_ME) && SharedPreferencesHelper.getIsSendMe(getContext())==true)
                    {
                        friends.setF_id(getFrndsData.get(i).getF_id());
                        friends.setFr_qty(getFrndsData.get(i).getFr_qty());
                        friend.add(friends);
                    }
                    else {
                        if(getFrndsData.get(i).getFr_name().equals(Constants.SEND_TO_ME) ) {
                        }
                        else {
                            friends.setF_id(getFrndsData.get(i).getF_id());
                            friends.setFr_qty(getFrndsData.get(i).getFr_qty());

                            friend.add(friends);
                        }
                    }
                }

                CartResponse cartResponse=new CartResponse();
                cartResponse.setData(friend);

                cartResponse.setA_id(SharedPreferencesHelper.getAlbumId(mContext));
                cartResponse.setUser_id(SharedPreferencesHelper.getUserId(mContext));
                cartResponse.setTotal_albums(String.valueOf(totalCount));
                cartResponse.setTotal_amount(String.valueOf(finalTotal));
                cartResponse.setCoupon(SharedPreferencesHelper.getCouponName(getContext()));

                json = gson.toJson(cartResponse);
                Log.e("JSON data updated",json);

                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.JSON_STRING,json);
                Log.e("JSON data updated ob",json);

                String url = MyApplication.URL_SERVER + MyApplication.URL_MAKE_PAYMENT;
                Log.e("JSON data updated url",url);

                JSONObject ob=mJsonParser.makeHttpRequest(url, "GET",params );

                if (ob == null) {
                    Log.d("RESPONSE", "ERRORRRRR");
                }
                else {
                    json = ob.toString();
                }
            }
            catch (Exception ex) {

                ex.printStackTrace();
            }

            return json;
        }

        @Override
        protected void onPostExecute(String s) {

            try{Gson gson = new Gson();
                if (s != null)
                    Log.e("cart data", s);

                else
                    Log.e("data", "cart is null");

                CartResponse cartResponse=gson.fromJson(s,CartResponse.class);
                Log.e("Cart response",cartResponse.getResponse()+"  "+cartResponse.getInv_id());
                int response= Integer.parseInt(cartResponse.getResponse());
                if(response==Constants.NEW_ENTRY)
                {
                    String invoice= String.valueOf(cartResponse.getInv_id());

                    SharedPreferencesHelper.setInvoiceId(invoice, getContext());
                    Intent intent=new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra(Constants.INVOICE_ID,SharedPreferencesHelper.getInvoiceId(getContext()));
                    intent.putExtra(Constants.USER_ID,SharedPreferencesHelper.getUserId(getContext()));
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(),"Error while making payment",Toast.LENGTH_LONG).show();
                }
                mProgress.dismiss();
            }catch (Exception e)
            {
                showNoConnectionDialog();
                //Toast.makeText(getContext(),"Internet not available,can not login",Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        }
    }
    private void showNoConnectionDialog() {
        mNoConnectionDialog = new Dialog(mContext, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        mNoConnectionDialog.setContentView(R.layout.dialog_noconnectivity);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button exit = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_exit);
        Button tryAgain = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_try_again);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AppCompatActivity act = (AppCompatActivity) mContext;
                act.finish();
            }
        });

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SendCartData().execute();
                mNoConnectionDialog.dismiss();
            }
        });
    }


}
