package team1XuongMobile.fpoly.myapplication.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import team1XuongMobile.fpoly.myapplication.R;


public class ManHinhChinhFragment extends Fragment {
    TextView tv_ten_nguoi_dung;
    EditText ed_cauhoi;
    ImageButton imgbt_sendcauhoi;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    FirebaseUser firebaseUser;
    String cauhoi;
    String[] Toemail;


    public ManHinhChinhFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_man_hinh_chinh, container, false);
        tv_ten_nguoi_dung = view.findViewById(R.id.tv_ten_nguoi_dung_manhinhchinh);
        ed_cauhoi = view.findViewById(R.id.ed_guicauhoi_manhinhchinh);
        imgbt_sendcauhoi = view.findViewById(R.id.imgbt_guicauhoi_manhinhchinh);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgbt_sendcauhoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cauhoi = ed_cauhoi.getText().toString();
                String email = "huy.mobcontact@gmail.com";
                 Toemail = email.split(",");
                if (ed_cauhoi.length() == 0) {
                    ed_cauhoi.requestFocus();
                    ed_cauhoi.setError("Hãy viết câu hỏi bạn muốn gửi");
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_EMAIL, Toemail);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Câu hỏi dành cho đội ngũ phát triển app");
                    intent.putExtra(Intent.EXTRA_TEXT, cauhoi);

                    intent.setType("message/rfc822");
                    startActivity(Intent.createChooser(intent, "Chọn Mail để gửi "));

                    ed_cauhoi.setText("");
                }

            }
        });

    }


}