package com.example.khanhsan_cnpm;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChonAdapter extends RecyclerView.Adapter<ChonAdapter.ViewHolder> {

    private final List<Chon> roles;
    private String selectedKey = null;
    private final OnRoleSelectedListener listener;
    public interface OnRoleSelectedListener {
        void onRoleSelected(String key);
    }

    public ChonAdapter(List<Chon> roles, OnRoleSelectedListener listener) {
        this.roles = roles;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        CardView card = new CardView(context);
        card.setCardElevation(8);
        card.setRadius(24);
        card.setUseCompatPadding(true);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        card.setContentPadding(32, 32, 32, 32);
        card.setCardBackgroundColor(Color.WHITE);

        layout.setPadding(16, 16, 16, 16);
        card.addView(layout);

        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chon role = roles.get(position);
        Context context = holder.itemView.getContext();
        LinearLayout layout = (LinearLayout) ((CardView) holder.itemView).getChildAt(0);
        layout.removeAllViews();

        ImageView icon = new ImageView(context);
        icon.setImageResource(role.iconRes);
        icon.setColorFilter(ContextCompat.getColor(context, android.R.color.white));
        icon.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_edittext));
        icon.setPadding(32, 32, 32, 32);

        TextView title = new TextView(context);
        title.setText(role.name);
        title.setTextSize(20);
        title.setTextColor(Color.BLACK);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 16, 0, 16);

        layout.addView(icon);
        layout.addView(title);

        if (role.key.equals(selectedKey)) {
            TextView selected = new TextView(context);
            selected.setText("✓ Đã chọn");
            selected.setTextColor(ContextCompat.getColor(context, R.color.blue_600));
            selected.setTypeface(null, Typeface.BOLD);
            selected.setGravity(Gravity.CENTER);
            layout.addView(selected);
        }

        holder.itemView.setOnClickListener(v -> {
            selectedKey = role.key;
            listener.onRoleSelected(role.key);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return roles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
