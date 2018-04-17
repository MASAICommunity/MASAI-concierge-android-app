package solutions.masai.masai.android.add_host;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.DialogHelper;

/**
 * Created by Semko on 2016-12-05.
 */

public class AddHostView {
    private View rootView;
    private AddHostViewListener listener;
    private Context context;
    private Handler handler;
    private ListView lvHosts;
    private TextView tvTitle;
    private ImageView ivBack;
    private ProgressDialog progressDialog;

    AddHostView(View rootView, Context context, AddHostViewListener listener) {
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
        lvHosts = (ListView) rootView.findViewById(R.id.activity_server_list_list_view);
        lvHosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                listener.onHostClicked(pos);
            }
        });
        tvTitle = (TextView) rootView.findViewById(R.id.activity_server_list_ala_action_bar_title);
        ivBack = (ImageView) rootView.findViewById(R.id.activity_server_list_ala_action_bar_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNavigateBack();
            }
        });
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

    public void setTitle(final String title) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                tvTitle.setText(title);
            }
        });
    }

    public void showOkDialog(final int message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.dialogOk(context, message);
            }
        });
    }


    public void setAddHostListAdapter(AddHostListAdapter addHostListAdapter) {
        lvHosts.setAdapter(addHostListAdapter);
    }

    public interface AddHostViewListener {
        void onHostClicked(int position);

        void onNavigateBack();
    }
}
