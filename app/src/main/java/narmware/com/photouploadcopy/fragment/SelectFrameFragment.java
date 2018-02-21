package narmware.com.photouploadcopy.fragment;

import android.app.Dialog;
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

import com.google.gson.Gson;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.adapter.FrameAdapter;
import narmware.com.photouploadcopy.models.Frame;
import narmware.com.photouploadcopy.models.FrameResponse;
import narmware.com.photouploadcopy.support.JSONParser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectFrameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectFrameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectFrameFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //CardView mCardAlbum;
    private JSONParser mJsonParser;
    List<Frame> mFrameItems;
    //List<Frame> temp;
    protected RecyclerView mRecyclerView;
    protected FrameAdapter mAdapter;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    protected Dialog mNoConnectionDialog;

    private OnFragmentInteractionListener mListener;

    public SelectFrameFragment() {
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
    public static SelectFrameFragment newInstance(String param1, String param2) {
        SelectFrameFragment fragment = new SelectFrameFragment();
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
        View view= inflater.inflate(R.layout.fragment_select_frame, container, false);
        mFrameItems = new ArrayList<>();

        init(view);
        return view;

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
    public void onResume() {
        super.onResume();

        mListener.onFragmentInteraction("Select Frame");
    }

    private void init(View view) {

        mJsonParser=new JSONParser();

        setFrameAdapter(view);
        new GetAllFrames().execute();

    }

    public void setFrameAdapter(View v){

            mRecyclerView = v.findViewById(R.id.recycler);
            mAdapter = new FrameAdapter(getContext(), mFrameItems);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setFocusable(false);

            mAdapter.notifyDataSetChanged();

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
           /* case R.id.album_card:
                fragmentManager=getFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack("null");
                fragmentTransaction.replace(R.id.fragment_container,new AlbumFragment());
                fragmentTransaction.commit();
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

    class GetAllFrames extends AsyncTask<String, String, String> {
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
                //params.put("user_id", SharedPreferencesHelper.getUserId(getContext()));

                String url = MyApplication.URL_SERVER + MyApplication.URL_GET_FRAME;
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
                    Log.e("FRAMe all data", s);

                else
                    Log.e("data", "login is null");

                FrameResponse response = gson.fromJson(s, FrameResponse.class);
                Frame[] array = response.getData();
                for (Frame item : array) {

                    mFrameItems.add(item);

                }
                for(int c=0;c < mFrameItems.size();c++)
                {
                    Log.e("FRAMe data", "Name  " + mFrameItems.get(c).getFrame_title());
                }
                mAdapter.notifyDataSetChanged();
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
        mNoConnectionDialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        mNoConnectionDialog.setContentView(R.layout.dialog_noconnectivity);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button exit = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_exit);
        Button tryAgain = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_try_again);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new GetAllFrames().execute();
                mNoConnectionDialog.dismiss();
            }
        });
    }

}
