package solutions.masai.masai.android.chat;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.none.emojioneandroidlibrary.EmojiEditText;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.DialogHelper;
import solutions.masai.masai.android.core.helper.TimeHelper;

class ChatView {
    private static int MILLISECONDS_150 = 150;
    private static int MILLISECONDS_90 = 90;
    private static int MILLISECONDS_50 = 50;
    private static int MILLISECONDS_200 = 200;
    private static int MILLISECONDS_100 = 100;
    private static int MILLISECONDS_0 = 0;
    private static int ALPHA_OPAQUE = 1;
    private static int ALPHA_TRANSPARENT = 0;
    private View rootView;
    private ChatViewListener listener;
    private Context context;
    private ListView chatList;
    private Handler handler;
    private EmojiEditText etChat;
    private ImageView ivSend;
    private ImageView ivBack;
    private ImageView ivAdd;
    private TextView tvHeader;
    private TextView tvLogo;
    private RelativeLayout rlUploadOptions;
    private LinearLayout llUploadOptionsIcons;
    private View rlUploadOptionsOrange;
    private LinearLayout llChatEnterMessage;
    private CoordinatorLayout coordinatorLayout;
    private ImageView ivKeyboard1;
    private ImageView ivCamera2;
    private ImageView ivGallery3;
    private ImageView ivLocation4;
    private ImageView ivFiles5;
    private LinearLayout llImageMessage;
    private int numberOfNewMessages = 0;
    private boolean isLastPosVisible = false;
    private ProgressDialog progressDialog;
    private TextView tvReconnected;

    ChatView(View rootView, ChatViewListener listener) {
        this.rootView = rootView;
        this.context = rootView.getContext();
        this.listener = listener;
        handler = new Handler(Looper.getMainLooper());
        initViews();
        initList();
    }

