package com.example.hw4_favoriteanimal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_animal_rating.view.*

/**
 * A simple [Fragment] subclass.
 */
class AnimalRating : Fragment() {
    private val TAG = "AnimalRating"
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_animal_rating, container, false)

        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        var animal: AnimalDetails

        viewModel.animals.observe(requireActivity(), Observer {
            animal = it
            Log.d(TAG, "loading animal details $animal")
            view.rating_image_title.text = animal.name
            view.rating_image.setImageResource(animal.imageId)
        })




        return view
    }

}
