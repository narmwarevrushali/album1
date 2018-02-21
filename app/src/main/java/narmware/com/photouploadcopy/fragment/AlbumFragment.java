package narmware.com.photouploadcopy.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.google.gson.Gson;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.adapter.AlbumAdapter;
import narmware.com.photouploadcopy.models.UserAlbum;
import narmware.com.photouploadcopy.models.UserAlbumResponse;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlbumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //CardView mCardAlbum;
    private JSONParser mJsonParser;
    TextView mTxtAlbumPrice,mTxtAlbumSize;
    String mAlbumSize,mAlbumPrice;

    List<UserAlbum> mAlbumItems;
    protected RecyclerView mRecyclerView;
    AlbumAdapter albumAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AlbumFragment() {
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
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_album, container, false);
        init(view);
        mListener.onFragmentInteraction("Album");
        SharedPreferencesHelper.setSelectedFragment("AlbumFragment",getContext());
        setAlbumAdapter(view);
        new GetAlbumSizePrice().execute();
        return view;

    }

    public void setAlbumAdapter(View v){
        mAlbumItems=new ArrayList<>();

       /* UserAlbum userAlbum=new UserAlbum();
        userAlbum.setA_size("4X7");
        userAlbum.setA_price("1200");
        mAlbumItems.add(userAlbum);*/

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        albumAdapter= new AlbumAdapter(getContext(), mAlbumItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);;
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        /*SnapHelper snapHelperTop = new LinearSnapHelper();
        snapHelperTop.attachToRecyclerView(mRecyclerView);*/
        mRecyclerView.setAdapter(albumAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);


    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if(enter) {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, 400);
        }else {
            return MoveAnimation.create(MoveAnimation.RIGHT, enter, 400);
        }
    }

    private void init(View view) {

        mJsonParser=new JSONParser();
       /* mCardAlbum=view.findViewById(R.id.album_card);
        mCardAlbum.setOnClickListener(this);
*/
        mTxtAlbumPrice=view.findViewById(R.id.txt_album_price);
        mTxtAlbumSize=view.findViewById(R.id.txt_album_size);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
            /*case R.id.album_card:
              *//*  Intent intent = new Intent(getContext(), AlbumSelectActivity.class);
                //set limit on number of images that can be selected, default is 10
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
                intent.putExtra("count", 0);
                startActivityForResult(intent, Constants.REQUEST_CODE);*//*
                mAlbumPrice=mTxtAlbumPrice.getText().toString();
                mAlbumSize=mTxtAlbumSize.getText().toString();
                SharedPreferencesHelper.setAlbumPrice(mAlbumPrice,getContext());
                new GetAlbumID().execute();
                Intent intent = new Intent(getContext(), SelectImagesActivity.class);
                startActivity(intent);
                break;*/
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


    class GetAlbumSizePrice extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(LoginActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(getContext());
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("Getting Albums...");
            mProgress.setCancelable(false);
            mProgress.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String json = null;
            try
            {
                Gson gson = new Gson();

                HashMap<String, String> params = new HashMap<>();
               // params.put(Constants.JSON_STRING,json);

                String url = MyApplication.URL_SERVER + MyApplication.URL_GET_ALBUM_SIZE_PRICE;
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
                    Log.e("Album data", s);

                else
                    Log.e("data", "Album is null");

                UserAlbumResponse response = gson.fromJson(s, UserAlbumResponse.class);
                UserAlbum[] array = response.getData();
                for (UserAlbum item : array) {

                    mAlbumItems.add(item);
                    Log.e("JSON Album data","album size"+item.getA_size()+"\n album price"+item.getA_price());

                }
                albumAdapter.notifyDataSetChanged();

             /*   int res= Integer.parseInt(response.getResponse());
                if(res== Constants.NEW_ENTRY)
                {

                }*/
                mProgress.dismiss();
            }catch (Exception e)
            {
                // Toast.makeText(mContext,"Internet not available,can not login",Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        }
    }

}
