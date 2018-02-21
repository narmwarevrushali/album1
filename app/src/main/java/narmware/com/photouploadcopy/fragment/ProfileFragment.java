package narmware.com.photouploadcopy.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.squareup.picasso.Picasso;

import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.activity.InvoiceActivity;
import narmware.com.photouploadcopy.support.ImageBlur;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView mTxtProName,mTxtProMail;
    ImageView mImgProf,mBlurBackground;
    private Bitmap bitmap;
    RelativeLayout rltvAddr,rltvFaq,rltvTc,rltvFrnds,mrltvOrderTrack;
    String mName,mEmail,mImgUrl;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
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
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        init(view);
        mListener.onFragmentInteraction("Profile");
        SharedPreferencesHelper.setSelectedFragment("ProfileFragment",getContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onFragmentInteraction("Profile");
        SharedPreferencesHelper.setSelectedFragment("ProfileFragment",getContext());
    }

    private void init(View view) {

        fragmentManager=getFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }

        mTxtProName= view.findViewById(R.id.prof_name);
        YoYo.with(Techniques.BounceIn)
                .duration(1500)
                .playOn(mTxtProName);

        mTxtProMail=view.findViewById(R.id.prof_mail);
        mImgProf=view.findViewById(R.id.prof_img);
        YoYo.with(Techniques.BounceIn)
                .duration(1500)
                .playOn(mImgProf);
        mBlurBackground=view.findViewById(R.id.background);

        rltvAddr=view.findViewById(R.id.rltv_addr);
        rltvFaq=view.findViewById(R.id.rltv_faq);
        rltvFrnds=view.findViewById(R.id.rltv_friends);
        rltvTc=view.findViewById(R.id.rltv_tc);
        mrltvOrderTrack=view.findViewById(R.id.rltv_order_track);

        rltvAddr.setOnClickListener(this);
        rltvFaq.setOnClickListener(this);
        rltvFrnds.setOnClickListener(this);
        rltvTc.setOnClickListener(this);
        mrltvOrderTrack.setOnClickListener(this);
        getData();

        mTxtProName.setText(mName);
        mTxtProMail.setText(mEmail);
      /*  Glide.with(getContext()).load(mImgUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImgProf);*/

        Picasso.with(getContext())
                .load(mImgUrl)
                .fit()
                .into(mImgProf);

        bitmap = new ImageBlur().getBitmapFromURL(mImgUrl);
        try {
            mBlurBackground.setImageBitmap(new ImageBlur().fastblur(bitmap, 4));
        }catch (Exception e)
        {

        }
    }

    public void getData()
    {
        mName= SharedPreferencesHelper.getUserName(getContext());
        mEmail=SharedPreferencesHelper.getUserEmail(getContext());
        mImgUrl=SharedPreferencesHelper.getUserProfPic(getContext());

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
            case R.id.rltv_addr:
                SharedPreferencesHelper.setSelectedFragment("AddressFragment",getContext());
                fragmentManager=getFragmentManager();
                for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                    fragmentManager.popBackStack();
                }

                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack("AddressFragment");
                fragmentTransaction.replace(R.id.fragment_container,new AddressFragment().newInstance(mName,mEmail,mImgUrl));
                fragmentTransaction.commit();

              //  Toast.makeText(getContext(),"Address", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rltv_faq:
                Toast.makeText(getContext(),"FAQ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rltv_tc:
                Toast.makeText(getContext(),"Terms & Conditions", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rltv_friends:
                SharedPreferencesHelper.setSelectedFragment("FriendsFragment",getContext());
                fragmentManager=getFragmentManager();
                for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                    fragmentManager.popBackStack();
                }
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack("null");
                fragmentTransaction.replace(R.id.fragment_container,new FriendsFragment());
                fragmentTransaction.commit();

               // Toast.makeText(getContext(),"Friends", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rltv_order_track:
                SharedPreferencesHelper.setSelectedFragment("InvoiceFragment",getContext());
                Intent intent=new Intent(getContext(),InvoiceActivity.class);
                startActivity(intent);
                getActivity().finish();
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


}
