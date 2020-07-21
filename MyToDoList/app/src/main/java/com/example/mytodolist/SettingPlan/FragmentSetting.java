package com.example.mytodolist.SettingPlan;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytodolist.R;
import com.example.mytodolist.TodayPlan.FragmentToday;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;


public class FragmentSetting extends Fragment implements View.OnClickListener {
    TextView tvSetting;
    Button btnRate, btnLanguage, btnAbout, btnBackground;
    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Locale locale;
    Resources resources;
    Configuration configuration;

    public FragmentSetting(Context context) {
        this.context = context;
    }

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getResources();
        configuration = resources.getConfiguration();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        btnRate = view.findViewById(R.id.btnRate);
        btnLanguage = view.findViewById(R.id.btnLanguage);
        btnAbout = view.findViewById(R.id.btnAbout);
        tvSetting = view.findViewById(R.id.tvSetting);
        btnBackground = view.findViewById(R.id.btnBackGround);


        btnBackground.setOnClickListener(this);
        btnRate.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnLanguage.setOnClickListener(this);

        sp = getActivity().getSharedPreferences("language", Context.MODE_PRIVATE);
        editor = sp.edit();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRate: {
                Toast.makeText(context, "Bao gio up len store thi lam", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btnLanguage: {
                PopupMenu popup = new PopupMenu(context, btnLanguage);
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.getMenuInflater().inflate(R.menu.menu_language, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                        //btnLanguage.setText(item.getTitle());
                        switch (item.getItemId()) {
                            case R.id.menuTiengViet: {

                                locale = new Locale("vi");

                                editor.putString("ngonngu", "vi");
                                editor.commit();
                                break;
                            }
                            case R.id.menuEnglish: {

                                locale = new Locale("en");

                                editor.putString("ngonngu", "en");
                                editor.commit();
                                break;
                            }
                        }
                        configuration.setLocale(locale);
                        getActivity().getResources().updateConfiguration(configuration,
                                getActivity().getResources().getDisplayMetrics());
                        getFragmentManager().beginTransaction().replace(R.id.container, new FragmentSetting(context)).addToBackStack(null).commit();
                        Snackbar snackbar = Snackbar.make(btnLanguage, R.string.snackbar_language, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        return true;
                    }
                });
                popup.show();
                break;
            }
            case R.id.btnAbout: {
                Toast.makeText(context, "Thong tin la bi mat", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btnBackGround: {
                getFragmentManager().beginTransaction().replace(R.id.container, new FragmentBackrground(context)).commit();
                break;
            }
        }
    }
}
