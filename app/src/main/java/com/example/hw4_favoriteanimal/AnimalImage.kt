package com.example.hw4_favoriteanimal

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_animal_image.*
import kotlinx.android.synthetic.main.fragment_animal_image.view.*

/**
 * A simple [Fragment] subclass.
 */
class AnimalImage : Fragment() {
    private val TAG = "AnimalImageFragment"
    private val FILE_NAME = "AnimalRatings"
    private val LIST = "AnimalList"
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animal_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        Log.d(TAG, "Creating AnimalImageFrag")

        var catImageTextView = view.cat_rating_text.id
        var dogImageTextView = view.dog_rating_text.id
        var rabbitRatingTextView = view.rabbit_rating_text.id
        var bearRatingTextView = view.beart_rating_text.id

        val sharedPreferences = activity!!.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val animals = sharedPreferences.getString(LIST, "") ?: ""

        if(animals.isNotEmpty()) {
            val sType = object : TypeToken<MutableList<AnimalDetails>>() {}.type
            val savedAnimalList = Gson().fromJson<MutableList<AnimalDetails>>(animals, sType)
            var animalList = mutableListOf<AnimalDetails>()

            animalList.addAll(savedAnimalList)
            Log.d(TAG, "Saved preference animal list $animalList")
            for(animal in animalList) {
                val textView = activity?.findViewById<TextView>(animal.textView)
                if(textView != null) {
                    textView.text = "Your Rating: ${animal.rating}"
                }
            }
        }


        view.cat_image.setOnClickListener{ view -> imageClick(view, catImageTextView, viewModel, "Cat")  }
        view.dog_image.setOnClickListener{view -> imageClick(view, dogImageTextView, viewModel, "Dog") }
        view.rabbit_image.setOnClickListener{view -> imageClick(view, rabbitRatingTextView, viewModel, "Rabbit") }
        view.bear_image.setOnClickListener{ view -> imageClick(view, bearRatingTextView, viewModel, "Bear") }

        viewModel.animalToImageFrag.observe(requireActivity(), Observer {
            var animal = it
            val textView = activity?.findViewById<TextView>(animal.textView)
            if(textView != null) {
                textView.text = "Your Rating: ${animal.rating}"
            }
        })
    }

    fun imageClick(view: View, ratingTextId: Int, viewModel: MainViewModel, animalName: String) {
        val imageId = when(view.id) {
            cat_image.id -> R.drawable.cat
            dog_image.id -> R.drawable.dog
            bear_image.id -> R.drawable.bear
            else -> R.drawable.rabbit
        }

        val stringRateVal = activity!!.findViewById<TextView>(ratingTextId).text.split(":")[1]
        viewModel.animalToRatingFrag.value = AnimalDetails(animalName, stringRateVal.toFloat() , imageId, ratingTextId)

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
