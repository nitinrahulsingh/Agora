package com.intelegain.agora.fragmments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.intelegain.agora.R;
import com.intelegain.agora.adapter.Contacts_recycler_view_adapter;
import com.intelegain.agora.api.urls.RetrofitClient;
import com.intelegain.agora.constants.Constants;
import com.intelegain.agora.interfeces.WebApiInterface;
import com.intelegain.agora.model.Employee_contacts;
import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.Sharedprefrences;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.intelegain.agora.api.urls.RetrofitClient.retrofit;

public class ContactsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    View view;

    RecyclerView recyclerView_contacts;
    Contacts_recycler_view_adapter contactsRecyclerViewAdapter;
    ArrayList<Employee_contacts> empContactsList;
    AppCompatTextView appCompatTextView;

    ImageView imgBoardMail, imgBoardPhone,
            imgHrMail, imgHrPhone,
            imgAccountsMail, imgAccountsPhone, img_filter;

    EditText editTextSearch;
    TextView tv_try_again;
    ProgressBar progressBar;

    private SwipeRefreshLayout swipeRefreshLayout;

    Intent intentToCall, intentToEmail;


    Retrofit retrofitClient;

    Contants2 contants2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contacts, container, false);

        initialize();
        findViews();
        if (Contants2.checkInternetConnection(getActivity()))
            getCompanyContacts();
        else {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);
            hideRecyclerView(true);
        }
        //generateDemoList();

        setListenersForViews();
        return view;
    }


    private void initialize() {
        empContactsList = new ArrayList<>();
        retrofitClient = RetrofitClient.getInstance(Constants.BASE_URL);
        contants2 = new Contants2();
    }

    private void findViews() {
        swipeRefreshLayout = view.findViewById(R.id.refreshlayout);
        img_filter = view.findViewById(R.id.img_filter);
        editTextSearch = view.findViewById(R.id.etSearchByName);

        imgBoardMail = view.findViewById(R.id.img_board_mail);
        imgBoardPhone = view.findViewById(R.id.img_board_call);

        imgHrMail = view.findViewById(R.id.img_hr_mail);
        imgHrPhone = view.findViewById(R.id.img_hr_call);

        imgAccountsMail = view.findViewById(R.id.img_accounts_mail);
        imgAccountsPhone = view.findViewById(R.id.img_accounts_call);

        progressBar = view.findViewById(R.id.progress_bar_contacts);
        recyclerView_contacts = view.findViewById(R.id.recycler_contacts);
        tv_try_again = view.findViewById(R.id.text_view_try_again);

    }

    private void getCompanyContacts() {
        contants2.showProgressDialog(getActivity());
        swipeRefreshLayout.setRefreshing(false);
        WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
        Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(getActivity());
        String strToken = mSharedPrefs.getString("Token", "");
        String strEmpId = mSharedPrefs.getString("emp_Id", "");


        Map<String, String> params = new HashMap<>();
        params.put("EmpId", strEmpId);


        Call<List<Employee_contacts>> call = apiInterface.getAllEmployeeContacts(strEmpId, strToken, params);
        call.enqueue(new Callback<List<Employee_contacts>>() {
            @Override
            public void onResponse(Call<List<Employee_contacts>> call, Response<List<Employee_contacts>> response) {
                switch (response.code())
                {
                    case 200:
                        swipeRefreshLayout.setRefreshing(false);

                        if (empContactsList != null && empContactsList.size() > 0)
                            empContactsList.clear();

                        if (response.body() != null && response.body().size() > 0) {
                            int iListSize = response.body().size();
                            for (int index = 0; index < iListSize; index++) {
                                switch (index) {
                                    case 0:
                                        imgBoardMail.setTag(TextUtils.isEmpty(response.body().get(index).empEmail) ? "" : response.body().get(index).empEmail);
                                        imgBoardPhone.setTag(TextUtils.isEmpty(response.body().get(index).empContact) ? "" : response.body().get(index).empContact);
                                        break;
                                    case 1:
                                        imgHrMail.setTag(TextUtils.isEmpty(response.body().get(index).empEmail) ? "" : response.body().get(index).empEmail);
                                        imgHrPhone.setTag(TextUtils.isEmpty(response.body().get(index).empContact) ? "" : response.body().get(index).empContact);
                                        break;
                                    case 2:
                                        imgAccountsMail.setTag(TextUtils.isEmpty(response.body().get(index).empEmail) ? "" : response.body().get(index).empEmail);
                                        imgAccountsPhone.setTag(TextUtils.isEmpty(response.body().get(index).empContact) ? "" : response.body().get(index).empContact);
                                        break;
                                    default:
                                        empContactsList.add(response.body().get(index));
                                }
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setUpRecyclerView();
                                    hideRecyclerView(false);
                                    // progressBar.setVisibility(View.GONE);
                                }
                            }, 100);

                        } else {
                            Contants2.showToastMessageAtCenter(getActivity(), getString(R.string.no_data_found), true);
                            hideRecyclerView(true);
                        }
                        contants2.dismissProgressDialog();
                        break;
                    case 403:
                        contants2.dismissProgressDialog();
                        Contants2.showOkAlertDialog(getActivity(), response.message(), "", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Contants2.forceLogout(getActivity());
                            }
                        });
                        break;
                    case 500:
                        contants2.dismissProgressDialog();
                        hideRecyclerView(true);
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                        break;
                    default:
                        hideRecyclerView(true);
                        contants2.dismissProgressDialog();
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                        break;
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
                contants2.dismissProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                hideRecyclerView(true);
                Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
            }
        });

    }

    private void hideRecyclerView(boolean hide) {
        if (hide) {
            recyclerView_contacts.setVisibility(View.GONE);
            tv_try_again.setVisibility(View.VISIBLE);
        } else {
            tv_try_again.setVisibility(View.GONE);
            recyclerView_contacts.setVisibility(View.VISIBLE);
        }

    }

    private void setUpRecyclerView() {
        // recycler
        recyclerView_contacts.setHasFixedSize(true);
        recyclerView_contacts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_contacts.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView_contacts.addItemDecoration(dividerItemDecoration);
        recyclerView_contacts.setItemAnimator(new DefaultItemAnimator());

        recyclerView_contacts.setLayoutManager(new LinearLayoutManager(getActivity()));

        contactsRecyclerViewAdapter = new Contacts_recycler_view_adapter(getActivity(), empContactsList);
        recyclerView_contacts.setAdapter(contactsRecyclerViewAdapter);

        setUpItemTouchHelper();
    }

    private void setListenersForViews() {
        swipeRefreshLayout.setOnRefreshListener(this);
        img_filter.setOnClickListener(this);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //mTaskManagerAdapter.getFilter().filter(s);
                contactsRecyclerViewAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgBoardMail.setOnClickListener(this);
        imgBoardPhone.setOnClickListener(this);


        imgHrMail.setOnClickListener(this);
        imgHrPhone.setOnClickListener(this);


        imgAccountsMail.setOnClickListener(this);
        imgAccountsPhone.setOnClickListener(this);

        tv_try_again.setOnClickListener(this);
    }

    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private void setUpItemTouchHelper()
    {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background, xMark;

            int xMarkMargin;
            boolean initiated;
            // LinearLayout.LayoutParams layoutParams;

            private void init() {
                background = new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.yellow));

                xMark = ContextCompat.getDrawable(getActivity(), R.drawable.ic_phone_call_white);
                //xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //int position = viewHolder.getAdapterPosition();

                /*if (isSelected)
                    return 0;*/
              /*  TestAdapter testAdapter = (TestAdapter)recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }*/
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                ///set here view swiped

                int swipedPosition = viewHolder.getAdapterPosition();


                contactsRecyclerViewAdapter.contactsList.get(swipedPosition).isSwiped = true;
                contactsRecyclerViewAdapter.notifyItemChanged(swipedPosition);

                //NotificationAdapter adapter = (NotificationAdapter) recyclerView_contacts.getAdapter();
                /*adapter.pendingRemoval(swipedPosition);
                closeAnyOpenState(swipedPosition);*/
                // boolean undoOn = adapter.isUndoOn();
                //if (undoOn) {

                // } else {
                //   adapter.remove(swipedPosition);
                // }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView_contacts);

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.img_filter:
                Contants2.showToastMessage(getActivity(), getActivity().getString(R.string.under_development), false);
                break;
            case R.id.img_board_mail:

                intentToEmail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + imgBoardMail.getTag()));


                /*intentToEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"hr@intelegain.com"});
                intentToEmail.setType("message/rfc822");*/
                getActivity().startActivity(intentToEmail);

                break;
            case R.id.img_board_call:
                intentToCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + imgBoardPhone.getTag()));
                getActivity().startActivity(intentToCall);
                break;
            case R.id.img_hr_mail:

                intentToEmail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + imgHrMail.getTag()));

                /*intentToEmail.setData(Uri.parse("mailto: hr@intelegain.com"));
                intentToEmail.setType("text/plain");*/

                getActivity().startActivity(intentToEmail);
                break;
            case R.id.img_hr_call:
                intentToCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + imgHrPhone.getTag()));

                getActivity().startActivity(intentToCall);
                break;
            case R.id.img_accounts_mail:

                intentToEmail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + imgAccountsMail.getTag()));
                /*intentToEmail.setData(Uri.parse("mailto: hr@intelegain.com"));
                intentToEmail.setType("text/plain");*/
                getActivity().startActivity(intentToEmail);
                break;

            case R.id.img_accounts_call:
                intentToCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + imgAccountsPhone.getTag()));
                getActivity().startActivity(intentToCall);
                break;
            case R.id.text_view_try_again:
                if (Contants2.checkInternetConnection(getActivity()))
                    getCompanyContacts();
                else {
                    Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);
                    hideRecyclerView(true);
                }
                break;
            default:
                break;

        }
    }


    private ProgressDialog progressDialog;

    /**
     * Show progresss dialog
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    /**
     * Dismiss the progress dialog
     */
    private void dismissProgressDialog() {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();
    }


    @Override
    public void onRefresh() {
        if (Contants2.checkInternetConnection(getActivity())) {
            getCompanyContacts();
            if (editTextSearch != null)
                editTextSearch.setText("");
        }
        else {
            swipeRefreshLayout.setRefreshing(false);
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);
            hideRecyclerView(true);
        }

    }
}

