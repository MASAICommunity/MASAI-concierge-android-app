package solutions.masai.masai.android.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.model.GooglePlace;

import java.util.Locale;

public class GooglePlacesAdapter extends ArrayAdapter<GooglePlace> {
    public GooglePlacesAdapter(Context context, GooglePlace[] train) {
        super(context, R.layout.item_google_card, train);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_google_card, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.google_place_name);
            viewHolder.tvRating = (TextView) convertView.findViewById(R.id.google_place_rating_text);
            viewHolder.tvType = (TextView) convertView.findViewById(R.id.google_place_type);
            viewHolder.ivPicture = (ImageView) convertView.findViewById(R.id.google_place_picture);
            viewHolder.llRating = (LinearLayout) convertView.findViewById(R.id.google_place_rating_ll);
            viewHolder.llSearch = (LinearLayout) convertView.findViewById(R.id.google_place_search);
            viewHolder.llCall = (LinearLayout) convertView.findViewById(R.id.google_place_call);
            viewHolder.llMap = (LinearLayout) convertView.findViewById(R.id.google_place_map);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GooglePlace googlePlace = getItem(position);
        if (googlePlace != null) {
            if (googlePlace.getName() != null) {
                viewHolder.tvName.setText(googlePlace.getName());
            }
            if (googlePlace.getRating() >= 0 && googlePlace.getRating() <= 5) {
                float rating = googlePlace.getRating();
                viewHolder.tvRating.setText(Float.toString(rating));
                setRating(viewHolder, (int) rating);
            }
            if (googlePlace.hasLocation()) {
                viewHolder.llMap.setVisibility(View.VISIBLE);
                viewHolder.llMap.setTag(R.string.google_cards_position_key, position);
                viewHolder.llMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = (int) view.getTag(R.string.google_cards_position_key);
                        GooglePlace googlePlace = getItem(pos);
                        startMap(googlePlace.getLatitude(), googlePlace.getLongitude(), googlePlace.getFormattedAddress());
                    }
                });
            } else {
                viewHolder.llMap.setVisibility(View.GONE);
            }
            if (googlePlace.getTypes() != null && googlePlace.getTypes().length > 0) {
                viewHolder.tvType.setText(googlePlace.getTypes()[0]);
            }
            if (googlePlace.getPhotoReference() != null) {
                int width = getContext().getResources().getDimensionPixelSize(R.dimen.card_image_width);
                String photoRequest = String.format(C.GOOGLE_PLACES_PHOTO_URL, googlePlace.getPhotoReference(), width);
                C.L("PHOTO REFERENCE URL = " + photoRequest);
                Glide.with(getContext()).load(photoRequest).into(viewHolder.ivPicture);
            }
            convertView.setTag(R.string.google_cards_position_key, position);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag(R.string.google_cards_position_key);
                    GooglePlace googlePlace = getItem(pos);
                    String search = "";
                    if (googlePlace.getFormattedAddress() != null) {
                        search = String.format("%s, %s", googlePlace.getName(), googlePlace.getFormattedAddress());
                    } else {
                        search = googlePlace.getName();
                    }
                    startSearch(search);
                }
            });
            viewHolder.llCall.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void startSearch(String name) {
        Uri uri = Uri.parse(String.format("http://www.google.com/#q=%s", name));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        getContext().startActivity(intent);
    }

    private void startMap(float latitude, float longitude, String name) {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", latitude, longitude, name);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        getContext().startActivity(intent);
    }

    private void setRating(ViewHolder viewHolder, int rating) {
        int pos = 0;
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                getContext().getResources().getDimensionPixelSize(R.dimen.star_size), getContext().getResources().getDimensionPixelSize(R.dimen.star_size));
        viewHolder.llRating.removeAllViews();
        while (pos < rating) {
            ImageView starSelected = new ImageView(getContext());
            starSelected.setLayoutParams(lparams);
            starSelected.setImageResource(R.drawable.ic_star_active);
            viewHolder.llRating.addView(starSelected);
            ++pos;
        }
        while (pos < 5) {
            ImageView starBlank = new ImageView(getContext());
            starBlank.setLayoutParams(lparams);
            starBlank.setImageResource(R.drawable.ic_star_inactive);
            viewHolder.llRating.addView(starBlank);
            ++pos;
        }
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvRating;
        TextView tvType;
        LinearLayout llRating;
        LinearLayout llSearch;
        LinearLayout llCall;
        LinearLayout llMap;
        ImageView ivPicture;
    }
}
