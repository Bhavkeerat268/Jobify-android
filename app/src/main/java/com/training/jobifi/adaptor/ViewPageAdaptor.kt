package com.training.jobifi.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.training.jobifi.fragment.NonSkilledWorkFragment
import com.training.jobifi.fragment.SkilledWorkFragment
import com.training.jobify.R


class ViewPageAdaptor(val fragmentManager: FragmentManager, viewPager: ViewPager?): FragmentPagerAdapter(fragmentManager) {
    /*View pager to navigate between skilled job fragment anbd non skilled fragment*/
    val fragments= arrayListOf<Fragment>()
    val titles= arrayListOf<String>()




    override fun getItem(position: Int): Fragment {
        when(position)
        {
            0->return NonSkilledWorkFragment()
            1->return SkilledWorkFragment()
            else-> return NonSkilledWorkFragment()
        }

    }

    override fun getCount(): Int {
        return 2
    }

    fun addFragment(fragment: Fragment,title:String)
    {
        fragmentManager.beginTransaction().replace(R.id.viewPager,fragment).commit()
        fragments.add(fragment)
        titles.add(title)


    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles.get(position)
    }



}
