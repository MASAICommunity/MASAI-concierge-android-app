package solutions.masai.masai.android.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.none.emojioneandroidlibrary.Emojione;

import org.lucasr.twowayview.TwoWayView;

import java.io.File;
import java.util.Calendar;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.TravelfolderUserRepo;
import solutions.masai.masai.android.core.communication.ConnectionManager;
import solutions.masai.masai.android.core.communication.CustomRequests;
import solutions.masai.masai.android.core.communication.HostConnection;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.DialogHelper;
import solutions.masai.masai.android.core.helper.RealmHelper;
import solutions.masai.masai.android.core.helper.TimeHelper;
import solutions.masai.masai.android.core.model.GooglePlace;
import solutions.masai.masai.android.core.model.Message;
import solutions.masai.masai.android.core.model.MessageAttachment;
import solutions.masai.masai.android.core.model.MessageType;
import solutions.masai.masai.android.core.model.PermissionQuery;
import solutions.masai.masai.android.core.model.RocketChatUrl;
import solutions.masai.masai.android.core.model.SyncState;
import solutions.masai.masai.android.core.model.Train;
import solutions.masai.masai.android.core.model.TravelFolderPermissionResponse;
import solutions.masai.masai.android.core.model.travelfolder_user.ContactInfo;
import solutions.masai.masai.android.profile.controller.my_data.ContactInfoController;

public class ChatAdapter extends ArrayAdapter<Message> {
    private int connectionPositionOnList;
    private OnChatAdapterListener listener;
    private static String temporaryImagePath = null;
    private static String temporaryImageName = null;
    private String userId;
    private String token;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isShowingPermissionDialog = false;
    private boolean showPermissionRequest;
    private boolean isShowingEmailPermissionDialog = false;

    private String roomId;

    public ChatAdapter(Context context, int connectionPositionOnList, String roomid, boolean showPermissionRequest, OnChatAdapterListener
            listener) {
        super(context, R.layout.item_list_chat);
        this.connectionPositionOnList = connectionPositionOnList;
        this.showPermissionRequest = showPermissionRequest;
        this.roomId = roomid;
        this.listener = listener;
        userId = ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost().getRcUser()
                .getUserId();
        token = ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost().getRcUser().getUserId();
    }

    public void setTemporaryImagePath(String imagePath, String imageName) {
        temporaryImagePath = imagePath;
        temporaryImageName = imageName;
    }

    @Override
    public int getCount() {
        return RealmHelper.getInstance().getMessages(roomId).size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = RealmHelper.getInstance().getMessages(roomId).get(position);
        String messageString = message.getMessage();
        convertView = null;
        if (messageString.startsWith("{") || messageString.startsWith("[")) {
            Train[] trains = ChatUtilities.getTrains(messageString);
            if (trains != null && trains.length > 0) {
                convertView = loadTrainCards(trains, convertView, parent, position);
            } else {
                GooglePlace[] places = ChatUtilities.getGooglePlaces(messageString);
                if (places != null && places.length > 0) {
                    convertView = loadGoogleCards(places, convertView, parent, position);
                } else {
                    PermissionQuery permissionQuery = ChatUtilities.getPermissionQuery(messageString);
                    if (permissionQuery != null && permissionQuery.getPayload() != null && permissionQuery.getUrl()
                            != null) {
                        convertView = loadPermissionQuery(message, permissionQuery, convertView, parent, position);
                    } else {
                        TravelFolderPermissionResponse permissionResponse = ChatUtilities.getTravelFolderPermissionResponse(messageString);
                        if (permissionResponse != null && permissionResponse.getStatus() != null) {
                            convertView = loadPermissionResponse(permissionResponse, convertView, parent, position);
                        }
                    }
                }
            }
        }
        String imageUrl = null;
        String imageLocalPath = null;
        if (message.getAttachments() != null && message.getAttachments().length > 0) {
            for (MessageAttachment attachment : message.getAttachments()) {
                if (attachment.getTitle().equals(C.TITLE_VALUE_FILE) && !attachment.getTitleUrl().isEmpty()) {
                    imageLocalPath = attachment.getTitleUrl();
                }
                if (attachment.getImageUrl() != null && !attachment.getImageUrl().isEmpty()) {
                    imageUrl = attachment.getImageUrl();
                }
            }
        }
        if (convertView == null) {
            convertView = loadStandardMessage(imageLocalPath, imageUrl, message, convertView, parent, position);
        }
        return convertView;
    }

