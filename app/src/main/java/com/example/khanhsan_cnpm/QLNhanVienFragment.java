package com.example.khanhsan_cnpm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class QLNhanVienFragment extends Fragment {

    private RecyclerView recyclerNhanVien;
    private Button btnThemNhanVien;
    private DatabaseReference nhanVienRef;
    private List<NhanVien> nhanVienList;
    private NhanVienAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qlnhanvien, container, false);

        recyclerNhanVien = view.findViewById(R.id.recyclerNhanVien);
        btnThemNhanVien = view.findViewById(R.id.btnThemNhanVien);

        nhanVienList = new ArrayList<>();
        adapter = new NhanVienAdapter(requireContext(), nhanVienList);
        recyclerNhanVien.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerNhanVien.setAdapter(adapter);

        adapter.setOnItemClickListener(new NhanVienAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(NhanVien nhanVien) {
                SuaNhanVienDialogFragment dialog = SuaNhanVienDialogFragment.newInstance(nhanVien);
                dialog.show(getParentFragmentManager(), "SuaNhanVienDialog");
            }

            @Override
            public void onDeleteClick(NhanVien nhanVien) {
                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference("nhanvien")
                        .child(nhanVien.getMaNV());
                ref.removeValue();
            }
        });

        nhanVienRef = FirebaseDatabase.getInstance().getReference("nhanvien");
        nhanVienRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nhanVienList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    NhanVien nv = data.getValue(NhanVien.class);
                    if (nv != null) {
                        nhanVienList.add(nv);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btnThemNhanVien.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container_main, new ThemNhanVienFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
