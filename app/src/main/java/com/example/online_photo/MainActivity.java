package com.example.online_photo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private NavController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //给碎片左上角添加返回按钮
        controller=Navigation.findNavController(findViewById(R.id.fragmnet));
        NavigationUI.setupActionBarWithNavController(this,controller);
    }

    @Override
    public boolean onSupportNavigateUp() {
        controller.navigateUp();
        return super.onSupportNavigateUp();
    }
}