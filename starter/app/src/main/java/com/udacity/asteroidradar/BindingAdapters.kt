package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidApiStatus
import com.udacity.asteroidradar.main.MainAsteroidRecyclerAdapter

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("itemHeading")
fun TextView.setItemHeading(item: Asteroid) {
    item.let {
        text = item.codename
    }
}

@BindingAdapter("itemApproachDate")
fun TextView.setItem(item: Asteroid) {
    item.let {
        text = item.closeApproachDate
    }
}

@BindingAdapter("picOfTheDay")
fun bindTextViewToPicOfTheDay(imageView: ImageView, pic: PictureOfTheDay?) {
    val context = imageView.context
    if (pic?.url != null) {
        Picasso.with(imageView.context).load(pic.url)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .error(R.drawable.placeholder_picture_of_day).into(imageView)
        imageView.contentDescription = context
            .getString(R.string.nasa_picture_of_day_content_description_format)
            .format(pic.title)
    } else {
        imageView.setImageResource(R.drawable.placeholder_picture_of_day)
        imageView.contentDescription = context.getString(R.string.nasa_picture_of_day_content_description_format)
    }
}

@BindingAdapter("dataList")
fun bindDataListToRecyclerView(recyclerView: RecyclerView, asteroidList: List<Asteroid>?) {
    val adapter = recyclerView.adapter as MainAsteroidRecyclerAdapter
    adapter.submitList(asteroidList)
}

@BindingAdapter("asteroidApiStatus")
fun bindAsteroidApiStatus(progressBar: ProgressBar, status: AsteroidApiStatus) {
    when (status) {
        AsteroidApiStatus.LOADING -> {
            progressBar.visibility = ProgressBar.VISIBLE
//            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        AsteroidApiStatus.ERROR -> {
            progressBar.visibility = ProgressBar.VISIBLE
//            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        AsteroidApiStatus.DONE -> {
            progressBar.visibility = ProgressBar.GONE
        }
    }
}

//@BindingAdapter("itemHazardous")
//fun ImageView.setImageSrc(item: Asteroid) {
//    item?.let {
//        setImageResource(
//            when (item.isPotentiallyHazardous) {
//                true -> R.drawable.ic_status_potentially_hazardous
//                else -> R.drawable.ic_status_normal
//            }
//        )
//    }
//}