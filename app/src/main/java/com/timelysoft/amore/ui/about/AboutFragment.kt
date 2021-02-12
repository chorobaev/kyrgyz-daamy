package com.timelysoft.amore.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.timelysoft.amore.R
import kotlinx.android.synthetic.main.fragment_about.*


class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initToolbar()


    }

    private fun initViews() {
//        val listQuestions = ArrayList<AboutResponse>()
//        listQuestions.add(AboutResponse("Первый вопрос", "Ответ на вопрос"))
//        listQuestions.add(AboutResponse("Второй вопрос", "Ответ на вопрос"))
//        listQuestions.add(AboutResponse("Третий вопрос", "Ответ на вопрос"))
//        listQuestions.add(AboutResponse("Четвертый вопрос", "Ответ на вопрос"))

//        about_expandablelist.setAdapter(
//            CustomExpandableListAdapter(
//                requireContext(),
//                listQuestions
//            )
//        )
    }

    private fun initToolbar() {
        about_back.setOnClickListener {
            findNavController().popBackStack()
        }
        about_toolbar_text.text = getString(R.string.menu_about)
    }

}
