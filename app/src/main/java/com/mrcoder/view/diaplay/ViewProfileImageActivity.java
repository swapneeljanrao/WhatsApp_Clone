package com.mrcoder.view.diaplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.mrcoder.R;
import com.mrcoder.common.Common;
import com.mrcoder.databinding.ActivityViewProfileImageBinding;

public class ViewProfileImageActivity extends AppCompatActivity {


    private ActivityViewProfileImageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_profile_image);

        binding.viewProfileZoomView.setImageBitmap(Common.IMAGE_BITMAP);
    }
}