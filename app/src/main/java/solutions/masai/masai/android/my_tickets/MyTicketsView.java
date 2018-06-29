package solutions.masai.masai.android.my_tickets;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.adapter.SimpleSectionedRecyclerViewAdapter;
import com.tolstykh.textviewrichdrawable.EditTextRichDrawable;

/**
 * Created by Semko on 2016-12-05.
 */

public class MyTicketsView {
    private View rootView;
    private MyTicketsViewListener listener;
    private Context context;
    private Handler handler;
    private RecyclerView lvHosts;
    private TextView tvTitle;
    private ImageView ivBack;
    private ProgressDialog progressDialog;

    private ToggleButton tgAll;
    private ToggleButton tgPast;
    private ToggleButton tgFuture;
    private Button btnSearch;
    private EditTextRichDrawable etSearch;
    private ImageView ivFilter;
    private LinearLayout searchLayout;
    private LinearLayout rv_container;
    private ConstraintLayout noJounreysLayout;

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private int heigth;
    private String time = "0";

    private boolean search = true;
    private boolean no = false;

    Animation slide_up;
    Animation slide_down;

    MyTicketsView(View rootView, Context context, MyTicketsViewListener listener) {
        this.rootView = rootView;
        this.context = context;
        this.listener = listener;
        handler = new Handler(Looper.getMainLooper());
        initViews();
    }

    private void initViews() {
        progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(rootView.getContext().getString(R.string.loading_data));
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        this.listener.initToolbar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBackArrowPressed();
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.activity_my_tickets_list_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        lvHosts = (RecyclerView) rootView.findViewById(R.id.activity_my_tickets_list_view);
        //tvTitle = (TextView) rootView.findViewById(R.id.activity_my_tickets_ala_action_bar_title);

        tgAll = (ToggleButton) rootView.findViewById(R.id.all);
        tgPast = (ToggleButton) rootView.findViewById(R.id.past);
        tgFuture = (ToggleButton) rootView.findViewById(R.id.future);
        btnSearch = (Button) rootView.findViewById(R.id.journey_search);
        etSearch = (EditTextRichDrawable) rootView.findViewById(R.id.activity_journey_search);
        searchLayout = (LinearLayout) rootView.findViewById(R.id.search_layout);
        noJounreysLayout = (ConstraintLayout) rootView.findViewById(R.id.no_journeys);
        //rv_container = (Fra) rootView.findViewById(R.id.rv_container);

        ivFilter = (ImageView) rootView.findViewById(R.id.filter);

        searchLayout.post(new Runnable() {
            @Override
            public void run() {
                heigth = searchLayout.getHeight();
                searchLayout.setY(-heigth);
                //searchLayout.bringToFront();

                ivFilter.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        toggle(searchLayout);
                    }
                });
            }
        });

        tgAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tgPast.setChecked(false);
                tgPast.setTextColor(ContextCompat.getColor(context, R.color.white));
                tgFuture.setChecked(false);
                tgFuture.setTextColor(ContextCompat.getColor(context, R.color.white));
                tgAll.setTextColor(ContextCompat.getColor(context, getFilterColorCode(tgAll.isChecked())));

                time = "0";
            }
        });
        tgPast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tgAll.setChecked(false);
                tgAll.setTextColor(ContextCompat.getColor(context, R.color.white));
                tgFuture.setChecked(false);
                tgFuture.setTextColor(ContextCompat.getColor(context, R.color.white));
                tgPast.setTextColor(ContextCompat.getColor(context, getFilterColorCode(tgPast.isChecked())));

                time = "1";
            }
        });
        tgFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tgAll.setChecked(false);
                tgAll.setTextColor(ContextCompat.getColor(context, R.color.white));
                tgPast.setChecked(false);
                tgPast.setTextColor(ContextCompat.getColor(context, R.color.white));
                tgFuture.setTextColor(ContextCompat.getColor(context, getFilterColorCode(tgFuture.isChecked())));

                time = "2";
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle(searchLayout);
                MyTicketsView.this.listener.search(time, etSearch.getText().toString());
                
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });



        /*ivBack = (ImageView) rootView.findViewById(R.id.activity_my_tickets_ala_action_bar_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNavigateBack();
            }
        });*/
    }

    private int getFilterColorCode(boolean isChecked) {
        int colorCode;
        return colorCode = ((isChecked == true) ? R.color.colorPrimary : R.color.white);
    }

    public void toggle(LinearLayout layout) {
        //http://stackoverflow.com/questions/19016674/android-translate-animation-permanently-move-view-to-new-position-using-animat

        //int cardViewWiidth = swipeView.getInner_cardViewWidth();
        ObjectAnimator move_out = ObjectAnimator.ofFloat(layout, "translationY", 0, -heigth);
        ObjectAnimator move_in = ObjectAnimator.ofFloat(layout, "translationY", -heigth, 0);
        //http://stackoverflow.com/questions/13022387/how-to-position-a-view-so-the-click-works-after-an-animation
        //http://stackoverflow.com/questions/9398057/android-move-a-view-on-touch-move-action-move

        if (search) {
            move_in.start();
        } else {
            move_out.start();
        }
        search = !search;

    }

    public void showNoJounrey(boolean show){
        if (show){
            noJounreysLayout.setVisibility(View.VISIBLE);
        }else {
            noJounreysLayout.setVisibility(View.GONE);
        }

    }

    public void showProgress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });
    }

    public void hideProgress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void setAdapter(SimpleSectionedRecyclerViewAdapter myTicketsAdapter) {
        lvHosts.setAdapter(myTicketsAdapter);
    }

    public void hideSearch() {
        handler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public interface MyTicketsViewListener {
        void onBackArrowPressed();

        void initToolbar(Toolbar toolbar);

        void search(String time, String query);
    }
}
