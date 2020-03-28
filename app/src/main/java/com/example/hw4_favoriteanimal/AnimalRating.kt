package com.example.hw4_favoriteanimal

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_animal_rating.view.*

/**
 * A simple [Fragment] subclass.
 */
class AnimalRating : Fragment() {
    private val TAG = "AnimalRating"
    private val FILE_NAME = "AnimalRatings"
    private val LIST = "AnimalList"

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
                    Log.d(TAG, "Setting on click for save")
                    val rating = ratingBar.rating
                    val animalDetailsToSave = AnimalDetails(animal.name, rating, animal.imageId, animal.textView)
                    save(animalDetailsToSave)
                    viewModel.animalToImageFrag.value = animalDetailsToSave
                    Toast.makeText(activity, "Saving rating $rating for ${animal.name}!!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun save(animal: AnimalDetails) {
        val sharedPreferences = activity!!.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        Log.d(TAG, "Entering save to shared preferences method")

        val sType = object : TypeToken<MutableList<AnimalDetails>>() {}.type
        val animals = sharedPreferences.getString(LIST, "") ?: ""

        Log.d(TAG, "Saving preferences for ${animal.name}")
        var savedAnimalList = Gson().fromJson<MutableList<AnimalDetails>>(animals, sType)
        var animalList = mutableListOf<AnimalDetails>()
        if(savedAnimalList != null) {
            animalList.addAll(savedAnimalList)
        }
        Log.d(TAG, "Animal Preference list before save $animalList")
        for(savedAnimal in animalList) {
            if(savedAnimal.name == animal.name) {
                animalList.remove(savedAnimal)
                break
            }
        }
        animalList.add(animal)
        Log.d(TAG, "Animal Preference list after save $animalList")
        val editor = sharedPreferences.edit()
        editor.putString(LIST, Gson().toJson(animalList))
        editor.apply()

    }
}
