package com.capstone.repoth.ui.view.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.repoth.databinding.ActivityNotificationBinding
import java.util.*

class NotificationActivity : AppCompatActivity() {
    private lateinit var bind: ActivityNotificationBinding
    private val locale: String = Locale.getDefault().language
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(bind.root)
        }




}