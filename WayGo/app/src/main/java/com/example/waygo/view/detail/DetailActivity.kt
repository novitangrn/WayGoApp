package com.example.waygo.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.waygo.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(){
    private lateinit var binding: ActivityDetailBinding
    private lateinit var vm: DetailViewModel

    companion object {
        var APIKEY: String = ""
        var id: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        vm = ViewModelProvider(this)[DetailViewModel::class.java]
        vm.getDetail(id, APIKEY)
        vm.descriptionNotes.observe(this) {
            binding.tvDeskripsi.text = it
        }
        vm.imageNotes.observe(this) {
            Glide.with(this).load(it).into(binding.ivDetail)
        }
        vm.nameNotes.observe(this) {
            binding.tvDetail.text = it
            supportActionBar?.title = it
        }
        vm.catNotes.observe(this) {
            binding.createdAtDetailValue.text = it
        }
        vm.latNotes.observe(this) {
            binding.latDetailValue.text = it
        }
        vm.longNotes.observe(this) {
            binding.longDetailValue.text = it
        }
        vm.showToast.observe(this) { show ->
            vm.toastMsg.observe(this) { msg ->
                showToast(show, msg)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showToast(show: Boolean, msg: String?) {
        if (show) {
            Toast.makeText(this@DetailActivity, msg, Toast.LENGTH_SHORT).show()
        }
    }
}