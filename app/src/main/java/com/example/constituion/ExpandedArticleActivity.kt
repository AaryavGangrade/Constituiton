package com.example.constituion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class ExpandedArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expanded_articles) // Set the layout

        // Retrieve the data from the intent
        val articleTitle = intent.getStringExtra("articleTitle")
        val articleContent = intent.getStringExtra("articleContent")

        // Display the data in your expanded_articles.xml layout
        val titleTextView = findViewById<TextView>(R.id.expandedArticleTitleTextView)
        val contentTextView = findViewById<TextView>(R.id.expandedArticleContentTextView)

        titleTextView.text = articleTitle
        contentTextView.text = articleContent
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Add any necessary logic to handle back navigation
        // For example, if you want to go back to the main activity:
        // finish()
    }
}
