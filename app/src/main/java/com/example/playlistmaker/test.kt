package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class test : AppCompatActivity() {

    private val imageUrl = "https://img.freepik.com/free-vector/open-blue-book-white_1308-69339.jpg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test)

        val image = findViewById<ImageView>(R.id.image)

        Glide
            .with(applicationContext)
            .load(imageUrl)
            .into(image)

//        Glide.with(applicationContext)
//            .load(imageUrl) Этот метод помещает в ImageView изображение-заглушку, если не удалось скачать изображение по ссылке,
//            переданной в метод load(). В качестве параметра можно указать идентификатор картинки из ресурсов. Чтобы её показать, интернет не нужен.
//            .placeholder(R.drawable.barsik)
//            .into(image)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}