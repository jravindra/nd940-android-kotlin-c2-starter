//package com.udacity.asteroidradar.main
//
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.databinding.BindingAdapter
//import com.udacity.asteroidradar.Asteroid
//import com.udacity.asteroidradar.R
//
//@BindingAdapter("itemHeading")
//fun TextView.setItemHeading(item: Asteroid) {
//    item?.let {
//        text = item.codename
//    }
//}
//
//@BindingAdapter("itemApproachDate")
//fun TextView.setItem(item: Asteroid) {
//    item?.let {
//        text = item.closeApproachDate
//    }
//}
//
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
