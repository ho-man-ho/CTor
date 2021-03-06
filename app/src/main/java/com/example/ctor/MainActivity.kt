package com.example.ctor

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: android.view.View) {
        val workingtv: TextView = findViewById(R.id.workingtv)
          if (view is Button)
          {
              if (view.text == ".") {
                  if (canAddDecimal)
                      workingtv.append(view.text)
                  canAddDecimal = false
              }
              else
                  workingtv.append(view.text)
              canAddOperation = true
          }
    }

    fun operationAction(view: android.view.View)
    {
       val workingtv: TextView = findViewById(R.id.workingtv)
        if (view is Button && canAddOperation)
        {
            workingtv.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }


    }
    fun allClear(view: android.view.View)
    {
        val workingtv: TextView = findViewById(R.id.workingtv)
        val resultstv: TextView = findViewById(R.id.resultstv)
        workingtv.text = ""
        resultstv.text = ""

    }
    fun backClear(view: android.view.View)
    {
        val workingtv: TextView = findViewById(R.id.workingtv)
        val length = workingtv.length()
        if (view is Button && canAddOperation)
        {
            workingtv.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
        if (length > 0)
            workingtv.text = workingtv.text.subSequence(0,length - 1)

    }
    fun kill(view: android.view.View)
    {
        val o = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ"))
        startActivity(o)
    }
    fun equals(view: android.view.View)
    {

            val resultstv: TextView = findViewById(R.id.resultstv)
            resultstv.text = calculateResults()

    }

    private fun calculateResults(): String {

        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtract(timesDivision)
        return result.toString()

    }

    private fun addSubtract(passedList:MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices){

            if (passedList[i] is Char && i != passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
            {
                list = calcTimeDiv(list)
            }

        return list

    }

    private fun calcTimeDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }

            }
            if (i > restartIndex)
                newList.add(passedList[i])

        }
        return newList

    }

    private fun digitsOperators(): MutableList<Any>
    {
        val workingtv: TextView = findViewById(R.id.workingtv)
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in workingtv.text)
        {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }

        }

        if (currentDigit !="")
            list.add(currentDigit.toFloat())

        return list


    }
}