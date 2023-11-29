package com.example.waygo.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waygo.LoadAdapter
import com.example.waygo.R
import com.example.waygo.SessionPrefs
import com.example.waygo.VmFactory
import com.example.waygo.adapter.PageAdapterTourism
import com.example.waygo.data.SessionUser
import com.example.waygo.databinding.ActivityMainBinding
import com.example.waygo.view.detail.DetailActivity
import com.example.waygo.view.login.LoginActivity
import com.example.waygo.view.maps.MapActivity
import com.example.waygo.view.tourism.AddTourismActivity

class MainActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sesPref: SessionPrefs
    private lateinit var intent: Intent
    private lateinit var adapter: PageAdapterTourism
    private val vm: MainVM by viewModels {
        VmFactory(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sesPref = SessionPrefs(this)
        binding.recyclerViewMain.layoutManager = LinearLayoutManager(this)
        getData()
        binding.addStory.setOnClickListener { addStory() }
//        binding.foodCategory.setOnClickListener { food() }
        binding.vacationCategory.setOnClickListener { vacation() }
    }

    private fun getData() {
        adapter = PageAdapterTourism(object : PageAdapterTourism.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                intent = Intent(this@MainActivity, DetailActivity::class.java)
                DetailActivity.apply {
                    this.id = id
                    APIKEY = sesPref.getToken().api_key.toString()
                }
                startActivity(intent)
            }
        })
        binding.recyclerViewMain.adapter =
            adapter.withLoadStateFooter(footer = LoadAdapter {
                adapter.retry()
            })
        vm.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun addStory() {
        val i = Intent(this@MainActivity, AddTourismActivity::class.java)
        startActivity(i)
    }

//    private fun food() {
//        val i = Intent(this@MainActivity, FoodActivity::class.java)
//        startActivity(i)
//    }
    private fun vacation() {
        val i = Intent(this@MainActivity, MainActivity::class.java)
        startActivity(i)
    }

    override fun onResume() {
        super.onResume()
        adapter.refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate menu xml into the activity
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                revokeToken()
                true
            }

            R.id.advancedView -> {
                intent = Intent(this@MainActivity, MapActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun revokeToken() {
        sesPref.setToken(SessionUser(null))
        val i = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(i)
        finish()
    }
}