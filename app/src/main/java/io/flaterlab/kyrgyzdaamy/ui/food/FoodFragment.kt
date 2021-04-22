package io.flaterlab.kyrgyzdaamy.ui.food


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.adapter.category.CategoryAdapter
import io.flaterlab.kyrgyzdaamy.adapter.category.CategoryListener
import io.flaterlab.kyrgyzdaamy.adapter.shimmer.Layout
import io.flaterlab.kyrgyzdaamy.adapter.shimmer.ShimmeringAdapter
import io.flaterlab.kyrgyzdaamy.databinding.FragmentFoodBinding
import io.flaterlab.kyrgyzdaamy.extension.*
import io.flaterlab.kyrgyzdaamy.service.*
import io.flaterlab.kyrgyzdaamy.service.model.RestaurantResponse
import io.flaterlab.kyrgyzdaamy.service.response.Category
import io.flaterlab.kyrgyzdaamy.service.response.Schedule
import io.flaterlab.kyrgyzdaamy.ui.MainActivity
import io.flaterlab.kyrgyzdaamy.ui.viewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class FoodFragment : BaseFragment(), CategoryListener {
    @ExperimentalCoroutinesApi
    private val viewModel by viewModels<FoodViewModel>()
    @Inject lateinit var db: FirebaseFirestore
    private val categoryAdapter = CategoryAdapter(this)
    @Inject lateinit var storage: FirebaseStorage

    @Inject lateinit var auth: FirebaseAuth



    private val binding by viewBinding(FragmentFoodBinding::bind)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_food)
    }


    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity

        if (auth.currentUser == null){
            activity.hideBottomNav()
            findNavController().navigate(FoodFragmentDirections.toAuthNavigation())
        }else{
            if (!hasInitializedRootView) {
                hasInitializedRootView = true
                init()
                activity.showBottomNav()
            }
            binding.noInternetLayout.update.setOnClickListener {
                if (isConnectedOrConnecting()) {

                    binding.foodCategoryRv.visibility = View.VISIBLE
                    binding.group.visibility = View.VISIBLE
                    binding.noInternetLayout.root.visibility = View.GONE
                    init()
                }
            }
        }

    }

    @ExperimentalCoroutinesApi
    private fun init() {
        getSchedules()
        loadRestaurant()
        initData()

    }

    private fun getSchedules() {

        val dateLocale = Calendar.getInstance()

        val date = SimpleDateFormat("EEEE", Locale.ENGLISH).format(dateLocale.time)

        db.collection("Schedule").document(date).get().addOnSuccessListener { document ->

            val schedule = document.toObject(Schedule::class.java)
            schedule?.let {
                val dateFrom = it.from.apply {
                    AppPreferences.dateFrom = this
                }
                val dateTo = it.to.apply {
                    AppPreferences.dateTo = this
                }

                "$dateFrom - $dateTo".apply {
                    AppPreferences.schedule = this
                }
            }

            if (AppPreferences.lastDay == null || AppPreferences.lastDay != date) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Режим работы ресторана")
                    .setMessage("${resources.getString(R.string.message_alert)} с ${AppPreferences.dateFrom} до ${AppPreferences.dateTo}")
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                        // Respond to positive button press
                        dialog.cancel()
                    }
                    .show()
            }
            AppPreferences.lastDay = date
        }.addOnFailureListener {

        }

    }

    private fun loadRestaurant() {

        db.collection("collection_details").document("KyrgyzDaamy").get()
            .addOnSuccessListener {
                val restaurantData = it.toObject(RestaurantResponse::class.java)
                restaurantData?.let { response ->
                    AppPreferences.currencyName = response.currency
                    AppPreferences.restaurant = response.id
                    AppPreferences.bankPay = response.onlinePaymentSupported

                    binding.restaurantDetailTitle.text = response.name
                }

            }
            .addOnFailureListener {
                toast(it.message)
            }

    }

    //keyAlias: amore password: amore123 password:0312490131Bo
    @ExperimentalCoroutinesApi
    private fun initData() {

        lifecycleScope.launchWhenCreated {
            val shimmerAdapter = ShimmeringAdapter(Layout.Menu, 7)
            binding.foodCategoryRv.apply {
                adapter = shimmerAdapter
            }
            viewModel.stateFlow.collect {
                categoryAdapter.set(it as ArrayList<Category>)
                binding.foodCategoryRv.apply {
                    adapter = categoryAdapter
                    addItemDecoration(
                        CustomPositionItemDecoration(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.divider
                            )!!
                        )
                    )
                    setHasFixedSize(true)
                }
            }
        }
    }


    override fun onCategoryClick(item: Category) {

        db.collection("categories").document(item.id).addSnapshotListener { value, error ->
            val category = value?.toObject(Category::class.java)
            category?.let {
                if (it.hasProducts) {

                    val action =
                        FoodFragmentDirections.toNavFoodItem(item.id, item.name, "")
                    findNavController().navigate(action)
                }

            }
        }
    }

}

