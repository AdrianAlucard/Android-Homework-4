package com.example.hw4_favoriteanimal

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_animal_image.*
import kotlinx.android.synthetic.main.fragment_animal_image.view.*

/**
 * A simple [Fragment] subclass.
 */
class AnimalImage : Fragment() {
    private val TAG = "AnimalImageFragment"

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_animal_image, container, false)

        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        val rating = "Your Rating: 4.0"
        view.cat_image.setOnClickListener{ view -> imageClick(view, rating, viewModel, "Cat")  }
        view.dog_image.setOnClickListener{view -> imageClick(view, rating, viewModel, "Dog") }
        view.rabbit_image.setOnClickListener{view -> imageClick(view, rating, viewModel, "Rabbit") }
        view.bear_image.setOnClickListener{ view -> imageClick(view, rating, viewModel, "Bear") }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    fun imageClick(view: View, ratingText: String, viewModel: MainViewModel, animalName: String) {
        val imageId = when(view.id) {
            cat_image.id -> R.drawable.cat
            dog_image.id -> R.drawable.dog
            bear_image.id -> R.drawable.bear
            else -> R.drawable.rabbit
        }

        val stringRateVal = ratingText.split(":")[1]
        viewModel.animals.value = AnimalDetails( animalName, stringRateVal.toDouble() , imageId)

        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // we are in portrait orientation
            // replace main container with detail fragment
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, AnimalRating())
                .addToBackStack(null)
                .commit()
        } else {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.animal_rating, AnimalRating())
                .addToBackStack(null)
                .commit()
        }
    }
}
