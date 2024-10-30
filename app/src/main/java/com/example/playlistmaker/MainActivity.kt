package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchButton = findViewById<Button>(R.id.button_search)
        val mediaButton = findViewById<Button>(R.id.button_media)
        val settingsButton = findViewById<Button>(R.id.button_settings)

        searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        mediaButton.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

//        // тост на первую кнопку
//        searchButton.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: android.view.View?) {
//                Toast.makeText(this@MainActivity, "Нажата кнопка поиска", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//        // тост на вторую кнопку
//        mediaButton.setOnClickListener {
//            Toast.makeText(this, "Нажата кнопка медиа", Toast.LENGTH_SHORT).show()
//        }
//
//        // тост на третью кнопку
//        settingsButton.setOnClickListener {
//            Toast.makeText(this, "Нажата кнопка настроек", Toast.LENGTH_SHORT).show()
//        }
    }
}


// Подгружаем макет test.xml и добавляем его к основному контейнеру
//        val inflater = LayoutInflater.from(this) // Инициализация inflater
//        val testView = inflater.inflate(R.layout.test, null) // Загружаем test.xml в testView
//
//        val container = findViewById<LinearLayout>(R.id.main_container) // Основной контейнер в activity_main
//        container.addView(testView) // Добавляем testView в контейнер
//
//        val image = testView.findViewById<ImageView>(R.id.poster) // Ищем ImageView в testView
//val image = findViewById<ImageView>(R.id.poster)
//
//image.setOnClickListener {
//    Toast.makeText(this@MainActivity, "Нажали на картинку!", Toast.LENGTH_SHORT).show()
//}
//  image.setBackgroundColor(getColor(R.color.black)) цвет
//  image.scaleType = ImageView.ScaleType.CENTER_CROP масштаб
// image.setImageResource(R.drawable.poster)  установить пикчу из ресов

