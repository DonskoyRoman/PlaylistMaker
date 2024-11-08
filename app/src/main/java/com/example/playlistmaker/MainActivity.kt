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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val imageUrl = "https://img.freepik.com/free-vector/open-blue-book-white_1308-69339.jpg"

    companion object {
        val hamsterNames = listOf("Алиса", "Бимбо", "Вжик", "Дасти", "Китти", "Мафин")

        val hamsterContent = listOf(
            "Говоря об отличии сирийских хомяков от обычных джунгариков, Марина Олеговна отмечает, что особой разницы, кроме размера, нет",
            "Помимо сбалансированного корма, который можно купить в зоомагазине, в рацион следует включать свежую траву, сено, овощи, фрукты.",
            "Регулярно следует чистить вольер, менять подстилку.",
            "Хомяки довольно активные животные. Для того чтобы животным было комфортно, в клетке, вольере следует установить различные приспособления.",
            "Оптимальная для содержания хомяков температура воздуха — 20–24С.",
            "Выпускать хомяков из клетки побегать по комнате можно, но при условии, что животное ручное."
        )
    }

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
//        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this) // если мы не определили его в XML


        searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        mediaButton.setOnClickListener {
//            val testIntent = Intent(this, test::class.java)
//            startActivity(testIntent)

            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

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

