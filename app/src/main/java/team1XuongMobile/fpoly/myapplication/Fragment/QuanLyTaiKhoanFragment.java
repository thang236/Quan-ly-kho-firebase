package team1XuongMobile.fpoly.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import team1XuongMobile.fpoly.myapplication.Adapter.NhanVienAdapter;
import team1XuongMobile.fpoly.myapplication.Adapter.QuanLyTaiKhoanAdapter;
import team1XuongMobile.fpoly.myapplication.Model.NhanVien;
import team1XuongMobile.fpoly.myapplication.R;


public class QuanLyTaiKhoanFragment extends Fragment implements QuanLyTaiKhoanAdapter.TaikhoanInterface {
    RecyclerView recyclerView_qltk;
    EditText inputsearchTK;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    String khstring = "";
    QuanLyTaiKhoanAdapter quanLyTaiKhoanAdapter;
    ArrayList<NhanVien> nhanVienArrayList;
    public static final String KEY_ID_NHAN_VIEN = "idNV";

    QuanLyTaiKhoanAdapter.TaikhoanInterface taikhoanInterface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_tai_khoan, container, false);
        recyclerView_qltk = view.findViewById(R.id.recyc_quanlytaikhoan);
        inputsearchTK = view.findViewById(R.id.edt_quanlytaikhoan_timkiem);
        taikhoanInterface = this;

        firebaseAuth = FirebaseAuth.getInstance();


        inputsearchTK.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    quanLyTaiKhoanAdapter.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loaddulieuQLTKFirebase();
        return view;

    }

    private void loaddulieuQLTKFirebase() {
        firebaseUser = firebaseAuth.getCurrentUser();
        nhanVienArrayList = new ArrayList<>();
        if (firebaseUser == null) {
            return;
        }
        String uid = firebaseUser.getUid();
        DatabaseReference useref = FirebaseDatabase.getInstance().getReference("Accounts").child(uid);
        useref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                khstring = "" + snapshot.child("kh").getValue(String.class);
                if (khstring != null) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Quan_Ly_Tai_Khoan");
                    Query query = ref.orderByChild("kh").equalTo(khstring);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            nhanVienArrayList.clear();
                            for (DataSnapshot dsnv : snapshot.getChildren()) {
                                NhanVien themnhanvien = dsnv.getValue(NhanVien.class);
                                nhanVienArrayList.add(themnhanvien);
                                Log.d("quanquan", "list" + nhanVienArrayList);
                            }
                            quanLyTaiKhoanAdapter = new QuanLyTaiKhoanAdapter(getContext(), nhanVienArrayList, taikhoanInterface);
                            recyclerView_qltk.setAdapter(quanLyTaiKhoanAdapter);
                            recyclerView_qltk.setLayoutManager(new LinearLayoutManager(getContext()));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Không lấy được dữ liệu lên Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void CachChucTKClick(String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("vaiTro", "Tổ Trưởng");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có muốn cách chức nhân viên này?");

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (id != null && !id.isEmpty()) {
                    ref.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(), "Cách Chức Thành Công", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Cách Chức Thất Bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void laydulieudangnhap() {
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                khstring = "" + snapshot.child("kh").getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}