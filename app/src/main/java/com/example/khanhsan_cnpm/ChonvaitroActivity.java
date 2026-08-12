package com.example.khanhsan_cnpm;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.TypedValue;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ChonvaitroActivity extends AppCompatActivity {

    private LinearLayout cardContainer;
    private Button btnContinue;
    private TextView tvHint;

    private String selectedRole = null;

    private final List<CardView> roleCards = new ArrayList<>();
    private final List<TextView> titleTexts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chonvaitro);

        cardContainer = findViewById(R.id.card_container);
        btnContinue = findViewById(R.id.btn_continue);
        tvHint = findViewById(R.id.tv_hint);

        createRoleCard("Quản lý", "manager", R.drawable.outline_manage_accounts_24, R.color.blue_500);
        createRoleCard("Nhân viên", "employee", R.drawable.outline_supervised_user_circle_24, R.color.green_500);

        btnContinue.setOnClickListener(v -> {
            if (selectedRole != null) {
                if (selectedRole.equals("manager")) {
                    startActivity(new Intent(this, LoginQuanLyActivity.class));
                } else if (selectedRole.equals("employee")) {
                    startActivity(new Intent(this, LoginNhanVienActivity.class));
                }
            } else {
                tvHint.setVisibility(View.VISIBLE);
            }
        });
    }

    private void createRoleCard(String title, String roleKey, int iconRes, int colorRes) {
        int cardWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 105, getResources().getDisplayMetrics());

        int cardHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 105, getResources().getDisplayMetrics());

        CardView card = new CardView(this);
        card.setCardElevation(8);
        card.setRadius(24);
        card.setUseCompatPadding(true);
        card.setClickable(true);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(cardWidth, cardHeight);
        cardParams.setMargins(16, 16, 16, 16);
        card.setLayoutParams(cardParams);

        // Khởi tạo background mặc định bằng code
        GradientDrawable defaultBackground = new GradientDrawable();
        defaultBackground.setColor(Color.WHITE);
        defaultBackground.setCornerRadius(24f);
        defaultBackground.setStroke(0, Color.TRANSPARENT);
        card.setBackground(defaultBackground);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER_HORIZONTAL);
        content.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        content.setPadding(16, 16, 16, 16);

        ImageView icon = new ImageView(this);
        int iconSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 55, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(iconSize, iconSize);
        iconParams.setMargins(0, 0, 0, 24);
        icon.setLayoutParams(iconParams);
        icon.setImageResource(iconRes);
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        icon.setColorFilter(ContextCompat.getColor(this, android.R.color.white));

        int bgDrawable = roleKey.equals("manager")
                ? R.drawable.circle_blue
                : R.drawable.circle_green;
        icon.setBackground(ContextCompat.getDrawable(this, bgDrawable));

        TextView txtTitle = new TextView(this);
        txtTitle.setText(title);
        txtTitle.setTextSize(15);
        txtTitle.setTypeface(null, Typeface.NORMAL);
        txtTitle.setTextColor(Color.BLACK);
        txtTitle.setGravity(Gravity.CENTER);

        content.addView(icon);
        content.addView(txtTitle);
        card.addView(content);

        card.setOnClickListener(v -> {
            selectedRole = roleKey;
            updateCardSelection();
        });

        roleCards.add(card);
        titleTexts.add(txtTitle);
        cardContainer.addView(card);
    }

    private void updateCardSelection() {
        btnContinue.setEnabled(true);
        btnContinue.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.blue_600));
        tvHint.setVisibility(View.GONE);

        for (int i = 0; i < roleCards.size(); i++) {
            CardView card = roleCards.get(i);
            TextView title = titleTexts.get(i);

            boolean isSelected = (selectedRole.equals("manager") && i == 0) ||
                    (selectedRole.equals("employee") && i == 1);

            // Tạo viền bo tròn và màu nền trắng
            GradientDrawable background = new GradientDrawable();
            background.setColor(Color.WHITE);
            background.setCornerRadius(24f);

            if (isSelected) {
                background.setStroke(6, Color.BLACK);  // Viền đen đậm
                title.setTypeface(null, Typeface.BOLD);
            } else {
                background.setStroke(0, Color.TRANSPARENT);  // Không viền
                title.setTypeface(null, Typeface.NORMAL);
            }

            card.setBackground(background);
        }
    }
}

