package com.example.clusterproject


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.clusterproject.databinding.ActivityInputBinding

class InputActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_input)
        if (savedInstanceState == null) {
            val source = intent.getStringExtra("origin")
            val destination = intent.getStringExtra("destination")
            val waypoints = intent.getStringExtra("waypoints")
            if (source != null) {
                mBinding.originEt.setText(source)
            }
            if (destination != null) {
                mBinding.destinationEt.setText(destination)
            }
            if (waypoints != null) {
                mBinding.waypointsEt.setText(waypoints)
            }
        }

        mBinding.setBtn.setOnClickListener(View.OnClickListener { v: View? ->
            val origin = mBinding.originEt.getText().toString()
            val destination = mBinding.destinationEt.getText().toString()
            val wayPoints = mBinding.waypointsEt.getText().toString()
            val intent = Intent()
            if (!TextUtils.isEmpty(origin)) {
                intent.putExtra("origin", origin)
            }
            if (!TextUtils.isEmpty(destination)) {
                intent.putExtra("destination", destination)
            }
            if (!TextUtils.isEmpty(wayPoints)) {
                intent.putExtra("waypoints", wayPoints)
            }
            setResult(RESULT_OK, intent)
            finish()
        })
    }
}