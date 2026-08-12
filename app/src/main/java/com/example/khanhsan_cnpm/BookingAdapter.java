package com.example.khanhsan_cnpm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvCustomerName.setText(booking.getTenKhach());
        holder.tvBookingInfo.setText("Phòng " + booking.getSoPhong() + " • " + booking.getNgayDat());
        holder.tvBookingStatus.setText(booking.getTrangThai());

        // Đổi màu trạng thái
        if ("Đã xác nhận".equalsIgnoreCase(booking.getTrangThai())) {
            holder.tvBookingStatus.setBackgroundResource(R.drawable.circle_green);
        } else if ("Chờ xử lý".equalsIgnoreCase(booking.getTrangThai())) {
            holder.tvBookingStatus.setBackgroundResource(R.drawable.circle_yellow);
        } else {
            holder.tvBookingStatus.setBackgroundResource(R.drawable.circle_gray);
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvBookingInfo, tvBookingStatus;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvBookingInfo = itemView.findViewById(R.id.tvBookingInfo);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);
        }
    }
}
