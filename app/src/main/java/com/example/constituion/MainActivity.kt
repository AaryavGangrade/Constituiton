package com.example.constituion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import data.ArticleContent
import data.ArticleNumbers
import data.ArticleTitles

data class Article(val number: String, val title: String, val content: String)

class MainActivity : AppCompatActivity() {
    private val articlesList: MutableList<Article> = mutableListOf()
    private lateinit var adapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Access the list of custom titles, numbers, and content
        val customTitles = ArticleTitles.titles
        val customNumbers = ArticleNumbers.numbers
        val customContent = ArticleContent.content

        // Determine the minimum size between customTitles, customNumbers, and customContent
        val minSize = minOf(customTitles.size, customNumbers.size, customContent.size)

        // Generate sample articles with custom titles, numbers, and content
        for (i in 0 until minSize) {
            val articleNumber = customNumbers[i]
            val articleTitle = customTitles[i]
            val articleContent = customContent[i]
            val article = Article(articleNumber, articleTitle, articleContent)
            articlesList.add(article)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = ArticleAdapter(articlesList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }
}

class ArticleAdapter(private val articles: List<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>(), Filterable {

    private var filteredArticles: List<Article> = articles

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.articles, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = filteredArticles[position]
        holder.articleTitleTextView.text = article.title
        holder.articleNumberTextView.text = "Article ${article.number}:"
        holder.articleContentTextView.text = article.content
    }

    override fun getItemCount(): Int {
        return filteredArticles.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val articleTitleTextView: TextView = itemView.findViewById(R.id.articleTitleTextView)
        val articleNumberTextView: TextView = itemView.findViewById(R.id.articleNumberTextView)
        val articleContentTextView: TextView = itemView.findViewById(R.id.articleContentTextView)

        // Initialize a variable to keep track of whether the item is expanded
        private var isExpanded = false

        init {
            // Set an OnClickListener to the root view (CardView or other container)
            itemView.setOnClickListener {
                if (canExpand()) {
                    // Toggle the expanded state
                    isExpanded = !isExpanded
                    // Call a function to update the UI based on the expanded state
                    updateUI()
                } else {
                    // Display a Snackbar error message when there is nothing to expand
                    Snackbar.make(itemView, "Nothing to expand", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        // Function to check if the item can be expanded
        private fun canExpand(): Boolean {
            // Get the TextView that displays the text you want to expand
            val textView = itemView.findViewById<TextView>(R.id.articleContentTextView)

            // Check if the number of lines in the TextView is greater than or equal to 5
            return textView.lineCount >= 5
        }

        // Function to update the UI based on the expanded state
        private fun updateUI() {
            if (isExpanded) {
                // Expand the item (show complete content)
                articleContentTextView.maxLines = Int.MAX_VALUE
            } else {
                // Collapse the item (show truncated content)
                articleContentTextView.maxLines = 5 // Adjust as needed
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Article>()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(articles)
                } else {
                    val filterPattern = constraint.toString().trim()
                    for (article in articles) {
                        if (article.number.contains(filterPattern, ignoreCase = true)) {
                            filteredList.add(article                            )
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.values is List<*>) {
                    @Suppress("UNCHECKED_CAST")
                    filteredArticles = results.values as List<Article>
                    notifyDataSetChanged()
                }
            }
        }
    }
}

