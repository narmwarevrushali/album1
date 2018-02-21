package narmware.com.photouploadcopy.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.adapter.AddCartFrndsAdapter;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.models.FriendsResponse;
import narmware.com.photouploadcopy.support.DatabaseAccess;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFrndsForCartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFrndsForCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFrndsForCartFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private JSONParser mJsonParser;
    Button mBtnAddFrnds;
    List<Friends> mCategoryItems;
    List<Friends> temp;
    LinearLayout mLinearEmpty;
    protected RecyclerView mCategoryRecyclerView;
    protected AddCartFrndsAdapter mCategoryAdapter;
    private OnFragmentInteractionListener mListener;
    public AddFrndsForCartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFrndsForCartFragment newInstance(String param1, String param2) {
        AddFrndsForCartFragment fragment = new AddFrndsForCartFragment();
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
        View view= inflater.inflate(R.layout.fragment_add_frnds_for_cart, container, false);
        init(view);
        mListener.onFragmentInteraction("Add Friends");
        return view;
    }

    private void init(View view) {
        mJsonParser=new JSONParser();

        mLinearEmpty=view.findViewById(R.id.frnds_empty);
        mBtnAddFrnds=view.findViewById(R.id.btn_add_cart_frnd);
        mBtnAddFrnds.setOnClickListener(this);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();
        temp =databaseAccess.getFriends();
        if(temp.size()!=0) {
            mLinearEmpty.setVisibility(View.INVISIBLE);
            setFrndAdapter(view);
        }

    }
    public void setFrndAdapter(View v){
        mCategoryItems = new ArrayList<>();

        if(temp.size()!=0) {
            mLinearEmpty.setVisibility(View.INVISIBLE);
            mCategoryRecyclerView = v.findViewById(R.id.recycler);
            mCategoryAdapter = new AddCartFrndsAdapter(getContext(), mCategoryItems);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mCategoryRecyclerView.setLayoutManager(mLayoutManager);
            mCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mCategoryRecyclerView.setAdapter(mCategoryAdapter);
            mCategoryRecyclerView.setNestedScrollingEnabled(false);
            mCategoryRecyclerView.setFocusable(false);


            for (int c = 0; c < temp.size(); c++) {


                    mCategoryItems.add(temp.get(c));
                    Log.e("Friends data", "Name  " + temp.get(c).getId());

            }

            mCategoryAdapter.notifyDataSetChanged();
        }
        else {
            mLinearEmpty.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btn_add_cart_frnd:
                fragmentManager=getFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack("null");
                fragmentTransaction.replace(R.id.fragment_container,new CartFullFragment());
                fragmentTransaction.commit();
                break;
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

    class getAllFrndsFromServer extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(LoginActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(getContext());
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("Geting Friends");
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
                params.put(Constants.USER_ID, SharedPreferencesHelper.getUserId(getContext()));

                String url = MyApplication.URL_SERVER + MyApplication.URL_GET_ALL_FRNDS;
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
                    Log.e("Friend all data", s);

                else
                    Log.e("data", "login is null");

                FriendsResponse response = gson.fromJson(s, FriendsResponse.class);
                Friends[] array = response.getData();
                for (Friends item : array) {

                    temp.add(item);

                }
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
                    databaseAccess.open();

                    for(int c=0;c < temp.size();c++)
                    {
                        databaseAccess.setFrndsProfile(temp.get(c).getAddress(),temp.get(c).getState(),temp.get(c).getDist(),temp.get(c).getCity(),temp.get(c).getPin(),temp.get(c).getMobile(),temp.get(c).getFr_name(),temp.get(c).getFr_email(),temp.get(c).getF_id(),"1",0);
                        Log.e("Friends data","Name  "+temp.get(c).getFr_name()+"server id  "+temp.get(c).getF_id());
                    }
                mProgress.dismiss();
            }catch (Exception e)
            {
                Toast.makeText(getContext(),"Internet not available,can not login",Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        }
    }

}
