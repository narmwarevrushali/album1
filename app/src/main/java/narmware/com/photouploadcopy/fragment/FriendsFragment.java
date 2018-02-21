package narmware.com.photouploadcopy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.LinearLayout;

import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.util.List;

import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.adapter.FriendsAdapter;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.support.DatabaseAccess;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FloatingActionButton mBtnAddFrnd;
    List<Friends> mCategoryItems;
    //List<Friends> temp;
    LinearLayout mLinearEmpty;
    protected RecyclerView mCategoryRecyclerView;
    protected FriendsAdapter mCategoryAdapter;
    private OnFragmentInteractionListener mListener;

    public FriendsFragment() {
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
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
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
        View view= inflater.inflate(R.layout.fragment_friends, container, false);
        init(view);
        mListener.onFragmentInteraction("Friends");
        return view;
    }

    private void init(View view) {

      /*  if(SharedPreferencesHelper.getSelectedFragment(getContext())==null){

        }
        else if(SharedPreferencesHelper.getSelectedFragment(getContext()).equals("FriendsProfileFragment"))
        {
            fragmentManager=getFragmentManager();
            for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                fragmentManager.popBackStack();
            }
            SharedPreferencesHelper.setSelectedFragment("Fragment",getContext());
        }
*/

        mBtnAddFrnd=view.findViewById(R.id.btn_add_frnd);
        mBtnAddFrnd.setOnClickListener(this);
        mLinearEmpty=view.findViewById(R.id.frnds_empty);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();
        mCategoryItems =databaseAccess.getFriends();
        Log.e("Friend size",mCategoryItems.size()+"");
        if(mCategoryItems.size()!=0) {
            mLinearEmpty.setVisibility(View.INVISIBLE);
            setFrndAdapter(view);
        }

    }
    public void setFrndAdapter(View v){

        if(mCategoryItems.size()!=0) {
            mLinearEmpty.setVisibility(View.INVISIBLE);
            mCategoryRecyclerView = v.findViewById(R.id.recycler);
            mCategoryAdapter = new FriendsAdapter(getContext(), mCategoryItems);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mCategoryRecyclerView.setLayoutManager(mLayoutManager);
            mCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mCategoryRecyclerView.setAdapter(mCategoryAdapter);
            mCategoryRecyclerView.setNestedScrollingEnabled(false);
            mCategoryRecyclerView.setFocusable(false);

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
            case R.id.btn_add_frnd:
                fragmentManager=getFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack("AddFrnds");
                fragmentTransaction.replace(R.id.fragment_container,new FriendsProfileFragment().newInstance("addFriend",0));
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


}