// taking response from json file

               /* Employee_contacts employee_contacts;
                try {
                    String json = new UtilHelper().loadJSONFromAsset(getActivity(),"json/getEmpDetails.json");
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo_inside = jsonArray.getJSONObject(i);
                        employee_contacts = new Employee_contacts();

                        if (i < 3) {
                            switch (i) {
                                case 0:
                                    imgBoardMail.setTag(jo_inside.getString("empEmail"));
                                    imgBoardPhone.setTag(jo_inside.getString("empContact"));
                                    break;
                                case 1:
                                    imgHrMail.setTag(jo_inside.getString("empEmail"));
                                    imgHrPhone.setTag(jo_inside.getString("empContact"));
                                    break;
                                case 2:
                                    imgAccountsMail.setTag(jo_inside.getString("empEmail"));
                                    imgAccountsPhone.setTag(jo_inside.getString("empContact"));
                                    break;
                            }
                            continue;
                        }

                        employee_contacts.isSwiped = false;
                        employee_contacts.empid = Integer.parseInt(jo_inside.getString("empid"));
                        employee_contacts.empName = jo_inside.getString("empName");
                        employee_contacts.empContact = jo_inside.getString("empContact");
                        employee_contacts.empEmail = jo_inside.getString("empEmail");
                        employee_contacts.designation = jo_inside.getString("Designation");

                        empContactsList.add(employee_contacts);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setUpRecyclerView();
                        // progressBar.setVisibility(View.GONE);
                    }
                }, 100);*/

                /*private void generateDemoList() {
        Employee_contacts employee_contacts;
        for (int i = 1; i < 10; i++) {

            employee_contacts = new Employee_contacts();


            employee_contacts.strUrl = "abc";
            employee_contacts.strEmpName = "emp " + (i + 1);
            employee_contacts.strEmpDesingation = "emp " + (i + 2);
            employee_contacts.strMail = "abc@gmail.com";
            employee_contacts.strPhoneNumber = "9969169161";
            employee_contacts.isSwiped = false;

            empContactsList.add(employee_contacts);


        }


    }*/

//    public String loadJSONFromAsset() {
//        String json = null;
//        try {
//            InputStream is = getActivity().getAssets().open("json/getEmpDetails.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//
//    }