    public int getNumberOfNewMessages() {
        return numberOfNewMessages;
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

    private void initViews() {
        progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(rootView.getContext().getString(R.string.loading_data));
        progressDialog.setCanceledOnTouchOutside(false);
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.activity_chat_cl);
        etChat = (EmojiEditText) rootView.findViewById(R.id.chat_et);
        etChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    ivSend.setVisibility(View.VISIBLE);
                } else {
                    ivSend.setVisibility(View.GONE);
                }
            }
        });
        ivSend = (ImageView) rootView.findViewById(R.id.chat_iv_send);
        ivSend.setVisibility(View.GONE);
        ivAdd = (ImageView) rootView.findViewById(R.id.chat_iv_add);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddClicked();
            }
        });
        chatList = (ListView) rootView.findViewById(R.id.lv_chat);
        ivBack = (ImageView) rootView.findViewById(R.id.chat_ala_action_bar_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNavigateBack();
            }
        });
        tvHeader = (TextView) rootView.findViewById(R.id.chat_ala_action_bar_title);
        tvLogo = (TextView) rootView.findViewById(R.id.chat_ala_action_bar_logo_tv);
        tvReconnected = (TextView) rootView.findViewById(R.id.chat_connected_message);
        rlUploadOptions = (RelativeLayout) rootView.findViewById(R.id.chat_add_things_rl);
        rlUploadOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This prevents touches from getting to the layout below
            }
        });
        rlUploadOptionsOrange = rootView.findViewById(R.id.chat_add_things_orange);
        llUploadOptionsIcons = (LinearLayout) rootView.findViewById(R.id.chat_add_things_icons_ll);
        llChatEnterMessage = (LinearLayout) rootView.findViewById(R.id.chat_enter_message_ll);
        ivKeyboard1 = (ImageView) rootView.findViewById(R.id.chat_add_things_keyboard_1);
        ivKeyboard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAddOptions();
            }
        });
        ivCamera2 = (ImageView) rootView.findViewById(R.id.chat_add_things_camera_2);
        ivCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTakePhotoClicked();
            }
        });
        llImageMessage = (LinearLayout) rootView.findViewById(R.id.chat_image_message);
        llImageMessage.setVisibility(View.GONE);
        ivGallery3 = (ImageView) rootView.findViewById(R.id.chat_add_things_gallery_3);
        ivGallery3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectPhotoFromGalleryClicked();
            }
        });
        ivLocation4 = (ImageView) rootView.findViewById(R.id.chat_add_things_location_4);
        ivLocation4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onShareLocationClicked();
            }
        });
        ivFiles5 = (ImageView) rootView.findViewById(R.id.chat_add_things_document_5);
        ivFiles5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectFileClicked();
            }
        });
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMessageSendClicked(etChat.getText().toString());
                etChat.setText("");
            }
        });
        prepareUploadOptionsAnimation();
    }

    private void resizeView(final View view, int startWidth, int endWidth) {
        ValueAnimator anim = ValueAnimator.ofInt(startWidth, endWidth);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = val;
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(MILLISECONDS_100);
        anim.start();
    }

    private void alphaAnimateView(final View view, float start, float end) {
        ValueAnimator anim = ValueAnimator.ofFloat(start, end);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        anim.setDuration(MILLISECONDS_100);
        anim.start();
    }

    public void showImageLoadingMessage() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                llImageMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideImageLoadingMessage() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                llImageMessage.setVisibility(View.GONE);
            }
        });
    }

    private void alphaShowButton(final View view, int time) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                alphaAnimateView(view, ALPHA_TRANSPARENT, ALPHA_OPAQUE);
            }
        }, time);
    }

    private void alphaHideView(final View view) {
        alphaAnimateView(view, ALPHA_OPAQUE, ALPHA_TRANSPARENT);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.INVISIBLE);
            }
        }, MILLISECONDS_100);
    }

    private void alphaHideButton(final View view, int time) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alphaHideView(view);
            }
        }, time);
    }

    private void prepareUploadOptionsAnimation() {
        llUploadOptionsIcons.setVisibility(View.GONE);
        ivKeyboard1.setVisibility(View.INVISIBLE);
        ivCamera2.setVisibility(View.INVISIBLE);
        ivGallery3.setVisibility(View.INVISIBLE);
        ivLocation4.setVisibility(View.INVISIBLE);
        ivFiles5.setVisibility(View.INVISIBLE);
    }

    public void showAddOptions() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                llUploadOptionsIcons.setVisibility(View.VISIBLE);
                rlUploadOptions.setVisibility(View.VISIBLE);
                rlUploadOptions.setAlpha(ALPHA_OPAQUE);
                rlUploadOptionsOrange.setAlpha(ALPHA_TRANSPARENT);
                resizeView(rlUploadOptions, llChatEnterMessage.getMeasuredHeight(), llChatEnterMessage.getMeasuredWidth());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlUploadOptionsOrange.setVisibility(View.VISIBLE);
                        alphaAnimateView(rlUploadOptionsOrange, ALPHA_TRANSPARENT, ALPHA_OPAQUE);
                        resizeView(rlUploadOptionsOrange, llChatEnterMessage.getMeasuredHeight(), llChatEnterMessage.getMeasuredWidth());
                    }
                }, MILLISECONDS_100);
                alphaShowButton(ivKeyboard1, MILLISECONDS_200);
                alphaShowButton(ivCamera2, MILLISECONDS_200);
                alphaShowButton(ivGallery3, MILLISECONDS_200);
                alphaShowButton(ivLocation4, MILLISECONDS_200);
                alphaShowButton(ivFiles5, MILLISECONDS_200);
            }
        });
    }

    public void hideAddOptions() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                alphaHideButton(ivFiles5, MILLISECONDS_0);
                alphaHideButton(ivLocation4, MILLISECONDS_0);
                alphaHideButton(ivGallery3, MILLISECONDS_0);
                alphaHideButton(ivCamera2, MILLISECONDS_0);
                alphaHideButton(ivKeyboard1, MILLISECONDS_0);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resizeView(rlUploadOptions, llChatEnterMessage.getMeasuredWidth(), llChatEnterMessage.getMeasuredHeight());
                    }
                }, MILLISECONDS_50);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alphaAnimateView(rlUploadOptions, ALPHA_OPAQUE, ALPHA_TRANSPARENT);
                        alphaAnimateView(rlUploadOptionsOrange, ALPHA_OPAQUE, ALPHA_TRANSPARENT);
                    }
                }, MILLISECONDS_90);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prepareUploadOptionsAnimation();
                        rlUploadOptionsOrange.setVisibility(View.GONE);
                        rlUploadOptions.setVisibility(View.GONE);
                    }
                }, MILLISECONDS_150);
            }
        });
    }

    private void showOkDialog(final int message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.dialogOk(context, message);
            }
        });
    }

    public void showImageIsNotLocalError() {
        showOkDialog(R.string.image_not_offline);
    }

    public void showUploadFailed() {
        showOkDialog(R.string.upload_failed);
    }

    public void showInvalidFileType() {
        showOkDialog(R.string.invalid_file_type);
    }

    public void showServerError() {
        showOkDialog(R.string.server_error);
    }

    public void showImageError() {
        showOkDialog(R.string.image_error);
    }

    public void showFileTooBigError() {
        showOkDialog(R.string.file_too_big_error);
    }

    public void setTitle(final String title) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                tvHeader.setText(title);
                tvLogo.setText(title.substring(0, 2));
            }
        });
    }

    private void initList() {
        chatList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount + 1 >= totalItemCount) {
                    numberOfNewMessages = 0;
                    isLastPosVisible = true;
                } else {
                    isLastPosVisible = false;
                }
            }
        });
        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                listener.onMessageClicked(pos);
            }
        });
    }

    public boolean isLastPosVisible() {
        return isLastPosVisible;
    }

    public void showInfoReconnecting() {
        tvReconnected.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvReconnected.setVisibility(View.GONE);
            }
        }, TimeHelper.SECONDS_THREE);
    }

    public void setChatListAdapter(final ChatAdapter adapter) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                chatList.setAdapter(adapter);
                chatList.setSelection(adapter.getCount() - 1);
            }
        });
    }

    public void updateList(final ChatAdapter adapter) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void scrollToBottom(final ChatAdapter adapter) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int lastVisiblePosition = chatList.getLastVisiblePosition();
                int firstVisiblePosition = chatList.getLastVisiblePosition();
                int visiblePositions = lastVisiblePosition - firstVisiblePosition;
                chatList.smoothScrollToPosition(adapter.getCount() - visiblePositions);
            }
        });
    }

    public void scrollToPosition(final ChatAdapter adapter, final int position) {
        if(position < chatList.getCount()) {
            chatList.smoothScrollToPosition(position);
        } else {
            scrollToBottom(adapter);
        }
    }

    public void hideMessageInput() {
        llChatEnterMessage.setVisibility(View.GONE);
    }

    public interface ChatViewListener {
        void onMessageSendClicked(String message);

        void onNavigateBack();

        void onMessageClicked(int position);

        void onAddClicked();

        void onTakePhotoClicked();

        void onShareLocationClicked();

        void onSelectPhotoFromGalleryClicked();

        void onSelectFileClicked();
    }
}