    private RocketChatUrl[] loadUrlInterpretations(String urls, LinearLayout llInterpretationHolder) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        RocketChatUrl rocketChatUrl[] = new Gson().fromJson(urls, RocketChatUrl[].class);
        for (RocketChatUrl url : rocketChatUrl) {
            if (url != null) {
                LinearLayout llUrlInterpretation = (LinearLayout) inflater.inflate(R.layout.item_url_interpretation, null, false);
                llUrlInterpretation.setTag(url.getUrl());
                llUrlInterpretation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getTag() != null) {
                            startBrower((String) view.getTag());
                        }
                    }
                });
                TextView tvContent = (TextView) llUrlInterpretation.findViewById(R.id.url_interpretation_domain);
                TextView tvTitle = (TextView) llUrlInterpretation.findViewById(R.id.url_interpretation_title);
                TextView tvUrl = (TextView) llUrlInterpretation.findViewById(R.id.url_interpretation_url);
                ImageView imageView = (ImageView) llUrlInterpretation.findViewById(R.id.url_interpretation_image);
                if (url.getMeta() != null && url.getMeta().getDescription() != null) {
                    tvContent.setText(url.getMeta().getDescription());
                }
                if (url.getMeta() != null && url.getMeta().getPageTitle() != null) {
                    String modedTitle = url.getMeta().getPageTitle().replace("\n", "");
                    tvTitle.setText(modedTitle);
                }
                if (url.getMeta() != null && url.getParsedUrl() != null && url.getParsedUrl().getHost() != null) {
                    tvUrl.setText(url.getParsedUrl().getHost());
                }
                if (url.getMeta() != null && url.getMeta().getOgImage() != null) {
                    String imageUrl = "";
                    if (url.getMeta().getOgImage().startsWith("/")) {
                        imageUrl = "http://" + url.getParsedUrl().getHost() + url.getMeta().getOgImage();
                    } else {
                        imageUrl = url.getMeta().getOgImage();
                    }
                    Glide.with(getContext()).load(imageUrl).into(imageView);
                } else {
                    imageView.setVisibility(View.GONE);
                }
                llInterpretationHolder.addView(llUrlInterpretation);
            }
        }
        return rocketChatUrl;
    }

    private void startBrower(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        getContext().startActivity(i);
    }

    private View loadTrainCards(Train[] trains, View convertView, ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.item_horizontal_sublist, parent, false);
        TwoWayView twoWayView = (TwoWayView) convertView.findViewById(R.id.horizontal_sublist);
        TrainAdapter trainAdapter = new TrainAdapter(getContext(), trains);
        twoWayView.setAdapter(trainAdapter);
        return convertView;
    }

    private View loadPermissionQuery(final Message message, final PermissionQuery permissionQuery, View convertView, ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.item_permission_query, parent, false);

        if(showPermissionRequest) {
            if(!isShowingEmailPermissionDialog) {
                ContactInfo contactInfo = TravelfolderUserRepo.getInstance().getTravelfolderUser().getContactInfo();
                if (!message.isPermissionConfirmed() && (contactInfo == null || contactInfo.getPrimaryEmail() == null ||
                        contactInfo.getPrimaryEmail().isEmpty())) {
                    isShowingPermissionDialog = true;
                    isShowingEmailPermissionDialog = true;
                    DialogHelper.dialogYesNo(getContext(), R.string.permission_title, R.string.permission_email_check, new DialogHelper
                            .OnYesNoDialogCallback() {
                        @Override
                        public void onYes() {
                            isShowingEmailPermissionDialog = false;
                            isShowingPermissionDialog = false;
                            Intent myIntent = new Intent(getContext(), ContactInfoController.class);
                            getContext().startActivity(myIntent);
                        }

                        @Override
                        public void onNo() {
                            isShowingEmailPermissionDialog = false;
                            RealmHelper.getInstance().updateMessageRequestPermissionState(roomId, message.getId(), true);
                            isShowingPermissionDialog = false;
                            sendPermissionMessage(message, false);
                        }
                    });
                }
            }

            if (!message.isPermissionConfirmed() && !isShowingPermissionDialog) {
                isShowingPermissionDialog = true;
                DialogHelper.dialogYesNo(getContext(), R.string.request_access, R.string.request_access_query, new DialogHelper.OnYesNoDialogCallback() {

                    @Override
                    public void onYes() {
                        RealmHelper.getInstance().updateMessageRequestPermissionState(roomId, message.getId(), true);
                        CustomRequests.grantAccessToTravelFolder(permissionQuery);
                        isShowingPermissionDialog = false;
                        sendPermissionMessage(message, true);
                    }

                    @Override
                    public void onNo() {
                        RealmHelper.getInstance().updateMessageRequestPermissionState(roomId, message.getId(), true);
                        isShowingPermissionDialog = false;
                        sendPermissionMessage(message, false);
                    }
                });
            }
        }
        return convertView;
    }

    private View loadPermissionResponse(final TravelFolderPermissionResponse permissionResponse, View convertView, ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.item_permission_response, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_permission_response);
        if (permissionResponse.getStatus().equals(TravelFolderPermissionResponse.PERMISSION_GRANTED)) {
            textView.setTextColor(getContext().getResources().getColor(R.color.green));
            textView.setText(R.string.permission_granted);
        } else {
            textView.setTextColor(getContext().getResources().getColor(R.color.red));
            textView.setText(R.string.permission_denied);
        }
        return convertView;
    }

    private View showChatClosedMessage(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View convertView = inflater.inflate(R.layout.item_permission_response, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_permission_response);
        textView.setText(R.string.chat_closed);
        return convertView;
    }

    private void sendPermissionMessage(Message message, boolean granted) {
        TravelFolderPermissionResponse permissionResponse = new TravelFolderPermissionResponse();
        if (granted) {
            permissionResponse.setRid(message.getRoomId());
            permissionResponse.setGrantedFor(ConnectionManager.getInstance().getUser().getAuth0Id());
            permissionResponse.setStatus(TravelFolderPermissionResponse.PERMISSION_GRANTED);
        } else {
            permissionResponse.setStatus(TravelFolderPermissionResponse.PERMISSION_DENIED);
        }
        String permissionResponseString = new Gson().toJson(permissionResponse);
        ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList)
                .sendMessage(null, message.getRoomId(), permissionResponseString, 0, 0, new HostConnection.ISendMessagesCallback() {
                    @Override
                    public void onMessageStateUpdated() {
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onConnectivityError() {
                        notifyDataSetChanged();
                    }
                });
    }

    private void downloadImage(String imageUrl, final ViewHolder viewHolder, final boolean animate) {
        if (imageUrl.startsWith("/")) {
            imageUrl = imageUrl.substring(1, imageUrl.length());
        }
        final String url = ConnectionManager.getInstance().getHostConnectionForPos(connectionPositionOnList).getHost().getHostRocketChatApiUrl().concat(imageUrl);
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Cookie", "rc_uid=" + userId + ";rc_token=" + token)
                .build());
        C.L("Loading image - downloading image " + url);
        DrawableRequestBuilder<GlideUrl> drawableRequestBuilder;
        try {
            if (animate) {
                drawableRequestBuilder = Glide.with(getContext()).load(glideUrl).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.add_message);
            } else {
                drawableRequestBuilder = Glide.with(getContext()).load(glideUrl).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade();
            }
            final String finalImageUrl = imageUrl;
            RequestListener<GlideUrl, GlideDrawable> imageListener = new RequestListener<GlideUrl, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {
                    C.L("Loading image " + url);
                    if (e != null) {
                        C.L("Loading image downloading image url onException " + e.getMessage());
                    }

                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    C.L("Loading image downloading image onResourceReady");
                    listener.onImageLoaded();
                    return false;
                }
            };
            drawableRequestBuilder.listener(imageListener).into(viewHolder.ivMessageImage);
            viewHolder.ivMessageImage.setTag(R.string.image_view_tag_source_url, url);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
    }

    private void loadTemporaryLocalImageAndStartImageDownload(final ViewHolder viewHolder, final String imageUrl) {
        ChatUtilities.loadFromFile(getContext(), temporaryImagePath, viewHolder.ivMessageImage, new RequestListener<File, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
                C.L("FLEFLE loading temp img exception");
                temporaryImagePath = null;
                downloadImage(imageUrl, viewHolder, true);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                C.L("FLEFLE loading temp image ready");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        temporaryImagePath = null;
                        downloadImage(imageUrl, viewHolder, false);
                    }
                }, C.SEC_5);
                listener.onImageLoaded();
                return false;
            }
        });
    }

    private boolean loadImageIntoImageView(String localImagePath, final Message message, final String imageUrl, final ViewHolder viewHolder, final boolean isMessageMine) {
        if ((imageUrl != null && !imageUrl.isEmpty()) ||
                (localImagePath != null && !localImagePath.isEmpty() &&
                        (localImagePath.endsWith(".jpg") || localImagePath.endsWith(".jpeg")
                                || localImagePath.endsWith(".png") || localImagePath.endsWith(".gif")))) {
            C.L("Loading image");
            viewHolder.rlMyMessage.setVisibility(View.GONE);
            viewHolder.llTheirMessage.setVisibility(View.GONE);
            viewHolder.rlMessageImage.setVisibility(View.VISIBLE);
            viewHolder.ivMessageImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChatUtilities.openPhotoPreview(getContext(), view, connectionPositionOnList);
                }
            });
            String date = TimeHelper.getHumanReadableDate(getContext(), message.getTimeStamp());
            String whosImage = ChatUtilities.getAppropriateMessageSender(message);
            String userSharedPhotoText = String.format(getContext().getString(R.string.user_shared_photo_at), whosImage, date);
            viewHolder.tvMessageImageText.setText(userSharedPhotoText);
            setSyncState(viewHolder.ivMessageImageStatus, message.getSyncstate());
            if ((imageUrl == null || imageUrl.isEmpty())) {
                C.L("Loading image - not yet uploaded loading local");
                ChatUtilities.loadFromFile(getContext(), localImagePath, viewHolder.ivMessageImage, null);
            } else if ((localImagePath == null || localImagePath.isEmpty())) {
                if (temporaryImagePath != null && imageUrl.contains(temporaryImageName)) {
                    C.L("Loading image - image uploaded loading temporary image");
                    loadTemporaryLocalImageAndStartImageDownload(viewHolder, imageUrl);
                } else {
                    C.L("Loading image - no local info about the image, downloading, " + imageUrl);
                    downloadImage(imageUrl, viewHolder, true);
                }
            }
            return true;
        } else {
            viewHolder.rlMessageImage.setVisibility(View.GONE);
            return false;
        }
    }

    private View loadGoogleCards(GooglePlace[] googlePlaces, View convertView, ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.item_horizontal_sublist, parent, false);
        TwoWayView twoWayView = (TwoWayView) convertView.findViewById(R.id.horizontal_sublist);
        GooglePlacesAdapter googlePlacesAdapter = new GooglePlacesAdapter(getContext(), googlePlaces);
        twoWayView.setAdapter(googlePlacesAdapter);
        return convertView;
    }

    private void setMessageSpacingBasedOnSender(ViewHolder viewHolder, Message message, Message previousMessage) {
        if (previousMessage == null || !message.hasSameSenderAs(previousMessage)) {
            viewHolder.llTheirMessageLogo.setVisibility(View.VISIBLE);
            viewHolder.margin.setVisibility(View.VISIBLE);
        } else {
            viewHolder.margin.setVisibility(View.GONE);
            viewHolder.llTheirMessageLogo.setVisibility(View.INVISIBLE);
        }
    }

    private View loadStandardMessage(String localImagePath, String imageUrl, Message message, View convertView, ViewGroup parent, int position) {
        final ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_message, parent, false);
            viewHolder.llTheirMessage = (LinearLayout) convertView.findViewById(R.id.message_item_their_ll);
            viewHolder.rlMyMessage = (RelativeLayout) convertView.findViewById(R.id.message_item_my_rl);
            viewHolder.tvTheirMessageText = (TextView) convertView.findViewById(R.id.message_item_their_message);
            viewHolder.tvMyMessageText = (TextView) convertView.findViewById(R.id.message_item_my_message);
            viewHolder.civMyMessageMap = (CustomImageView) convertView.findViewById(R.id.message_item_my_map_view);
            viewHolder.tvMyMessageAddress = (TextView) convertView.findViewById(R.id.message_item_my_map_address);
            viewHolder.tvTheirMessageLogoText = (TextView) convertView.findViewById(R.id.message_item_their_logo_text);
            viewHolder.ivMessageLogoImage = (ImageView) convertView.findViewById(R.id.message_item_their_logo_image);
            viewHolder.llTheirMessageLogo = (LinearLayout) convertView.findViewById(R.id.message_item_their_logo);
            viewHolder.ivMyMessageState = (ImageView) convertView.findViewById(R.id.message_item_my_message_state_ic);
            viewHolder.tvMessageTime = (TextView) convertView.findViewById(R.id.message_item_time);
            viewHolder.tvMyMessageTime = (TextView) convertView.findViewById(R.id.message_item_my_message_time);
            viewHolder.tvTheirMessageTime = (TextView) convertView.findViewById(R.id.message_item_their_message_time);
            viewHolder.tvTheirMessageTime = (TextView) convertView.findViewById(R.id.message_item_their_message_time);
            viewHolder.ivMessageImage = (ImageView) convertView.findViewById(R.id.message_item_image);
            viewHolder.tvMessageImageText = (TextView) convertView.findViewById(R.id.message_item_image_text);
            viewHolder.rlMessageImage = (RelativeLayout) convertView.findViewById(R.id.message_item_image_rl);
            viewHolder.llUrlInterpretation = (LinearLayout) convertView.findViewById(R.id.message_item_interpretation_ll);
            viewHolder.ivMessageImageStatus = (ImageView) convertView.findViewById(R.id.message_item_image_status);
            viewHolder.tvMyMessageMapTime = (TextView) convertView.findViewById(R.id.message_item_my_message_time_image);
            viewHolder.margin = convertView.findViewById(R.id.message_item_margin_top);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String messageString = "";
        viewHolder.tvMyMessageMapTime.setVisibility(View.GONE);
        boolean isMessageMine = userId.equals(message.getUser().getId());
        Message previousMessage = position - 1 >= 0 ? RealmHelper.getInstance().getMessages(roomId).get(position - 1) : null;
        setMessageSpacingBasedOnSender(viewHolder, message, previousMessage);
        boolean isRegularTextMessage = !loadImageIntoImageView(localImagePath, message, imageUrl, viewHolder, isMessageMine);
        if (isRegularTextMessage) {
            String urls = message.getUrls();
            messageString = message.getMessage();
            if (urls != null && !urls.isEmpty()) {
                RocketChatUrl urlsToRemove[] = loadUrlInterpretations(urls, viewHolder.llUrlInterpretation);
                for (RocketChatUrl urlToRemove : urlsToRemove) {
                    if (urlToRemove.getUrl() != null && urlToRemove.getParsedUrl() != null && urlToRemove.getParsedUrl().getHost() != null) {
                        messageString = messageString.replace(urlToRemove.getUrl(), urlToRemove.getParsedUrl().getHost());
                    }
                }
            }
            if (message.getAttachments() != null) {
                for (MessageAttachment attachment : message.getAttachments()) {
                    if (attachment.getTitle() != null && !attachment.getTitle().isEmpty() && (attachment.getImageType() == null || attachment.getImageType().isEmpty())) {
                        String title = "";
                        if (attachment.getTitleUrl() != null) {
                            String filePath = attachment.getTitleUrl();
                            String[] pathSegments = filePath.split("/");
                            title = pathSegments[pathSegments.length - 1];
                        } else if (!attachment.getTitle().equals(C.TITLE_VALUE_FILE)) {
                            title = attachment.getTitle();
                        }
                        messageString = title.concat(" ").concat(C.FILE_FOLDER_ICON);
                    }
                }
            }
            boolean isSingleEmoji = Emojione.isOneEmojiWithNoText(messageString);
            if (MessageType.USER_JOINED.getType().equals(message.getType())) {
                messageString = String.format(getContext().getString(R.string.user_joined), messageString);
            }
            Spannable text = getMessageWithEmoji(messageString, isSingleEmoji);
            showMessageTime(viewHolder, message, previousMessage);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(message.getTimeStamp());
            if (isMessageMine) {
                viewHolder.tvMyMessageText.setText(text);
                if (isSingleEmoji) {
                    viewHolder.tvMyMessageText.setBackground(null);
                } else {
                    viewHolder.tvTheirMessageText.setBackgroundResource(R.drawable.round_bg_flat_blue);
                }

                if(message.isSearched()) {
                    viewHolder.tvMyMessageText.setBackgroundResource(R.drawable.round_bg_green);
                }
                viewHolder.llTheirMessage.setVisibility(View.GONE);
                viewHolder.rlMyMessage.setVisibility(View.VISIBLE);
                viewHolder.tvMyMessageTime.setVisibility(View.VISIBLE);
                viewHolder.tvTheirMessageTime.setVisibility(View.GONE);
                viewHolder.tvMyMessageTime.setText(TimeHelper.getTimeOfDay(getContext(), calendar));
                setSyncState(viewHolder.ivMyMessageState, message.getSyncstate());
                if (message.getLocation() != null) {
                    String latitude = Float.toString(message.getLocation().getLat());
                    String longitude = Float.toString(message.getLocation().getLng());
                    viewHolder.civMyMessageMap.setVisibility(View.VISIBLE);
                    viewHolder.tvMyMessageAddress.setVisibility(View.VISIBLE);
                    if (message.getLocation().getLng() != 0 && message.getLocation().getLat() != 0) {
                        viewHolder.tvMyMessageMapTime.setVisibility(View.VISIBLE);
                        viewHolder.tvMyMessageMapTime.setText(TimeHelper.getTimeOfDay(getContext(), calendar));
                        viewHolder.tvMyMessageTime.setVisibility(View.GONE);
                        float scale = ((float) getContext().getResources().getDimension(R.dimen.map_scale) / 10);
                        String mapWidth = Integer.toString((int) (getContext().getResources().getDimension(R.dimen.chat_view_map_view_width) * scale));
                        String mapHeight = Integer.toString((int) (getContext().getResources().getDimension(R.dimen.chat_view_map_view_height) * scale));
                        String requestMapString = "https://maps.googleapis.com/maps/api/staticmap?center=".concat(latitude).concat(",").concat(longitude)
                                .concat("&zoom=16&size=").concat(mapWidth).concat("x").concat(mapHeight)
                                .concat("&maptype=roadmap%20&markers=color:red%7Clabel:S%7C").concat(latitude).concat(",").concat(longitude)
                                .concat("&scale=2&key=").concat(C.GOOGLE_MAPS_API_KEY);
                        C.L("trying map scale = " + Float.toString(scale));
                        C.L("trying map with url = " + requestMapString);
                        Glide.with(getContext()).load(requestMapString).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        }).into(viewHolder.civMyMessageMap);
                        CustomRequests.getAddressFromCoordinates(message.getLocation().getLat(), message.getLocation().getLng(), viewHolder.tvMyMessageAddress, handler);
                    }
                    viewHolder.tvMyMessageText.setVisibility(View.GONE);
                } else {
                    viewHolder.civMyMessageMap.setVisibility(View.GONE);
                    viewHolder.tvMyMessageAddress.setVisibility(View.GONE);
                }
            } else {
                viewHolder.tvTheirMessageTime.setVisibility(View.VISIBLE);
                viewHolder.tvMyMessageTime.setVisibility(View.GONE);
                viewHolder.tvTheirMessageTime.setText(TimeHelper.getTimeOfDay(getContext(), calendar));
                loadAvatarImage(message, viewHolder);
                if (isSingleEmoji) {
                    viewHolder.tvTheirMessageText.setBackground(null);
                } else {
                    viewHolder.tvTheirMessageText.setBackgroundResource(R.drawable.round_bg_red);
                }

                if(message.isSearched()) {
                    viewHolder.tvTheirMessageText.setBackgroundResource(R.drawable.round_bg_green);
                }

                if(messageString.equals(getContext().getString(R.string.chat_closed))) {
                    return showChatClosedMessage(parent);
                }
                viewHolder.tvTheirMessageText.setText(text);
                viewHolder.llTheirMessage.setVisibility(View.VISIBLE);
                viewHolder.rlMyMessage.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private void loadAvatarImage(Message message, ViewHolder viewHolder) {
        String username = message.getUser().getUsername();
        String rocketchatUrl = ConnectionManager.getInstance().getHostUrlByRoomId(message.getRoomId());
        Glide.with(getContext()).load(rocketchatUrl + C.ROCKETCHAT_AVATAR_PARTIAL_ROUTE + username)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        viewHolder.tvTheirMessageLogoText.setText(message.getUser().getUsername().substring(0, 2));
                        viewHolder.ivMessageLogoImage.setVisibility(View.GONE);
                        viewHolder.llTheirMessageLogo.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        viewHolder.llTheirMessageLogo.setVisibility(View.GONE);
                        viewHolder.ivMessageLogoImage.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(viewHolder.ivMessageLogoImage);
    }

    private Spannable getMessageWithEmoji(String orgText, boolean large) {
        Spannable spannable = Emojione.replaceAll(getContext(), orgText, large);
        return spannable;
    }

    private void showMessageTime(ViewHolder viewHolder, Message message, Message previousMessage) {
        if ((previousMessage == null) || (previousMessage != null && message.getTimeStamp() - previousMessage.getTimeStamp() > C.MIN_TIME_DIFFERENCE)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(message.getTimeStamp());
            viewHolder.tvMessageTime.setText(TimeHelper.getHumanReadableDate(getContext(), calendar));
            viewHolder.tvMessageTime.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvMessageTime.setText("");
            viewHolder.tvMessageTime.setVisibility(View.GONE);
        }
    }

    private void setSyncState(ImageView ivState, int syncstate) {
        int marginSS = (int) getContext().getResources().getDimension(R.dimen.margin_ss);
        int marginXS = (int) getContext().getResources().getDimension(R.dimen.margin_xs);
        switch (syncstate) {
            case SyncState.NOT_SYNCED:
            case SyncState.SYNCED:
                ivState.setImageResource(R.drawable.ic_message_sent);
                ivState.setPadding(marginXS, marginXS, marginXS, marginXS);
                break;
            case SyncState.SYNCING:
                ivState.setImageResource(R.drawable.ic_message_sending);
                ivState.setPadding(marginXS, marginXS, marginXS, marginXS);
                break;
            case SyncState.FAILED:
                ivState.setImageResource(R.drawable.ic_message_failed);
                ivState.setPadding(marginSS, marginSS, marginSS, marginSS);
                break;
        }
    }

    public interface OnChatAdapterListener {
        void onImageLoaded();
    }

    private static class ViewHolder {
        View margin;
        RelativeLayout rlMyMessage;
        LinearLayout llTheirMessage;
        LinearLayout llTheirMessageLogo;
        LinearLayout llUrlInterpretation;
        TextView tvTheirMessageText;
        TextView tvMyMessageText;
        CustomImageView civMyMessageMap;
        TextView tvMyMessageMapTime;
        TextView tvMyMessageAddress;
        TextView tvMyMessageTime;
        TextView tvTheirMessageTime;
        RelativeLayout rlMessageImage;
        ImageView ivMessageImage;
        ImageView ivMessageImageStatus;
        TextView tvMessageImageText;
        TextView tvTheirMessageLogoText;
        ImageView ivMessageLogoImage;
        ImageView ivMyMessageState;
        TextView tvMessageTime;
    }
}
