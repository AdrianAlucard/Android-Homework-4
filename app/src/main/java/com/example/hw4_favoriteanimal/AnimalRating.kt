package com.example.hw4_favoriteanimal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_animal_rating.view.*

/**
 * A simple [Fragment] subclass.
 */
class AnimalRating : Fragment() {
    private val TAG = "AnimalRating"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_animal_rating, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        viewModel.animalToRatingFrag.observe(requireActivity(), Observer {
            var animal = it
            Log.d(TAG, "loading animal details $animal")
            view.rating_image_title.text = animal.name
            view.rating_image.setImageResource(animal.imageId)
            view.rating_bar.rating = animal.rating
            view.rating_save_btn.setOnClickListener{}

            val ratingBar = activity?.findViewById<RatingBar>(R.id.rating_bar)
            val saveBtn = activity?.findViewById<Button>(R.id.rating_save_btn)
            if(saveBtn != null && ratingBar != null) {
                saveBtn.setOnClickListener{
                    val rating = ratingBar.rating

                    viewModel.animalToImageFrag.value = AnimalDetails(animal.name, rating, animal.imageId, animal.textView)
                    Toast.makeText(activity, "Saving rating $rating for ${animal.name}!!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
