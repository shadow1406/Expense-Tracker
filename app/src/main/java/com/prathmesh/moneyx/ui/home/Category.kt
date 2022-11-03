package com.prathmesh.moneyx.ui.home

import com.prathmesh.moneyx.R

sealed class Category(val icon: Int,val color: String,val bg: String,val title: String,val id: Int){
    object Shopping : Category(R.drawable.ic_shopping,"#FCAC12","#FCEED4","Shopping",1)
    object Subscription : Category(R.drawable.ic_subscription,"#7F3DFF","#EEE5FF","Subscription",2)
    object Food : Category(R.drawable.ic_food,"#FD3C4A","#FDD5D7","Food",3)
    object Transport : Category(R.drawable.ic_transport,"#0077FF","#d8eaff","Transport",4)
    object Personal : Category(R.drawable.ic_personal,"#ffa500","#ffe5b4","Personal",5)
    object Salary : Category(R.drawable.ic_salary,"#228B22","#90EE90","Salary",6)
    object Other : Category(R.drawable.ic_transaction,"#ff43a4","#ffcff1","Other",7)
    object PassiveIncome : Category(R.drawable.ic_passive,"#0077FF","#d8eaff","Passive Income",8)
}
